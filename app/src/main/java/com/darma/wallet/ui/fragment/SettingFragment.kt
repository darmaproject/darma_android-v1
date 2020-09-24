package com.darma.wallet.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.darma.wallet.MyApplication
import com.darma.wallet.R
import com.darma.wallet.base.BaseFragment
import com.darma.wallet.bean.LanguageType
import com.darma.wallet.ui.activity.*
import com.darma.wallet.utils.*
import com.darma.wallet.utils.dialog.DeleteWalletDialog
import com.darma.wallet.utils.dialog.EnterPwdDialog
import com.wallet.WalletManager
import com.wallet.model.WalletErrorException
import kotlinx.android.synthetic.main.fragment_setting.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SettingFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initData() {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun initView() {


        item_rescan.setOnClickListener {

            intentTo(ReScanBlockActivity::class.java)
        }

        item_node.setOnClickListener {

            intentTo(NodesActivity::class.java)
        }
        item_change_pwd.setOnClickListener {

            intentTo(EditPwdActivity::class.java)
        }
        item_private_key.setOnClickListener {

            EnterPwdDialog.createDialog(context)
                .setInputPassWordListener { dialog, etPwd ->

                    if (WalletManager.getInstance().openWallet.pwd == etPwd.editText?.text.toString()) {

                        intentTo(PrivateKeyActivity::class.java)
                        dialog.dismiss()
                    } else {

                        etPwd.error = getString(R.string.str_pwd_error)
                    }

                }.show()

        }
        item_seed.setOnClickListener {

            EnterPwdDialog.createDialog(context)
                .setInputPassWordListener { dialog, etPwd ->

                    if (WalletManager.getInstance().openWallet.pwd == etPwd.editText?.text.toString()) {

                        var intent = Intent(context, MnemonicWordActivity::class.java)
                        intent.putExtra(MnemonicWordActivity.FROM, MnemonicWordActivity.FROM_SHOW_SEED)
                        startActivity(intent)
                        dialog.dismiss()
                    } else {

                        etPwd.error = getString(R.string.str_pwd_error)
                    }

                }.show()
        }
        item_language.setOnClickListener {

            intentTo(SelectLanguageActivity::class.java)
        }
        item_interval.setOnClickListener {

            intentTo(SynchronousIntervalActivity::class.java)
        }

        item_agreement.setOnClickListener {

            intentTo(AgreementActivity::class.java)
        }

        item_unlock.setOnClickListener {

            intentTo(SetUnlockWayActivity::class.java)
        }


        item_backup.setOnClickListener {

            EnterPwdDialog.createDialog(context)
                .setInputPassWordListener { dialog, etPwd ->

                    if (WalletManager.getInstance().openWallet.pwd == etPwd.editText?.text.toString()) {

                        backupWallet()
                        dialog.dismiss()
                    } else {

                        etPwd.error = getString(R.string.str_pwd_error)
                    }

                }.show()
        }
        tv_delete.setOnClickListener {
            DeleteWalletDialog.createDialog(context)
                .setOnConfirmListener {
                    //                    showLoadingDialog()
//                    FileUtils.deleteWallet(context,data)
//                    toolbar?.postDelayed({
//                        dismissLoadingDialog()
//                        loadData()
//                    },1000)

                    EnterPwdDialog.createDialog(context)
                        .setInputPassWordListener { dialog, etPwd ->

                            if (WalletManager.getInstance().checkOpenWalletPWd(etPwd.editText?.text.toString())) {
                                dialog.dismiss()
                                deleteAndExit()
                            } else {

                                etPwd.error = getString(R.string.str_pwd_error)
                            }

                        }.show()

                }.show()

        }

        tv_edition.setText(getString(R.string.str_edition) + "  " + AppUtils.getVersionName(context))



    }

    override fun onResume() {
        super.onResume()

        refreshInfos()
    }
    private fun refreshInfos() {

        setUnlockInfo()
        setLanguageInfo()
    }


    private fun setLanguageInfo() {
       var select= MyApplication.context.getSelectLanguage()

        for(language in LanguageType.values()){
            if(select==language.locale){
                item_language?.setInfoText(language.language)
            }
        }
        if(select==LanguageType.SYSTEM.locale){

            item_language?.setInfoText(getString(R.string.atr_auto))
        }
    }

    private fun setUnlockInfo() {
        if (SettingManager.isFingerprintOpen(context)||SettingManager.isGestureOpen(context)) {
            item_unlock.setInfoText(getString(R.string.str_enabled))
        }else{
            item_unlock.setInfoText(getString(R.string.str_not_use))
        }
    }

    private fun deleteAndExit() {
        showLoadingDialog()
        ThreadPoolUtils.getInstance().execute {

            var wallet = WalletManager.getInstance().openWallet
            WalletManager.getInstance().closeWallet()
            FileUtils.deleteWallet(context, wallet)
            tv_delete?.postDelayed({
                dismissLoadingDialog()
                reStartApp()

            }, 1000)
        }
    }

    private fun backupWallet() {


        showLoadingDialog()

        ThreadPoolUtils.getInstance().execute {


            try {
                var path = WalletManager.getInstance().backupWallet(
                    FileUtils.getWalletBackUpFilePath(context),
                    WalletManager.getInstance().openWallet.pwd
                )


                ShareUtils.shareFile(path, context)

                item_backup.post {
                    dismissLoadingDialog()
                }
            } catch (w: WalletErrorException) {
                w.printStackTrace()

                item_backup.post {
                    dismissLoadingDialog()
                    showToast(w.error.errMsg)
                }
            } catch (e: Exception) {
                e.printStackTrace()

                item_backup.post {
                    dismissLoadingDialog()
                    e.message?.let {
                        showToast(it)

                    }
                }
            }

        }


    }

    override fun lazyLoad() {

        var node=WalletManager.getInstance().selectNode
        if(node.id==NodeUtils.NODE_AUTO.id){

            item_node.setInfoText(getString(R.string.str_node_auto))
        }else{
            item_node.setInfoText(node.tag)
        }


    }
}
