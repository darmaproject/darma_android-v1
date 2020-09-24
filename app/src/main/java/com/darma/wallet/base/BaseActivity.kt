package com.darma.wallet.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.darma.wallet.MyApplication
import com.darma.wallet.R
import com.darma.wallet.bean.LanguageType
import com.darma.wallet.ui.activity.MainActivity
import com.darma.wallet.ui.activity.WelcomeActivity
import com.darma.wallet.utils.AppUtils
import com.darma.wallet.utils.LanguageUtil
import com.darma.wallet.utils.SettingManager
import com.darma.wallet.utils.ToastUtils
import com.darma.wallet.utils.dialog.LoadingDialog
import com.wallet.WalletManager
import org.simple.eventbus.EventBus
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.reflect.Method
import java.util.*


abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    BaseFragment.OnFragmentInteractionListener {

    private var recordTime = true
    private var checkUnlock = true

    var toolbar: Toolbar? = null
    override fun attachBaseContext(context: Context) {
        val language = MyApplication.context.getSelectLanguage()

        super.attachBaseContext(LanguageUtil.attachBaseContext(context, language))
    }

    fun getWalletManager():WalletManager{
        return WalletManager.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())

        initToolbar()
        initData()
        initView()
        initMenu()
//        loadData()
//        initListener()


        EventBus.getDefault().register(this)



        Log.d("luo", "onCreate  ---------  " + localClassName)
    }

    @SuppressLint("RestrictedApi")
    private fun initMenu() {
        var menu = toolbar?.getMenu();
        if (menu is MenuBuilder) {

            menu.setOptionalIconsVisible(true)
        }

        var moreIcon = ContextCompat.getDrawable(this, R.drawable.abc_ic_menu_overflow_material);
        if(moreIcon != null) {
            moreIcon.setColorFilter(ContextCompat.getColor(this, R.color.text_color_black), PorterDuff.Mode.SRC_ATOP)
            toolbar?.setOverflowIcon(moreIcon)
        }
    }

    private fun initToolbar() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setTitleText(title: String) {

        var tvTitle = findViewById<TextView>(R.id.tv_header_title)
        tvTitle?.setText(title)
    }

    fun setTitleText(titleResId: Int) {

        setTitleText(getString(titleResId))
    }


    abstract fun layoutId(): Int

    abstract fun initData()
    abstract fun initView()

    abstract fun loadData()

    fun isCheckUnlock(): Boolean {
        return checkUnlock
    }

    fun setCheckUnlock(checkUnlock: Boolean) {
        this.checkUnlock = checkUnlock
    }



    fun isRecordTime(): Boolean {
        return recordTime
    }

    fun setRecordTime(recordTime: Boolean) {
        this.recordTime = recordTime
    }
    override fun onResume() {
        super.onResume()
        loadData()
        if(isCheckUnlock()){

            SettingManager.checkUnlock(this)
        }
    }
    override fun onPause() {
        super.onPause()
        if (isRecordTime()) {

            SettingManager.recordLastTime(this)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    fun openKeyBord(mEditText: EditText, mContext: Context) {
        try {
            val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        } catch (e: Exception) {

        }
    }

    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        try {
            val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
        } catch (e: Exception) {

        }
    }

    fun showToast(str: String) {
        ToastUtils.showToast(this, str)
    }

    fun showToast(strId: Int) {
        showToast(getString(strId))
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

        Log.d("luo", "onDestroy  ---------  " + localClassName)
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
    }

    fun intentTo(cls: Class<*>) {

        intentTo(cls, Bundle())
    }

    fun intentTo(cls: Class<*>, bundle: Bundle) {
        var intent = Intent(this, cls)
        bundle?.let {

            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun reStartApp() {


        var intent = Intent(this, WelcomeActivity::class.java)



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    fun intentToMain() {

        var intent = Intent(this, MainActivity::class.java)


//        Log.d("luo","MainActivity isOpen  =- "+MainActivity.isOpen)
        if (MainActivity.isOpen) {

//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else {

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    fun intentToMain(bundle: Bundle) {

        var intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)

//        Log.d("luo","MainActivity isOpen  =- "+MainActivity.isOpen)
        if (MainActivity.isOpen) {

//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else {

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    fun intentTo(cls: Class<*>, @NonNull requestCode: Int) {
        startActivityForResult(Intent(this, cls), requestCode)
    }

    fun intentTo(cls: Class<*>, @NonNull requestCode: Int, bundle: Bundle) {

        var intent = Intent(this, cls)


        bundle?.let {

            intent.putExtras(bundle)
        }

        startActivityForResult(intent, requestCode)
    }

    fun fullScreen(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                val decorView = window.decorView
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                decorView.systemUiVisibility = option
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT

            } else {
                val window = activity.window
                val attributes = window.attributes
                val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                attributes.flags = attributes.flags or flagTranslucentStatus


                window.attributes = attributes
            }
        }
    }


    fun copyText(text:String){
        AppUtils.copyTextToClipboard(this,text)

        showToast( getString(R.string.str_copy_success))

    }

    var mLoadingDialog: LoadingDialog? = null
    fun showLoadingDialog() {
        if (isDestroyed || isFinishing) {
            return
        }
        dismissLoadingDialog()
        mLoadingDialog = LoadingDialog(this)
        mLoadingDialog?.show()
    }

    fun dismissLoadingDialog() {
        mLoadingDialog?.let {
            it.dismiss()
        }
    }

}


