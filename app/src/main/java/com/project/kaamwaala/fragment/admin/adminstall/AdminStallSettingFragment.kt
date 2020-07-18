package com.project.kaamwaala.fragment.admin.adminstall


import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.*
import com.project.kaamwaala.adapter.admin.FreeShipping.FreeShippingListAdapter
import com.project.kaamwaala.adapter.admin.MinOrder.MinOrderListAdapter
import com.project.kaamwaala.adapter.admin.Shipping.ShippingListAdapter
import com.project.kaamwaala.adapter.admin.TimeSlot.TimeSlotListAdapter
import com.project.kaamwaala.dialog.admin.*
import com.project.kaamwaala.model.LocalAddressModel
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.StoreItem
import com.project.kaamwaala.model.category.CategoryItem
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.network.PrefsConstants
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_admin_category_detail.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.tvCategory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AdminStallSettingFragment : Fragment(), KodeinAware, OnItemClickSetting, OnItemDelete,
    OnItemDeleteFreeShipping, OnItemDeleteShipping, OnItemDeleteTime, OnItemSelectCategory,
    OnItemSelectCity ,OnclickAddress {
    override val kodein by kodein()
    private val factory: AdminStallVMF by instance<AdminStallVMF>()
    private lateinit var VM: AdminStallVM

    private val SELECT_PICTURE = 1
    var fileUri: Uri? = null
    var picUri: Uri? = null
    val MEDIA_TYPE_IMAGE = 1
    private val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100
    private val IMAGE_DIRECTORY_NAME = "Real Estate"
    val MEDIA_TYPE_VIDEO = 2

    var picturePath = ""
    var staff_image_url = ""
    var selectedImagePath: kotlin.String? = null
    var filename: kotlin.String? = ""
    var ext: kotlin.String? = ""
    var bitmap: Bitmap? = null



    var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    var PERMISSION_ALL = 4
    var serverResponseCode = 0


    var list: MutableList<String> = mutableListOf()
    var MinOrderlist: MutableList<MinOrderValueItem> = mutableListOf()
    var Shippinglist: MutableList<ShippingChargesItem> = mutableListOf()
    var FreeShippinglist: MutableList<FreeShippingAmountItem> = mutableListOf()
    var TimsSlotlist: MutableList<TimeSlotItem> = mutableListOf()
    var categorylist: MutableList<CategoryItem> = mutableListOf()
    var listOpening: MutableList<StoreItem> = mutableListOf()

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    lateinit var response: AdminStallDetailResponse

    lateinit var callback: OnItemClickSetting
    lateinit var callbackDelete: OnItemDelete
    lateinit var callbackFreeShipping: OnItemDeleteFreeShipping
    lateinit var callbackShipping: OnItemDeleteShipping
    lateinit var callbackTime: OnItemDeleteTime
    lateinit var callbackCategory: OnItemSelectCategory
    lateinit var callbackCity: OnItemSelectCity
    lateinit var callback2:OnclickAddress

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_setting, container, false)
        VM = ViewModelProviders.of(this, factory).get(AdminStallVM::class.java)

        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        callback = this
        callbackDelete = this
        callbackFreeShipping = this
        callbackShipping = this
        callbackTime = this
        callbackCategory = this
        callbackCity = this
        callback2 = this


        response = Paper.book().read("stalldetail")


        for (i in 0 until response.MinOrderValueItem.size) {
            MinOrderlist.add(response.MinOrderValueItem[i])
        }

        for (i in 0 until response.FreeShippingAmountItem.size) {
            FreeShippinglist.add(response.FreeShippingAmountItem[i])
        }

        for (i in 0 until response.ShippingChargesItem.size) {
            Shippinglist.add(response.ShippingChargesItem[i])
        }

        for (i in 0 until response.TimeSlotItem.size) {
            TimsSlotlist.add(response.TimeSlotItem[i])
        }


        rvMinimumOrder.apply {
            rvMinimumOrder.layoutManager = LinearLayoutManager(context)
            adapter = MinOrderListAdapter(MinOrderlist, callbackDelete)
        }

        rvFreeShipping.apply {
            rvFreeShipping.layoutManager = LinearLayoutManager(context)
            adapter = FreeShippingListAdapter(FreeShippinglist, callbackFreeShipping)
        }

        rvShippingCharge.apply {
            rvShippingCharge.layoutManager = LinearLayoutManager(context)
            adapter = ShippingListAdapter(Shippinglist, callbackShipping)
        }

        rvTimeSlot.apply {
            rvTimeSlot.layoutManager = LinearLayoutManager(context)
            adapter = TimeSlotListAdapter(TimsSlotlist, callbackTime)
        }


        //SetOnClickListener
        ivEditName.setOnClickListener(clickListener)
        ivEditAbout.setOnClickListener(clickListener)
        //  ivEditLocation.setOnClickListener(clickListener)
        ivEditVacation.setOnClickListener(clickListener)
        ivEditPhone.setOnClickListener(clickListener)
        ivEditDeliveryBoundary.setOnClickListener(clickListener)
        ivEditMinimumOrder.setOnClickListener(clickListener)
        ivEditFreeShipping.setOnClickListener(clickListener)
        ivEditShippingCharge.setOnClickListener(clickListener)
        ivEditGst.setOnClickListener(clickListener)
        ivEditStandardBoundary.setOnClickListener(clickListener)
        ivEditMinDelivery.setOnClickListener(clickListener)
        ivEditMinAdvanceDays.setOnClickListener(clickListener)
        ivEditAddress.setOnClickListener(clickListener)
        ivEditOrderlist.setOnClickListener(clickListener)
        ivEditTerms.setOnClickListener(clickListener)
        ivEditRefund.setOnClickListener(clickListener)
        ivEditWeekly.setOnClickListener(clickListener)

        tvAddNew.setOnClickListener(clickListener)
        tvAddNewFreeShipping.setOnClickListener(clickListener)
        tvAddNewShipping.setOnClickListener(clickListener)
        tvAddNewTimeSlot.setOnClickListener(clickListener)


        ivStallImage.setOnClickListener {
            if (checkAndRequestPermissions()) {
                val photoPickerIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    photoPickerIntent, SELECT_PICTURE
                )
            }
        }
        ivEditOpeningTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                tvOpeningTime.text = SimpleDateFormat("HH:mm a").format(cal.time)

                CreateStallApi(
                    CreateStallRequest(
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        0,
                        "",
                        ""
                        ,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        false,
                        0,
                        "OpeningTime",
                        0,
                        "",
                        0,
                        "",
                        "",
                        "",
                        tvOpeningTime.text.toString(),
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        response.Store_Id,
                        "",
                        Prefs.getString(PrefsConstants.UserId, ""),
                        "",
                        ""
                        ,
                        ""
                    )
                )

            }
            TimePickerDialog(
                activity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()


        }
        ivEditClosingTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                tvClosingTime.text = SimpleDateFormat("HH:mm a").format(cal.time)



                CreateStallApi(
                    CreateStallRequest(
                        "",
                        "",
                        "",
                        "",
                        0,
                        tvClosingTime.text.toString(),
                        0,
                        "",
                        ""
                        ,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        false,
                        0,
                        "ClosingTime",
                        0,
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        response.Store_Id,
                        "",
                        Prefs.getString(PrefsConstants.UserId, ""),
                        "",
                        ""
                        ,
                        ""
                    )
                )
            }
            TimePickerDialog(
                activity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()


        }
        tvCategory.setOnClickListener {openMenuCategory() }
        tvLocation.setOnClickListener {openMenuCity() }


        //SetValue
        setvalue()

        switchlistener()


    }


    fun switchlistener() {
        switchVacation.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            1,
                            "IsVacation",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "IsVacation",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchChat.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            true,
                            0,
                            "IsChat",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "IsChat",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchStandardDelivery.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "true",
                            "",
                            "",
                            false,
                            0,
                            "EnableStanderdDelivery",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "false",
                            "",
                            "",
                            false,
                            0,
                            "EnableStanderdDelivery",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchAdvanceBooking.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "true",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "AllowAdvanceBooking",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "false",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "AllowAdvanceBooking",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchEnablepickup.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "true",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "true",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnablePickup",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "false",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "false",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnablePickup",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchCOD.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "true",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "true"
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnableCOD",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "false",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "false"
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnableCOD",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchPaytm.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "true",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "true"
                            ,
                            "",
                            "",
                            "true",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnablePaymentOnDelevery",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "false",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "false"
                            ,
                            "",
                            "",
                            "false",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnablePaymentOnDelevery",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchRating.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "true",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "true"
                            ,
                            "",
                            "",
                            "true",
                            "",
                            "true",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnableRating",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "false",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "false"
                            ,
                            "",
                            "",
                            "false",
                            "",
                            "false",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnableRating",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

        switchOrder.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "true",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "true"
                            ,
                            "",
                            "true",
                            "true",
                            "",
                            "true",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnableOrderByList",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                } else {
                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "false",
                            "",
                            0,
                            "",
                            0,
                            "",
                            "false"
                            ,
                            "",
                            "false",
                            "false",
                            "",
                            "false",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "EnableOrderByList",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )
                }
            }
        })

    }


    fun setvalue() {



        Glide.with(this) //1
            .asBitmap()
            .load(response.StoreImageUrl)
          //  .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    ivStallImage.setImageBitmap(resource)

                }
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)


                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })



        if (response.StoreName.isEmpty()) {
           // tvName.visibility = View.GONE
        } else {
            tvName.visibility = View.VISIBLE
            tvName.text = response.StoreName
        }




        if (response.Location.equals("null")||response.Location.equals(null)||response.Location.isEmpty()) {
            tvLocation.text = "Select City"
        } else {

            tvLocation.text = response.Location
        }



        if (response.CategoryName.isEmpty()) {
            tvCategory.text = "No Tag"
        } else {
            tvName.visibility = View.VISIBLE
            tvCategory.text = response.CategoryName
        }


        if (response.About.isEmpty()) {
           // tvAbout.visibility = View.GONE
        } else {
            tvAbout.visibility = View.VISIBLE
            tvAbout.text = response.About
        }


        if (response.VacationMessage.isEmpty()) {
         //   tvVacation.visibility = View.GONE
        } else {
            tvVacation.visibility = View.VISIBLE
            tvVacation.text = response.VacationMessage

        }

        if (response.PhoneNumber.isEmpty()) {
           // tvPhone.visibility = View.GONE
        } else {
            tvPhone.visibility = View.VISIBLE
            tvPhone.text = response.PhoneNumber

        }

        if (response.OpeningTime.isEmpty()) {
           // tvOpeningTime.visibility = View.GONE
        } else {
            tvOpeningTime.visibility = View.VISIBLE
            tvOpeningTime.text = response.OpeningTime

        }

        if (response.ClosingTime.isEmpty()) {
         //   tvClosingTime.visibility = View.GONE
        } else {
            tvClosingTime.visibility = View.VISIBLE
            tvClosingTime.text = response.ClosingTime

        }

        if (response.DeleveryBoundry.isEmpty()) {
          //  tvDeliveryBoundary.visibility = View.GONE
        } else {
            tvDeliveryBoundary.visibility = View.VISIBLE
            tvDeliveryBoundary.text = response.DeleveryBoundry

        }

        if (response.MinOrderValue.isEmpty()) {
           // tvMinOrderValue.visibility = View.GONE
        } else {
            tvMinOrderValue.visibility = View.VISIBLE
            tvMinOrderValue.text = response.MinOrderValue

        }

        if (response.FreeShopingAmt.isEmpty()) {
          //  tvFreeShipping.visibility = View.GONE
        } else {
            tvFreeShipping.visibility = View.VISIBLE
            tvFreeShipping.text = response.FreeShopingAmt

        }

        if (response.ShopingCharges.isEmpty()) {
          //  tvFreeShipping.visibility = View.GONE
        } else {
            tvShippingCharge.visibility = View.VISIBLE
            tvShippingCharge.text = response.ShopingCharges
        }

        if (response.GSTRate.isEmpty()) {
         //   tvGSTRate.visibility = View.GONE
        } else {
            tvGSTRate.visibility = View.VISIBLE
            tvGSTRate.text = response.GSTRate
        }

        if (response.StanderdDeliveryBoundry.isEmpty()) {
          //  tvStandardDeliveryBoundary.visibility = View.GONE
        } else {
            tvStandardDeliveryBoundary.visibility = View.VISIBLE
            tvStandardDeliveryBoundary.setText(response.StanderdDeliveryBoundry)

        }

        if (response.MinDeleveryTime.isEmpty()) {
          //  tvMinimumDeliveryTime.visibility = View.GONE
        } else {
            tvMinimumDeliveryTime.visibility = View.VISIBLE
            tvMinimumDeliveryTime.setText(response.MinDeleveryTime)

        }

        if (response.MinAdvanceBokingDays.isEmpty()) {
          //  tvMinAdvanceDay.visibility = View.GONE
        } else {
            tvMinAdvanceDay.visibility = View.VISIBLE
            tvMinAdvanceDay.setText(response.MinAdvanceBokingDays)

        }

        if (response.Address.isEmpty()) {
          //  tvAddress.visibility = View.GONE
        } else {
            tvAddress.visibility = View.VISIBLE
            tvAddress.setText(response.Address)

        }

        if (response.ListName.isEmpty()) {
         //   tvOrderList.visibility = View.GONE
        } else {
            tvOrderList.visibility = View.VISIBLE
            tvOrderList.setText(response.ListName)
        }

        if (response.TermsAndCondition.isEmpty()) {
         //   tvTerms.visibility = View.GONE
        } else {
            tvTerms.visibility = View.VISIBLE
            tvTerms.setText(response.TermsAndCondition)
        }
        if (response.RefundPolicy.isEmpty()) {
         //   tvRefund.visibility = View.GONE
        } else {
            tvRefund.visibility = View.VISIBLE
            tvRefund.setText(response.RefundPolicy)
        }

        if (response.WeeklyOff.isEmpty()) {
         //   tvWeeklyOff.visibility = View.GONE
        } else {
            tvWeeklyOff.visibility = View.VISIBLE
            tvWeeklyOff.setText(response.RefundPolicy)
        }



        if (response.IsVacation.equals("False") || response.IsVacation.isEmpty()) {
            switchVacation.isChecked = false
        } else {
            switchVacation.isChecked = true
        }

        if (response.IsChat.equals("False") || response.IsChat.isEmpty()) {
            switchChat.isChecked = false
        } else {
            switchChat.isChecked = true
        }

        if (response.EnableStanderdDelivery.equals("False") || response.EnableStanderdDelivery.isEmpty()) {
            switchStandardDelivery.isChecked = false
        } else {
            switchStandardDelivery.isChecked = true
        }

        if (response.AllowAdvanceBooking.equals("False") || response.AllowAdvanceBooking.isEmpty()) {
            switchAdvanceBooking.isChecked = false
        } else {
            switchAdvanceBooking.isChecked = true
        }

        if (response.EnablePickup.equals("False") || response.EnablePickup.isEmpty()) {
            switchEnablepickup.isChecked = false
        } else {
            switchEnablepickup.isChecked = true
        }

        if (response.EnableCOD.equals("False") || response.EnableCOD.isEmpty()) {
            switchCOD.isChecked = false
        } else {
            switchCOD.isChecked = true
        }

        if (response.EnablePaymentOnDelevery.equals("False") || response.EnablePaymentOnDelevery.isEmpty()) {
            switchPaytm.isChecked = false
        } else {
            switchPaytm.isChecked = true
        }



        if (response.EnableRating.equals("False") || response.EnableRating.isEmpty()) {
            switchRating.isChecked = false
        } else {
            switchRating.isChecked = true
        }

        if (response.EnableOrderByList.equals("False") || response.EnableOrderByList.isEmpty()) {
            switchOrder.isChecked = false
        } else {
            switchOrder.isChecked = true
        }


    }


    private fun openMenu(line: String, type: String, category: String, texttitle: String) {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment = StallSettingDialog.newInstance(
            line, type, category, texttitle

        )
        newFragment.show(ft, "update")
        newFragment.setCallBack(callback)
    }


    private fun openMenu1(line: String, type: String, category: String, texttitle: String) {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment = WeeklyListDialog.newInstance(
            line, type, category, texttitle

        )
        newFragment.show(ft, "update")
        newFragment.setCallBack(callback)
    }

    private fun openMenuCategory() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment = SelectCategoryDialog.newInstance()
        newFragment.show(ft, "update")
        newFragment.setCallBack(callbackCategory)
    }

    private fun openMenuCity() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment = SelectCityDialog.newInstance()
        newFragment.show(ft, "update")
        newFragment.setCallBack(callbackCity)
    }


    val clickListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.ivEditName -> openMenu("1", "text", "", "NAME")
            R.id.ivEditAbout -> openMenu("2", "text", "", "ABOUT")
            // R.id.ivEditLocation -> openMenu("2", "text", "", "LOCATION")
            R.id.ivEditVacation -> openMenu("2", "text", "", "ON VACATION MESSAGE")
            R.id.ivEditPhone -> openMenu("1", "number", "", "PHONE_NUMBER")
            R.id.ivEditDeliveryBoundary -> openMenu(
               "1",
                "number",
                "",
                "DELIVERY BOUNDARY(Meters)"
            )
            R.id.ivEditMinimumOrder -> openMenu("1", "number", "", "MIN ORDER VALUE")
            R.id.ivEditFreeShipping -> openMenu(
                "1",
                "number",
                "",
                "FREE SHIPPING AMOUNT"
            )
            R.id.ivEditShippingCharge -> openMenu(
               "1",
                "number",
                "",
                "SHIPPING CHARGES"
            )
            R.id.ivEditGst -> openMenu("1", "number", "", "GST RATE(%)")
            R.id.ivEditStandardBoundary -> openMenu(
             "1",
                "number",
                "",
                "STANDARD DELIVERY BOUNDARY(Meters)"
            )
            R.id.ivEditMinDelivery -> openMenu(
               "1",
                "text",
                "",
                "MIN DELIVERY TIME(Default is 2 Hours)"
            )
            R.id.ivEditMinAdvanceDays -> openMenu(
               "1",
                "number",
                "",
                "MIN ADVANCE DAYS"
            )
            R.id.ivEditAddress -> /*openMenu("2", "text", "", "ADDRESS")*/   updateAddressDialog()
            R.id.ivEditOrderlist -> openMenu("2", "text", "", "List")
            R.id.ivEditTerms -> openMenu("2", "text", "", "TERMS & CONDITIONS")
            R.id.ivEditRefund -> openMenu("2", "text", "", "REFUND POLICY")
            R.id.ivEditWeekly -> openMenu1("2", "text", "", "Weekly")

            R.id.tvAddNew -> openMenu("1", "text", "", "MIN ORDER VALUE(By Meters)")
            R.id.tvAddNewFreeShipping -> openMenu(
               "1",
                "text",
                "",
                "FREE SHIPPING AMOUNT(By Meters)"
            )
            R.id.tvAddNewShipping -> openMenu(
             "1",
                "text",
                "",
                "SHIPPING CHARGES(By Meters)"
            )
            R.id.tvAddNewTimeSlot -> openMenu("1", "text", "", "TIME SLOT")


        }
    }


    //==========================Check permission ========================================//

    private fun checkAndRequestPermissions(): Boolean {
        val camerapermission =
            activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        val writepermission = activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        val listPermissionsNeeded = ArrayList<String>()

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            }
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED

                // Fill with actual results from user
                if (grantResults.size > 0) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    // Check for both permissions
                    if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    ) {


                        //else any one or both the permissions are not granted
                    } else {

                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (activity?.let {
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                    it,
                                    Manifest.permission.CAMERA
                                )
                            }!!
                            || activity?.let {
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                    it, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                            }!!
                        ) {
                            showDialogOK("Service Permissions are required for this app",
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            // proceed with logic by disabling the related features or quit the app.
                                            activity?.finish()
                                    }
                                })
                        } else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }//permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                    }
                }
            }
        }


    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        activity?.let {
            AlertDialog.Builder(it)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
        }
    }

    private fun explain(msg: String) {
        val dialog = activity?.let { AlertDialog.Builder(it) }
        dialog?.setMessage(msg)?.setPositiveButton("Yes") { paramDialogInterface, paramInt ->
            //  permissionsclass.requestPermission(type,code);
            startActivity(
                Intent(
                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:com.example.parsaniahardik.kotlin_marshmallowpermission")
                )
            )
        }?.setNegativeButton("Cancel") { paramDialogInterface, paramInt -> activity?.finish() }
        dialog?.show()
    }

    //============================Code for Camera and Gallary===================================//
    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri =
            getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
        val photoURI = FileProvider.getUriForFile(
            activity!!,
            BuildConfig.APPLICATION_ID.toString() + ".provider",
            getOutputMediaFile(MEDIA_TYPE_IMAGE)!!
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        // start the image capture Intent
        startActivityForResult(
            intent,
            CAMERA_CAPTURE_IMAGE_REQUEST_CODE
        )
    }

    fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    private fun getOutputMediaFile(type: Int): File? {

        // External sdcard location
        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            IMAGE_DIRECTORY_NAME
        )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(
                    IMAGE_DIRECTORY_NAME,
                    "Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME + " directory"
                )
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val mediaFile: File
        mediaFile =
            if (type == MEDIA_TYPE_IMAGE) {
                File(
                    mediaStorageDir.path + File.separator
                            + "IMG_" + timeStamp + ".jpg"
                )
            } else if (type == MEDIA_TYPE_VIDEO) {
                File(
                    mediaStorageDir.path + File.separator
                            + "VID_" + timeStamp + ".mp4"
                )
            } else {
                return null
            }
        return mediaFile
    }

    fun getImageOrientation(imagePath: String?): Int {
        var rotate = 0
        try {
            val imageFile = File(imagePath)
            val exif = ExifInterface(
                imageFile.absolutePath
            )
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotate
    }

    //method to convert string into base64
    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }

    fun getFileType(path: String): String {
        var fileType: String? = null
        fileType = path.substring(path.indexOf('.', path.lastIndexOf('/')) + 1).toLowerCase()
        return fileType
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                picturePath = fileUri!!.path.toString()
                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1)
                Log.d("filename_camera", filename)
                val selectedImagePath = picturePath

                //    selectedImagePath =SiliCompressor.with(getActivity()).compress(picturePath,  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));;
                val uri = Uri.parse(picturePath)
                ext = "jpg"
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(selectedImagePath, options)
                val REQUIRED_SIZE = 500
                var scale = 1
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) scale *= 2
                options.inSampleSize = scale
                options.inJustDecodeBounds = false
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options)
             //   ivStallImage.setImageBitmap(bitmap);

                Glide.with(this) //1
                    .asBitmap()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .skipMemoryCache(true) //2
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            ivStallImage.setImageBitmap(bitmap)

                        }
                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)


                        }
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })




                val matrix = Matrix()
                matrix.postRotate(getImageOrientation(picturePath).toFloat())
                val rotatedBitmap = Bitmap.createBitmap(
                    bitmap!!,
                    0,
                    0,
                    bitmap!!.getWidth(),
                    bitmap!!.getHeight(),
                    matrix,
                    true
                )
                val bao = ByteArrayOutputStream()
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
                val ba = bao.toByteArray()
                staff_image_url = getEncoded64ImageStringFromBitmap(rotatedBitmap)

            }
        } else if (requestCode == SELECT_PICTURE) {
            if (data != null) {
                val contentURI = data.data
                //get the Uri for the captured image
                picUri = data.data
                val filePathColumn =
                    arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity!!.contentResolver
                    .query(contentURI!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                picturePath = cursor.getString(columnIndex)
                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1)
                selectedImagePath = picturePath
                //   selectedImagePath = SiliCompressor.with(getActivity()).compress(picturePath,  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/ApnaGullak/images"));;
                Log.d("path", picturePath)
                println("Image Path : $picturePath")
                cursor.close()
                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1)
                ext = getFileType(picturePath)
                val selectedImagePath = picturePath
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(selectedImagePath, options)
                val REQUIRED_SIZE = 500
                var scale = 1
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) scale *= 2
                options.inSampleSize = scale
                options.inJustDecodeBounds = false
                bitmap =
                    BitmapFactory.decodeFile(selectedImagePath, options)


               ivStallImage.setImageBitmap(bitmap);






                val matrix = Matrix()
                matrix.postRotate(getImageOrientation(picturePath).toFloat())
                try {
                    val rotatedBitmap = Bitmap.createBitmap(
                        bitmap!!,
                        0,
                        0,
                        bitmap!!.getWidth(),
                        bitmap!!.getHeight(),
                        matrix,
                        true
                    )
                    val bao = ByteArrayOutputStream()
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)



                    staff_image_url = getEncoded64ImageStringFromBitmap(rotatedBitmap)

                    CreateStallApi(
                        CreateStallRequest(
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0,
                            "",
                            ""
                            ,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            0,
                            "StoreImageUrl",
                            0,
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            staff_image_url,
                            "",
                            response.Store_Id,
                            "",
                            Prefs.getString(PrefsConstants.UserId, ""),
                            "",
                            ""
                            ,
                            ""
                        )
                    )

                    val ba = bao.toByteArray()
                } catch (e: Exception) {
                    Log.e("Exception", e.toString())
                }
            } else {

                // Snackbar.make(getActivity(), "Please enter Username.", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(activity, "Unable to Select the Image.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    //==========================================API===================================================//
    private fun CreateStallApi(createStallRequest: CreateStallRequest) {
        VM?.responseStall = MutableLiveData()
        VM?.responseStall?.observe(this, Observer {

            val response = (it as CreateStallResponse)
            if (response.Status) {
                //startActivity(Intent(dialog!!.context,InventoryActivity::class.java))
            } else {


            }

        })
        VM.createstall(createStallRequest)
    }

    private fun MinOrderApi(minOrderRequest: MinOrderRequest) {
        VM?.responseMinOrder = MutableLiveData()
        VM?.responseMinOrder?.observe(this, Observer {

            val response = (it as MinOrderResponse)
            if (response.Status) {

                MinOrderlist[MinOrderlist.size - 1].Code = response.Id
            } else {


            }

        })
        VM.minorder(minOrderRequest)
    }

    private fun FreeShippingApi(freeShippingRequest: FreeShippingRequest) {
        VM?.responseFreeShipping = MutableLiveData()
        VM?.responseFreeShipping?.observe(this, Observer {

            val response = (it as FreeShippingResponse)
            if (response.Status) {
                FreeShippinglist[FreeShippinglist.size - 1].Code = response.Id
            } else {


            }

        })
        VM.freeshipping(freeShippingRequest)
    }

    private fun ShippingApi(shippingChargesRequest: ShippingChargesRequest) {
        VM?.responseShipping = MutableLiveData()
        VM?.responseShipping?.observe(this, Observer {

            val response = (it as ShippingChargesResponse)
            if (response.Status) {
                Shippinglist[Shippinglist.size - 1].Code = response.Id
            } else {


            }

        })
        VM.shippingvalue(shippingChargesRequest)
    }

    private fun TimeSlotApi(timeSlotRequest: TimeSlotRequest) {
        VM?.responseTimeSlot = MutableLiveData()
        VM?.responseTimeSlot?.observe(this, Observer {

            val response = (it as TimeSlotResponse)
            if (response.Status) {
                TimsSlotlist[TimsSlotlist.size - 1].Code = response.Id
            } else {


            }

        })
        VM.timeslotvalue(timeSlotRequest)
    }



    //========================SetOnCLickListener=============================================//
    override fun onClick(settingtype: String, value: String) {

        Log.v("hfhhfhfhhf", settingtype)
        Log.v("hfhhfhfhhf", value)

        if (settingtype == "NAME") {
            tvName.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "StoreName",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    value,
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "ABOUT") {
            tvAbout.text = value
            CreateStallApi(
                CreateStallRequest(
                    value,
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "About",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "ON VACATION MESSAGE") {
            tvVacation.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "VacationMessage",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    value,
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "PHONE_NUMBER") {
            tvPhone.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "PhoneNumber",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    value,
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }


        if (settingtype == "DELIVERY BOUNDARY(Meters)") {
            tvDeliveryBoundary.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    value,
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "DeleveryBoundry",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "MIN ORDER VALUE") {
            tvMinOrderValue.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "MinOrderValue",
                    0,
                    "",
                    0,
                    "",
                    "",
                    value,
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "FREE SHIPPING AMOUNT") {
            tvFreeShipping.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    value,
                    "",
                    false,
                    0,
                    "FreeShopingAmt",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "SHIPPING CHARGES") {
            tvShippingCharge.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "ShopingCharges",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    value,
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }


        if (settingtype == "GST RATE(%)") {
            tvGSTRate.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    value,
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    value,
                    false,
                    0,
                    "GSTRate",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "STANDARD DELIVERY BOUNDARY(Meters)") {
            tvStandardDeliveryBoundary.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    value,
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "StanderdDeliveryBoundry",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    value,
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "MIN DELIVERY TIME(Default is 2 Hours)") {
            tvMinimumDeliveryTime.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    value,
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "MinDeleveryTime",
                    0,
                    "",
                    0,
                    "",
                    value,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "MIN ADVANCE DAYS") {
            tvMinAdvanceDay.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "MinAdvanceBokingdays",
                    0,
                    "",
                    0,
                    value,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "ADDRESS") {
            tvAddress.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    value,
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "Address",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "List") {
            tvOrderList.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    value,
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "ListName",
                    0,
                    value,
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "TERMS & CONDITIONS") {
            tvTerms.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "TermsAndCondition",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    value,
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "REFUND POLICY") {
            tvRefund.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "RefundPolicy",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    value,
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    ""
                    ,
                    ""
                )
            )
        }

        if (settingtype == "Weekly") {
            tvWeeklyOff.text = value
            CreateStallApi(
                CreateStallRequest(
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    "",
                    ""
                    ,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false,
                    0,
                    "WeeklyOff",
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    value,
                    "",
                    "",
                    0,
                    "",
                    "",
                    response.Store_Id,
                    "",
                    Prefs.getString(PrefsConstants.UserId, ""),
                    "",
                    value
                    ,
                    ""
                )
            )
        }


//


        if (settingtype == "MIN ORDER VALUE(By Meters)") {
            MinOrderlist.add(MinOrderValueItem("", "", value))
            rvMinimumOrder.apply {
                rvMinimumOrder.layoutManager = LinearLayoutManager(context)
                adapter = MinOrderListAdapter(MinOrderlist, callbackDelete)
            }

            MinOrderApi(MinOrderRequest("Insert", "", response.Store_Id, value))
        }

        if (settingtype == "FREE SHIPPING AMOUNT(By Meters)") {
            FreeShippinglist.add(FreeShippingAmountItem("", "", value))
            rvFreeShipping.apply {
                rvFreeShipping.layoutManager = LinearLayoutManager(context)
                adapter = FreeShippingListAdapter(FreeShippinglist, callbackFreeShipping)
            }
            FreeShippingApi(FreeShippingRequest("Insert", "", response.Store_Id, value))
        }


        if (settingtype == "SHIPPING CHARGES(By Meters)") {
            Shippinglist.add(ShippingChargesItem("", "", value))
            rvShippingCharge.apply {
                rvShippingCharge.layoutManager = LinearLayoutManager(context)
                adapter = ShippingListAdapter(Shippinglist, callbackShipping)
            }
            ShippingApi(ShippingChargesRequest("Insert", "", response.Store_Id, value))
        }

        if (settingtype == "TIME SLOT") {
            TimsSlotlist.add(TimeSlotItem("", "", value))
            rvTimeSlot.apply {
                rvTimeSlot.layoutManager = LinearLayoutManager(context)
                adapter = TimeSlotListAdapter(TimsSlotlist, callbackTime)
            }
            TimeSlotApi(TimeSlotRequest("Insert", "", response.Store_Id, value))
        }
    }

    override fun onClickDelete(id: String, position: Int) {
        MinOrderlist.removeAt(position)
        MinOrderApi(MinOrderRequest("Delete", id, response.Store_Id, ""))
        rvMinimumOrder.apply {
            rvMinimumOrder.layoutManager = LinearLayoutManager(context)
            adapter = MinOrderListAdapter(MinOrderlist, callbackDelete)
        }
    }

    override fun onClickFreeShipping(id: String, position: Int) {
        FreeShippingApi(FreeShippingRequest("Delete", id, response.Store_Id, ""))
        FreeShippinglist.removeAt(position)
        rvFreeShipping.apply {
            rvFreeShipping.layoutManager = LinearLayoutManager(context)
            adapter = FreeShippingListAdapter(FreeShippinglist, callbackFreeShipping)
        }
    }

    override fun onClickShipping(id: String, position: Int) {
        ShippingApi(ShippingChargesRequest("Delete", id, response.Store_Id, ""))
        Shippinglist.removeAt(position)
        rvShippingCharge.apply {
            rvShippingCharge.layoutManager = LinearLayoutManager(context)
            adapter = ShippingListAdapter(Shippinglist, callbackShipping)
        }
    }

    override fun onClickTime(id: String, position: Int) {
        TimeSlotApi(TimeSlotRequest("Delete", id, response.Store_Id, ""))
        TimsSlotlist.removeAt(position)
        rvTimeSlot.apply {
            rvTimeSlot.layoutManager = LinearLayoutManager(context)
            adapter = TimeSlotListAdapter(TimsSlotlist, callbackTime)
        }

    }

    override fun onClickCategory(id: String, name: String) {
        tvCategory.text = name

        CreateStallApi(
            CreateStallRequest(
                "",
                "",
                "",
                id,
                0,
                "",
                0,
                "",
                ""
                ,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
                0,
                "CategoryId",
                0,
                "",
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                response.Store_Id,
                "",
                Prefs.getString(PrefsConstants.UserId, ""),
                "",
                ""
                ,
                ""
            )
        )
    }

    override fun onClickCity(id: String, name: String) {
        tvLocation.text = name

        CreateStallApi(
            CreateStallRequest(
                "",
                "",
                "",
                "",
                id.toInt(),
                "",
                0,
                "",
                ""
                ,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
                0,
                "CityId",
                0,
                "",
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                response.Store_Id,
                "",
                Prefs.getString(PrefsConstants.UserId, ""),
                "",
                ""
                ,
                ""
            )
        )
    }



    fun addAddress(localAddressModel: LocalAddressModel, updateAddressDialog: UpdateAddressDialog) {


    }





    private fun updateAddressDialog() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment = UpdateAddressDialog.newInstance(null, callback2!!, -1)
        newFragment.show(ft, "update address")
        newFragment.setCallBack(callback2)
    }

    override fun onClickAddress(localAddressModel: LocalAddressModel) {
        tvAddress.text = localAddressModel.houseBuildingName+" "+localAddressModel.landmark+" "+localAddressModel.areaColony+" "+localAddressModel.city+" "+localAddressModel.state+" "+localAddressModel.pin
        CreateStallApi(
            CreateStallRequest(
                "",
                tvAddress.text.toString(),
                "",
                "",
                0,
                "",
                0,
                "",
                ""
                ,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
                0,
                "Address",
                0,
                "",
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                response.Store_Id,
                "",
                Prefs.getString(PrefsConstants.UserId, ""),
                "",
                ""
                ,
                ""
            )
        )
    }


}


