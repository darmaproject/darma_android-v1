package com.darma.wallet.widget

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.bean.OrderBean
import com.darma.wallet.bean.PairInfoBean
import com.darma.wallet.net.ApiNetConfig
import com.darma.wallet.net.BaseOnResultListener
import com.darma.wallet.net.HttpHelper
import com.darma.wallet.ui.activity.ExchangeDetailActivity
import com.darma.wallet.utils.MathUtils
import com.darma.wallet.utils.SwapViewUpDownAnimate
import com.darma.wallet.utils.ToastUtils
import com.wallet.model.Coin
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.view_exchange_card.view.*
import java.math.BigDecimal

/**
 * Created by Darma Project on 2019/12/6.
 */
class ExchangeCardView : FrameLayout {

    private var view: View? = null
    var swapAnimate: SwapViewUpDownAnimate? = null
    var isChange = false

    private var activity: BaseActivity? = null


    var sendCoin: String = "DMC"

    var receivedCoin: String = "USDT"


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        view = View.inflate(context, R.layout.view_exchange_card, this)

        initExchangeView()

        initData()
    }


    /**
     * set default data
     *
     * ecv_up is the Upper view
     * ecv_down is the below view
     *
     */
    private fun initData() {
        ecv_up.setCoin(sendCoin,8)
        ecv_down.setCoin(receivedCoin,8)
        ecv_up.isSend(true)
        ecv_down.isSend(false)


//        ecv_up.setAddress("","2EDusqZCpQZCrxBwf4GSZeiaP6hso3nCLh5YEEtxLogi1g7KY5nrjn3RYZcqfJJdV5wfnpTD7bTkBupZ7Qr277y2Sk58cbv7jARurhpHvuntgYufd3STcL9iGQJvgf8aYb7g")
//        ecv_down.setAddress("","LU3H4KYBEoWejksYMpYXfS5Htvg82c8UmZ")
    }


    /**
     * init exchange views
     *
     *
     */
    fun initExchangeView() {
        swapAnimate = SwapViewUpDownAnimate()
        swapAnimate?.downView = ecv_down
        swapAnimate?.upView = ecv_up
        iv_change.setOnClickListener {

            if (swapAnimate?.isOnAnimate!!) {

            } else {

                var temp = sendCoin
                sendCoin = receivedCoin
                receivedCoin = temp

                if (isChange) {
                    swapAnimate?.swapViewBack()

                    isChange = false


                    ecv_up.isSend(true)
                    ecv_down.isSend(false)
                } else {
                    swapAnimate?.swapViewUpDown()

                    isChange = true


                    ecv_up.isSend(false)
                    ecv_down.isSend(true)
                }

                refresh()
            }
        }


        ecv_up.getAmountEditText().addTextChangedListener(upViewWatcher)

        ecv_down.getAmountEditText().addTextChangedListener(downViewWatcher)




        btn_commit.setOnClickListener {
            if (checkInput()) {

                postExchange()
            }
        }
    }

    fun setAddressListener(mGetAddressListener: ExchangeCardItemView.GetAddressByScanOrContactsListener) {

        ecv_down.setAddressListener(mGetAddressListener)
        ecv_up.setAddressListener(mGetAddressListener)
    }

    private fun checkInput(): Boolean {

        var sendAmount = getSendView().getAmount()
        if (StringUtils.isEmpty(sendAmount)) {
            showToast(context.getString(R.string.str_toast_send_amount_cannot_be_null))
            return false
        }

        var receivedAmount = getReceivedView().getAmount()
        if (StringUtils.isEmpty(receivedAmount)) {
            showToast(context.getString(R.string.str_toast_receive_amount_cannot_be_null))
            return false
        }

        var sendAddress = getSendAddress()
        if (StringUtils.isEmpty(sendAddress)) {
            showToast(context.getString(R.string.str_toast_refund_address_cannot_be_null))
            return false
        }

        var receiveAddress = getReceivedAddress()
        if (StringUtils.isEmpty(receiveAddress)) {
            showToast(context.getString(R.string.str_toast_receive_address_cannot_be_null))
            return false
        }




        if (getReceivedAmount().isLessThan(Coin.parseCoin(pairInfo?.data?.quota_lower_limit))) {

            showToast(
                String.format(
                    context.getString(R.string.tr_toast_exchange_coin_amount_cannot_less_than),
                    receivedCoin,
                    pairInfo?.data?.quota_lower_limit
                )
            )

            return false
        }

        if (getReceivedAmount().isGreaterThan(Coin.parseCoin(pairInfo?.data?.quota_upper_limit))) {

            showToast(
                String.format(
                    context.getString(R.string.str_toast_exchange_amount_cannot_more_than),
                    receivedCoin,
                    pairInfo?.data?.quota_upper_limit
                )
            )

            return false
        }



        return true
    }

    fun getReceivedAmount(): Coin {
        return try {
            Coin.parseCoin(getReceivedView().getAmount())
        } catch (e: Exception) {
            Coin.ZERO
        }
    }

    fun showToast(toast: String?) {

        ToastUtils.showToast(context, toast)

    }

    var upViewWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            var numberStr = ecv_up.getAmountEditText().text.toString()
            if(StringUtils.isEmpty(numberStr)){

                ecv_down.getAmountEditText().removeTextChangedListener(downViewWatcher)
                ecv_down.getAmountEditText().setText("")
                ecv_down.getAmountEditText().addTextChangedListener(downViewWatcher)
            }else {
                try {

                    var amount = BigDecimal(numberStr)


                    if (isChange) {
                        ecv_down.getAmountEditText().removeTextChangedListener(downViewWatcher)
                        ecv_down.setAmount(MathUtils.divide(amount, getRate()))
                        ecv_down.getAmountEditText().addTextChangedListener(downViewWatcher)

                    } else {

                        ecv_down.getAmountEditText().removeTextChangedListener(downViewWatcher)
                        ecv_down.setAmount(MathUtils.multiply(amount, getRate()))
                        ecv_down.getAmountEditText().addTextChangedListener(downViewWatcher)
                    }
                } catch (e: Exception) {

                    e.printStackTrace()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    var downViewWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            var numberStr = ecv_down.getAmountEditText().text.toString()
            if(StringUtils.isEmpty(numberStr)){

                ecv_up.getAmountEditText().removeTextChangedListener(upViewWatcher)
                ecv_up.getAmountEditText().setText("")
                ecv_up.getAmountEditText().addTextChangedListener(upViewWatcher)

            }else {
                try {

                    var amount = BigDecimal(numberStr)


                    if (isChange) {

                        ecv_up.getAmountEditText().removeTextChangedListener(upViewWatcher)
                        ecv_up.setAmount(MathUtils.multiply(amount, getRate()))
                        ecv_up.getAmountEditText().addTextChangedListener(upViewWatcher)

                    } else {
                        ecv_up.getAmountEditText().removeTextChangedListener(upViewWatcher)
                        ecv_up.setAmount(MathUtils.divide(amount, getRate()))
                        ecv_up.getAmountEditText().addTextChangedListener(upViewWatcher)

                    }
                } catch (e: Exception) {

                    e.printStackTrace()

                }
            }

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }


    private var pairInfo: PairInfoBean? = null

    /**
     * Set transaction pair information
     * @param pairInfo
     *
     */
    fun setPairInfo(pairInfo: PairInfoBean?) {

        this.pairInfo = pairInfo

        //set rate
        tv_rate.setText("1 " + sendCoin + " = " + pairInfo?.data?.price + receivedCoin)

        //set show coin
        getSendView().setCoin(sendCoin,pairInfo?.data?.base_scale)
        getReceivedView().setCoin(receivedCoin,pairInfo?.data?.quota_scale)


        getSendView().setMinLimit("")
        getSendView().setMaxLimit("")


        getReceivedView().setMinLimit(pairInfo?.data?.quota_lower_limit)
        getReceivedView().setMaxLimit(pairInfo?.data?.quota_upper_limit)

//        if (sendCoin.equals(Config.COIN_DARMA)) {
//            getSendView().refreshAddress(
//                WalletManager.getInstance().openWallet.name,
//                WalletManager.getInstance().address
//            )
//
//        }


    }

    fun getRate(): BigDecimal? {
        return BigDecimal(pairInfo?.data?.price)
    }

    /**
     * get upper view which is for send
     */
    fun getSendView(): ExchangeCardItemView {
        if (!isChange) {
            return ecv_up
        } else {
            return ecv_down
        }
    }

    /**
     * get lower view which is for received
     */
    fun getReceivedView(): ExchangeCardItemView {
        if (isChange) {
            return ecv_up
        } else {
            return ecv_down
        }
    }

    fun refresh() {
        getPairInfo()
    }


    fun setActivity(activity: BaseActivity) {
        this.activity = activity
    }

    /**
     * get pair info from service
     *
     * @see setPairInfo
     */
    private fun getPairInfo() {
        HttpHelper
            .get()
            .url(ApiNetConfig.ORDER_PARAMETER_QUERY)
            .addParams("pair", getPair())
//            .addParams("pair","sero_ltc")
            .execute(object : BaseOnResultListener<PairInfoBean> {
                override fun onSuccess(bean: PairInfoBean?) {
                    setPairInfo(bean)
                    handlerSuccess()
                }

                override fun onFailure(code: Int, error: String?) {

                    handlerFail(code,error)
                }

            }, PairInfoBean::class.java)
    }


    private fun postExchange() {

        activity?.showLoadingDialog()
        HttpHelper
            .post()
            .url(ApiNetConfig.ORDER_CREATE)
            .addParams("pair", getPair())
            .addParams("quota_amount", getReceivedAmount().toPlainString())
            .addParams("quota_dest_address", getReceivedAddress())
            .addParams("base_src_address", getSendAddress())
            .execute(object : BaseOnResultListener<OrderBean> {
                override fun onSuccess(bean: OrderBean?) {
                    activity?.dismissLoadingDialog()
//                    setPairInfo(pairInfo)

                    var bundle = Bundle()
                    bundle.putSerializable(Config.DATA, bean?.data)
                    activity?.intentTo(ExchangeDetailActivity::class.java, bundle)

//                    showToast("Success")
                }

                override fun onFailure(code: Int, error: String?) {

                    activity?.dismissLoadingDialog()
                    error?.let { showToast(it) }
                }

            }, OrderBean::class.java)
    }

    private fun handlerSuccess(){

        btn_commit.isEnabled=true
    }
    private fun handlerFail(code: Int, error: String?){


        if(code==HttpHelper.FAIL_CODE){

            if(btn_commit.isEnabled){

                showToast(error)
                btn_commit.isEnabled=false
            }
            tv_rate.setText(error)
        }

    }
    private fun getReceivedAddress(): String {

        return getReceivedView().getAddress()
    }

    private fun getSendAddress(): String {

        return getSendView().getAddress()
    }

    /**
     * @return pair as a param for get pair info from service
     *
     * @see getPairInfo
     *
     *
     */
    fun getPair(): String {
        return (sendCoin + "_" + receivedCoin).toLowerCase()
    }


}