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
import com.darma.wallet.db.NodeDB
import com.darma.wallet.db.WalletDataBase
import com.darma.wallet.utils.NodeUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.darma.wallet.utils.dialog.BottomSelectDialog
import com.darma.wallet.widget.EmptyAdapter
import com.wallet.WalletManager
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_contacts.*

class NodesActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_recyclerview
    }


    private var adapter: EmptyAdapter<NodeDB>? = null

    private var listData: MutableList<NodeDB>? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    override fun initData() {
        setResult(Activity.RESULT_CANCELED)
        listData = ArrayList()
    }

    override fun initView() {

        setTitleText(R.string.str_node)

        toolbar?.inflateMenu(R.menu.toolbar_add)

        toolbar?.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.add -> {


                    intentTo(EditNodeActivity::class.java)
                }
            }
            return@setOnMenuItemClickListener true
        }
        initRecyclerView()


    }

    private fun initRecyclerView() {


        adapter = object : EmptyAdapter<NodeDB>(this, R.layout.item_contacts_view, listData) {
            override fun convert(holder: ViewHolder?, data: NodeDB?, position: Int) {


                var tv_remark = holder?.getView<TextView>(R.id.tv_remark)
                var tv_address = holder?.getView<TextView>(R.id.tv_address)

                var iv_selected = holder?.getView<ImageView>(R.id.iv_selected)


                if(data?.id==NodeUtils.NODE_AUTO.id) {

                    tv_remark?.setText(R.string.str_node_auto)

                    tv_address?.setText(R.string.str_node_auto_notice)
                }else{

                    tv_address?.setText(data?.tag)

                    tv_remark?.setText(data?.ip + ":" + data?.post)
                }

                if (WalletManager.getInstance().selectNode.id == data?.id) {

                    tv_remark?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_pink))
                    tv_address?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_pink))
                    iv_selected?.visibility = View.VISIBLE
                } else {
                    tv_remark?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray))
                    tv_address?.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_gray))
                    iv_selected?.visibility = View.GONE
                }



                holder?.itemView?.setOnClickListener {

                   if(data?.id==NodeUtils.NODE_AUTO.id){
                       if(WalletManager.getInstance().selectNode.id !=data?.id){
                           BottomSelectDialog.createDialog(this@NodesActivity)
                               .addItem(getString(R.string.str_select)) {

                                   select(data)
                               }
                               .show()
                       }

                   }else if (WalletManager.getInstance().selectNode.id == data?.id) {
                        BottomSelectDialog.createDialog(this@NodesActivity)
                            .addItem(getString(R.string.str_ping)) {

                                goPingAct(data)
                            }
                            .show()
                    } else if (data?.isDefault == true) {
                        BottomSelectDialog.createDialog(this@NodesActivity)
                            .addItem(getString(R.string.str_select)) {
                                select(data)
                            }.addItem(getString(R.string.str_ping)) {

                                goPingAct(data)
                            }
                            .show()

                    } else {
                        BottomSelectDialog.createDialog(this@NodesActivity)
                            .addItem(getString(R.string.str_select)) {
                                select(data)
                            }
                            .addItem(getString(R.string.str_edit)) {

                                goEditAct(data)
                            }.addItem(getString(R.string.str_ping)) {

                                goPingAct(data)
                            }.addItem(getString(R.string.str_delet)) {

                                delete(data)
                            }.show()
                    }
                }


            }

        }
        adapter?.setEmptyView(R.layout.layout_empty_view) {
            it.setText(R.id.tv_empty, getString(R.string.str_no_available_nodes))
        }
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = linearLayoutManager
    }

    private fun goPingAct(data: NodeDB?) {


        var intent = Intent(this, PingActivity::class.java)
        if (data != null) {

            intent.putExtra("data", data)
        }
        startActivity(intent)


    }

    private fun select(data: NodeDB?) {

        showLoadingDialog()
        WalletManager.getInstance().saveSelectNode(data?.toNode())

        toolbar?.postDelayed({
            adapter?.notifyDataSetChanged()
            dismissLoadingDialog()
        }, 2000)


    }

    private fun goEditAct(data: NodeDB?) {

        var intent = Intent(this, EditNodeActivity::class.java)
        if (data != null) {

            intent.putExtra("data", data)
        }
        startActivity(intent)
    }


    private fun delete(data: NodeDB?) {

        showLoadingDialog()

        ThreadPoolUtils.getInstance().execute {
            WalletDataBase.getInstance(this).nodesDao.delete(data)
            toolbar?.postDelayed({
                dismissLoadingDialog()
                loadData()
            }, 1000)
        }

    }

    override fun loadData() {

        ThreadPoolUtils.getInstance().execute {
            listData?.clear()

            var list = NodeUtils.getNodes(this)

            list?.let {

                listData?.addAll(it)
            }
            toolbar?.post {

                adapter?.notifyDataSetChanged()

            }
        }
    }
}
