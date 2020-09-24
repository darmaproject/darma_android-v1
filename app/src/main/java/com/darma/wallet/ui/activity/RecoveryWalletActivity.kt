package com.darma.wallet.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_recovery_wallet.*

class RecoveryWalletActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_recovery_wallet
    }

    override fun initData() {

    }

    override fun initView() {
//        fullScreen(this)
        ll_private_key.setOnClickListener {

            intentTo(PrivateKeyRecoveryActivity::class.java)

        }
        ll_view_only.setOnClickListener {

            intentTo(ViewOnlyRecoveryActivity::class.java)

        }
        ll_mnemonic.setOnClickListener {

            intentTo(MnemonicRecoveryActivity::class.java)

        }
    }

    override fun loadData() {
    }

}
