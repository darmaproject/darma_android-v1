package com.wallet;

import android.content.Context;
import android.util.Log;
import appwallet.AppWalletStats;
import appwallet.Appwallet;
import appwallet.MobileWallet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wallet.bean.*;
import com.wallet.model.*;
import com.wallet.utils.GsonUtils;
import com.wallet.utils.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import static com.wallet.WalletConfig.*;

/**
 * Created by Darma Project on 2019/9/24.
 */
public class WalletManager {

    private static String TAG="luo";

    private static WalletManager INSTANCE = new WalletManager();

    private File walletFileDir;//Wallet file

    private FileFilter mFileFilter;


    private MobileWallet mMobileWallet;


    private Wallet openWallet;//Wallet currently open

    private Rate coin_rate = Rate.NEGATIVE;
    private Context context;

    AppModel appModel;

    private AppWalletStats mAppWalletStats;

    private WalletStatusInfoBean mWalletStatusInfoBean;//Wallet status information

    private WalletManager() {

        mFileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(WALLET_FILE_SUFFIX))
                    return true;
                return false;
            }
        };


        mAppWalletStats = new AppWalletStats() {
            @Override
            public void dumpWalletStats(String s) {

                Log.v("luo", "dumpWalletStats = " + s);

                if (!StringUtils.isEmpty(s)) {
                    try {
                        mWalletStatusInfoBean = new Gson().fromJson(s, WalletStatusInfoBean.class);
                        appModel.onWalletStatusInfo(mWalletStatusInfoBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        mMobileWallet = newMobileWallet();
    }

    public static WalletManager getInstance() {
        return INSTANCE;
    }

    public Rate getCoinRate() {
        if (coin_rate == null || coin_rate.equals(Rate.NEGATIVE)) {
            String str = StringUtils.getStringFromSp(context, "coin_rate", null);

            if (!StringUtils.isEmpty(str)) {
                coin_rate = Rate.fromString(str);
            }
        }
        return coin_rate;
    }

    /**
     * Setting exchange rate
     *
     * @param coin_rate
     */
    public void saveRate(Rate coin_rate) {
        this.coin_rate = coin_rate;

        StringUtils.saveStringToSp(context, "coin_rate", coin_rate.toString());
    }

    public void setAppContext(Context context, AppModel appModel) {
        this.context = context;
        this.appModel = appModel;
    }

    /**
     * Set wallet file path
     *
     * @param path
     */
    public void setWalletFilesPath(String path) throws Exception {

        if (StringUtils.isEmpty(path)) {
            throw new Exception("path is Empty");
        }


        walletFileDir = new File(path);

        if (!walletFileDir.exists()) {
            walletFileDir.mkdirs();
        }

        Log.v("luo", "walletFileDir = " + walletFileDir.getAbsolutePath());

    }

    public boolean isViewOnlyWallet() {


        return mMobileWallet.is_View_Only();
    }


    public boolean checkOpenWalletPWd(String pwd) {
        return mMobileWallet.check_Password(pwd);
    }

    public Wallet getOpenWallet() {
        return openWallet;
    }

    /**
     * Get available balance
     *
     * @return
     */
    public Coin getUnlockedBalance() {
        if (mWalletStatusInfoBean == null) {
            return Coin.ZERO;
        }
        return mWalletStatusInfoBean.getUnlocked_balance();
    }
    /**
     * Get Wallet Height
     *
     * @return
     */
    public long getWalletHeight() {
        if (mWalletStatusInfoBean == null) {
            return mWalletStatusInfoBean.getWallet_height();
        }
        return mWalletStatusInfoBean.getWallet_height();
    }
    /**
     * Get wallet address
     *
     * @return
     */
    public String getAddress() {
        return mMobileWallet.get_Wallet_Address();
    }

    /**
     * Get integration address
     *
     * @return
     */
    public String getIntergratedAddress() {
        return mMobileWallet.generate_Intergrated_Address(32);
    }


    /**
     * paymentID
     *
     * @return
     */
    public String getPaymentID(String intergratedAddress) {

        AddressCheckBean address = new Gson().fromJson(mMobileWallet.verify_Address(intergratedAddress), AddressCheckBean.class);

        return address.getPaymentID();
    }


    public void setOpenWallet(Wallet openWallet) {
        this.openWallet = openWallet;
    }

    public File getWalletFilesDir() {
        return walletFileDir;
    }

    /**
     * Get a list of all wallets
     *
     * @return
     */
    public List<Wallet> getWallets() {

        List<Wallet> list = new ArrayList<>();


        for (File file : walletFileDir.listFiles(mFileFilter)) {
            list.add(new Wallet(file));
        }

        return list;
    }

    private MobileWallet newMobileWallet() {

        MobileWallet mobileWallet = Appwallet.newMobileWallet();
        mobileWallet.set_DumpStats_Callback(mAppWalletStats);
        return mobileWallet;
    }


    /**
     * Set open wallet and wallet control class
     *
     * @param mobileWallet
     * @param wallet
     */
    private void setMobileWallet(MobileWallet mobileWallet, Wallet wallet) {

        if (isOpenWallet()) {
            mMobileWallet.stop_Update_Blance();
            mMobileWallet.close_Encrypted_Wallet();
        }
        mMobileWallet = mobileWallet;
        openWallet = wallet;
        afterOpen();
    }

    /**
     * Open wallet
     *
     * @param wallet
     * @param pwd
     * @return
     * @throws WalletErrorException
     */
    public void openWallet(Wallet wallet, String pwd) throws WalletErrorException {
        MobileWallet mobileWallet = newMobileWallet();

        Log.w("luo", wallet.getPath());
        if (mobileWallet.open_Encrypted_Wallet(wallet.getPath(), pwd)) {

            wallet.setPwd(pwd);
            setMobileWallet(mobileWallet, wallet);

        } else {
            mobileWallet = null;
            Log.w("luo", "open_Encrypted_Wallet false");
            throw new WalletErrorException(getError());
        }
    }

    private void afterOpen() {
        StringUtils.saveStringToSp(context, LAST_OPEN_WALLET, openWallet.getPath());
        saveWalletToDB(openWallet);
        startUpdate();


//        Log.w("seed", getMnemonicWordStr());
    }

    public Wallet getLastOpenWallet() {
        String path = StringUtils.getStringFromSp(context, LAST_OPEN_WALLET, "");
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                return new Wallet(file);
            }
        }
        return null;
    }

    public boolean isOpenWallet() {
        if (openWallet != null) {
            return true;
        }
        return false;
    }

    public File newWalletFile(String name) {
        return new File(getWalletFilesDir(), name + WALLET_FILE_SUFFIX);
    }

    /**
     * Create Wallet
     */
    public void createWallet(String path, String pwd) throws WalletErrorException {
        MobileWallet mobileWallet = newMobileWallet();

        boolean success = mobileWallet.create_Encrypted_Wallet(newWalletFile(path).getAbsolutePath(), pwd);
        if (success) {

            mobileWallet.set_Daemon_Address(getSelectNode().getUrl());
            mobileWallet.set_Initial_Height_Default();
            Wallet wallet = new Wallet(newWalletFile(path));
            wallet.setPwd(pwd);

            setMobileWallet(mobileWallet, wallet);

        } else {
            throw new WalletErrorException(getError());
        }


    }

    /**
     * Resynchronization
     */
    public void reScanBlock() {

        mMobileWallet.rescan_From_Height();

    }

    /**
     * Get private key
     */
    public PrivateKey getPrivateKey() {

        String json = mMobileWallet.get_Keys(openWallet.getPwd());
        Log.v("PrivateKey", json);
        PrivateKey key = new Gson().fromJson(json, PrivateKey.class);
        return key;
    }

    /**
     * Get seed code
     */
    public String getMnemonicWordStr() {

        String seeds = mMobileWallet.get_Seeds(getOpenWallet().getPwd());

        return seeds;
    }

    public Node getSelectNode() {


//        String node = StringUtils.getStringFromSp(context, SELECTED_NODE, DEFUALT_NODE);

        return appModel.getSelectNode();
    }

    public void saveSelectNode(Node node) {
//        StringUtils.saveStringToSp(context, SELECTED_NODE, node);

        appModel.saveSelectNode(node);

    }


    public void setNodeAddress(String node) {
        if (isOpenWallet()) {
           if(! mMobileWallet.set_Daemon_Address(node)){
              new WalletErrorException(getError()).printStackTrace();
           }
        }
    }

    public void changePwd(String password) throws WalletErrorException {
        if (!mMobileWallet.change_Password(password)) {
            throw new WalletErrorException(getError());
        }

        openWallet.setPwd(password);

    }


    /**
     * Get seed code
     */
    public String[] getMnemonicWordList() {

        String seeds = getMnemonicWordStr();
        if (StringUtils.isEmpty(seeds)) {

            return null;
        }

        String[] list = seeds.split(MNEMONIC_WORD_SPLIT);

        return list;
    }

    /**
     * Start synchronization
     */
    public void startUpdate() {


        setNodeAddress(getSelectNode().getUrl());

        if (isOpenWallet()) {
            if (!mMobileWallet.update_Wallet_Balance()) {
                new WalletErrorException(getError()).printStackTrace();
            }
        }
    }

    /**
     * Closing Wallet
     *
     * @throws WalletErrorException
     */
    public void closeWallet() {

        if (openWallet != null) {
            mMobileWallet.stop_Update_Blance();
            if (mMobileWallet.close_Encrypted_Wallet()) {
                openWallet = null;
                Log.e("luo", "closeWallet success");
            }
        }

        mMobileWallet = newMobileWallet();

    }

    /**
     * Private key recovery Wallet
     *
     * @param path
     * @param pwd
     * @param height
     * @param spendkey
     * @throws WalletErrorException
     */
    public void recoveryWalletByPrivateKey(String path, String pwd, long height, String spendkey) throws WalletErrorException {
        MobileWallet mobileWallet = newMobileWallet();
        if (mobileWallet.create_Encrypted_Wallet_SpendKey(newWalletFile(path).getAbsolutePath(), pwd, spendkey)) {
            mobileWallet.set_Daemon_Address(getSelectNode().getUrl());

            if (height == WALLET_HEIGHT_DEFAULT) {

                mobileWallet.set_Initial_Height_Default();
            } else {

                mobileWallet.set_Initial_Height(height);
            }
            Wallet wallet = new Wallet(newWalletFile(path));
            wallet.setPwd(pwd);

            setMobileWallet(mobileWallet, wallet);
        } else {

            throw new WalletErrorException(getError());
        }

    }

    /**
     * Restore read only Wallet
     *
     * @param path
     * @param pwd
     * @param height
     * @param spendkey
     * @throws WalletErrorException
     */
    public void recoveryWalletByViewOnlyKey(String path, String pwd, long height, String spendkey) throws WalletErrorException {
        MobileWallet mobileWallet = newMobileWallet();
        if (mobileWallet.create_Encrypted_Wallet_ViewOnly(newWalletFile(path).getAbsolutePath(), pwd, spendkey)) {
            mobileWallet.set_Daemon_Address(getSelectNode().getUrl());

            if (height == WALLET_HEIGHT_DEFAULT) {

                mobileWallet.set_Initial_Height_Default();
            } else {

                mobileWallet.set_Initial_Height(height);
            }
            Wallet wallet = new Wallet(newWalletFile(path));
            wallet.setPwd(pwd);

            setMobileWallet(mobileWallet, wallet);
        } else {

            throw new WalletErrorException(getError());
        }

    }

    /**
     * Mnemonic recovery Wallet
     *
     * @param path
     * @param pwd
     * @param height
     * @param mnemonics
     * @throws WalletErrorException
     */
    public void recoveryWalletByMnemonic(String path, String pwd, long height, String mnemonics) throws WalletErrorException {
        MobileWallet mobileWallet = newMobileWallet();
        if (mobileWallet.recovery_Encrypted_Wallet(newWalletFile(path).getAbsolutePath(), pwd, mnemonics)) {
            mobileWallet.set_Daemon_Address(getSelectNode().getUrl());

            if (height == WALLET_HEIGHT_DEFAULT) {

                mobileWallet.set_Initial_Height_Default();
            } else {

                mobileWallet.set_Initial_Height(height);
            }
            Wallet wallet = new Wallet(newWalletFile(path));
            wallet.setPwd(pwd);

            setMobileWallet(mobileWallet, wallet);
        } else {

            throw new WalletErrorException(getError());
        }

    }

    /**
     * Get the current status of wallet
     * <p>
     *
     * @return
     */
    public String getWalletStatus() {

        if (mWalletStatusInfoBean != null) {

            if (mWalletStatusInfoBean.getWallet_online()) {

                if (mWalletStatusInfoBean.getWallet_available()) {


                    if (mWalletStatusInfoBean.getDaemon_topo_height() == 0) {
                        return STATUS_CONECTING;
                    } else if ((mWalletStatusInfoBean.getDaemon_topo_height() - mWalletStatusInfoBean.getWallet_topo_height()) <= 1) {

                        return STATUS_COMPLETION;
                    } else {

                        return STATUS_SYNCHING;
                    }
                } else {

                    return STATUS_WALLET_UNAVAILABLE;
                }
            } else {
                return STATUS_WALLET_OFFLINE;
            }
        }
        return STATUS_WALLET_OFFLINE;
    }

    /**
     * Get transactions
     *
     * @return
     */
    public List<TransactionRecordBean> getTransactions(boolean in,boolean out,String index) {
        String transfers = mMobileWallet.get_Transfers(in, out,  index, "20");
        Log.v("luo", transfers);

        if (StringUtils.isEmpty(transfers)) {
            return null;
        }

//        List<TransactionRecordBean> list = new Gson().fromJson(transfers, new TypeToken<List<TransactionRecordBean>>() {
//        }.getType());
        List<TransactionRecordBean> list= GsonUtils.gsonToList(transfers,TransactionRecordBean.class);

        Log.v("luo", "list = " + list.size());
        return list;
    }

    public Transaction sendAll(String address, String payment_id, String password) throws WalletErrorException {

        String result = mMobileWallet.transfer_Everything(address, "0", payment_id, 0, false, password);

        Log.v("Transaction", "result = " + result);
        if (StringUtils.isEmpty(result)) {
            throw new WalletErrorException(getError());
        }

        Transaction transaction = Transaction.from(result);
        transaction.setReceive_address(address);
        transaction.setPayment_id(payment_id);
        return transaction;

    }

    public Transaction createTransfer(String address, String amountstr, String payment_id, String password) throws WalletErrorException {

        String result = mMobileWallet.transfer(address, amountstr, "0", payment_id, 0, false, password);

        Log.v("Transaction", "result = " + result);
        if (StringUtils.isEmpty(result)) {
            throw new WalletErrorException(getError());
        }

        Transaction transaction = Transaction.from(result);
        transaction.setReceive_address(address);
        transaction.setPayment_id(payment_id);

        return transaction;

    }

    public String sendTransaction(Transaction trans) throws WalletErrorException {
        String txid = mMobileWallet.send_Raw_Transaction(trans.getTransfer_txhex());

        if (StringUtils.isEmpty(txid)) {
            throw new WalletErrorException(getError());
        }

        return txid;
    }


    public void saveWalletToDB(Wallet wallet) {
        appModel.saveWalletToDB(wallet);
    }

    public String backupWallet(String path, String pwd) throws WalletErrorException {

        String str = mMobileWallet.backup_WalletFile(path, pwd);

        if (StringUtils.isEmpty(str)) {
            throw new WalletErrorException(getError());
        }
        return str;
    }

    public void restoreWalletFile(String backupPath, String name, String pwd, long height) throws WalletErrorException {

        Wallet wallet = new Wallet(newWalletFile(name));
        wallet.setPwd(pwd);
        if (!Appwallet.restore_WalletFile(backupPath, wallet.getPath(), pwd, height)) {
            throw new WalletErrorException(getError());
        }

        openWallet(wallet, pwd);
    }

    public WalletBackupInfo checkBackUpWalletFile(String path) throws WalletErrorException {

        String str = Appwallet.check_Backup_WalletFile(path);

        if (StringUtils.isEmpty(str)) {
            throw new WalletErrorException(getError());
        }
        return WalletBackupInfo.from(str);
    }

    public Ping pingNodeAddress(String address) {
        String str = Appwallet.ping_Daemon_Address(address);
        Log.v("luo", str);
        return Ping.from(str);
    }


    public long getSyncDelay() {

        return mMobileWallet.get_Delay_Sync();
    }

    public void setSyncDelay(long l) {

        mMobileWallet.set_Delay_Sync(l);
    }

    /**
     * Get error information
     *
     * @return
     */
    private WalletError getError() {

        return WalletError.from(Appwallet.getLastError());
    }


}
