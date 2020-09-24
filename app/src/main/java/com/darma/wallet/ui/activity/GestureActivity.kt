package com.darma.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.GestureUnlock
import com.darma.wallet.utils.SettingManager
import com.darma.wallet.widget.gestureunlock.fragment.GestureCreateFragment
import com.darma.wallet.widget.gestureunlock.fragment.GestureVerifyFragment
import com.darma.wallet.widget.gestureunlock.vo.ConfigGestureVO
import com.darma.wallet.widget.gestureunlock.vo.ResultVerifyVO

class GestureActivity : BaseActivity() {


    private val TAG = "dds_test"


    private var currentFragment: Fragment? = null
    private var mGestureCreateFragment: GestureCreateFragment? = null
    private var mGestureVerifyFragment: GestureVerifyFragment? = null


    companion object {

        const val TYPE_GESTURE_CREATE = 1
        const val TYPE_GESTURE_VERIFY = 2
        const val TYPE_GESTURE_MODIFY = 3


        const val REQUEST_CODE_VERIFY = 2000

        const val RESULT_CODE_VERIFY_SUCCESS = 2001
        const val RESULT_CODE_VERIFY_FAIL = 2002


        @JvmStatic
        fun openActivity(activity: Activity, type: Int) {
            val intent = Intent(activity, GestureActivity::class.java)
            intent.putExtra("type", type)
//            if (activity !is Activity) {
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            }
            activity.startActivityForResult(intent, REQUEST_CODE_VERIFY)
        }


    }


    override fun layoutId(): Int {
        return R.layout.activity_gesture
    }

    override fun initData() {
    }

    override fun initView() {

        initVar()
        setResult(Activity.RESULT_CANCELED)

    }

    override fun loadData() {
    }


    private fun initVar() {
        val intent = intent
        val type = intent.getIntExtra("type", 2)
        if (type == TYPE_GESTURE_CREATE) {
            //Initialize gesture password
            showCreateGestureLayout()
        } else if (type == TYPE_GESTURE_VERIFY) {
            //Gesture password authentication
            showVerifyGestureLayout()
        } else if (type == TYPE_GESTURE_MODIFY) {
            // Change gesture password
            showModifyGestureLayout()
        } else {
            //Invalid operation, exit
            finish()
        }
    }


    /**
     * Display the layout of initialization gesture password
     */
    private fun showCreateGestureLayout() {

        setTitleText(getString(R.string.str_set_gesture_pwd))

        if (mGestureCreateFragment == null) {
            mGestureCreateFragment = GestureCreateFragment()
            mGestureCreateFragment?.setGestureCreateListener(object :
                GestureCreateFragment.GestureCreateListener {

                override fun onCreateFinished(gestureCode: String) {
                    GestureUnlock.getInstance()
                        .setGestureCode(this@GestureActivity, gestureCode)
                    this@GestureActivity.finish()
                    SettingManager.openGesture(this@GestureActivity)
                }

                override fun onCreateFailed(result: ResultVerifyVO) {
                }

                override fun closeLayout() {
                }

                override fun onCancel() {
                    // Cancel creating gesture password
                    this@GestureActivity.finish()
                }

                override fun onEventOccur(eventCode: Int) {

                }
            })
        }
        mGestureCreateFragment?.setData(ConfigGestureVO.defaultConfig())
        safeAddFragment(mGestureCreateFragment, R.id.fragment_container, "GestureCreateFragment")
    }

    /**
     * Display the layout of the authentication gesture password
     */
    private fun showVerifyGestureLayout() {
        //        ctbvTitle.setVisibility(View.GONE);
        setTitleText(getString(R.string.str_verify_gesture_password))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.BLACK

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        if (mGestureVerifyFragment == null) {
            mGestureVerifyFragment = GestureVerifyFragment()
            mGestureVerifyFragment?.setGestureVerifyListener(object :
                GestureVerifyFragment.GestureVerifyListener {
                override fun onVerifyResult(result: ResultVerifyVO) {
                    if (result.isFinished) {
                        //success
                        Log.d(TAG, "onVerifyResult:success")


                        setResult(RESULT_CODE_VERIFY_SUCCESS)
                        finish()
                    } else {
                        Log.d(TAG, "onVerifyResult:fail")

                        setResult(RESULT_CODE_VERIFY_FAIL)
                        finish()
                    }

                }

                override fun closeLayout() {
                    this@GestureActivity.finish()
                }

                override fun onStartCreate() {
                    //The gesture password is turned on, but the local area is cleared and needs to be reset.

                }

                override fun onCancel() {
                    //Forget password and use other methods

                }

                override fun onEventOccur(eventCode: Int) {
                    Log.d(TAG, "onEventOccur:$eventCode")

                }
            })
        }
        mGestureVerifyFragment?.setData(ConfigGestureVO.defaultConfig())
        safeAddFragment(mGestureVerifyFragment, R.id.fragment_container, "GestureVerifyFragment")
        mGestureVerifyFragment?.setGestureCodeData(GestureUnlock.getInstance().getGestureCodeSet(this))
    }


    private fun showModifyGestureLayout() {
        setTitleText(getString(R.string.str_gesture_old_pwd))
        if (mGestureVerifyFragment == null) {
            mGestureVerifyFragment = GestureVerifyFragment()
            mGestureVerifyFragment?.setGestureVerifyListener(object :
                GestureVerifyFragment.GestureVerifyListener {
                override fun onVerifyResult(result: ResultVerifyVO) {
                    if (result.isFinished) {
                        //success
                        Log.d(TAG, "onVerifyResult:success")
                        setTitleText(getString(R.string.str_set_new_gesture_pwd))
                        showCreateGestureLayout()
                    } else {
                        Log.d(TAG, "onVerifyResult:fail")
                    }

                }

                override fun closeLayout() {}

                override fun onStartCreate() {
                    //The gesture password is turned on, but the local area is cleared and needs to be reset.

                }

                override fun onCancel() {
                    //Forget password and use other methods

                }

                override fun onEventOccur(eventCode: Int) {
                    Log.d(TAG, "onEventOccur:$eventCode")

                }
            })
        }
        mGestureVerifyFragment?.setData(ConfigGestureVO.defaultConfig())
        safeAddFragment(mGestureVerifyFragment, R.id.fragment_container, "GestureVerifyFragment")
        mGestureVerifyFragment?.setGestureCodeData(GestureUnlock.getInstance().getGestureCodeSet(this))
    }

    private fun safeAddFragment(fragment: Fragment?, id: Int, tag: String) {

        if (fragment == null) {
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
        if (currentFragment == fragment)
            return
        if (!fragment.isAdded) {
            transaction.hide(currentFragment!!).add(containerLayoutId, fragment, tag).commit()
        } else {
            transaction.hide(currentFragment!!).show(fragment).commit()
        }
        currentFragment!!.setUserVisibleHint(false)
        currentFragment = fragment
        currentFragment!!.setUserVisibleHint(true)
    }


}
