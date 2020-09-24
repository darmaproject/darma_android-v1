package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.ContactsDB
import com.darma.wallet.db.WalletDataBase
import com.darma.wallet.event.RequestCode
import com.darma.wallet.utils.QrUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_edit_contacts.*

class EditContactsActivity : BaseActivity() {


    var contacts: ContactsDB? = null

    override fun layoutId(): Int {
        return R.layout.activity_edit_contacts
    }

    var coin_code=""

    override fun initData() {
        coin_code=intent.getStringExtra(Config.COIN_CODE)
        intent.getSerializableExtra("data")?.let{

            contacts = it as ContactsDB
        }
    }


    override fun initView() {


        setTitleText(getString(R.string.str_address_book))

        btn_commit.setOnClickListener {


            if (checkInput()) {

                showLoading()


                insertOrUpdate()
            }
        }


        iv_scan.setOnClickListener {

            intentTo(ScanActivity::class.java,RequestCode.REQUEST_SCAN)
        }
        et_remark.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_remark.error = ""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_address.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_address.error = ""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        contacts?.let{
            et_address.editText?.setText(it.address)
            et_remark.editText?.setText(it.remark)
            et_payment_id.editText?.setText(it.payment_id)
        }
    }

    /**
     *
     * Loading
     *
     */
    private fun showLoading() {

        et_remark.isEnabled = false
        et_address.isEnabled = false
        et_payment_id.isEnabled = false
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

        et_remark.isEnabled = true
        et_address.isEnabled = true
        et_payment_id.isEnabled = true
        btn_commit.progress = -1
        showToast(error)
    }

    private fun insertOrUpdate() {


        var remark = et_remark.editText?.text.toString()
        var address = et_address.editText?.text.toString()
        var payment_id = et_payment_id.editText?.text.toString()

        ThreadPoolUtils.getInstance().execute {
        try {

            if (contacts == null) {
                contacts = ContactsDB()
                contacts?.address = address
                contacts?.remark = remark

                contacts?.coin_code=coin_code
                contacts?.payment_id=payment_id

                    WalletDataBase.getInstance(this).contactsDao.insert(contacts)

            } else {

                contacts?.address = address
                contacts?.remark = remark
                contacts?.payment_id=payment_id

                    WalletDataBase.getInstance(this).contactsDao.update(contacts)


            }


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
    }


    private fun checkInput(): Boolean {

        var checkEmpty = false
        var name = et_remark.editText?.text.toString()

        if (StringUtils.isEmpty(name)) {
            et_remark.error = getString(R.string.str_please_input_remark)
            checkEmpty = true
        }
        var address = et_address.editText?.text.toString()

        if (StringUtils.isEmpty(address)) {
            et_address.error = getString(R.string.str_please_input_receive_address)
            checkEmpty = true
        }

        if (checkEmpty) {
            return false
        }


        return true
    }

    override fun loadData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        when (requestCode) {
            RequestCode.REQUEST_SCAN -> {
                var res = data?.getStringExtra(ScanActivity.INTENT_EXTRA_RESULT)

                if (!StringUtils.isEmpty(res)) {
                    var map = QrUtils.getUriParams(res)
                    if (map == null || map.isEmpty()) {

                        showToast(getString(R.string.str_invalid_qr_code))
                    } else {

                        map.get(QrUtils.URI_ADDRESS)?.let {
                            et_address.editText?.setText(it)
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
        }
    }
}
