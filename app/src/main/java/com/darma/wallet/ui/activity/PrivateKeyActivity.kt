package com.darma.wallet.ui.activity

import android.view.WindowManager
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.AppUtils
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_private_key.*

class PrivateKeyActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_private_key
    }

    override fun initData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun initView() {

        setTitleText(R.string.str_show_key)


        iv_copy_1.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  et_view_key_open.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }
        iv_copy_2.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  et_view_key_private.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }
        iv_copy_3.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  et_private_key_open.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }
        iv_copy_4.setOnClickListener {
            AppUtils.copyTextToClipboard(this,  et_private_key_private.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }
    }

    override fun loadData() {

        var key=WalletManager.getInstance().privateKey

        et_view_key_open.editText?.setText(key.viewkey_Public)
        et_view_key_private.editText?.setText(key.viewkey_Secret)
        et_private_key_open.editText?.setText(key.spendkey_Public)
        et_private_key_private.editText?.setText(key.spendkey_Secret)
    }

}
