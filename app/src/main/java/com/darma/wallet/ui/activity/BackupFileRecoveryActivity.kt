package com.darma.wallet.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.CheckUtils
import com.wallet.ErrorCode
import com.wallet.WalletManager
import com.wallet.bean.WalletBackupInfo
import com.wallet.bean.WalletError
import com.wallet.model.WalletErrorException
import com.wallet.utils.DateUtils
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_backup_file_recovery.*

class BackupFileRecoveryActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_backup_file_recovery
    }

    var info: WalletBackupInfo? = null

    override fun initData() {
        info = intent.getSerializableExtra("data") as WalletBackupInfo

        if (info == null) {
            finish()
        }
    }

    override fun initView() {

        setTitleText(R.string.str_recover_from_backup)

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
        et_pwd.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_pwd.error = ""
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
                btn_recovery_wallet.setProgress(0);
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        info?.let {

            et_name.editText?.setText(it.name)
            et_height.editText?.setText(""+it.height)
            et_time.editText?.setText(DateUtils.scendToString(it.ctime))
            et_file_name.editText?.setText(""+it.getFileName())
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

        et_pwd.isEnabled = false
        et_height.isEnabled = false
        et_name.isEnabled = false

        btn_recovery_wallet.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_recovery_wallet.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun createWallet() {

        var name = et_name.editText?.text.toString()
        var pwd = et_pwd.editText?.text.toString()
        var heightStr = et_height.editText?.text.toString()
        var height= 0L
        if(!StringUtils.isEmpty(heightStr)){
            height=heightStr.toLong()
        }
        try {
            WalletManager.getInstance().restoreWalletFile(info?.path,name, pwd,height)

            btn_recovery_wallet.postDelayed({

                createSuccess()
            }, 2000)
        } catch (e: WalletErrorException) {
            e.printStackTrace()
            btn_recovery_wallet.postDelayed({

                createFail(e.error)
            }, 2000)
        }

    }

    private fun createSuccess() {
        btn_recovery_wallet.progress = 100

        btn_recovery_wallet.postDelayed({

            finish()
            intentTo(MainActivity::class.java)
        }, 500)
    }

    private fun createFail(error: WalletError) {

        et_pwd.isEnabled = true
        et_height.isEnabled = true
        et_name.isEnabled = true
        btn_recovery_wallet.progress = -1

        showToast(error.errMsg)


        var code=error.errCode
        var msg=error.errMsg

        when(error.errCode){
            ErrorCode.getErrBadWalletFile()->{
                et_pwd.error=msg
            }
            ErrorCode.getErrInvalidPassword()->{
                et_pwd.error=msg
            }
            ErrorCode.getErrPaswordMisMatch()->{
                et_pwd.error=msg
            }
            ErrorCode.getErrInvalidFileName()->{
                et_name.error=msg
            }

            ErrorCode.getErrExist()->{
                et_name.error=msg
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
        return true
    }


}
