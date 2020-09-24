package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.WalletDataBase
import com.darma.wallet.event.EventConfigs
import com.darma.wallet.utils.FileUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.darma.wallet.utils.dialog.BottomSelectDialog
import com.darma.wallet.utils.dialog.DeleteWalletDialog
import com.darma.wallet.utils.dialog.EnterPwdDialog
import com.wallet.WalletManager
import com.wallet.model.Wallet
import com.wallet.model.WalletErrorException
import com.wallet.utils.DateUtils
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_wallet_manager.*
import org.simple.eventbus.EventBus

class WalletManagerActivity : BaseActivity() {

    private var adapter: CommonAdapter<Wallet>? = null

    private var listData: MutableList<Wallet>? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun layoutId(): Int {
        return R.layout.activity_wallet_manager
    }

    override fun initData() {
        setResult(Activity.RESULT_CANCELED)
        listData=ArrayList()
    }

    override fun initView() {

        initRecyclerView()


        btn_create_wallet.setOnClickListener {
            intentTo(CreateWalletActivity::class.java)
        }
        btn_recovery_wallet.setOnClickListener {
            intentTo(RecoveryWalletActivity::class.java)
        }
    }

    private fun initRecyclerView() {


        adapter = object : CommonAdapter<Wallet>(this, R.layout.item_wallet_manager_view, listData) {
            override fun convert(holder: ViewHolder?, data: Wallet?, position: Int) {



                var tv_name=holder?.getView<TextView>(R.id.tv_name)
                var tv_time=holder?.getView<TextView>(R.id.tv_time)
                var tv_address=holder?.getView<TextView>(R.id.tv_address)

                var iv_selected=holder?.getView<ImageView>(R.id.iv_selected)
                tv_name?.setText(data?.name)
                if(data?.name==WalletManager.getInstance().lastOpenWallet.name){
                    iv_selected?.visibility=View.VISIBLE
                    tv_name?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_pink))
                    tv_time?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_pink))
                    tv_address?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_pink))

                }else{
                    iv_selected?.visibility=View.GONE
                    tv_name?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_black))
                    tv_time?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray))
                    tv_address?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray))

                }

                holder?.itemView?.setOnClickListener {
                    if(data?.name!=WalletManager.getInstance().lastOpenWallet.name) {


                        BottomSelectDialog.createDialog(this@WalletManagerActivity)
                            .addItem(getString(R.string.str_open)) {

                                openWallet(data)

                            }.addItem(getString(R.string.str_re_name)) {

                                editName(data)

                            }.addItem(getString(R.string.str_delet)) {

                                deleteWallet(data)
                            }.show()
                    }

                }
                ThreadPoolUtils.getInstance().execute {
                    WalletDataBase.getInstance(this@WalletManagerActivity)?.walletDao?.getWallet( data?.name)?.let{

                        var updateTime=it.updateTime
                        runOnUiThread {

                            tv_time?.setText(DateUtils.longToDay(updateTime))
                            tv_address?.setText(it.address)

                        }


                    }

                }




            }

        }

        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = linearLayoutManager
    }

    private fun deleteWallet(data: Wallet?) {

        DeleteWalletDialog.createDialog(this)
            .setOnConfirmListener {
                showLoadingDialog()
                FileUtils.deleteWallet(this,data)
                toolbar?.postDelayed({
                    dismissLoadingDialog()
                    loadData()
                },1000)

            }.show()
    }

    private fun editName(data: Wallet?) {
        var intent= Intent(this,EditWalletNameActivity::class.java)
        intent.putExtra("data",data)
        startActivity(intent)
    }

    private fun openWallet(wallet: Wallet?) {

        EnterPwdDialog.createDialog(this)
            .setCheckPassWordListener(object :
                EnterPwdDialog.CheckPassWordListener {
                override fun success(password: String) {

                    try {
                        WalletManager.getInstance().openWallet(wallet, password)

                        intentToMain()
                        EventBus.getDefault().post(EventConfigs.EVENT_OPEN_NEW_WALLET)

                    }catch (e:WalletErrorException){
                        e.printStackTrace()

                    }
                }

                override fun cancel() {

                }

            })
//            .setInputPassWordListener { dialog, etPwd ->
//
//                try {
//                    WalletManager.getInstance().openWallet(wallet, etPwd.editText?.text.toString())
//
//                    dialog.dismiss()
//                    intentToMain()
//                    EventBus.getDefault().post(EventConfigs.EVENT_OPEN_NEW_WALLET)
//
//                }catch (e:WalletErrorException){
//                    e.printStackTrace()
//                    etPwd.error=e.error.errMsg
//
//                }
//
//            }
            .show()

    }


    override fun loadData() {
        listData?.clear()
        listData?.addAll(WalletManager.getInstance().wallets)
        adapter?.notifyDataSetChanged()
    }
}
