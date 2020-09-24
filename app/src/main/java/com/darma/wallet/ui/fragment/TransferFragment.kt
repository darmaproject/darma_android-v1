package com.darma.wallet.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.darma.wallet.Config

import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.base.BaseFragment
import com.darma.wallet.db.ContactsDB
import com.darma.wallet.event.RequestCode
import com.darma.wallet.ui.activity.ContactsActivity
import com.darma.wallet.ui.activity.OrderLogActivity
import com.darma.wallet.ui.activity.ScanActivity
import com.darma.wallet.utils.QrUtils
import com.darma.wallet.utils.SwapViewUpDownAnimate
import com.darma.wallet.widget.ExchangeCardItemView
import com.orhanobut.logger.Logger
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.fragment_transfer.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TransferFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TransferFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TransferFragment : BaseFragment(), ExchangeCardItemView.GetAddressByScanOrContactsListener {


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransferFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_transfer
    }


    /**
     * utils for swap upper and lower view with animate
     */
    var swapAnimate: SwapViewUpDownAnimate? = null
    /**
     * true : upper and lower has change location
     * false: not
     */
    var isChange = false


    /**
     * default coin name for send
     */
    var sendCoin: String = "SERO"

    /**
     * default coin name for received
     */
    var receivedCoin: String = "LTC"




    var time=Timer()

    override fun initData() {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        time.schedule(object :TimerTask(){
            override fun run() {
                if(!isHidden&&!isPause){

                    exchangeCardView.refresh()
                }
            }
        }, Date(), 10000)
    }

    override fun initView() {

        exchangeCardView.setActivity(activity as BaseActivity)

        iv_history.setOnClickListener {
            intentTo(OrderLogActivity::class.java)
        }


        exchangeCardView.setAddressListener(this)
    }

    override fun lazyLoad() {

        exchangeCardView.refresh()

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

//        if(!hidden){
//            lazyLoad()
//        }
    }



    var exchangeItemView:ExchangeCardItemView?=null

    override fun getAddressByContacts(view : ExchangeCardItemView){
        this.exchangeItemView=view

        var bundle= Bundle()
        bundle.putString(Config.COIN_CODE, view.getCoin())
        intentTo(ContactsActivity::class.java, RequestCode.REQUEST_SELECT_ADDRESS,bundle)

    }

    override fun getAddressByScan(view : ExchangeCardItemView){
        this.exchangeItemView=view

        intentTo(ScanActivity::class.java, RequestCode.REQUEST_SCAN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        when (requestCode) {
            RequestCode.REQUEST_SCAN -> {
                var res = data?.getStringExtra(ScanActivity.INTENT_EXTRA_RESULT)

                Logger.w("res = "+res)
                if (!StringUtils.isEmpty(res)) {
                    var map = QrUtils.getUriParams(res)
                    if (QrUtils.checkDarmaUri(res)&&map != null) {
                        map.get(QrUtils.URI_ADDRESS)?.let {
                            setAddress("",it)
                        }
                        map.get(QrUtils.URI_AMOUNT)?.let {
                            setAmount(it)
                        }
//                        showToast(getString(R.string.str_invalid_qr_code))
                    } else {


                        setAddress("",res)

//                        var payment_id = map.get(QrUtils.URI_PAYMENT_ID)
//                        if (!StringUtils.isEmpty(payment_id)) {
//                            et_payment_id.editText?.setText(payment_id)
//                        } else {
//
//                            et_payment_id.editText?.setText("")
//                        }
                    }
                }

            }
            RequestCode.REQUEST_SELECT_ADDRESS -> {
                var item = data?.getSerializableExtra(Config.DATA) as ContactsDB

                item?.let {
                    setAddress(it.remark,it.address)
//                    et_address.editText?.setText(it.address)

//                    var payment_id = it.payment_id
//                    if (!StringUtils.isEmpty(payment_id)) {
//                        et_payment_id.editText?.setText(payment_id)
//                    } else {
//
//                        et_payment_id.editText?.setText("")
//                    }
                }

            }
        }

    }

    private fun setAddress(name : String?,address : String?){
        exchangeItemView?.setAddress(name,address)

    }
    private fun setAmount(amount : String?){

        amount?.let { exchangeItemView?.setAmount(it) }
    }

    var isPause=false;
    override fun onResume() {
        super.onResume()
        isPause=false
    }

    override fun onPause() {
        super.onPause()
        isPause=true
    }

    override fun onDestroy() {
        super.onDestroy()
        time.cancel()
    }
}
