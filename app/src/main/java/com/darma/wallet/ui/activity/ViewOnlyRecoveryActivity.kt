package com.darma.wallet.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.CheckUtils
import com.wallet.ErrorCode
import com.wallet.WalletManager
import com.wallet.bean.WalletError
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_view_only_recovery.*

class ViewOnlyRecoveryActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_view_only_recovery
    }

    override fun initData() {
    }

    override fun initView() {


        btn_recovery_wallet.setOnClickListener {

            if (checkInput()) {

                showLoading()
                createWallet()
            }
        }


        et_name.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(!StringUtils.isEmpty(s.toString())){
                    et_name.error=""
                    btn_recovery_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_pwd.editText?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!StringUtils.isEmpty(s.toString())){
                    et_pwd.error=""
                    btn_recovery_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_pwd_re.editText?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!StringUtils.isEmpty(s.toString())){
                    et_pwd_re.error=""
                    btn_recovery_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_height.editText?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!StringUtils.isEmpty(s.toString())){
                    et_height.error=""
                    btn_recovery_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_private_key_open.editText?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!StringUtils.isEmpty(s.toString())){
                    et_private_key_open.error=""
                    btn_recovery_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_view_key_private.editText?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!StringUtils.isEmpty(s.toString())){
                    et_view_key_private.error=""
                    btn_recovery_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })


    }

    override fun loadData() {

    }

    /**
     *
     * Loading
     *
     */
    private fun showLoading() {

        et_name.isEnabled=false
        et_pwd.isEnabled=false
        et_pwd_re.isEnabled=false
        et_height.isEnabled=false


        et_private_key_open.isEnabled=false
        et_view_key_private.isEnabled=false

        btn_recovery_wallet.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_recovery_wallet.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }
    private fun createWallet() {

        var name = et_name.editText?.text.toString()
        var pwd = et_pwd.editText?.text.toString()
        var private_key = et_private_key_open.editText?.text.toString()
        var view_key = et_view_key_private.editText?.text.toString()

        var heightStr = et_height.editText?.text.toString()
        var height= 0L
        if(!StringUtils.isEmpty(heightStr)){
            height=heightStr.toLong()
        }
        try {
            WalletManager.getInstance().recoveryWalletByViewOnlyKey(name, pwd,height,private_key+view_key)
            btn_recovery_wallet.postDelayed({

                createSuccess()
            },2000)
        } catch (e: WalletErrorException) {
            e.printStackTrace()
            btn_recovery_wallet.postDelayed({

                createFail(e.error)
            },2000)
        }

    }

    private fun createSuccess(){
        btn_recovery_wallet.progress = 100

        btn_recovery_wallet.postDelayed({

            intentToMain()
        },500)
    }
    private fun createFail( error: WalletError){

        et_name.isEnabled=true
        et_pwd.isEnabled=true
        et_pwd_re.isEnabled=true
        et_height.isEnabled=true
        et_private_key_open.isEnabled=true
        et_view_key_private.isEnabled=true
        btn_recovery_wallet.progress = -1


        var code=error.errCode
        var msg=error.errMsg
        showToast(msg)
        when(error.errCode){

            ErrorCode.getErrInvalidFileName()->{
                et_name.error=msg
            }

            ErrorCode.getErrExist()->{
                et_name.error=msg
            }

            ErrorCode.getErrInvalidPassword()->{
                et_pwd.error=msg
            }

            ErrorCode.getErrInvalidKeys()->{
                et_view_key_private.error=msg
            }
        }

    }
    private fun checkInput(): Boolean {

        var checkEmpty = false
        var name = et_name.editText?.text.toString()

        if (StringUtils.isEmpty(name)) {
            et_name.error = getString(R.string.str_please_input_wallet_name)
            checkEmpty = true
        }
        var pwd = et_pwd.editText?.text.toString()

        if (StringUtils.isEmpty(pwd)) {
            et_pwd.error = getString(R.string.str_please_input_pwd)
            checkEmpty = true
        }
        var pwdre = et_pwd_re.editText?.text.toString()
        if (StringUtils.isEmpty(pwdre)) {
            et_pwd_re.error = getString(R.string.str_please_input_re_pwd)
            checkEmpty = true
        }

        var private_key = et_private_key_open.editText?.text.toString()
        if (StringUtils.isEmpty(private_key)) {
            et_private_key_open.error = getString(R.string.str_please_input_private_key_open)
            checkEmpty = true
        }

        var view_key = et_view_key_private.editText?.text.toString()
        if (StringUtils.isEmpty(view_key)) {
            et_view_key_private.error = getString(R.string.str_please_input_view_keys_private)
            checkEmpty = true
        }
        if (checkEmpty) {
            return false
        }


        if (!CheckUtils.isLegalWalletName(name)) {
            et_name.error = getString(R.string.str_wallet_name_rule)
            return false
        }

        if (!CheckUtils.isLegalPwd(pwd)) {
            et_pwd.error = getString(R.string.str_wallet_pwd_rule)
            return false
        }
        if (pwd != pwdre) {
            et_pwd_re.error = getString(R.string.str_two_inconsistent_passwords)
            return false
        }
        if (!CheckUtils.isLegalViewOnlyKey(private_key)) {
            et_private_key_open.error = getString(R.string.str_private_key_open_64_chars)
            return false
        }
        if (!CheckUtils.isLegalViewOnlyKey(view_key)) {
            et_view_key_private.error = getString(R.string.str_view_key_private_64_chars)
            return false
        }
        return true
    }


}
