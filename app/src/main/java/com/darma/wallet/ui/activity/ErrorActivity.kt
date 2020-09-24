package com.darma.wallet.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.wallet.bean.WalletError
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_error
    }
    var error:WalletError?=null
    override fun initData() {
        error=intent.getSerializableExtra("data") as WalletError
    }

    override fun initView() {

        setTitleText(getString(R.string.str_error))


        error?.let{
            tv_error.setText(it.errMsg)
        }
    }

    override fun loadData() {
    }

}
