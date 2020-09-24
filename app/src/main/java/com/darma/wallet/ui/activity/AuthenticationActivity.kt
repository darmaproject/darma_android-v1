package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.CheckUtils
import com.wallet.ErrorCode
import com.wallet.WalletManager
import com.wallet.bean.WalletError
import com.wallet.model.Wallet
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_authentication.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AuthenticationActivity : BaseActivity() {


    var wallet:Wallet?=null

    override fun layoutId(): Int {

        return R.layout.activity_authentication
    }

    override fun initData() {

        if (WalletManager.getInstance().isOpenWallet) {
            WalletManager.getInstance().closeWallet()
        }
        wallet=WalletManager.getInstance().lastOpenWallet

        if(wallet==null){
            wallet=WalletManager.getInstance().wallets[0];
        }
    }

    override fun initView() {

        et_pwd.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(!StringUtils.isEmpty(s.toString())){
                    et_pwd.error=""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        btn_commit.setOnClickListener {


            if (checkInput()) {

                showLoading()
                openWallet()
            }
        }

        cl_wallet.setOnClickListener {
            intentTo(WalletListActivity::class.java,1001)
        }
        tv_wallet.text=wallet?.name
    }

    override fun loadData() {

//        et_pwd.editText?.setText("123456")
//        openWallet()
    }

    private fun openWallet() {

        var pwd = et_pwd.editText?.text.toString()

        try {
            WalletManager.getInstance().openWallet(wallet, pwd)
            btn_commit.postDelayed({

                success()
            },2000)
        } catch (e: WalletErrorException) {
            e.printStackTrace()

            btn_commit.postDelayed({

                fail(e.error)
            },2000)
        }


    }

    /**
     *
     * Loading
     *
     */
    private fun showLoading() {

        et_pwd.isEnabled=false

        btn_commit.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_commit.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun success(){
        btn_commit.progress = 100

        btn_commit.postDelayed({

            finish()
            intentToMain()
        },500)
    }
    private fun fail(error: WalletError){

        et_pwd.isEnabled=true
        btn_commit.progress = -1
//        et_pwd.error=error


        var code=error.errCode
        var msg=error.errMsg

        when(error.errCode){

            ErrorCode.getErrInvalidPassword()->{
                et_pwd.error=msg
            }
            ErrorCode.getErrPaswordMisMatch()->{
                et_pwd.error=msg
            }
            else -> et_pwd.error=msg

        }
    }
    private fun checkInput(): Boolean {

        var pwd = et_pwd.editText?.text.toString()

        if (StringUtils.isEmpty(pwd)) {
            et_pwd.error = getString(R.string.str_please_input_pwd)

            return false
        }

        if (!CheckUtils.isLegalPwd(pwd)) {
            et_pwd.error = getString(R.string.str_wallet_pwd_rule)
            return false
        }

        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            1001->{
                if(resultCode== Activity.RESULT_OK){
                    wallet= data?.getSerializableExtra("data") as Wallet?
                    tv_wallet.text=wallet?.name

                }
            }
        }
    }
}
