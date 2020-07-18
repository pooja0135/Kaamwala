package com.project.kaamwaala.dialog.admin

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.Category.CityAdapter
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallVM
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallVMF
import com.project.kaamwaala.model.category.CategoryItem
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.OnItemSelect
import com.project.kaamwaala.OnItemSelectCity
import kotlinx.android.synthetic.main.select_category_dialog.ivClose
import kotlinx.android.synthetic.main.select_category_dialog.tvTag
import kotlinx.android.synthetic.main.select_city_dialog.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SelectCityDialog() : DialogFragment(), KodeinAware, OnItemSelect {
    override val kodein by kodein()
    private val factory: AdminStallVMF by instance<AdminStallVMF>()
    private lateinit var viewModel: AdminStallVM
    private var callback: Onclick? = null
    lateinit var callback1: OnItemSelectCity
    lateinit var callbackCategory: OnItemSelect
    var categorylist: MutableList<CategoryItem> = mutableListOf()
    var servicelist: MutableList<CategoryItem> = mutableListOf()
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


    fun setCallBack(callback: OnItemSelectCity)
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
        viewModel = ViewModelProviders.of(this, factory).get(AdminStallVM::class.java)
    }

    companion object {

        fun newInstance(): SelectCityDialog {
            val f =
                SelectCityDialog()
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


        tvTag.setText("Select City")

        callbackCategory=this
      GetCityApi()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.select_city_dialog, container)

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

    private fun GetCityApi() {
        viewModel?.responseCity = MutableLiveData()
        viewModel?.responseCity?.observe(this, Observer {

            val response = (it as CityResponse)

            if (response.Status) {


                rvCity.apply {
                    rvCity.layoutManager = LinearLayoutManager(context)
                    adapter = CityAdapter(response.CityItem,callbackCategory) }



            } else {


                //  activity?.toast("Something went wrong.")
            }
        })

        viewModel?.getCity()
    }


    override fun onClick(id: String, name: String) {
        dialog!!.dismiss()
        callback1.onClickCity(id,name)
    }

}