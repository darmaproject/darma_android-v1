package com.darma.wallet.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_initial.*
import java.lang.NullPointerException

class InitialActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_initial
    }

    override fun initData() {


    }

    override fun initView() {
//        fullScreen(this)
        btn_create_wallet.setOnClickListener {

            intentTo(CreateWalletActivity::class.java)

        }

        btn_recovery_wallet.setOnClickListener {


            intentTo(RecoveryWalletActivity::class.java)
        }
    }

    override fun loadData() {
    }

}
