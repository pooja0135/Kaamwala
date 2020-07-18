package com.project.kaamwaala.dialog.admin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.dashboard.DashboardActivity
import com.project.kaamwaala.activity.admin.AdminDashboardActivity
import com.project.kaamwaala.activity.login.LoginVM
import com.project.kaamwaala.activity.login.LoginVMF
import com.project.kaamwaala.model.login.UpdateLoginRequest
import com.project.kaamwaala.model.login.UpdateloginResponse
import com.project.kaamwaala.network.PrefsConstants
import kotlinx.android.synthetic.main.admin_mode_dialog.*
import kotlinx.android.synthetic.main.weekly_list.ivClose
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class AdminModeDialog() : DialogFragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: LoginVMF by instance<LoginVMF>()
    private var viewModel: LoginVM? = null
    private var field: String? = null
    private var oldValue: String? = null
    private var callback: Onclick? = null



    var user="S"
    override fun onStart() {
        super.onStart()
        val dialog = dialog

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {

        fun newInstance(): AdminModeDialog {
            val f =
                AdminModeDialog()
            val args = Bundle()

            f.arguments = args
            return f
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        viewModel = ViewModelProviders.of(this, factory).get(LoginVM::class.java)

        ivClose.setOnClickListener { dialog!!.dismiss() }

        if (Prefs.getString(PrefsConstants.UserType,"").equals("Admin"))
        {
            switchAdmin.isChecked=true
        }
        else
        {
            switchAdmin.isChecked=false
        }


        switchAdmin.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    progressbar.visibility=View.VISIBLE
                   performLogin(UpdateLoginRequest("Admin",Prefs.getString(PrefsConstants.UserId,"")),"1")
                } else {
                    progressbar.visibility=View.VISIBLE
                    performLogin(UpdateLoginRequest("Customer",Prefs.getString(PrefsConstants.UserId,"")),"0")
                }
            }
        })



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.admin_mode_dialog, container)
        return rootView
        }

    interface Onclick {
        fun success(message: String)
    }

    private fun onSuccess(message: String) {
        callback?.success(message)
        dismiss()
    }

    private fun onError(message: String) {
       // rootView.snackbar(message)
    }


    private fun performLogin(loginRequest: UpdateLoginRequest,type:String) {
        viewModel?.responseUpdateLogin = MutableLiveData()
        viewModel?.responseUpdateLogin?.observe(this, Observer {

            val response = (it as UpdateloginResponse)
            if (response.Status) {
               Log.v("dfhffhhffh",type)

                progressbar.visibility=View.GONE
                if (type.equals("1"))
                {
                    Prefs.putString(PrefsConstants.UserType, "Admin")
                    startActivity(Intent(context, AdminDashboardActivity::class.java))

                }
                else
                {
                    Prefs.putString(PrefsConstants.UserType,"Customer")
                    startActivity(Intent(context, DashboardActivity::class.java))
                }


            } else {


            }
        })

        viewModel?.updatelogin(loginRequest)
    }


}


