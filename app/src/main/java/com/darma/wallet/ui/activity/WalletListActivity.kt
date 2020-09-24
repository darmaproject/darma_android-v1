package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.WalletDataBase
import com.darma.wallet.utils.ThreadPoolUtils
import com.wallet.WalletManager
import com.wallet.model.Wallet
import com.wallet.utils.DateUtils
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_receive.*
import kotlinx.android.synthetic.main.activity_wallet_list.*
import kotlinx.android.synthetic.main.activity_wallet_list.toolbar

class WalletListActivity : BaseActivity() {

    private var adapter: CommonAdapter<Wallet>? = null

    private var listData: MutableList<Wallet>? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun layoutId(): Int {
        return R.layout.activity_wallet_list
    }

    override fun initData() {
        setResult(Activity.RESULT_CANCELED)
        listData=ArrayList()
    }

    override fun initView() {
        toolbar?.inflateMenu(R.menu.toolbar_add)

        toolbar?.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.add -> {


                    intentTo(InitialActivity::class.java)
                }
            }
            return@setOnMenuItemClickListener true
        }

        adapter = object : CommonAdapter<Wallet>(this, R.layout.item_wallet_view, listData) {
            override fun convert(holder: ViewHolder?, data: Wallet?, position: Int) {


                holder?.setText(R.id.tv_name, data?.name)

                var tv_time=holder?.getView<TextView>(R.id.tv_time)

                ThreadPoolUtils.getInstance().execute {
                    WalletDataBase.getInstance(this@WalletListActivity)?.walletDao?.getWallet( data?.name)?.let{

                        var updateTime=it.updateTime
                        runOnUiThread {

                            tv_time?.setText(DateUtils.longToDay(updateTime))
                        }
                    }

                }

                holder?.itemView?.setOnClickListener {
                    var intent=Intent()
                    intent.putExtra("data",data)
                    setResult(Activity.RESULT_OK,intent)
                    finish()

                }
            }

        }

        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = linearLayoutManager

    }

    override fun loadData() {
        listData?.clear()
        listData?.addAll(WalletManager.getInstance().wallets)
        adapter?.notifyDataSetChanged()
    }

}
