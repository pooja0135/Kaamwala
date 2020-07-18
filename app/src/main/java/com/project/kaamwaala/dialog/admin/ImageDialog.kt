package com.project.kaamwaala.dialog.admin

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.OnAddImageUrl
import com.project.kaamwaala.R
import com.project.kaamwaala.OnItemCreateCategory
import com.project.kaamwaala.OnItemDeleteImage
import com.project.kaamwaala.model.stall_category_product.ProductImagesItem
import kotlinx.android.synthetic.main.create_category_dialog.*
import kotlinx.android.synthetic.main.create_category_dialog.tvCancel
import kotlinx.android.synthetic.main.create_category_dialog.tvOk
import kotlinx.android.synthetic.main.create_image_dialog.*
import kotlinx.android.synthetic.main.inflate_admin_product_list.view.*
import kotlinx.android.synthetic.main.product_image_dialog.*
import kotlinx.android.synthetic.main.product_image_dialog.view.*


class ImageDialog() : DialogFragment()  {

    private var field: String? = null
    private var oldValue: String? = null
    private var callback: Onclick? = null
    lateinit var callback1: OnItemDeleteImage
    private var imageurl:String?=null
    private var id:String?=null

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


    fun setCallBack(callback: OnItemDeleteImage)
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

        try{
            imageurl  = arguments!!.getString("imageurl")
            id  = arguments!!.getString("id")
        }
        catch (e:Exception)
        {

        }
    }

    companion object {

        fun newInstance(imageurl:String,id:String): ImageDialog {
            val f =
                ImageDialog()
            val args = Bundle()
            args.putString("imageurl",imageurl)
            args.putString("id",id)
            f.arguments = args
            return f
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)


        Log.v("hfhfhffh",imageurl)

        Glide.with(context!!) //1
            .load(imageurl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(ivImage)

        ivClose.setOnClickListener {  dialog!!.dismiss() }


        tvDelete.setOnClickListener {
            dialog!!.dismiss()
            callback1.onClickDeleteImage(id.toString(),"",imageurl.toString())
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.product_image_dialog, container)



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