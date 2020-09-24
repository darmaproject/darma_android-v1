package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.darma.wallet.Config
import com.darma.wallet.Config.*
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.ContactsDB
import com.darma.wallet.event.EventConfigs
import com.darma.wallet.event.RequestCode.*
import com.darma.wallet.utils.QrUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.darma.wallet.utils.dialog.EnterPwdDialog
import com.wallet.ErrorCode
import com.wallet.WalletManager
import com.wallet.bean.Transaction
import com.wallet.bean.WalletError
import com.wallet.bean.WalletStatusInfoBean
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_send.*
import org.simple.eventbus.Subscriber

class SendActivity : BaseActivity() {


    var sendAll = false

    override fun layoutId(): Int {
        return R.layout.activity_send
    }

    override fun initData() {
    }

    override fun initView() {


        tv_wallet.setText(WalletManager.getInstance().openWallet.name)
        tv_balance.setText(WalletManager.getInstance().getUnlockedBalance().toPlainString())

        btn_send.setOnClickListener {


            if (checkInput()) {

                showLoading()
                send()
            }
        }
        iv_scan.setOnClickListener {
            intentTo(ScanActivity::class.java, REQUEST_SCAN)
        }
        iv_contacts.setOnClickListener {
            var bundle= Bundle()
            bundle.putString(COIN_CODE,COIN_DARMA)
            intentTo(ContactsActivity::class.java, REQUEST_SELECT_ADDRESS,bundle)
        }
        et_address.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_address.error = ""
                    btn_send.setProgress(0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_payment_id.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_payment_id.error = ""
                    btn_send.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        et_amount.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_amount.error = ""
                    btn_send.setProgress(0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        iv_all.setOnClickListener {
            if (sendAll) {
                sendAll = false

//                et_amount.hint = getString(R.string.str_money)
                et_amount.editText?.setText("")
                et_amount.error=""
                et_amount.isEnabled = true
                tv_send_all_notice.visibility = View.GONE
            } else {

                sendAll = true
//                et_amount.hint = getString(R.string.str_send_all)
                et_amount.editText?.setText("")
                et_amount.error=""
                et_amount.isEnabled = false
                tv_send_all_notice.visibility = View.VISIBLE
            }
        }

//        et_desc.editText?.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                if(!StringUtils.isEmpty(s.toString())){
//                    et_desc.error=""
//                    btn_send.setProgress(0)
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//        })
    }

    /**
     *
     * Loading
     *
     */
    private fun showLoading() {

        et_address.isEnabled = false
        et_payment_id.isEnabled = false
        et_amount.isEnabled = false
//        et_desc.isEnabled=false

        btn_send.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_send.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun sendSuccess(trans: Transaction) {
        btn_send.progress = 100

        btn_send.postDelayed({

//            trans.receive_address = getAddress()
            var intent = Intent(this, SendConfirmActivity::class.java)
            intent.putExtra(Config.FROM, SendConfirmActivity.FROM_SEND)
            intent.putExtra(SendConfirmActivity.TRANS_DATA, trans)
            startActivity(intent)
        }, 500)
    }

    private fun sendFail(error: WalletError) {

        enabledInput()

        btn_send.progress = -1

        password=""

        var code = error.errCode
        var msg = error.errMsg
        showToast(msg)
        when (error.errCode) {

            ErrorCode.getErrInvalidAddress() -> {
                et_address.error = msg
            }

            ErrorCode.getErrInvalidPaymentID() -> {

                et_payment_id.error = msg
            }

            ErrorCode.getErrInvalidAmount() -> {
                et_amount.error = msg
            }

        }
    }

    private fun getAddress(): String {
        return et_address.editText?.text.toString()
    }

    private fun getPaymentId(): String {
        return et_payment_id.editText?.text.toString()
    }

    private fun getAmount(): String {
        return et_amount.editText?.text.toString()
    }


    var password="";
    private fun send() {

        var address = getAddress()
        var payment_id = getPaymentId()
        var amount = getAmount()
//        var desc = et_desc.editText?.text.toString()

        ThreadPoolUtils.getInstance().execute {

            try {

                var trans: Transaction? = null
                if (sendAll) {
                    trans = WalletManager.getInstance().sendAll(address, payment_id, password)

                } else {
                    trans = WalletManager.getInstance().createTransfer(address, amount, payment_id, password)

                }
                btn_send.postDelayed({

                    sendSuccess(trans)
                }, 2000)


            } catch (e: WalletErrorException) {
                e.printStackTrace()
                btn_send.postDelayed({


                    if(e.error.errCode==ErrorCode.getErrInvalidPassword()){

                        EnterPwdDialog.createDialog(this)
                            .setCheckPassWordListener(object :
                                EnterPwdDialog.CheckPassWordListener {
                                override fun success(password: String) {

                                    this@SendActivity.password=password

                                    send()
                                }

                                override fun cancel() {

                                    sendFail(e.error)
                                }

                            })
                            .show()
                    }else {

                        sendFail(e.error)
                    }
                }, 2000)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        enabledInput()

        btn_send.progress = 0
    }

    fun enabledInput() {

        et_address.isEnabled = true
        et_payment_id.isEnabled = true
//        et_desc.isEnabled=true
        if (!sendAll) {

            et_amount.isEnabled = true
        }
    }

    private fun checkInput(): Boolean {

        var checkEmpty = false
        var address = getAddress()

        if (StringUtils.isEmpty(address)) {
            et_address.error = getString(R.string.str_please_input_receive_address)
            checkEmpty = true
        }
        var amount = getAmount()

        if (StringUtils.isEmpty(amount) && !sendAll) {
            et_amount.error = getString(R.string.str_please_input_amount)
            checkEmpty = true
        }
        if (checkEmpty) {
            return false
        }


        return true
    }

    @Subscriber(tag = EventConfigs.EVENT_WALLET_STATUS_INFO)
    fun getWalletStatusInfoEven(even: WalletStatusInfoBean) {

        tv_balance.setText(even.unlocked_balance.toPlainString())


    }

    override fun loadData() {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        when (requestCode) {
           REQUEST_SCAN -> {
                var res = data?.getStringExtra(ScanActivity.INTENT_EXTRA_RESULT)

                if (!StringUtils.isEmpty(res)) {
                    var map = QrUtils.getUriParams(res)
                    if (map == null || map.isEmpty()) {

                        showToast(getString(R.string.str_invalid_qr_code))
                    } else {

                        map.get(QrUtils.URI_ADDRESS)?.let {
                            et_address.editText?.setText(it)
                        }

                        map.get(QrUtils.URI_AMOUNT)?.let {
                            et_amount.editText?.setText(it)
                        }

                        var payment_id = map.get(QrUtils.URI_PAYMENT_ID)
                        if (!StringUtils.isEmpty(payment_id)) {
                            et_payment_id.editText?.setText(payment_id)
                        } else {

                            et_payment_id.editText?.setText("")
                        }
                    }
                }

            }
            REQUEST_SELECT_ADDRESS -> {
                var item = data?.getSerializableExtra(DATA) as ContactsDB

                item?.let {
                    et_address.editText?.setText(it.address)

                    var payment_id = it.payment_id
                    if (!StringUtils.isEmpty(payment_id)) {
                        et_payment_id.editText?.setText(payment_id)
                    } else {

                        et_payment_id.editText?.setText("")
                    }
                }

            }
        }
    }
}
