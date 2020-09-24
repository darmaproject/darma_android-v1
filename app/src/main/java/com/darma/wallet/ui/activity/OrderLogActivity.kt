package com.darma.wallet.ui.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.OrderDB
import com.darma.wallet.db.OrderDataBase
import com.darma.wallet.utils.DateTimeUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.darma.wallet.widget.EmptyAdapter
import com.scwang.smartrefresh.header.WaveSwipeHeader
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_contacts.mRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.collections.ArrayList

class OrderLogActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_recyclerview_refreshable
    }


    private var adapter: EmptyAdapter<OrderDB>? = null

    private var listData: MutableList<OrderDB>? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    override fun initData() {
        setResult(Activity.RESULT_CANCELED)
        listData = ArrayList()
    }

    override fun initView() {

        setTitleText(getString(R.string.str_order_log))

        initRecyclerView()


    }

    private fun initRecyclerView() {


        adapter = object : EmptyAdapter<OrderDB>(this, R.layout.item_order_record_layout, listData) {
            override fun convert(holder: ViewHolder?, data: OrderDB?, position: Int) {


                var base_coin=data?.base_coin?.toUpperCase()
                var quota_coin=data?.quota_coin?.toUpperCase()

                holder?.setText(R.id.tv_pair,base_coin+"/"+quota_coin)
                holder?.setText(R.id.tv_amount,data?.base_amount_total+" "+base_coin)
                holder?.setText(R.id.tv_quota_amount,"≈"+data?.quota_amount+" "+quota_coin)

                holder?.setText(R.id.tv_time,DateTimeUtils.stampToDateTime(data?.created_at))


                holder?.itemView?.setOnClickListener {
                    intentOrderDetailAct(data)
                }
//                holder?.itemView?.setOnLongClickListener {
//
//                    return@setOnLongClickListener  false
//                }

            }

        }
        adapter?.setEmptyView(R.layout.layout_empty_view) {
//            it.getView<ImageView>(R.id.iv_empty).setImageResource(R)
            it.setText(R.id.tv_empty, getString(R.string.str_no_records))
        }
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = linearLayoutManager

        mRefreshLayout.setEnableRefresh(true)
            .setEnableLoadmore(false)
            .setRefreshHeader(WaveSwipeHeader(this))
            .setPrimaryColorsId(R.color.color_white, R.color.text_color_black)
//            .setOnRefreshListener {
//                refresh()
//
//            }
    }


    fun intentOrderDetailAct(data: OrderDB?) {

        var bundle=Bundle()

        bundle.putSerializable(Config.DATA,data)
        intentTo(OrderLogDetailActivity::class.java,bundle)
    }

    private fun delete(data: OrderDB?) {

        showLoadingDialog()

        ThreadPoolUtils.getInstance().execute {
            OrderDataBase.getInstance(this).orderDao.delete(data)
            toolbar?.postDelayed({
                dismissLoadingDialog()
                loadData()
            }, 1000)
        }

    }

    override fun loadData() {

        ThreadPoolUtils.getInstance().execute {


            var list = OrderDataBase.getInstance(this).orderDao.allOrders


            listData?.clear()
            list?.let {
                it.reverse()
                listData?.addAll(it)
            }
            toolbar?.post {

                adapter?.notifyDataSetChanged()

            }
        }
    }
}
