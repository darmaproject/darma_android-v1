package com.darma.wallet

import android.app.Application
import android.os.Build
import com.darma.wallet.Config.TIME_OUT
import com.darma.wallet.bean.LanguageType
import com.darma.wallet.db.WalletDB
import com.darma.wallet.db.WalletDataBase
import com.darma.wallet.event.EventConfigs
import com.darma.wallet.utils.FileUtils
import com.darma.wallet.utils.LanguageUtil
import com.darma.wallet.utils.NodeUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.google.gson.Gson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.Logger.addLogAdapter
import com.parkingwang.okhttp3.LogInterceptor.LogInterceptor
import com.wallet.WalletConfig.SELECTED_NODE
import com.wallet.WalletManager
import com.wallet.bean.Node
import com.wallet.bean.WalletStatusInfoBean
import com.wallet.model.AppModel
import com.wallet.model.Wallet
import com.wallet.utils.StringUtils
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.OkHttpClient
import org.simple.eventbus.EventBus
import java.io.File
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.properties.Delegates


class MyApplication : Application(), AppModel {


    companion object {
        var context: MyApplication by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()

        context = this


        initWalletManager()

        initLogger()

        initFile()

        initLanguage()
        initOkhttp()
    }


    private fun initLanguage() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

            LanguageUtil.changeAppLanguage(this, getSelectLanguage())

        }
    }

    fun getSelectLanguage(): String {

        return LanguageUtil.getSelectLanguage(this)
    }

    private fun initFile() {
        FileUtils.init(this)
    }

    private fun initLogger() {

        if (BuildConfig.DEBUG) {

            addLogAdapter(AndroidLogAdapter())
        }
    }

    private fun initOkhttp() {
        val okHttpClient: OkHttpClient
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)//
            //                .sslSocketFactory(new NoSSLv3Factory())
            //                .addInterceptor(new LogInterceptor())
            .sslSocketFactory(getUnsafeSSLSocketFactory())
            .hostnameVerifier { hostname, session -> true }
        if (BuildConfig.DEBUG) {

            builder.addInterceptor(LogInterceptor())
        }
        okHttpClient = builder.build()

        OkHttpUtils.initClient(okHttpClient)
    }
    private fun getUnsafeSSLSocketFactory(): SSLSocketFactory {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager

            return sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun initWalletManager() {

        WalletManager.getInstance().setWalletFilesPath(filesDir.absolutePath + File.separator + "wallet")

        WalletManager.getInstance().setAppContext(this, this)
    }


    override fun getSelectNode(): Node {

        var str = StringUtils.getStringFromSp(context, SELECTED_NODE, "")

        var node = NodeUtils.NODE_AUTO.toNode()

        try {
            if (!StringUtils.isEmpty(str)) {
                node = Gson().fromJson(str, Node::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (StringUtils.isEmpty(node.url)) {
            var url = ""
            for (item in NodeUtils.getDefaultNodes(this)) {
                if (item.id != NodeUtils.NODE_AUTO.id) {
                    url = url + "," + item.url
                }
            }
            url = url.substring(1)
            node.url = url
        }
        return node
    }

    override fun saveSelectNode(node: Node?) {

        node?.let {
            ThreadPoolUtils.getInstance().execute {


                Logger.d(node)


                var url = ""
                if (node.id == NodeUtils.NODE_AUTO.id) {

                    for (item in NodeUtils.getNodes(this)) {
                        if (item.id != NodeUtils.NODE_AUTO.id) {
                            url = url + "," + item.url
                        }
                    }
                    url = url.substring(1)
                } else {
                    url = node.url
                }
                node.url = url
                StringUtils.saveStringToSp(context, SELECTED_NODE, Gson().toJson(node))

                Logger.e(url)

                WalletManager.getInstance().setNodeAddress(url)
            }
        }
    }

    override fun saveWalletToDB(wallet: Wallet?) {
        ThreadPoolUtils.getInstance().execute {


            WalletDataBase.getInstance(this)?.walletDao?.getWallet(wallet?.name)?.let {

                it.updateTime = System.currentTimeMillis()
                it.address = WalletManager.getInstance().getAddress()
                WalletDataBase.getInstance(this)?.walletDao?.update(it)
                return@execute
            }

            var walletDb = WalletDB()
            walletDb.address = WalletManager.getInstance().getAddress();
            walletDb.updateTime = System.currentTimeMillis();
            walletDb.createTime = System.currentTimeMillis();
            walletDb.name = WalletManager.getInstance().openWallet.name
            walletDb.path = WalletManager.getInstance().openWallet.path
            walletDb.pwd = WalletManager.getInstance().openWallet.pwd
            WalletDataBase.getInstance(this)?.walletDao?.insert(walletDb)
        }
    }

    override fun onWalletStatusInfo(info: WalletStatusInfoBean?) {

        EventBus.getDefault().post(info, EventConfigs.EVENT_WALLET_STATUS_INFO)
    }
}
