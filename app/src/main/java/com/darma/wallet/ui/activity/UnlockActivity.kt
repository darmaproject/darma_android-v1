package com.darma.wallet.ui.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.FrameLayout
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.ui.fragment.FingerprintFragment
import com.darma.wallet.ui.fragment.GestureFragment
import com.darma.wallet.utils.GestureUnlock
import com.darma.wallet.utils.SettingManager
import com.darma.wallet.widget.gestureunlock.fragment.GestureVerifyFragment
import com.darma.wallet.widget.gestureunlock.vo.ConfigGestureVO
import com.darma.wallet.widget.gestureunlock.vo.ResultVerifyVO
import com.wallet.WalletManager
import kotlinx.android.synthetic.main.activity_unlock.*

class UnlockActivity : BaseActivity() {


    val INTENT_TO = "intent_to"
    val INTENT_TO_INDEX = "intent_to_index"


    internal var fragmentContainer: FrameLayout? = null


    private var currentFragment: Fragment? = null
    private var mGestureVerifyFragment: GestureFragment? = null
    private var mFingerprintFragment: FingerprintFragment? = null
    override fun layoutId(): Int {
        return R.layout.activity_unlock
    }


    override fun initView() {

        tv_name?.setText(WalletManager.getInstance().openWallet.name)
        tv_gesture?.setOnClickListener {
            if (isGesture) {
                closeAct()
            } else if (SettingManager.isGestureOpen(this)) {

                showVerifyGestureLayout()
            } else {

                closeAct()
            }
        }

    }

    override fun loadData() {
    }

    override fun initData() {

        setCheckUnlock(false)
//        fullScreen(this)

        GestureUnlock.getInstance().init(this.applicationContext)


        if(SettingManager.isFingerprintOpen(this)){

            showVerifyFingerprintLayout()
            if (!SettingManager.isGestureOpen(this)) {
                tv_gesture?.setText(getString(R.string.str_exit_wallet))
            }
        }else if (SettingManager.isGestureOpen(this)) {
            showVerifyGestureLayout()
        }else{
            finish()
        }
    }


     var isGesture = false

    private fun showVerifyFingerprintLayout() {
        if (mFingerprintFragment == null) {
            mFingerprintFragment = FingerprintFragment()
            mFingerprintFragment?.setOnResult(object : FingerprintFragment.OnResult {
                override fun onSuccess() {
                    onUnLockSuccess()
                }

                override fun onError(error: String) {


                    if (SettingManager.isGestureOpen(this@UnlockActivity)) {
                        showVerifyGestureLayout()
                    }
                }
            })
        }
        safeAddFragment(mFingerprintFragment, R.id.fragment_container, "FingerprintFragment")
    }

    private fun showVerifyGestureLayout() {
        tv_gesture?.setText(getString(R.string.str_forget_gesture_pwd))
        isGesture = true
        if (null == mGestureVerifyFragment) {
            mGestureVerifyFragment = GestureFragment()
            mGestureVerifyFragment?.setGestureVerifyListener(object :
                GestureVerifyFragment.GestureVerifyListener {
                override fun onVerifyResult(result: ResultVerifyVO) {
                    if (result.isFinished) {

                        onUnLockSuccess()
                    } else {

                        closeAct()
                    }

                }

                override fun closeLayout() {

                }

                override fun onStartCreate() {

                }

                override fun onCancel() {

                }

                override fun onEventOccur(eventCode: Int) {

                }
            })
        }
        mGestureVerifyFragment?.setData(ConfigGestureVO.defaultConfig())
        safeAddFragment(mGestureVerifyFragment, R.id.fragment_container, "GestureVerifyFragment")
        mGestureVerifyFragment?.setGestureCodeData(GestureUnlock.getInstance().getGestureCodeSet(this))
    }


    private fun safeAddFragment(fragment: Fragment?, id: Int, tag: String) {
        if(fragment==null){
            return
        }
        var fragment = fragment
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val tempFragment = fragmentManager.findFragmentByTag(tag)
        if (tempFragment != null) {
            fragment = tempFragment
        }
        if (fragment.isAdded) {
            addOrShowFragment(fragmentTransaction, fragment, id, tag)
        } else {
            if (currentFragment != null && currentFragment!!.isAdded()) {
                fragmentTransaction.hide(currentFragment!!).add(id, fragment, tag).commit()
            } else {
                fragmentTransaction.add(id, fragment, tag).commit()
            }
            currentFragment = fragment
        }
    }
    private fun addOrShowFragment(
        transaction: FragmentTransaction,
        fragment: Fragment,
        containerLayoutId: Int,
        tag: String
    ) {

        if (currentFragment == null || currentFragment === fragment)
            return
        if (!fragment.isAdded) {
            transaction.hide(currentFragment!!).add(containerLayoutId, fragment, tag).commit()
        } else {
            transaction.hide(currentFragment!!).show(fragment).commit()
        }
        currentFragment?.setUserVisibleHint(false)
        currentFragment = fragment
        currentFragment?.setUserVisibleHint(true)
    }


    private fun closeAct() {

        reStartApp()

        finish()
    }

    private fun onUnLockSuccess() {


        finish()
    }



    override fun onBackPressed() {
        //        super.onBackPressed();

    }
}
