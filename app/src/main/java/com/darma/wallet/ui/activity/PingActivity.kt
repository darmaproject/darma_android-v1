package com.darma.wallet.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.NodeDB
import com.darma.wallet.utils.ThreadPoolUtils
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_ping.*

class PingActivity : BaseActivity() {

    var node:NodeDB?=null
    override fun layoutId(): Int {
        return R.layout.activity_ping
    }


    override fun initData() {

        node=intent.getSerializableExtra("data") as NodeDB
    }

    override fun initView() {


        setTitleText(R.string.str_ping)
    }

    override fun loadData() {


        node?.let{

            ThreadPoolUtils.getInstance().execute {

                var ping=WalletManager.getInstance().pingNodeAddress(it.url)

                ll_complete.postDelayed({

                    ll_loading.visibility= View.GONE
                    ll_complete.visibility= View.VISIBLE

                    if(ping.status==0){
                        tv_fail.visibility=View.VISIBLE
                    }else{

                        ll_success.visibility=View.VISIBLE

                        tv_block_height.setText(getString(R.string.str_block_height_1)+"  "+ping.topo_height)
                        tv_ping.setText(getString(R.string.str_delay)+"  "+ping.delay+"ms")


                        progressbar.setProgress(ping.score)


                    }

                },2000)

            }
        }
    }

}
