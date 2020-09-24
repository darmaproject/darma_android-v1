package com.darma.wallet.ui.activity

import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.view.View
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.bean.OrderBean
import com.darma.wallet.bean.OrderBean.Status
import com.darma.wallet.db.OrderDB
import com.darma.wallet.db.OrderDataBase
import com.darma.wallet.net.ApiNetConfig
import com.darma.wallet.net.BaseOnResultListener
import com.darma.wallet.net.HttpHelper
import com.darma.wallet.utils.DateTimeUtils
import com.darma.wallet.utils.ThreadPoolUtils
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.activity_order_detail.et_amount
import kotlinx.android.synthetic.main.activity_order_detail.et_order_id
import kotlinx.android.synthetic.main.activity_order_detail.et_status
import kotlinx.android.synthetic.main.activity_order_detail.iv_copy_order_id
import kotlinx.android.synthetic.main.layout_empty_view.*
import java.lang.Exception

class OrderDetailActivity : BaseActivity() {


    var order :OrderDB?=null


    override fun layoutId(): Int {
        return R.layout.activity_order_detail
    }

    override fun initData() {

        try {

            order= intent.getSerializableExtra(Config.DATA) as OrderDB?

        }catch (e :Exception){
            e.printStackTrace()

        }

        if(order!=null){
            showContent()
            setOrderData(order)
        }else{
            showEmpty()
        }

//        order?.order_id="sero-9ygMbe"
    }

    override fun onResume() {
        super.onResume()

        order?.let {
            getOrderDetailData()
        }
    }


    private fun showContent() {

        sv_content.visibility=View.VISIBLE
        vEmpty.visibility=View.GONE
    }

    private fun showEmpty() {

        sv_content.visibility=View.GONE
        vEmpty.visibility=View.VISIBLE

        tv_empty.setText(getString(R.string.str_no_order_data))

    }


    private fun setOrderData(data: OrderDB?) {

        data?.let {



            var coinSend=it.base_coin?.toUpperCase()
            var coinReceive =it.quota_coin?.toUpperCase()


            et_order_id.editText?.setText(it.order_id)
            et_status.editText?.setText(it.state_string)


            et_create_time.editText?.setText(DateTimeUtils.stampToDateTime(it.created_at))
//            et_timeout_time.editText?.setText(DateTimeUtils.stampToDateTime(it.expires_at))
            et_price.editText?.setText(it.order_price+" "+coinReceive)

            et_closed_price.editText?.setText(it.final_price+" "+coinReceive)


            et_closed_amount.editText?.setText(it.quota_real_amount)
            et_closed_amount.hint=getString(R.string.str_total_transaction_aomunt)+"("+coinReceive+")"

            et_send_coin.editText?.setText(coinSend)
            et_amount.editText?.setText(it.base_amount_total)
            et_receive_coin.editText?.setText(coinReceive)
            et_estimate_amount.editText?.setText(it.quota_amount)

            et_sent_amount.editText?.setText(it.base_received_amount)

            et_returning_amount.editText?.setText(it.refund_amount)


            et_send_address.editText?.setText(it.base_receiving_integrated_address)
            et_send_address.hint=getString(R.string.str_send_Address1)+"("+coinSend+")"

            et_receive_address.editText?.setText(it. quota_dest_address)
            et_receive_address.hint=getString(R.string.str_receive_address1)+"("+coinReceive+")"


            et_refund_address.editText?.setText(it.refund_address)
            et_refund_address.hint=getString(R.string.str_Refund_address)+"("+coinSend+")"

            et_send_txid.editText?.setText(it.base_transaction_id)
            et_receive_txid.editText?.setText(it.quota_transaction_id)



            setStatus(it.state)

            startTimer(it.seconds_till_timeout * 1000)

        }

    }
    var cdTimer: CountDownTimer? = null
    private fun startTimer(time: Long) {

        if(time==0L){
//            et_timeout_time.editText?.setText(getString(R.string.str_timeout))

            et_timeout_time.editText?.setText("0")
        }else {

            cdTimer?.cancel()
            cdTimer = null
            cdTimer = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    et_timeout_time.editText?.setText(getString(R.string.str_time_out))
                }

                override fun onTick(millisUntilFinished: Long) {

                    et_timeout_time.editText?.setText(
                        DateTimeUtils.getCountDownTime(
                            millisUntilFinished
                        )
                    )
                }

            }
            cdTimer?.start()
        }
    }

    /**
     * TIMED_OUT：
     * UNPAID：
     * PAID_UNCONFIRMED：
     * PAID：
     * SENDING：
     * SUCCESS：
     * FAILED_REFUNDED：
     * FAILED_UNREFUND：
     */
    private fun setStatus(state: String?) {


        et_timeout_time.visibility=View.VISIBLE
        et_closed_price.visibility=View.VISIBLE
        et_returning_amount.visibility=View.VISIBLE
        et_sent_amount.visibility=View.VISIBLE

        et_closed_amount.visibility=View.VISIBLE

        cl_send_txid.visibility= View.VISIBLE
        cl_receive_txid.visibility= View.VISIBLE
        cl_refund_address.visibility= View.VISIBLE


        et_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_black))
        et_sent_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_black))
        et_closed_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_black))
        et_returning_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_black))



        when(state){
            Status.TIMED_OUT->{

                et_closed_price.visibility=View.GONE
                et_returning_amount.visibility=View.GONE
                et_sent_amount.visibility=View.GONE

                et_closed_amount.visibility=View.GONE

                cl_send_txid.visibility= View.GONE
                cl_receive_txid.visibility= View.GONE
                cl_refund_address.visibility= View.GONE


                et_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))
            }
            Status.UNPAID->{

                et_closed_price.visibility=View.GONE
                et_returning_amount.visibility=View.GONE
                et_sent_amount.visibility=View.GONE

                et_closed_amount.visibility=View.GONE

                cl_send_txid.visibility= View.GONE
                cl_receive_txid.visibility= View.GONE
                cl_refund_address.visibility= View.GONE

                et_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))


            }
            Status.PAID_UNCONFIRMED->{


                et_timeout_time.visibility=View.GONE
                et_returning_amount.visibility=View.GONE

                et_closed_price.visibility=View.GONE
                et_closed_amount.visibility=View.GONE

                cl_receive_txid.visibility= View.GONE
                cl_refund_address.visibility= View.GONE

                et_sent_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))


            }
            Status.PAID->{


                et_timeout_time.visibility=View.GONE
                et_returning_amount.visibility=View.GONE

                et_closed_price.visibility=View.GONE
                et_closed_amount.visibility=View.GONE

                cl_receive_txid.visibility= View.GONE
                cl_refund_address.visibility= View.GONE

                et_sent_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))

            }
            Status.SENDING->{


                et_timeout_time.visibility=View.GONE
                et_returning_amount.visibility=View.GONE



                cl_receive_txid.visibility= View.GONE
                cl_refund_address.visibility= View.GONE

                et_closed_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))


            }
            Status.SUCCESS->{


                et_timeout_time.visibility=View.GONE
                et_returning_amount.visibility=View.GONE
                cl_refund_address.visibility= View.GONE


                et_closed_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))


            }
            Status.FAILED_REFUNDED->{

                et_timeout_time.visibility=View.GONE
                et_closed_price.visibility=View.GONE
                et_sent_amount.visibility=View.GONE

                et_closed_amount.visibility=View.GONE

                cl_send_txid.visibility= View.GONE
                cl_receive_txid.visibility= View.GONE


                et_returning_amount.setHint(getString(R.string.str_returned_amount))

                et_returning_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))


            }
            Status.FAILED_UNREFUND->{

                et_timeout_time.visibility=View.GONE
                et_closed_price.visibility=View.GONE
                et_sent_amount.visibility=View.GONE

                et_closed_amount.visibility=View.GONE

                cl_send_txid.visibility= View.GONE
                cl_receive_txid.visibility= View.GONE


                et_returning_amount.setHint(getString(R.string.str_amount_to_be_teturned))

                et_returning_amount.editText?.setTextColor(ContextCompat.getColor(this,R.color.text_color_pink))

            }
        }
    }

    override fun initView() {

        setTitleText(getString(R.string.str_details))


        iv_copy_order_id.setOnClickListener {
            copyText(et_order_id.editText?.text.toString())
        }
        iv_copy_send_address.setOnClickListener {
            copyText(et_send_address.editText?.text.toString())
        }
        iv_copy_receive_address.setOnClickListener {
            copyText(et_receive_address.editText?.text.toString())
        }
        iv_copy_send_txid.setOnClickListener {
            copyText(et_send_txid.editText?.text.toString())
        }
        iv_copy_receive_txid.setOnClickListener {
            copyText(et_receive_txid.editText?.text.toString())
        }

    }



    override fun loadData() {
//        getOrderDetailData()
    }


    private fun getOrderDetailData() {

        showLoadingDialog()
        HttpHelper
            .get()
            .url(ApiNetConfig.ORDER_STATUS_QUERY)
            .addParams("order_id", order?.order_id)
            .execute(object : BaseOnResultListener<OrderBean> {
                override fun onSuccess(bean: OrderBean?) {
                    dismissLoadingDialog()


                    setOrderData(bean?.data)

                    ThreadPoolUtils.getInstance().execute {
                        OrderDataBase.getInstance(this@OrderDetailActivity)
                            .insertOrUpdate(bean?.data)
                    }

                }

                override fun onFailure(code:Int,error: String?) {

                    dismissLoadingDialog()
                    error?.let { showToast(it) }


                }

            }, OrderBean::class.java)
    }
}
