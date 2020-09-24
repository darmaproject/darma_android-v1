package com.darma.wallet.ui.activity

import android.Manifest
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.bean.OutputDetial
import com.darma.wallet.utils.AppUtils
import com.darma.wallet.utils.ScreenShotUtils
import com.darma.wallet.utils.ShareUtils
import com.darma.wallet.utils.dialog.CommonAlertDialog
import com.darma.wallet.utils.dialog.CopyAlertDialog
import com.orhanobut.logger.Logger
import com.wallet.bean.TransactionRecordBean
import com.wallet.model.Coin
import com.wallet.utils.DateUtils
import com.wallet.utils.StringUtils
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import pub.devrel.easypermissions.EasyPermissions

class TransactionDetailActivity : BaseActivity() {

    var trans: TransactionRecordBean? = null

    private var adapter: CommonAdapter<OutputDetial>? = null

    private var listData: MutableList<OutputDetial>? = null
    override fun layoutId(): Int {
        return R.layout.activity_transaction_detail
    }

    override fun initData() {

        trans = intent.getSerializableExtra("data") as TransactionRecordBean
    }

    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        EasyPermissions.requestPermissions(this, getString(R.string.str_permission_request), 0, *perms)

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == 0) {
            if (perms.isNotEmpty()) {
                share()
            }
        }
    }

    private fun share() {

        showLoadingDialog()
        toolbar?.postDelayed({
            try {
                ShareUtils.share(ScreenShotUtils.getViewBitmap(ll_root), this)
            } catch (e: Exception) {
                e.printStackTrace()

                Logger.w(" checkPermission() ")
                checkPermission()
            }
            dismissLoadingDialog()
        }, 1000)
    }

    override fun initView() {
        toolbar?.inflateMenu(R.menu.toolbar_transaction_detail)

        toolbar?.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.block_browser -> {


                    AppUtils.viewUrl(this, "http://explorer.darmacash.com/tx?hash="+trans?.txid)

                }
                R.id.transaction_key -> {


                    if (StringUtils.isEmpty(trans?.secret_tx_key)) {

                        CommonAlertDialog.createDialog(this)
                            .setTitle(getString(R.string.str_tx_key))
                            .setContent(getString(R.string.str_view_tx_key_notice))
                            .setConfirm(getString(R.string.str_cancel))
                            .show()
                    } else {

                        CopyAlertDialog.createDialog(this)
                            .setTitle(getString(R.string.str_tx_key))
                            .setContent(trans?.secret_tx_key)
                            .setConfirm(getString(R.string.str_cancel))
                            .show()
                    }


                }
                R.id.share -> {

                    share()

                }
            }
            return@setOnMenuItemClickListener true
        }

        iv_copy_txid.setOnClickListener {
            AppUtils.copyTextToClipboard(this, et_txid.editText?.text.toString())
            showToast(getString(R.string.str_copy_success))
        }

        trans?.let {


            if (StringUtils.isEmpty(it.payment_id_str)) {

                et_payment_id.visibility = View.GONE
            } else {

                et_payment_id.editText?.setText(it.payment_id_str)
            }
            et_amount.editText?.setText(it.amount.toPlainString())
            et_fee.editText?.setText(it.details.fees.toPlainString())
            et_txid.editText?.setText(it.txid)

            et_date.editText?.setText(DateUtils.dateToString(it.time))

            et_height.editText?.setText("" + it.height)
            et_topo_height.editText?.setText("" + it.topoheight)
            if (it.status == 0) {

                et_status.editText?.setText(R.string.str_receive)
            } else {
                et_status.editText?.setText(R.string.str_send)

            }


            when(it.unlock_status){
                0->{

                    tv_status.setText(getString(R.string.str_send_success))

                    iv_status.setImageResource(R.mipmap.ic_success)
                }
                1->{

                    tv_status.setText(getString(R.string.str_pending))

                    iv_status.setImageResource(R.mipmap.ic_in_progress)
                }
                2->{

                    tv_status.setText(getString(R.string.str_trans_locked))

                    iv_status.setImageResource(R.mipmap.ic_trans_locked)
                }
            }
        }



        initRecyclerView()

    }

    private fun initRecyclerView() {
        listData = ArrayList()

        recycler_detail.setLayoutManager(LinearLayoutManager(this))
        recycler_detail.setHasFixedSize(true)
        adapter = object : CommonAdapter<OutputDetial>(this, R.layout.item_tansaction_detial, listData) {
            override fun convert(holder: ViewHolder?, data: OutputDetial?, position: Int) {
                holder?.setText(R.id.tv_address,data?.address)

                holder?.setText(R.id.tv_amount,data?.amount?.toPlainString())
                holder?.setText(R.id.tv_unit,Coin.COIN_UNIT)

                val text = holder?.setText(R.id.tv_title, getString(R.string.str_transfer_address)+(position+1))
            }

        }
        recycler_detail.adapter=adapter
    }

    override fun loadData() {


        trans?.details?.let{
            val amounts=it.amount
            val tos=it.to

            listData?.clear()
            if(amounts!=null&&tos!=null) {


                for (index in amounts.indices) {

                    var output = OutputDetial()
                    if (tos.size > index) {
                        output.address = tos[index]
                    } else {
                        output.address = ""
                    }
                    output.setAmount(amounts[index])

                    listData?.add(output)
                }
            }
            adapter?.notifyDataSetChanged()
        }


    }



}
