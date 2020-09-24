package com.darma.wallet.ui.activity

import android.os.Build
import android.widget.SeekBar
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_synchronous_interval.*

class SynchronousIntervalActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_synchronous_interval
    }

    override fun initData() {
    }

    override fun initView() {

        setTitleText(R.string.str_sync_interval)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekbar.min=5
        }
        seekbar.max=120

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if(progress<5){
                    seekbar.progress=5
                    return
                }

                WalletManager.getInstance().syncDelay=progress.toLong()
                tv_sync.setText( ""+progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


    }

    override fun loadData() {



        var progress=WalletManager.getInstance().syncDelay
       tv_sync.setText( ""+progress)
        seekbar.progress=progress.toInt()
    }



}
