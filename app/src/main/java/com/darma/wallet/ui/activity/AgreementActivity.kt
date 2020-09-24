package com.darma.wallet.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import kotlinx.android.synthetic.main.activity_agreement.*

class AgreementActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_agreement
    }

    override fun initData() {
    }

    override fun initView() {
        setTitleText(R.string.str_agreement)
        webView.loadUrl("file:///android_asset/agreement.html")
    }

    override fun loadData() {

    }

}
