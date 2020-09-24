package com.darma.wallet.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.CheckUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.orhanobut.logger.Logger
import com.wallet.ErrorCode
import com.wallet.WalletManager
import com.wallet.bean.WalletError
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_create_wallet.*

class CreateWalletActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_create_wallet
    }

    override fun initData() {

    }

    override fun initView() {
        btn_create_wallet.setOnClickListener {
            Logger.v("btn_create_wallet OnClick");


            if (checkInput()) {

                showLoading()
                createWallet()
            }
        }


        et_name.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_name.error = ""
                    btn_create_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_pwd.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_pwd.error = ""
                    btn_create_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_pwd_re.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_pwd_re.error = ""
                    btn_create_wallet.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }

    /**
     *
     * Loading
     *
     */
    private fun showLoading() {

        et_name.isEnabled = false
        et_pwd.isEnabled = false
        et_pwd_re.isEnabled = false

        btn_create_wallet.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_create_wallet.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    override fun onResume() {
        super.onResume()

        et_name.isEnabled = true
        et_pwd.isEnabled = true
        et_pwd_re.isEnabled = true
        btn_create_wallet.progress = 0

    }

    private fun createSuccess() {
        btn_create_wallet.progress = 100

        btn_create_wallet.postDelayed({

            intentTo(MnemonicWordActivity::class.java)
        }, 500)
    }

    private fun createFail(error: WalletError) {

        et_name.isEnabled = true
        et_pwd.isEnabled = true
        et_pwd_re.isEnabled = true
        btn_create_wallet.progress = -1
        showToast(error.errMsg)


        var code = error.errCode
        var msg = error.errMsg

        when (error.errCode) {

            ErrorCode.getErrExist() -> {
                et_name.error = msg
            }
            ErrorCode.getErrInvalidPassword() -> {
                et_pwd.error = msg
            }
        }
    }

    private fun createWallet() {

        var name = et_name.editText?.text.toString()
        var pwd = et_pwd.editText?.text.toString()

        ThreadPoolUtils.getInstance().execute {

            try {
                WalletManager.getInstance().createWallet(name, pwd)
                btn_create_wallet.postDelayed({

                    createSuccess()
                }, 2000)
            } catch (e: WalletErrorException) {
                e.printStackTrace()
                btn_create_wallet.postDelayed({

                    createFail(e.error)
                }, 2000)
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

        return true
    }

    override fun loadData() {

    }


}
