package com.darma.wallet.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.CheckUtils
import com.wallet.WalletManager
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_edit_pwd.*

class EditPwdActivity : BaseActivity() {




    override fun layoutId(): Int {
        return R.layout.activity_edit_pwd
    }

    override fun initData() {
    }


    override fun initView() {


        setTitleText(getString(R.string.str_change_pwd))

        btn_commit.setOnClickListener {


            if (checkInput()) {

                showLoading()


                create()
            }
        }


        et_last_pwd.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_last_pwd.error = ""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_new_pwd.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_new_pwd.error = ""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_re_pwd.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_re_pwd.error = ""
                    btn_commit.setProgress(0);
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

        et_last_pwd.isEnabled = false
        et_new_pwd.isEnabled = false
        et_re_pwd.isEnabled = false
        btn_commit.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_commit.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun createSuccess() {
        btn_commit.progress = 100

        btn_commit.postDelayed({

            onBackPressed()

        }, 500)
    }

    private fun createFail(error: String) {


        et_last_pwd.isEnabled = true
        et_new_pwd.isEnabled = true
        et_re_pwd.isEnabled = true
        btn_commit.progress = -1
        showToast(error)
    }

    private fun create() {


        var last_pwd = et_last_pwd.editText?.text.toString()
        var new_pwd = et_new_pwd.editText?.text.toString()


            try {



                WalletManager.getInstance().changePwd(new_pwd)

                btn_commit.postDelayed({

                    createSuccess()
                }, 2000)
            } catch (e: WalletErrorException) {
                e.printStackTrace()
                btn_commit.postDelayed({

                    createFail(e.error.errMsg)
                }, 2000)
            } catch (e: Exception) {

                e.printStackTrace()

                btn_commit.postDelayed({

                    e.message?.let { createFail(it) }
                }, 2000)
            }
    }


    private fun checkInput(): Boolean {

        var checkEmpty = false
        var last_pwd = et_last_pwd.editText?.text.toString()

        if (StringUtils.isEmpty(last_pwd)) {
            et_last_pwd.error = getString(R.string.str_please_input_last_pwd)
            checkEmpty = true
        }
        var new_pwd = et_new_pwd.editText?.text.toString()

        if (StringUtils.isEmpty(new_pwd)) {
            et_new_pwd.error = getString(R.string.str_please_input_new_pwd)
            checkEmpty = true
        }


        var re_pwd = et_re_pwd.editText?.text.toString()
        if (StringUtils.isEmpty(re_pwd)) {
            et_re_pwd.error = getString(R.string.str_please_re_input_pwd)
            checkEmpty = true
        }
        if (checkEmpty) {
            return false
        }


        if (!CheckUtils.isLegalPwd(last_pwd)) {
            et_last_pwd.error = getString(R.string.str_wallet_pwd_rule)
            return false
        }
        if (!CheckUtils.isLegalPwd(new_pwd)) {
            et_new_pwd.error = getString(R.string.str_wallet_pwd_rule)
            return false
        }
        if (last_pwd == new_pwd) {
            et_new_pwd.error = getString(R.string.str_new_pwd_equal_last_pwd)
            return false
        }

        if (re_pwd != new_pwd) {
            et_re_pwd.error = getString(R.string.str_two_inconsistent_passwords)
            et_new_pwd.error = getString(R.string.str_two_inconsistent_passwords)
            return false
        }


        if(WalletManager.getInstance().openWallet.pwd!=last_pwd){

            et_last_pwd.error = getString(R.string.str_pwd_error)

            return false
        }
        return true
    }

    override fun loadData() {
    }

}
