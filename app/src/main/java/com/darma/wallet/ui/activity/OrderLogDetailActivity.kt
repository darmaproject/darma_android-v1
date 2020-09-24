package com.darma.wallet.ui.activity

import android.os.Bundle
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.bean.OrderBean
import com.darma.wallet.db.OrderDB
import com.darma.wallet.db.OrderDataBase
import com.darma.wallet.net.ApiNetConfig
import com.darma.wallet.net.BaseOnResultListener
import com.darma.wallet.net.HttpHelper
import com.darma.wallet.utils.DateTimeUtils
import com.darma.wallet.utils.ThreadPoolUtils
import kotlinx.android.synthetic.main.activity_order_log_detail.*

class OrderLogDetailActivity : BaseActivity() {


    override fun layoutId(): Int {
        return R.layout.activity_order_log_detail
    }

    var order :OrderDB?=null
    override fun initData() {


        order= intent.getSerializableExtra(Config.DATA) as OrderDB?
//        order?.order_id="sero-9ygMbe"
        setOrderData(order)
    }

    private fun setOrderData(order: OrderDB?) {

        order?.let {
            var coinSend=it.base_coin?.toUpperCase()
            var coinReceive =it.quota_coin?.toUpperCase()


            et_order_id.editText?.setText(it.order_id)
            et_status.editText?.setText(it.state_string)


            et_time.editText?.setText(DateTimeUtils.stampToDateTime(it.created_at))

            et_pair.editText?.setText(coinSend+"/"+coinReceive)

            et_amount.editText?.setText(it.base_amount_total)


            ThreadPoolUtils.getInstance().execute {
                OrderDataBase.getInstance(this@OrderLogDetailActivity)
                    .insertOrUpdate(it)
            }
        }

    }

    override fun initView() {

        setTitleText(getString(R.string.str_details1))

        toolbar?.inflateMenu(R.menu.toolbar_log)
        toolbar?.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.detail -> {

                    getOrderDetailData()
                }
            }
            return@setOnMenuItemClickListener true
        }

        iv_copy_order_id.setOnClickListener {

            copyText(et_order_id.editText?.text.toString())
        }

    }

    fun intentOrderDetailAct(data: OrderDB?) {

        var bundle=Bundle()

        data?.let {
            bundle.putSerializable(Config.DATA,data)
        }
        intentTo(OrderDetailActivity::class.java,bundle)


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

                    intentOrderDetailAct(bean?.data)

                }

                override fun onFailure(code:Int,error: String?) {

                    dismissLoadingDialog()
                    error?.let { showToast(it) }

                    intentOrderDetailAct(null)

                }

            }, OrderBean::class.java)
    }
    override fun loadData() {

        HttpHelper
            .get()
            .url(ApiNetConfig.ORDER_STATUS_QUERY)
            .addParams("order_id", order?.order_id)
            .execute(object : BaseOnResultListener<OrderBean> {
                override fun onSuccess(bean: OrderBean?) {

                    setOrderData(bean?.data)

                }

                override fun onFailure(code:Int,error: String?) {


                }

            }, OrderBean::class.java)
    }



}
