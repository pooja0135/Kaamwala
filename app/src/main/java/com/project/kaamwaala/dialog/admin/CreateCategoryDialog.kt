package com.project.kaamwaala.dialog.admin

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.project.kaamwaala.R
import com.project.kaamwaala.OnItemCreateCategory
import kotlinx.android.synthetic.main.create_category_dialog.*
import kotlinx.android.synthetic.main.create_category_dialog.tvCancel
import kotlinx.android.synthetic.main.create_category_dialog.tvOk


class CreateCategoryDialog() : DialogFragment()  {

    private var field: String? = null
    private var oldValue: String? = null
    private var callback: Onclick? = null
    lateinit var callback1: OnItemCreateCategory

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


    fun setCallBack(callback: OnItemCreateCategory)
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
      //  field = arguments!!.getString("field")
    //    oldValue = arguments!!.getString("old_value")

    }

    companion object {

        fun newInstance(): CreateCategoryDialog {
            val f =
                CreateCategoryDialog()
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

        tvCancel.setOnClickListener { dialog!!.dismiss() }
        tvOk.setOnClickListener {

            if (etCategory.text.toString().isEmpty())
            {
                Toast.makeText(context, "Enter Category", Toast.LENGTH_SHORT).show()
            }
            else
            {
                dialog!!.dismiss()
                callback1.onClickCategory(etCategory.text.toString())
            }


        }



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.create_category_dialog, container)



            return rootView
        }







    fun setOnHeadlineSelectedListener(callback: Onclick) {
        this.callback = callback
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