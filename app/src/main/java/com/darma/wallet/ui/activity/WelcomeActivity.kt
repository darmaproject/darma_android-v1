package com.darma.wallet.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.FileUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.orhanobut.logger.Logger
import com.wallet.ErrorCode
import com.wallet.WalletManager
import com.wallet.bean.WalletError
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_welcome.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class WelcomeActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initData() {


    }

    override fun initView() {

        val uri = intent.data

        if (uri != null && !StringUtils.isEmpty(uri.path)) {

            checkPermission()
        } else {

            goMain()
        }
    }

    private fun goCheck() {
        val uri = intent.data

        if (uri != null && !StringUtils.isEmpty(uri.path)) {
//            val scheme = uri.scheme
//            val host = uri.host
//            val port = uri.port.toString() + ""
//            path = uri.path
//            val query = uri.query

            Logger.w("uri = " + uri.toString())


//            checkFile(UriUtils.getPath(this,uri))
            ThreadPoolUtils.getInstance().execute {

                checkFile(uri)
            }
        }

    }


    override fun loadData() {


    }

    var hasCheck = false
    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        EasyPermissions.requestPermissions(this, getString(R.string.str_permission_request), 0, *perms)
        hasCheck = true
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == 0) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                goCheck()
            }
        }
    }

    fun checkFile(uri: Uri) {

//        Logger.w("path = "+path)

        try {


//            Logger.w("checkFile exists = "+checkFile.exists())
            var file = File(FileUtils.getTempFilePath(this), uri.lastPathSegment)
            file.createNewFile()
            FileUtils.copyFile(this, uri, file)

            Logger.w("path = " + file.absolutePath)
            var info = WalletManager.getInstance().checkBackUpWalletFile(file.absolutePath)

            if (info != null && !StringUtils.isEmpty(info.name)) {
                info.path = file.absolutePath
                var intent = Intent(this, BackupFileRecoveryActivity::class.java)
                intent.putExtra("data", info)
                startActivity(intent)

                finish()

            }
//            file.delete()

        } catch (w: WalletErrorException) {

            w.printStackTrace()

            if (w.error.errCode == ErrorCode.getErrInvalidFileName() && !hasCheck) {

            } else {
                showToast(w.error.errMsg)
                finish()
                var intent = Intent(this, ErrorActivity::class.java)
                intent.putExtra("data", w.error)
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            var message = ""
            e.message?.let {
                message = it
            }
            showToast(message)

            finish()
            var intent = Intent(this, ErrorActivity::class.java)
            intent.putExtra("data", WalletError.create(0, message))
            startActivity(intent)
        }
    }

    fun goMain() {
        root.postDelayed({
           if (WalletManager.getInstance().wallets != null && WalletManager.getInstance().wallets.size != 0) {

                intentTo(AuthenticationActivity::class.java)
                finish()
            } else {

                intentTo(InitialActivity::class.java)
                finish()
            }
        }, 2000)
    }

}
