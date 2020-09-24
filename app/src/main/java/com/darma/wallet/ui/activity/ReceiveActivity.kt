package com.darma.wallet.ui.activity

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.*
import com.orhanobut.logger.Logger
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_receive.*
import pub.devrel.easypermissions.EasyPermissions

class ReceiveActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_receive
    }

    override fun initData() {

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

        iv_change.setOnClickListener {
            ll_payment_id.visibility= View.VISIBLE

            var intergratedAddress=WalletManager.getInstance().intergratedAddress
            et_integrated_address.editText?.setText(intergratedAddress)

            et_payment_id.editText?.setText(WalletManager.getInstance().getPaymentID(intergratedAddress))

            loadData()

        }

        iv_copy_payment.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  et_payment_id.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }

        iv_copy_integrated.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  et_integrated_address.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }
        tv_address.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  tv_address?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }

        et_number.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                loadData()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_number.editText?.setOnKeyListener(object:View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == EditorInfo.IME_ACTION_DONE) {
                    et_payment_id.requestFocus()

                    return true
                }
                return false
            }

        })
        et_payment_id.requestFocus()
    }

    override fun loadData() {

        var address = WalletManager.getInstance().address

        tv_address.setText(address)
        loadAddress(  QrUtils.toShareQRuri(address, getAmount(),getPaymentID()))

    }

    private fun getAmount():String{
        return et_number.editText?.text.toString()
    }
    private fun getPaymentID():String{
        return et_payment_id.editText?.text.toString()
    }
    private fun getIntergratedAddress():String{
        return et_integrated_address.editText?.text.toString()
    }
    private fun loadAddress(addressStr: String) {
        var qrBitmap: Bitmap? = null//Cache.getQrBigBitmapCache();
        if (qrBitmap == null) {
            val r = resources
            val px = AppUtils. convertDpToPx(r, 225)
            Log.i("Util", addressStr)
            qrBitmap = QrUtils.encodeAsBitmap(addressStr, px, px, BLACK, WHITE)
        }
        iv_qr.setImageBitmap(qrBitmap)
    }
}
