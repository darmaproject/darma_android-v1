package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.ui.fragment.HomeFragment
import com.darma.wallet.ui.fragment.TransferFragment
import com.darma.wallet.utils.ThreadPoolUtils
import com.wallet.WalletManager
import com.wallet.bean.Transaction
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_send_confirm.*

class SendConfirmActivity : BaseActivity() {


    companion object{

        /**
         * @see SendActivity.sendSuccess
         */
        const val FROM_SEND="from_send"
        /**
         * @see ExchangeDetailActivity.sendSuccess
         */
        const val FROM_EXCHANGE="from_exchange"

        const val TRANS_DATA="trans_data"
    }

    override fun layoutId(): Int {
        return R.layout.activity_send_confirm
    }


    var trans:Transaction?=null

    override fun initData() {
        trans=intent.getSerializableExtra(TRANS_DATA) as Transaction
        setResult(Activity.RESULT_CANCELED)
    }

    override fun initView() {

        btn_commit.setOnClickListener {
            send()
        }

        trans?.let{

            et_address.editText?.setText(it.receive_address)

            if(StringUtils.isEmpty(it.payment_id)){

                et_payment_id.visibility= View.GONE
            }else{

                et_payment_id.visibility= View.VISIBLE
                et_payment_id.editText?.setText(it.payment_id)
            }
            et_amount.editText?.setText(it.amount.toPlainString())
            et_fee.editText?.setText(it.fee.toPlainString())
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


        btn_commit.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_commit.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun sendSuccess(txid: String) {
        btn_commit.progress = 100

        var from=intent.getStringExtra(Config.FROM)

        btn_commit.postDelayed({

            when(from){
                FROM_EXCHANGE->{

                    var bundle =intent.extras

                    intentTo(OrderDetailActivity::class.java, bundle)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                else->{

                    var intent= Intent(this,SendSuccessActivity::class.java)
                    intent.putExtra("data",trans)
                    startActivity(intent)
                }
            }


        },500)
    }
    private fun sendFail( error:String){

        btn_commit.progress = -1
        showToast(error)
    }
    private fun send() {

        showLoading()

        ThreadPoolUtils.getInstance().execute {
            try {
                var txid = WalletManager.getInstance().sendTransaction(trans)
                btn_commit.postDelayed({

                    sendSuccess(txid)
                }, 2000)
            } catch (e: WalletErrorException) {
                e.printStackTrace()
                btn_commit.postDelayed({

                    //                if(){
//
//                }
                    sendFail(e.error.errMsg)
                }, 2000)
            }
        }
    }

}
