package com.darma.wallet.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.darma.wallet.R
import com.darma.wallet.base.BaseActivity
import com.darma.wallet.db.NodeDB
import com.darma.wallet.db.WalletDataBase
import com.darma.wallet.utils.CheckUtils
import com.darma.wallet.utils.NodeUtils
import com.darma.wallet.utils.ThreadPoolUtils
import com.wallet.WalletManager
import com.wallet.model.WalletErrorException
import com.wallet.utils.StringUtils
import kotlinx.android.synthetic.main.activity_edit_node.*

class EditNodeActivity : BaseActivity() {



    var node: NodeDB? = null
    
    override fun layoutId(): Int {
        return R.layout.activity_edit_node
    }

    override fun initData() {
        intent.getSerializableExtra("data")?.let{

            node = it as NodeDB
        }
    }


    override fun initView() {


        if(node==null){

            setTitleText(getString(R.string.str_node_add))
        }else{

            setTitleText(getString(R.string.str_edit_node))
        }

        btn_commit.setOnClickListener {


            if (checkInput()) {

                showLoading()


                create()
            }
        }


        et_ip.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_ip.error = ""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_post.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_post.error = ""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_tag.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!StringUtils.isEmpty(s.toString())) {
                    et_tag.error = ""
                    btn_commit.setProgress(0);
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        node?.let{
            et_ip.editText?.setText(it.ip)
            et_post.editText?.setText(it.post)
            et_tag.editText?.setText(it.tag)
            et_name.editText?.setText(it.username)
            et_pwd.editText?.setText(it.userpwd)
        }
    }

    /**
     *
     * Loading
     *
     */
    private fun showLoading() {

        et_ip.isEnabled = false
        et_post.isEnabled = false
        et_name.isEnabled = false
        et_pwd.isEnabled = false
        et_tag.isEnabled = false
        btn_commit.setIndeterminateProgressMode(true); // turn on indeterminate progress
        btn_commit.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress


    }

    private fun createSuccess() {
        btn_commit.progress = 100

        btn_commit.postDelayed({

            onBackPressed()

        }, 500)
    }

    private fun createFail(error: String) {

        et_ip.isEnabled = true
        et_post.isEnabled = true
        et_name.isEnabled = true
        et_pwd.isEnabled = true
        et_tag.isEnabled = true
        btn_commit.progress = -1
        showToast(error)
    }

    private fun create() {


        var ip = et_ip.editText?.text.toString()
        var post = et_post.editText?.text.toString()

        var tag = et_tag.editText?.text.toString()
        var name = et_name.editText?.text.toString()
        var pwd = et_pwd.editText?.text.toString()

        ThreadPoolUtils.getInstance().execute {
            try {

                if (node == null) {
                    node = NodeDB()
                    node?.ip = ip
                    node?.post = post
                    node?.tag = tag
                    node?.username = name
                    node?.userpwd = pwd

                    node?.url = ip+":"+post
                    WalletDataBase.getInstance(this).nodesDao.insert(node)

                } else {

                    node?.ip = ip
                    node?.post = post
                    node?.tag = tag
                    node?.username = name
                    node?.userpwd = pwd

                    node?.url = ip+":"+post
                    WalletDataBase.getInstance(this).nodesDao.update(node)


                }


                if(WalletManager.getInstance().selectNode.id==NodeUtils.NODE_AUTO.id){
                    WalletManager.getInstance().saveSelectNode(NodeUtils.NODE_AUTO.toNode())
                }
                btn_commit.postDelayed({

                    createSuccess()
                }, 2000)
            } catch (e: WalletErrorException) {
                e.printStackTrace()
                btn_commit.postDelayed({

                    createFail(e.error.errMsg)
                }, 2000)
            } catch (e: Exception) {

                e.printStackTrace()

                btn_commit.postDelayed({

                    e.message?.let { createFail(it) }
                }, 2000)
            }
        }
    }


    private fun checkInput(): Boolean {

        var checkEmpty = false
        var ip = et_ip.editText?.text.toString()

        if (StringUtils.isEmpty(ip)) {
            et_ip.error = getString(R.string.str_please_input_ip)
            checkEmpty = true
        }
        var post = et_post.editText?.text.toString()

        if (StringUtils.isEmpty(post)) {
            et_post.error = getString(R.string.str_please_input_post)
            checkEmpty = true
        }


        var tag = et_tag.editText?.text.toString()
        if (StringUtils.isEmpty(tag)) {
            et_tag.error = getString(R.string.str_please_input_tag)
            checkEmpty = true
        }
        if (checkEmpty) {
            return false
        }
        if (!CheckUtils.isLegalPost(post)) {
            et_post.error = getString(R.string.str_initial_port)
            return false
        }

        return true
    }

    override fun loadData() {
    }

}
