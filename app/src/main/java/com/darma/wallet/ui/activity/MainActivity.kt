package com.darma.wallet.ui.activity

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.bean.ExchangeStatusBean
import com.darma.wallet.bean.PairInfoBean
import com.darma.wallet.net.ApiNetConfig
import com.darma.wallet.net.BaseOnResultListener
import com.darma.wallet.net.HttpHelper
import com.darma.wallet.ui.fragment.HomeFragment
import com.darma.wallet.ui.fragment.SettingFragment
import com.darma.wallet.ui.fragment.TransferFragment
import com.orhanobut.logger.Logger
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    companion object {
        var isOpen = false

    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        isOpen = true


    }


    override fun initView() {

        if (!WalletManager.getInstance().isOpenWallet) {
            Logger.e("no OpenWallet reStartApp")


            reStartApp()
            return
        }

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        nav_view.selectedItemId = R.id.navigation_wallet
//        AppUtils.adjustNavigationIcoSize(this,nav_view)
//        switchFragment(0)
    }

    override fun loadData() {

    }


    private var mHomeFragment: HomeFragment? = null
    private var mTransferFragment: TransferFragment? = null
    private var mSettingFragment: SettingFragment? = null
    private var mIndex = 0

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_wallet -> {
                switchFragment(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_transfer -> {
                switchFragment(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                switchFragment(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 // HomeFragment
            -> mHomeFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.newInstance("", "").let {
                mHomeFragment = it
                transaction.add(R.id.fl_container, it, "home")
            }
            1  //TransferFragment
            -> mTransferFragment?.let {
                transaction.show(it)
            } ?: TransferFragment.newInstance("", "").let {
                mTransferFragment = it
                transaction.add(R.id.fl_container, it, "transfer")
            }
            2  //SettingFragment
            -> mSettingFragment?.let {
                transaction.show(it)

            } ?: SettingFragment.newInstance("", "").let {
                mSettingFragment = it
                transaction.add(R.id.fl_container, it, "setting")
            }
            else -> {

            }
        }

        mIndex = position
        transaction.commitAllowingStateLoss()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        nav_view.selectedItemId = R.id.navigation_wallet

        if (!WalletManager.getInstance().isOpenWallet) {
            Logger.e("no OpenWallet reStartApp")


            reStartApp()
        }
    }

    /**
     * hideFragments
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mTransferFragment?.let { transaction.hide(it) }
        mSettingFragment?.let { transaction.hide(it) }


    }

    override fun onResume() {
        super.onResume()
//        getRate()
//        nav_view.menu.getItem(1).isVisible=true
        getIsEnableTrans()
    }

    private fun getIsEnableTrans() {
        HttpHelper
            .get()
            .url(ApiNetConfig.EXCHANGE_STATUS)
//            .addParams("pair","sero_ltc")
            .execute(object : BaseOnResultListener<ExchangeStatusBean> {
                override fun onSuccess(bean: ExchangeStatusBean?) {
                    if(bean?.data?.status=="1"){
                        nav_view.menu.getItem(1).isVisible=true
                    }else{

                        nav_view.menu.getItem(1).isVisible=false
                    }
                }

                override fun onFailure(code: Int, error: String?) {

                }

            }, ExchangeStatusBean::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        isOpen = false
//        Logger.e("MainActivity onDestroy closeWallet")
//        WalletManager.getInstance().closeWallet()

//        Log.d("luo"," System.exit(0)  -----");
//        System.exit(0)

    }
//    private fun getRate() {
//        OkHttpUtils
//            .get()
//            .url("https://www.bitalong.net/Mapi/Ajax/whpj_usdt")
//            .addParams("coins", "dero")
//            .build()
//            .execute(object : StringCallback() {
//
//                override fun onError(call: Call, e: Exception, id: Int) {}
//
//                override fun onResponse(response: String, id: Int) {
//
//                    try {
//                        val mJSONObject = JSONObject(response)
//
//                        val data = mJSONObject.get("data") as JSONObject
//                        val coin = data.getDouble("dero")
//                        val timestamp = mJSONObject.getLong("timestamp")
//                        val rate = Rate("USD", BigDecimal(coin), timestamp)
//                        WalletManager.getInstance().saveRate(rate)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//
//
//                }
//            })
//    }
}
