package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.darma.wallet.Config
import com.darma.wallet.MyApplication
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.bean.LanguageType
import com.darma.wallet.utils.dialog.BottomSelectDialog
import com.darma.wallet.widget.EmptyAdapter
import com.wallet.WalletManager
import com.wallet.utils.StringUtils
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_contacts.*


class SelectLanguageActivity : BaseActivity() {


    override fun layoutId(): Int {
        return R.layout.activity_recyclerview
    }


    private var adapter: EmptyAdapter<LanguageType>? = null

    private var listData: ArrayList<LanguageType>? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    override fun initData() {
        setResult(Activity.RESULT_CANCELED)
        listData = ArrayList()


    }

    override fun initView() {

        setTitleText(getString(R.string.str_language_select))

        initRecyclerView()


    }

    fun getSelectLanguage(): String {

        return MyApplication.context.getSelectLanguage()
    }

    private fun changeLanguage(language: LanguageType) {

        StringUtils.saveStringToSp(this, Config.SELECT_LANGUAGE, language.locale)


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

//            LanguageUtil.changeAppLanguage(MyApplication.context, language.language)

            reStartApp()
            WalletManager.getInstance().closeWallet()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
//            intentToMain()
        }

    }

    private fun initRecyclerView() {


        adapter = object : EmptyAdapter<LanguageType>(this, R.layout.item_language_view, listData) {
            override fun convert(holder: ViewHolder?, data: LanguageType?, position: Int) {


                var tv_language = holder?.getView<TextView>(R.id.tv_language)

                var iv_selected = holder?.getView<ImageView>(R.id.iv_selected)

                tv_language?.setText(data?.language)


                if (data == LanguageType.SYSTEM) {

                    tv_language?.setText(R.string.atr_auto)

                }


                if (getSelectLanguage() == data?.locale) {

                    tv_language?.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.text_color_pink
                        )
                    )
                    iv_selected?.visibility = View.VISIBLE
                } else {
                    tv_language?.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.text_color_gray
                        )
                    )
                    iv_selected?.visibility = View.GONE
                }



                holder?.itemView?.setOnClickListener {

                    if (getSelectLanguage() != data?.locale) {
                        BottomSelectDialog.createDialog(this@SelectLanguageActivity)
                            .addItem(getString(R.string.str_select)) {

                                if (data != null) {
                                    changeLanguage(data)
                                }
                            }
                            .show()
                    }
                }


            }

        }
        adapter?.setEmptyView(R.layout.layout_empty_view) {
        }
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = linearLayoutManager
    }


    override fun loadData() {
        listData?.clear()

        listData?.addAll(LanguageType.values())
    }
}
