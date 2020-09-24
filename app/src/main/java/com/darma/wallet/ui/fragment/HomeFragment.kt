package com.darma.wallet.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.darma.wallet.R
import com.darma.wallet.base.BaseFragment
import com.darma.wallet.event.EventConfigs
import com.darma.wallet.ui.activity.ReceiveActivity
import com.darma.wallet.ui.activity.SendActivity
import com.darma.wallet.ui.activity.TransactionDetailActivity
import com.darma.wallet.ui.activity.WalletManagerActivity
import com.darma.wallet.utils.DateTimeUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.scwang.smartrefresh.header.WaveSwipeHeader
import com.wallet.WalletConfig
import com.wallet.WalletManager
import com.wallet.bean.TransactionRecordBean
import com.wallet.bean.WalletStatusInfoBean
import com.wallet.utils.DateUtils
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import org.simple.eventbus.Subscriber


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BaseFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : BaseFragment() {


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
            HomeFragment().apply {
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
        return R.layout.fragment_home
    }

    override fun initData() {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    private var adapter: CommonAdapter<TransactionRecordBean>? = null

    private var listData: MutableList<TransactionRecordBean>? = null
    private var listDataAll: MutableList<TransactionRecordBean>? = null
    private var listDataSend: MutableList<TransactionRecordBean>? = null
    private var listDataReceive: MutableList<TransactionRecordBean>? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun initView() {


        initRecyclerView()


        cl_top.setOnClickListener {
            scrollToTop()
        }

        tv_title.setText(WalletManager.getInstance().openWallet.name)

        iv_menu.setOnClickListener {
            intentTo(WalletManagerActivity::class.java)
        }
        radioGroup.setOnCheckedChangeListener { _, checkedId ->


            when (checkedId) {
                R.id.rb_all -> {
                    index = 0
                }
                R.id.rb_received -> {
                    index = 1
                }
                R.id.rb_send -> {
                    index = 2
                }
            }


            setList()
            if(listData==null|| listData?.size==0){
                refresh()
            }
        }

        btn_receive.setOnClickListener {
            intentTo(ReceiveActivity::class.java)
        }
        btn_send.setOnClickListener {

            intentTo(SendActivity::class.java)
        }
    }

    var index = 0


    fun scrollToTop() {
        val behavior = (appBarLayout.getLayoutParams() as CoordinatorLayout.LayoutParams).behavior
        if (behavior is AppBarLayout.Behavior) {
            val appBarLayoutBehavior = behavior as AppBarLayout.Behavior?
            appBarLayoutBehavior!!.topAndBottomOffset = 0

            mRecyclerView.scrollToPosition(0)
        }
    }

    private fun initRecyclerView() {
        listData = ArrayList()
        listDataAll = ArrayList()
        listDataSend = ArrayList()
        listDataReceive = ArrayList()
        adapter =
            object : CommonAdapter<TransactionRecordBean>(context, R.layout.item_transaction_record_layout, listData) {
                override fun convert(holder: ViewHolder?, data: TransactionRecordBean?, position: Int) {

//                holder?.setText(R.id.tv_transaction, t)


                    var ivTrans = holder?.getView<ImageView>(R.id.iv_trans)
                    var tvTrans = holder?.getView<TextView>(R.id.tv_trans)

                    var tv_status = holder?.getView<TextView>(R.id.tv_status)
                    if (data?.status == 1) {//send

                        ivTrans?.setImageResource(R.mipmap.ic_send_item)
                        tvTrans?.setText(R.string.str_send)
                    } else {

                        ivTrans?.setImageResource(R.mipmap.ic_receive_item)
                        tvTrans?.setText(R.string.str_receive)
                    }

                    holder?.setText(R.id.tv_number, data?.amount?.toFriendlyString())
//                holder?.setText(R.id.tv_number_usd, data?.amount?.toRateString())
                    holder?.setText(R.id.tv_time, DateUtils.dateToString(data?.time))


                    var tvMonth = holder?.getView<TextView>(R.id.tv_month)

                    var month = DateTimeUtils.getMonthAndYear(context, data?.time)
                    tvMonth?.text = month
                    if (position >= 1) {

                        var monthLast = DateTimeUtils.getMonthAndYear(context, data?.time)

                        if (month == monthLast) {
                            tvMonth?.visibility = View.GONE
                        } else {

                            tvMonth?.visibility = View.VISIBLE
                        }
                    } else {

                        tvMonth?.visibility = View.VISIBLE
                    }

                    context?.let {
                        when (data?.unlock_status) {
                            0 -> {

                                tv_status?.setText(getString(R.string.str_completed))
                                tv_status?.setTextColor(ContextCompat.getColor(it, R.color.text_color_green))
                            }
                            1 -> {

                                tv_status?.setText(getString(R.string.str_sync))
                                tv_status?.setTextColor(ContextCompat.getColor(it, R.color.text_color_pink))

                            }
                            2 -> {

                                tv_status?.setText(getString(R.string.str_locked_1))
                                tv_status?.setTextColor(ContextCompat.getColor(it, R.color.text_color_red))

                            }
                            else ->{
                                tv_status?.setText(getString(R.string.str_completed))
                                tv_status?.setTextColor(ContextCompat.getColor(it, R.color.text_color_green))

                            }
                        }
                    }

                    holder?.itemView?.setOnClickListener {
                        var intent = Intent(context, TransactionDetailActivity::class.java)
                        intent.putExtra("data", data)
                        startActivity(intent)
                    }
                }

            }

        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = linearLayoutManager

        mRefreshLayout.setEnableRefresh(true)
            .setEnableLoadmore(true)
            .setRefreshHeader(WaveSwipeHeader(context))
            .setPrimaryColorsId(R.color.color_white, R.color.text_color_black)
            .setOnRefreshListener {
                refresh()

            }
            .setOnLoadmoreListener {

                loadMore()
            }
    }

    private fun loadMore() {
        onLoad = true
        ThreadPoolUtils.getInstance().execute {
            loadList()
            setList()
        }
    }

    private fun getIndex(): String {
        var listc=getCurrentList()
        if (listc == null || listc.size == 0) {
            return ""
        } else {
            return listc.get(listc.size - 1).height.toString()
        }
    }




    private fun refresh() {
        if (onLoad)
            return
        onLoad = true
        ThreadPoolUtils.getInstance().execute {
            clearList()
            loadList()
            setList()
        }
    }


    private fun clearList(){
        when (index) {
            0 -> {
                listDataAll?.clear()
            }
            1 -> {
                listDataReceive?.clear()
            }
            2 -> {
                listDataSend?.clear()
            }
        }
    }

    private fun loadList() {

        var list = getTransactions()
        if(list==null){
            return
        }
        when (index) {
            0 -> {
                listDataAll?.addAll(list)
            }
            1 -> {
                listDataReceive?.addAll(list)
            }
            2 -> {
                listDataSend?.addAll(list)
            }
        }
    }
    private fun setList() {

        listData?.clear()
        when (index) {
            0 -> {
                listDataAll?.let { listData?.addAll(it) }
            }
            1 -> {
                listDataReceive?.let { listData?.addAll(it) }
            }
            2 -> {
                listDataSend?.let { listData?.addAll(it) }
            }
        }

        overLoad()
    }
    private fun getCurrentList(): MutableList<TransactionRecordBean>? {
        var list=listDataAll
        when (index) {
            0 -> {
                list=listDataAll
            }
            1 -> {
                list=listDataReceive
            }
            2 -> {
               list=listDataSend
            }
        }

        return list
    }
    private fun getTransactions( ): List<TransactionRecordBean>? {
        var list :MutableList<TransactionRecordBean>?= null
        when (index) {
            0 -> {
                list=WalletManager.getInstance().getTransactions(true,true,getIndex())
            }
            1 -> {
                list=WalletManager.getInstance().getTransactions(true,false,getIndex())
            }
            2 -> {
                list=WalletManager.getInstance().getTransactions(false,true,getIndex())
            }
        }
        return list
    }

    override fun lazyLoad() {

//        Logger.d("lazyLoad()")

        refresh()

    }

    override fun onResume() {
        super.onResume()

        var name = WalletManager.getInstance().openWallet.name


        if (name != tv_title.text.toString()) {

            tv_title.setText(name)
            rb_all.isChecked = true
        }
        if (WalletManager.getInstance().isViewOnlyWallet) {

            btn_send.isEnabled = false

            btn_send.setText(R.string.str_send_view_only)

        } else {

            btn_send.isEnabled = true
            btn_send.setText(R.string.str_send)
        }
    }

    @Subscriber(tag = EventConfigs.EVENT_WALLET_STATUS_INFO)
    fun getWalletStatusInfoEven(even: WalletStatusInfoBean) {

//        Logger.d("getWalletStatusInfoEven "+even)
        context?.let {

            var name = WalletManager.getInstance().openWallet.name


            if (name != tv_title.text.toString()) {

                tv_title.setText(name)
                rb_all.isChecked = true

            }
//            tv_title.setText(WalletManager.getInstance().openWallet.name)

            var balance = even.unlocked_balance.toPlainString()
            var locking = getString(R.string.str_locked_1) + even.locked_balance.toPlainString()
            if (tv_balance.text.toString() != even.unlocked_balance.toPlainString() || tv_locking.text.toString() != locking) {

                refresh()
            }

            tv_balance.setText(balance)
            tv_locking.setText(locking)

            if (even.wallet_complete) {

                btn_send.isEnabled = true
                btn_send.setText(R.string.str_send)
            }

            when (WalletManager.getInstance().walletStatus) {
                WalletConfig.STATUS_WALLET_OFFLINE -> {

                    iv_status.setImageResource(R.mipmap.ic_disconnect)
                    ll_status.setBackgroundResource(R.drawable.shape_status_bg)
                    tv_status.text = getString(R.string.str_wallet_offline)
                    tv_status.setTextColor(ContextCompat.getColor(it, R.color.text_color_gray))
                    tv_height.visibility = View.GONE

                    progressbar_loading.visibility = View.GONE
                    iv_status.visibility = View.VISIBLE

                    btn_send.isEnabled = false
                }

                WalletConfig.STATUS_WALLET_UNAVAILABLE -> {

                    iv_status.setImageResource(R.mipmap.ic_unavailable)
                    ll_status.setBackgroundResource(R.drawable.shape_status_complete_bg)
                    tv_status.text = getString(R.string.str_wallet_disconnected)
                    tv_status.setTextColor(ContextCompat.getColor(it, R.color.text_color_gray))

                    tv_height.setTextColor(ContextCompat.getColor(it, R.color.text_color_gray))
                    tv_height.visibility = View.VISIBLE

                    progressbar_loading.visibility = View.GONE
                    iv_status.visibility = View.VISIBLE
                    tv_height.text = getString(R.string.str_block_height) + even.wallet_topo_height
                }
                WalletConfig.STATUS_SYNCHING -> {

//                    iv_status.setImageResource(R.mipmap.ic_synching)
                    ll_status.setBackgroundResource(R.drawable.shape_status_bg)
                    tv_status.text = getString(R.string.str_start_sync) + " " + even.synchronousProgress
                    tv_status.setTextColor(ContextCompat.getColor(it, R.color.text_color_gray))

                    tv_height.setTextColor(ContextCompat.getColor(it, R.color.text_color_gray))
                    tv_height.visibility = View.VISIBLE

                    progressbar_loading.visibility = View.VISIBLE
                    iv_status.visibility = View.GONE
                    tv_height.text =
                        getString(R.string.str_residue_block) + (even.daemon_topo_height - even.wallet_topo_height)

                }
                WalletConfig.STATUS_COMPLETION -> {

                    iv_status.setImageResource(R.mipmap.ic_complete)
                    ll_status.setBackgroundResource(R.drawable.shape_status_complete_bg)
                    tv_status.text = getString(R.string.str_sync_complete)
                    tv_status.setTextColor(ContextCompat.getColor(it, R.color.text_color_pink))

                    tv_height.setTextColor(ContextCompat.getColor(it, R.color.text_color_pink))
                    tv_height.visibility = View.VISIBLE

                    progressbar_loading.visibility = View.GONE
                    iv_status.visibility = View.VISIBLE
                    tv_height.text = getString(R.string.str_block_height) + even.wallet_topo_height
                }

                WalletConfig.STATUS_CONECTING -> {
                    ll_status.setBackgroundResource(R.drawable.shape_status_bg)
                    progressbar_loading.visibility = View.VISIBLE
                    tv_status.text = getString(R.string.str_start_sync)
                    tv_height.visibility = View.GONE
                    iv_status.visibility = View.GONE
                }
            }
        }
    }


    var onLoad = false
    private fun overLoad() {
        onLoad = false
        mRefreshLayout?.post {
            adapter?.notifyDataSetChanged()
            mRefreshLayout.finishRefresh()
            mRefreshLayout.finishLoadmore()
        }
    }

}
