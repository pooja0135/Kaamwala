package com.project.kaamwaala.dialog.admin

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.project.kaamwaala.R
import com.project.kaamwaala.OnItemProductPrice

import kotlinx.android.synthetic.main.price_variant_dialog.*
import kotlinx.android.synthetic.main.price_variant_dialog.ivCheck
import kotlinx.android.synthetic.main.price_variant_dialog.ivClose


class PriceVariantDialog() : DialogFragment()  {

    private var field: String? = null
    private var oldValue: String? = null
    private var callback: Onclick? = null
    lateinit var callback1: OnItemProductPrice


    private var price:String?=null
    private var saleprice:String?=null
    private var describe:String?=null
    private var id:String?=null
    private var action:String?=null

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



    fun setCallBack(callback: OnItemProductPrice)
    {
        this.callback1=callback
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


        try{
            price  = arguments!!.getString("price")
            saleprice       = arguments!!.getString("saleprice")
            describe   = arguments!!.getString("describe")
            id  = arguments!!.getString("id")
            action  = arguments!!.getString("action")
        }
       catch (e:Exception)
       {

       }
    }

    companion object {

        fun newInstance(price: String, saleprice: String, describe: String, id: String,action: String): PriceVariantDialog {
            val f =
                PriceVariantDialog()
            val args = Bundle()
            args.putString("price", price)
            args.putString("saleprice", saleprice)
            args.putString("describe", describe)
            args.putString("id", id)
            args.putString("action", action)
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

        etPrice.setText(price)
        etSalePrice.setText(saleprice)
        etDescribe.setText(describe)





        ivCheck.setOnClickListener {

            if (etPrice.text.toString().isEmpty())
            {
                Toast.makeText(context, "Enter Price", Toast.LENGTH_SHORT).show()
            }
            else
            {
                dialog!!.dismiss()
                if (action.equals("Update"))
                {
                        callback1.onClickProductPrice(etPrice.text.toString(),etSalePrice.text.toString(),etDescribe.text.toString(),"Update",id!!)


                }
                else
                {
                    callback1.onClickProductPrice(etPrice.text.toString(),etSalePrice.text.toString(),etDescribe.text.toString(),"Insert","")

                }

            }
        }



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.price_variant_dialog, container)
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