package com.darma.wallet.ui.activity

import android.Manifest
import android.view.View
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.AppUtils
import com.darma.wallet.utils.ScreenShotUtils
import com.darma.wallet.utils.ShareUtils
import com.orhanobut.logger.Logger
import com.wallet.bean.Transaction
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_send_success.*
import pub.devrel.easypermissions.EasyPermissions

class SendSuccessActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_send_success
    }

    var trans: Transaction?=null

    override fun initData() {

        trans=intent.getSerializableExtra("data") as Transaction

    }


    private fun checkPermission(){
        val perms = arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE)
        EasyPermissions.requestPermissions(this, getString(R.string.str_permission_request), 0, *perms)

    }
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == 0) {
            if (perms.isNotEmpty()) {
                share()
            }
        }
    }
    private fun share(){

        showLoadingDialog()
        toolbar?.postDelayed( {
            try {
                ShareUtils.share(ScreenShotUtils.getViewBitmap(ll_root),this)
            }catch ( e:Exception){
                e.printStackTrace()

                Logger.w(" checkPermission() ")
                checkPermission()
            }
            dismissLoadingDialog()
        },1000)
    }
    override fun initView() {
        toolbar?.inflateMenu(R.menu.toolbar_share)

        toolbar?.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.share -> {

                    share()

                }
            }
            return@setOnMenuItemClickListener true
        }

        trans?.let{


            if(StringUtils.isEmpty(it.payment_id)){

                et_payment_id.visibility= View.GONE
            }else{

                et_payment_id.editText?.setText(it.payment_id)
            }
            et_amount.editText?.setText(it.amount.toPlainString())
            et_fee.editText?.setText(it.fee.toPlainString())
            et_txid.editText?.setText(it.txid)

        }


        iv_copy_txid.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  et_txid.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }

    }


    override fun loadData() {
    }

    override fun onBackPressed() {
        super.onBackPressed()

        intentToMain()
    }

}
