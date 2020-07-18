package com.project.kaamwaala.dialog.admin

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.project.kaamwaala.R
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallVM
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallVMF
import com.project.kaamwaala.OnItemClickCreateStall
import kotlinx.android.synthetic.main.create_stall_dialog.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class CreateStallDialog() : DialogFragment() , KodeinAware {
    override val kodein by kodein()
    private val factory: AdminStallVMF by instance<AdminStallVMF>()

    private lateinit var VM: AdminStallVM
    private var callback: Onclick? = null
    lateinit var callback1: OnItemClickCreateStall
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

    fun setCallBack(callback: OnItemClickCreateStall)
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

        fun newInstance(): CreateStallDialog {
            val f =
                CreateStallDialog()
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



        ivCheck.setOnClickListener {


            if (etStall.text.toString().isEmpty())
            {
                Toast.makeText(context, "Enter Store Name", Toast.LENGTH_SHORT).show()
            }
            else
            {
                dialog!!.dismiss()
                callback1.onClickCreateStall(etStall.text.toString())
            }

         //   startActivity(Intent(dialog!!.context,InventoryActivity::class.java))
        }

        VM = ViewModelProviders.of(this, factory).get(AdminStallVM::class.java)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.create_stall_dialog, container)

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