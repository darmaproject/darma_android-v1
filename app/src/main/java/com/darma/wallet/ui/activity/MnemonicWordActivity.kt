package com.darma.wallet.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.WindowManager
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.AppUtils
import com.darma.wallet.utils.DialogUtils
import com.wallet.WalletManager
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_mnemonic_word.*

class MnemonicWordActivity : BaseActivity() {

    companion object{
        var FROM="from"
        var FROM_SHOW_SEED="from_show_seed"
    }

    private var listData: MutableList<String>? = null
    private var adapter: CommonAdapter<String>? = null
    override fun layoutId(): Int {
        return R.layout.activity_mnemonic_word
    }

    var justShow=false
    override fun initData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        listData = ArrayList()

        intent.getStringExtra(FROM)?.let{
            when(it){
                FROM_SHOW_SEED->{

                    justShow=true
                }
            }
        }
    }

    override fun initView() {
        toolbar?.inflateMenu(R.menu.toolbar_copy)

        toolbar?.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.copy -> {
                    AppUtils.copyTextToClipboard(this, WalletManager.getInstance().mnemonicWordStr)
                    DialogUtils.showAlertDialog(
                        this,
                        getString(R.string.str_copy_success),
                        getString(R.string.str_copy_mnemonic_word_notice)
                    )
                }
            }
            return@setOnMenuItemClickListener true
        }

        btn_next.setOnClickListener {
            intentTo(MnemonicWordVerifyActivity::class.java)
        }
        if(justShow){
            btn_next.visibility= View.GONE

            setTitleText(R.string.str_seed_key)

        }

        adapter = object : CommonAdapter<String>(this, R.layout.item_mnemonic_layout, listData) {
            override fun convert(holder: ViewHolder?, data: String?, position: Int) {

                holder?.setText(R.id.tv_number, "" + (position + 1))
                holder?.setText(R.id.tv_mnemonic, data)


            }

        }

        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager= GridLayoutManager(this, 4)


    }

    override fun loadData() {

        listData?.clear()
        listData?.addAll(WalletManager.getInstance().mnemonicWordList)

        adapter?.notifyDataSetChanged()

    }

    override fun onBackPressed() {
        super.onBackPressed()

//        if(!justShow) {
//            var file = WalletManager.getInstance().openWallet.walletFile
//            WalletManager.getInstance().closeWallet()
//            file.delete()
//        }
    }


}
