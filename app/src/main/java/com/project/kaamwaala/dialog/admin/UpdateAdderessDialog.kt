package com.project.kaamwaala.dialog.admin

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.OnItemClickSetting
import com.project.kaamwaala.OnclickAddress
import com.project.kaamwaala.R
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallSettingFragment
import com.project.kaamwaala.model.LocalAddressModel
import com.project.kaamwaala.utils.C
import com.project.kaamwaala.utils.checkIfEmpty
import com.project.kaamwaala.utils.hideKeyboard
import com.project.kaamwaala.utils.setErrorWithFocus
import kotlinx.android.synthetic.main.add_address.*


import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class UpdateAddressDialog(var callback: OnclickAddress) :
    DialogFragment(), KodeinAware {
    override val kodein by kodein()

    var index: Int = -1
    lateinit var callback1: OnclickAddress
    //private var callback: Onclick? = null
    private var lstAddress: LocalAddressModel? = null

    fun setCallBack(callback: OnclickAddress)
    {
        this.callback1=callback!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (requireArguments().getSerializable("data") != null)
            lstAddress = requireArguments().getSerializable("data") as LocalAddressModel
        index = requireArguments().getInt("position")

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun getAddress() {
        try {
            val geocoder = Geocoder(context, Locale.US)
            val addressList = geocoder.getFromLocation(
                Prefs.getDouble(C.currentLat, 0.0),
                Prefs.getDouble(C.currentLon, 0.0),
                1
            )
            city.setText(addressList!![0].locality)
            area.setText(addressList!![0].subLocality)
            pincode.setText(addressList!![0].postalCode)
            state.setText(addressList!![0].adminArea)
            address.setText(
                addressList!![0].getAddressLine(0)
                    .split(",")[0] + "," + addressList!![0].getAddressLine(0).split(",")[1]
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("LOCATION ERROR: " + e)

            Log.v("uuufufhufhufhhf",e.toString())
        }
    }

    private fun initLayout(lstAddress: LocalAddressModel) {
        try {
            address.setText(lstAddress.houseBuildingName)
            landMark.setText(lstAddress.landmark)
            city.setText(lstAddress.city)
            pincode.setText(lstAddress.pin)
            state.setText(lstAddress.state)
            area.setText(lstAddress.areaColony)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_address, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()

        currentLocation.setOnClickListener {
            getAddress()
        }


        if (lstAddress != null) {
            initLayout(lstAddress!!)
            title.text = "Update Address"
        } else {
            title.text = "Add Address"
        }
    }

    private fun setupListener() {
        close.setOnClickListener {
            dismiss()
        }
        save.setOnClickListener {
            hideKeyboard()
            when {
                address.checkIfEmpty() -> address.setErrorWithFocus("Enter Address")
                city.checkIfEmpty() -> city.setErrorWithFocus("Enter your city")
                pincode.checkIfEmpty() -> pincode.setErrorWithFocus("Enter your pincode")
                state.checkIfEmpty() -> state.setErrorWithFocus("Enter your state")
                else -> {
                    if (lstAddress != null) {
                        val a = LocalAddressModel(
                            address.text.toString(),
                            area.text.toString(),
                            city.text.toString(),
                            state.text.toString(),
                            pincode.text.toString(),
                            landMark.text.toString()

                        )
                        callback1.onClickAddress(a)
                      //  (this as AdminStallSettingFragment).updateAddress(a, this)
                    } else {
                        val a = LocalAddressModel(
                            address.text.toString(),
                            area.text.toString(),
                            city.text.toString(),
                            state.text.toString(),
                            pincode.text.toString(),
                            landMark.text.toString()

                        )
                    //    (this as AdminStallSettingFragment).updateAddress(a,this)
                        dialog!!.dismiss()
                        callback1.onClickAddress(a)

                    }
                }
            }
        }
    }


    companion object {
        fun newInstance(
            lstAddress: LocalAddressModel?,
            callback: OnclickAddress,
            position: Int
        ): UpdateAddressDialog {
            val f =
                UpdateAddressDialog(callback)
            val args = Bundle()
            args.putSerializable("data", lstAddress)
            args.putInt("position", position)
            f.arguments = args
            return f
        }
    }


    private fun onSuccess() {

        dismiss()
    }

}