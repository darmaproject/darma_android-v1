package com.darma.wallet.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.wallet.WalletManager
import com.wallet.model.WalletErrorException
import kotlinx.android.synthetic.main.activity_re_scan_block.*

class ReScanBlockActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_re_scan_block
    }

    override fun initData() {
    }

    override fun initView() {

        btn_confirm.setOnClickListener {
            showLoading()
            rescan()
        }
    }

    override fun loadData() {
    }


    /**
     *
     * Loading
     *
     */
    private fun showLoading() {


        btn_confirm.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_confirm.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun success(){
        btn_confirm.progress = 100

        btn_confirm.postDelayed({

            intentToMain()
        },500)
    }
    private fun fail( error:String){

        btn_confirm.progress = -1
        showToast(error)
    }
    private fun rescan() {


        try {
            WalletManager.getInstance().reScanBlock()
            btn_confirm.postDelayed({

                success()
            },2000)
        } catch (e: WalletErrorException) {
            e.printStackTrace()
            btn_confirm.postDelayed({

                fail(e.error.errMsg)
            },2000)
        }

    }
}
