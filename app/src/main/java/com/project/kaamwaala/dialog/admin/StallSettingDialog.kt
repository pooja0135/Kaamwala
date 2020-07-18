package com.project.kaamwaala.dialog.admin

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.DialogFragment
import com.project.kaamwaala.R
import com.project.kaamwaala.OnItemClickSetting
import kotlinx.android.synthetic.main.create_category_dialog.tvCancel
import kotlinx.android.synthetic.main.create_category_dialog.tvOk
import kotlinx.android.synthetic.main.create_stall_setting_dialog.*


class StallSettingDialog() : DialogFragment()  {

    private var field: String? = null
    private var oldValue: String? = null
    private var callback: Onclick? = null
    private var line:String?=null
    private var type:String?=null
    private var category:String?=null
    private var texttitle:String?=null
    lateinit var callback1: OnItemClickSetting

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

    fun setCallBack(callback: OnItemClickSetting)
    {
        this.callback1=callback!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        field      = arguments!!.getString("field")

        line  = arguments!!.getString("line")
        type       = arguments!!.getString("type")
        category   = arguments!!.getString("category")
        texttitle  = arguments!!.getString("texttitle")


    //    oldValue = arguments!!.getString("old_value")

    }

    companion object {

        fun newInstance(  line: String,
                          type: String,
                         category: String,
                         texttitle: String): StallSettingDialog {
            val f =
                StallSettingDialog()
            val args = Bundle()
            args.putString("line", line)
            args.putString("type", type)
            args.putString("category", category)
            args.putString("texttitle", texttitle)
            f.arguments = args
            return f
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        tvCancel.setOnClickListener { dialog!!.dismiss() }
        tvOk.setOnClickListener { dialog!!.dismiss()
            if (line.equals("1")) {
                callback1.onClick(texttitle.toString(), etSingleLine.text.toString())
            }
            else
            {
                callback1.onClick(texttitle.toString(), etMultipleLine.text.toString())
            }
        }


        tvUpdate.setText("Update "+texttitle)

        if (line.equals("1"))
        {
            etSingleLine.setHint("Enter a value")
            etSingleLine.visibility=View.VISIBLE
            etMultipleLine.visibility=View.GONE

            if (type.equals("number"))
            {
                etSingleLine.inputType= InputType.TYPE_CLASS_NUMBER
            }
            else
            {
                etSingleLine.inputType= InputType.TYPE_CLASS_TEXT
            }
        }
        else
        {
            etMultipleLine.setHint("Enter a value")
            etSingleLine.visibility=View.GONE
            etMultipleLine.visibility=View.VISIBLE

            if (type.equals("number"))
            {
                etMultipleLine.inputType= InputType.TYPE_CLASS_NUMBER
            }
            else
            {
                etMultipleLine.inputType= InputType.TYPE_CLASS_TEXT
            }
        }



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.create_stall_setting_dialog, container)



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