package com.darma.wallet.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.CheckUtils
import com.darma.wallet.utils.FileUtils
import com.wallet.ErrorCode
import com.wallet.bean.WalletError
import com.wallet.model.Wallet
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_edit_wallet_name.*

class EditWalletNameActivity : BaseActivity() {


    var wallet: Wallet? = null

    override fun layoutId(): Int {
        return R.layout.activity_edit_wallet_name
    }

    override fun initData() {
        wallet = intent.getSerializableExtra("data") as Wallet
    }


    override fun initView() {


        setTitleText(R.string.str_re_name)

        btn_commit.setOnClickListener {


            if (checkInput()) {

                showLoading()
                createWallet()
            }
        }


        et_name.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_name.error = ""
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

        et_name.isEnabled = false

        btn_commit.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_commit.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun createSuccess() {
        btn_commit.progress = 100

        btn_commit.postDelayed({

            onBackPressed()

        }, 500)
    }

    private fun createFail(error: WalletError) {

        et_name.isEnabled = true
        btn_commit.progress = -1


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


        }
    }

    private fun createWallet() {

        var name = et_name.editText?.text.toString()

        try {
            FileUtils.editWalletName(this, wallet, name)
            btn_commit.postDelayed({

                createSuccess()
            }, 2000)
        } catch (e: WalletErrorException) {
            e.printStackTrace()
            btn_commit.postDelayed({

                createFail(e.error)
            }, 2000)
        } catch (e: Exception) {

            e.printStackTrace()

            btn_commit.postDelayed({

                e.message?.let { createFail(WalletError.create(ErrorCode.DEFALUT,it)) }
            }, 2000)
        }

    }


    private fun checkInput(): Boolean {

        var checkEmpty = false
        var name = et_name.editText?.text.toString()

        if (StringUtils.isEmpty(name)) {
            et_name.error = getString(R.string.str_please_input_wallet_name)
            checkEmpty = true
        }

        if (checkEmpty) {
            return false
        }



        if (!CheckUtils.isLegalWalletName(name)) {
            et_name.error = getString(R.string.str_wallet_name_rule)
            return false
        }



        return true
    }

    override fun loadData() {
    }


}
