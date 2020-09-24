package com.darma.wallet.ui.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.WindowManager
import android.widget.TextView
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.wallet.WalletManager
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_mnemonic_word.mRecyclerView
import kotlinx.android.synthetic.main.activity_mnemonic_word_verify.*
import java.util.*
import kotlin.collections.ArrayList

class MnemonicWordVerifyActivity : BaseActivity() {


    private var listData: MutableList<String>? = null
    private var answerWords: MutableList<String>? = null//
    private var choseWords: MutableList<String>? = null//
    private var adapter: CommonAdapter<String>? = null
    override fun layoutId(): Int {
        return R.layout.activity_mnemonic_word_verify
    }

    override fun initData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        listData = ArrayList()
        choseWords = ArrayList()
    }

    override fun initView() {

        btn_verify.isEnabled = false
        btn_verify.setOnClickListener {

            if (checkAnswer()) {

                intentToMain()
            } else {

                showToast(getString(R.string.str_verify_fail_please_retry))

                clearChose()

            }

        }

        adapter = object : CommonAdapter<String>(this, R.layout.item_mnemonic_layout, listData) {
            override fun convert(holder: ViewHolder?, data: String?, position: Int) {


                var tvMnemonic = holder?.getView<TextView>(R.id.tv_mnemonic)
                tvMnemonic?.setText(data)

                tvMnemonic?.setTextColor(resources.getColor(R.color.text_color_black))

                holder?.itemView?.setOnClickListener {

                    choseWords?.let {

                        if (it.size == 4) {
                            return@setOnClickListener
                        }

                        if (data != null && !it.contains(data)) {
                            it.add(data)
                            tvMnemonic?.setTextColor(resources.getColor(R.color.text_color_very_gray))


                            setChoseWords(it.size - 1)
                        }

                        if (it.size == 4) {
                            btn_verify.isEnabled = true
                        }
                    }

                }

            }

        }

        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = GridLayoutManager(this, 4)

        tv_skip.setOnClickListener {
            intentToMain()
        }

    }

    private fun clearChose() {
        btn_verify.isEnabled = false
        choseWords?.clear()
        loadData()
    }


    private fun setChoseWords(index: Int) {

        when (index) {
            0 -> {
                tv_mnemonic_1?.text = choseWords!!.get(index)
            }
            1 -> {
                tv_mnemonic_2?.text = choseWords!!.get(index)
            }
            2 -> {
                tv_mnemonic_3?.text = choseWords!!.get(index)
            }
            3 -> {
                tv_mnemonic_4?.text = choseWords!!.get(index)
            }
        }

    }

    private fun checkAnswer(): Boolean {
        if (choseWords!!.size == 4) {
            var i = 0
            while (i < 4) {
                if (answerWords!!.get(i) != choseWords!!.get(i)) {
                    return false
                }
                i++
            }

            return true

        }
        return false
    }

    override fun loadData() {

        listData?.clear()
        listData?.addAll(WalletManager.getInstance().mnemonicWordList)

        listData?.let {
            randomAnswer(it)
        }

        listData?.shuffle()
        adapter?.notifyDataSetChanged()

    }

    private fun randomAnswer(list: MutableList<String>) {

        var random = Random()
        answerWords = ArrayList()
        var num1 = getRandNum(random, list)
        answerWords!!.add(list.get(num1))
        var num2 = getRandNum(random, list)
        answerWords!!.add(list.get(num2))
        var num3 = getRandNum(random, list)
        answerWords!!.add(list.get(num3))
        var num4 = getRandNum(random, list)
        answerWords!!.add(list.get(num4))

        tv_number_1.setText("" + (num1 + 1))
        tv_number_2.setText("" + (num2 + 1))
        tv_number_3.setText("" + (num3 + 1))
        tv_number_4.setText("" + (num4 + 1))


        tv_mnemonic_1.text = ""
        tv_mnemonic_2.text = ""
        tv_mnemonic_3.text = ""
        tv_mnemonic_4.text = ""


    }

    private fun getRandNum(random: Random, list: MutableList<String>): Int {
        var num = random.nextInt(list.size)
        if (answerWords!!.contains(list.get(num))) {
            return getRandNum(random, list)
        }
        return num
    }


}
