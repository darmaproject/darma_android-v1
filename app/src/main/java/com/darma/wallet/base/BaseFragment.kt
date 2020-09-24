package com.darma.wallet.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darma.wallet.ui.activity.MainActivity
import com.darma.wallet.ui.activity.WelcomeActivity
import com.darma.wallet.utils.ToastUtils
import com.darma.wallet.utils.dialog.LoadingDialog
import org.simple.eventbus.EventBus
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


 abstract class BaseFragment: Fragment(),EasyPermissions.PermissionCallbacks{




    open var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        EventBus.getDefault().register(this)
        return inflater.inflate(getLayoutId(),null)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }


    override fun onResume() {
        super.onResume()
        lazyLoad()

    }
    /**
     * Loading layout
     */
    @LayoutRes
    abstract fun getLayoutId():Int


    /**
     *Initialization data
     */
    abstract fun initData()

    /**
     * Initialization  ViewI
     */
    abstract fun initView()

    /**
     * Lazy loading
     */
    abstract fun lazyLoad()



     fun reStartApp() {

         var intent = Intent(context, WelcomeActivity::class.java)



         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
         intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
         startActivity(intent)
     }

     fun intentToMain() {

         var intent = Intent(context, MainActivity::class.java)


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
     fun intentTo(cls: Class<*>) {
         startActivity(Intent(context, cls))
     }

     fun intentTo(cls: Class<*>, bundle: Bundle) {
         var intent = Intent(context, cls)
         bundle?.let {

             intent.putExtras(bundle)
         }
         startActivity(intent)
     }
     fun intentTo(cls: Class<*>, requestCode: Int) {
        startActivityForResult(Intent(context, cls), requestCode)
    }


     fun intentTo(cls: Class<*>, @NonNull requestCode: Int, bundle: Bundle) {

         var intent = Intent(context, cls)


         bundle?.let {

             intent.putExtras(bundle)
         }

         startActivityForResult(intent, requestCode)
     }
    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }


    /**
     * Override the onRequestPermissionsResult() method of the activity or fragment to request permission.
     *Call EasyPermissions.onRequestPermissionsResult() to implement the callback.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * Execute callback when permission is applied successfully
     *
     * @param requestCode
     * @param perms
     */
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    /**
     * Callback executed when permission application fails
     *
     * @param requestCode
     * @param perms
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    open interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    fun showToast(str: String) {
        ToastUtils.showToast(context, str)
    }

    var mLoadingDialog: LoadingDialog?=null
    fun showLoadingDialog() {
        if (isHidden || !isVisible) {
            return
        }
        dismissLoadingDialog()
        mLoadingDialog = LoadingDialog(context)
        mLoadingDialog?.show()
    }

    fun dismissLoadingDialog() {
        mLoadingDialog?.let{
            it.dismiss()
        }
    }
}
