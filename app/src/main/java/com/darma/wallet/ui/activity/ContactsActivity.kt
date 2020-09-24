package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.darma.wallet.Config
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.ContactsDB
import com.darma.wallet.db.WalletDataBase
import com.darma.wallet.utils.ThreadPoolUtils
import com.darma.wallet.utils.dialog.BottomSelectDialog
import com.darma.wallet.widget.EmptyAdapter
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_contacts.mRecyclerView
import kotlinx.android.synthetic.main.activity_wallet_manager.*

class ContactsActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_contacts
    }


    private var adapter: EmptyAdapter<ContactsDB>? = null

    private var listData: MutableList<ContactsDB>? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    var coin_code=""

    override fun initData() {
        coin_code=intent.getStringExtra(Config.COIN_CODE)

        setResult(Activity.RESULT_CANCELED)
        listData = ArrayList()
    }

    override fun initView() {

        setTitleText(R.string.str_address_book)

        toolbar?.inflateMenu(R.menu.toolbar_add)

        toolbar?.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.add -> {


                    var bundle= Bundle()
                    bundle.putString(Config.COIN_CODE, coin_code)
                    intentTo(EditContactsActivity::class.java,bundle)
                }
            }
            return@setOnMenuItemClickListener true
        }
        initRecyclerView()


    }

    private fun initRecyclerView() {


        adapter = object : EmptyAdapter<ContactsDB>(this, R.layout.item_contacts_view, listData) {
            override fun convert(holder: ViewHolder?, data: ContactsDB?, position: Int) {


                var tv_remark = holder?.getView<TextView>(R.id.tv_remark)
                var tv_address = holder?.getView<TextView>(R.id.tv_address)

                var iv_selected = holder?.getView<ImageView>(R.id.iv_selected)
                tv_remark?.setText(data?.remark)

                tv_address?.setText(data?.address)




                holder?.itemView?.setOnClickListener {
                    var intent = Intent()
                    intent.putExtra("data", data)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

                holder?.itemView?.setOnLongClickListener {
                    BottomSelectDialog.createDialog(this@ContactsActivity)
                        .addItem(getString(R.string.str_edit)) {

                            goEditAct(data)
                        }.addItem(getString(R.string.str_delet)) {

                            delete(data)
                        }.show()
                    return@setOnLongClickListener true
                }
            }

        }
        adapter?.setEmptyView(R.layout.layout_empty_view){
            it.setText(R.id.tv_empty,String.format(getString(R.string.str_no_address_data),coin_code))
        }
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = linearLayoutManager
    }

    private fun goEditAct(data: ContactsDB?) {

        var intent=Intent(this,EditContactsActivity::class.java)

        intent.putExtra(Config.COIN_CODE,coin_code)
        if(data!=null){

            intent.putExtra("data",data)
        }
        startActivity(intent)
    }


    private fun delete(data: ContactsDB?) {

        showLoadingDialog()

        ThreadPoolUtils.getInstance().execute {
            WalletDataBase.getInstance(this).contactsDao.delete(data)
            toolbar?.postDelayed({
                dismissLoadingDialog()
                loadData()
            }, 1000)

        }

    }

    override fun loadData() {

        ThreadPoolUtils.getInstance().execute {
            listData?.clear()

            var list = WalletDataBase.getInstance(this).contactsDao?.getContacts(coin_code)
            list?.let {

                listData?.addAll(it)
            }
            toolbar?.post{

                adapter?.notifyDataSetChanged()

            }
        }
    }
}
