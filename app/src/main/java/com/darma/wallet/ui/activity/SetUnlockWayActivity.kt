package com.darma.wallet.ui.activity

import android.app.KeyguardManager
import android.content.Intent
import android.os.Build
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import ch.ielse.view.SwitchView
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.utils.GestureUnlock
import com.darma.wallet.utils.SettingManager
import com.darma.wallet.utils.dialog.FingerprintDialog
import kotlinx.android.synthetic.main.activity_set_unlock_way.*


class SetUnlockWayActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_set_unlock_way
    }

    override fun initData() {
    }

    override fun initView() {

        setTitleText(getString(R.string.str_pwd_setting))



        initFingerprintSwitchView()

        initGestureSwitchView()


    }

    private fun initGestureSwitchView() {
        sv_gesture.setOnClickListener {
            showGesture()
        }

        if (SettingManager.isGestureOpen(this)) {
            sv_gesture.toggleSwitch(true)
        } else {
            sv_gesture.toggleSwitch(false)
        }
    }

    private fun showGesture() {

        if (SettingManager.isGestureOpen(this)) {

            GestureUnlock.getInstance().init(this.applicationContext)
            GestureUnlock.getInstance().verifyGestureUnlock(this)

        } else {

            GestureUnlock.getInstance().init(this.applicationContext)
            GestureUnlock.getInstance().createGestureUnlock(this)
        }
    }

    private fun initFingerprintSwitchView() {
        sv_fingerprint.setOnStateChangedListener(object : SwitchView.OnStateChangedListener {
            override fun toggleToOn(view: SwitchView?) {
                if (supportFingerprint()) {


                    FingerprintDialog.createDialog(this@SetUnlockWayActivity)
                        .setOnResultListener(object : FingerprintDialog.OnResult {
                            override fun onDismiss() {
                                view?.toggleSwitch(false)
                            }

                            override fun onSuccess() {

                                view?.toggleSwitch(true)
                                SettingManager.openFingerprint(this@SetUnlockWayActivity)
                            }

                            override fun onError(error: String?) {
                                error?.let {

                                    showToast(it)
                                }
                            }

                        }).show()
                } else {

                    view?.toggleSwitch(false)
                }
            }

            override fun toggleToOff(view: SwitchView?) {
                if (supportFingerprint()) {
                    FingerprintDialog.createDialog(this@SetUnlockWayActivity)
                        .setOnResultListener(object : FingerprintDialog.OnResult {
                            override fun onDismiss() {
                                view?.toggleSwitch(true)
                            }

                            override fun onSuccess() {
                                view?.toggleSwitch(false)
                                SettingManager.closeFingerprint(this@SetUnlockWayActivity)
                            }

                            override fun onError(error: String?) {
                                error?.let {

                                    showToast(it)
                                }
                            }

                        }).show()
                } else {

                    view?.toggleSwitch(false)
                }
            }
        })

        if (SettingManager.isFingerprintOpen(this)) {
            sv_fingerprint.toggleSwitch(true)
        } else {
            sv_fingerprint.toggleSwitch(false)
        }
    }


    private fun supportFingerprint(): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            showToast(R.string.str_fingerprint_unlocking_is_not_supported)
            return false
        } else {
            try {
                val keyguardManager = getSystemService(KeyguardManager::class.java)
                val managerCompat = FingerprintManagerCompat.from(this)

                if (!managerCompat.isHardwareDetected) {
                    showToast(R.string.str_fingerprint_unlocking_is_not_supported)
                    return false
                } else if (keyguardManager == null || !keyguardManager.isKeyguardSecure) {
                    showToast(R.string.str_please_set_the_lock_screen_and_add_a_fingerprint_first)
                    return false
                } else if (!managerCompat.hasEnrolledFingerprints()) {
                    showToast(R.string.str_add_at_least_one_fingerprint_to_the_system)
                    return false
                }
            } catch (e: Exception) {

                showToast(""+e.message)
            }
        }
        return true
    }

    override fun loadData() {
    }

    override fun onResume() {
        super.onResume()
        initGestureSwitchView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            GestureActivity.RESULT_CODE_VERIFY_SUCCESS -> {

                SettingManager.closeGesture(this)
            }
            GestureActivity.RESULT_CODE_VERIFY_FAIL ->

                showToast(R.string.str_close_gesture_fail_toast)
        }
    }
}
