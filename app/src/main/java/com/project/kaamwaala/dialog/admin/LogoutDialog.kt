package com.project.kaamwaala.dialog.admin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.login.LoginActivity
import com.project.kaamwaala.network.PrefsConstants
import kotlinx.android.synthetic.main.logout_dialog.*
import kotlinx.android.synthetic.main.weekly_list.ivClose


class LogoutDialog() : DialogFragment()  {

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

        fun newInstance(): LogoutDialog {
            val f =
                LogoutDialog()
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

        ivClose.setOnClickListener { dialog!!.dismiss() }
        cardview.setOnClickListener {   Prefs.putBoolean(PrefsConstants.KEY_ISLOGGED_IN, false)
            Prefs.putString(PrefsConstants.UserName, "")
            Prefs.putString(PrefsConstants.UserId, "")
            Prefs.putString(PrefsConstants.UserImage,"")
            Prefs.putString(PrefsConstants.UserEmail, "")
            Prefs.putString(PrefsConstants.UserType, "")
            context!!.startActivity(Intent(context, LoginActivity::class.java))
            (context as Activity).finish()
        }


        Glide.with(context!!) //1
            .load(Prefs.getString(PrefsConstants.UserImage,""))
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(ivUser)

        tvName.setText(Prefs.getString(PrefsConstants.UserName,""))
        tvEmail.setText(Prefs.getString(PrefsConstants.UserEmail,""))

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.logout_dialog, container)
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


}