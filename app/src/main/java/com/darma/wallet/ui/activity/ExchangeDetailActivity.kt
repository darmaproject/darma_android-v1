package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.bean.OrderBean
import com.darma.wallet.db.OrderDB
import com.darma.wallet.db.OrderDataBase
import com.darma.wallet.net.ApiNetConfig
import com.darma.wallet.net.BaseOnResultListener
import com.darma.wallet.net.HttpHelper
import com.darma.wallet.utils.AppUtils
import com.darma.wallet.utils.DateTimeUtils
import com.darma.wallet.utils.QrUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.darma.wallet.utils.dialog.EnterPwdDialog
import com.wallet.ErrorCode
import com.wallet.WalletManager
import com.wallet.bean.Transaction
import com.wallet.bean.WalletError
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_exchange_detail.*

/**
 *
 * Exchange details page
 *
 */
class ExchangeDetailActivity : BaseActivity() {

    companion object{
        const val REQUEST_CODE_SEND_CONFIRM=1001

    }


    override fun layoutId(): Int {
        return R.layout.activity_exchange_detail
    }

    var orderBean: OrderBean.Data? = null
    override fun initData() {
        orderBean = intent.getSerializableExtra(Config.DATA) as OrderBean.Data?

        setOrderData(orderBean)

    }

    override fun initView() {

        setTitleText(getString(R.string.str_trade_details))

        iv_copy_order_id.setOnClickListener {
            copyText(et_order_id.editText?.text.toString())
        }
        iv_copy_address.setOnClickListener {
            copyText(et_address.editText?.text.toString())
        }
    }

    override fun loadData() {
        getOrderDetailData()
    }


    private fun getOrderDetailData() {

        showLoadingDialog()
        HttpHelper
            .get()
            .url(ApiNetConfig.ORDER_STATUS_QUERY)
            .addParams("order_id", orderBean?.order_id)
            .execute(object : BaseOnResultListener<OrderBean> {
                override fun onSuccess(bean: OrderBean?) {
                    dismissLoadingDialog()


                    setOrderData(bean?.data)

                    ThreadPoolUtils.getInstance().execute {
                        OrderDataBase.getInstance(this@ExchangeDetailActivity)
                            .insertOrUpdate(bean?.data)
                    }

                }

                override fun onFailure(code: Int, error: String?) {

                    dismissLoadingDialog()
                    error?.let { showToast(it) }
                }

            }, OrderBean::class.java)
    }

    private fun setOrderData(bean: OrderBean.Data?) {
        orderBean = bean
        orderBean?.let {

            var coin = it.base_coin?.toUpperCase()

            et_amount.editText?.setText(it.base_amount_total + " " + coin)


            et_status.editText?.setText(it.state_string)


            et_order_id.editText?.setText(it.order_id)

            et_payment_id.editText?.setText(it.base_required_payment_id_long)

            et_address.editText?.setText(it.base_receiving_integrated_address)



            if (it.base_coin?.toUpperCase() == Config.COIN_DARMA.toUpperCase()) {
                tv_notice_1.setText(
                    String.format(
                        getString(
                            R.string.str_trade_notice21,
                            "DARMA", it.base_amount_total + " " + coin
                        )
                    )
                )
                tv_notice_2.setText(R.string.str_trade_notice22)
                btn_commit.setOnClickListener {

                    if (!timeOut) {

                        sendDMC()
                        showLoadingDialog()
                    } else {
//                        showToast(R.string.str_timeout)
                        intentOrderDetailData()
                    }
                }
                btn_commit.setText(R.string.str_pay_now)
            } else {
//                tv_notice_1.setText(String.format(getString(R.string.str_order_detail_pay_notice,getWalletManager().openWallet.name)))
                tv_notice_1.setText(
                    String.format(
                        getString(
                            R.string.str_trade_notice11,
                            coin, it.base_amount_total + " " + coin
                        )
                    )
                )
                tv_notice_2.setText(R.string.str_trade_notice12)
                btn_commit.setOnClickListener {
                    intentOrderDetailData()
                }
                btn_commit.setText(getString(R.string.str_view_order_detail))
            }





            loadAddress(it.base_receiving_integrated_address)

            startTimer(it.seconds_till_timeout * 1000)

        }

    }

    var timeOut = false

    var cdTimer: CountDownTimer? = null
    private fun startTimer(time: Long) {
        cdTimer?.cancel()
        cdTimer = null
        cdTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                tv_time.setText(getString(R.string.str_time_out))
                timeOut = true
                btn_commit.setText(getString(R.string.str_view_order_detail))
            }

            override fun onTick(millisUntilFinished: Long) {

                timeOut = false
                tv_time.setText(DateTimeUtils.getCountDownTime(millisUntilFinished))
            }

        }
        cdTimer?.start()
    }


    private fun loadAddress(addressStr: String?) {
        if (StringUtils.isEmpty(addressStr)) {
            return
        }
        var qrBitmap: Bitmap? = null//Cache.getQrBigBitmapCache();
        if (qrBitmap == null) {
            val r = resources
            val px = AppUtils.convertDpToPx(r, 225)
            Log.i("Util", addressStr)
            qrBitmap = QrUtils.encodeAsBitmap(addressStr, px, px, Color.BLACK, Color.WHITE)
        }
        iv_qr.setImageBitmap(qrBitmap)
    }


    fun intentOrderDetailAct(data: OrderDB?) {

        var bundle=Bundle()

        data?.let {
            bundle.putSerializable(Config.DATA,data)
        }
        intentTo(OrderDetailActivity::class.java,bundle)


    }
    private fun intentOrderDetailData() {

        intentOrderDetailAct(orderBean)

//        showLoadingDialog()
//        HttpHelper
//            .get()
//            .url(ApiNetConfig.ORDER_STATUS_QUERY)
//            .addParams("order_id", orderBean?.order_id)
//            .execute(object : BaseOnResultListener<OrderBean> {
//                override fun onSuccess(bean: OrderBean?) {
//                    dismissLoadingDialog()
//
//                    intentOrderDetailAct(bean?.data)
//
//                }
//
//                override fun onFailure(code:Int,error: String?) {
//
//                    dismissLoadingDialog()
//                    error?.let { showToast(it) }
//
//                    intentOrderDetailAct(null)
//
//                }
//
//            }, OrderBean::class.java)
    }

    var password = ""
    private fun sendDMC() {

        showLoadingDialog()

        ThreadPoolUtils.getInstance().execute {

            try {
                var trans: Transaction? = null
                trans = WalletManager.getInstance().createTransfer(
                    orderBean?.base_receiving_integrated_address,
                    orderBean?.base_amount_total,
                    "",
                    password
                )

                dismissLoadingDialog()
                sendSuccess(trans)
            } catch (e: WalletErrorException) {
                dismissLoadingDialog()
                e.printStackTrace()
                btn_commit.postDelayed({


                    if (e.error.errCode == ErrorCode.getErrInvalidPassword()) {

                        EnterPwdDialog.createDialog(this)
                            .setCheckPassWordListener(object :
                                EnterPwdDialog.CheckPassWordListener {
                                override fun success(password: String) {

                                    this@ExchangeDetailActivity.password = password

                                    sendDMC()
                                }

                                override fun cancel() {

                                    sendFail(e.error)
                                }

                            })
                            .show()
                    } else {

                        sendFail(e.error)
                    }
                }, 2000)
            }
        }

    }

    private fun sendSuccess(trans: Transaction) {


//        //            trans.receive_address = getAddress()
//        var intent = Intent(this, SendConfirmActivity::class.java)
//        intent.putExtra(Config.FROM, SendConfirmActivity.FROM_EXCHANGE)
//        intent.putExtra(Config.DATA, trans)
//        startActivity(intent)

        var bundle=Bundle()
        bundle.putString(Config.FROM, SendConfirmActivity.FROM_EXCHANGE)
        bundle.putSerializable(SendConfirmActivity.TRANS_DATA, trans)
        bundle.putSerializable(Config.DATA, orderBean)

        intentTo(SendConfirmActivity::class.java,REQUEST_CODE_SEND_CONFIRM,bundle)
    }


    private fun sendFail(error: WalletError) {

        showToast(error.errMsg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_CODE_SEND_CONFIRM-> if(resultCode== Activity.RESULT_OK){
                finish()
            }
        }
    }

}
