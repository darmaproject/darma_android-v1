package com.darma.wallet.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import com.darma.wallet.Config
import com.darma.wallet.R
import com.orhanobut.logger.Logger
import com.wallet.WalletManager
import com.wallet.utils.StringUtils
import io.reactivex.annotations.Nullable
import kotlinx.android.synthetic.main.view_exchange_card_item.view.*

/**
 * Created by Darma Project on 2019/12/6.
 */
class ExchangeCardItemView : FrameLayout {


    private var view: View? = null

    private var coin = ""

    private var scale = 8
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


    var lockRefreshAddress=false
    private fun init() {

        view = View.inflate(context, R.layout.view_exchange_card_item, this)

        iv_contacts.setOnClickListener {
            mGetAddressListener?.getAddressByContacts(this)
        }

        iv_scan.setOnClickListener {
            mGetAddressListener?.getAddressByScan(this)

        }

        et_address.editText?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                tv_wallet.setText("")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_number.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                var number = getAmount()
                val a = number.indexOf(".")
                if (a != -1) {
                    val b = number.substring(number.indexOf("."), number.length).length
                    if (b > scale + 1) {
                        number =
                            number.substring(0, number.length - (b - scale - 1))
                        setAmount(number)

                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    var mGetAddressListener: GetAddressByScanOrContactsListener? = null
    fun setAddressListener(mGetAddressListener: GetAddressByScanOrContactsListener) {
        this.mGetAddressListener = mGetAddressListener
    }

    fun setCoin(coin: String,scale :Int?) {

        var nCoin=coin.toUpperCase()
        if ( nCoin.equals(Config.COIN_DARMA)) {
            refreshAddress(
                WalletManager.getInstance().openWallet.name,
                WalletManager.getInstance().address
            )
        }



        setScale(scale)


        if (this.coin==nCoin) {
            return
        }

        Logger.w("setCoin  coin = "+coin)
        this.coin = nCoin
        tv_coin.setText(coin)

        lockRefreshAddress=false
        refreshAddress("", "")

        setMinLimit("")
        setMaxLimit("")


    }

    private fun setScale(scale: Int?) {
        if (scale != null) {
            this.scale=scale
        }

        setAmount(getAmount())


    }

    fun getCoin():String{
        return coin
    }

    fun setAmount(amount: String) {
        et_number.setText(amount)
        try {
            et_number.setSelection(amount.length)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun refreshAddress(addressName: String?, address: String?){

        if(lockRefreshAddress){
            return
        }

        Logger.w("coin = "+coin+" refreshAddress  addressName = "+addressName+" address = "+address)

        if(StringUtils.isEmpty(address)) {

            et_address.editText?.setText("")
        }else {
            et_address.editText?.setText(address)
        }

        if(StringUtils.isEmpty(addressName)){

            tv_wallet.setText("")
        }else{
            tv_wallet.setText(addressName)

        }

    }
    fun setAddress(addressName: String?, address: String?) {

        Logger.w("coin = "+coin+" setAddress  addressName = "+addressName+" address = "+address)
        lockRefreshAddress=true


        if(StringUtils.isEmpty(address)) {

            et_address.editText?.setText("")
        }else {
            et_address.editText?.setText(address)
        }

        if(StringUtils.isEmpty(addressName)){

            tv_wallet.setText("")
        }else{
            tv_wallet.setText(addressName)

        }

    }

    fun setMinLimit(@Nullable min: String?) {
        if (!StringUtils.isEmpty(min)) {

            tv_min.setText(context.getString(R.string.str_min) + min + coin)
        } else {

            tv_min.setText("")
        }
    }

    fun setMaxLimit(@Nullable max: String?) {
        if (!StringUtils.isEmpty(max)) {

            tv_max.setText(context.getString(R.string.str_max) + max + coin)
        } else {

            tv_max.setText("")
        }
    }


    fun getAmountEditText(): EditText {
        return et_number
    }

    fun getAmount(): String {
        return et_number.text.toString()
    }

    fun getAddress(): String {
        return et_address.editText?.text.toString()
    }

    private var isSend = false
    fun isSend(isSend: Boolean) {
        this.isSend = isSend
        if (isSend) {
            tv_title.setText(R.string.str_you_will_send)

            et_address.hint = context.getString(R.string.str_Refund_address)

            setMaxLimit("")
            setMinLimit("")
        } else {

            tv_title.setText(R.string.str_you_will_get)
            et_address.hint = context.getString(R.string.str_address)
        }
    }

    fun isSend(): Boolean {
        return isSend
    }


    interface GetAddressByScanOrContactsListener {
        fun getAddressByContacts(view: ExchangeCardItemView)
        fun getAddressByScan(view: ExchangeCardItemView)
    }
}