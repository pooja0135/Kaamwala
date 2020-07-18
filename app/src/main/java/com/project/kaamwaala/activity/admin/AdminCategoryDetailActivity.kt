package com.project.kaamwaala.activity.admin

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.project.kaamwaala.BuildConfig

import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.Product.AdminProductListAdapter
import com.project.kaamwaala.dialog.admin.CreateCategoryDialog
import com.project.kaamwaala.dialog.admin.CreateCategoryProductDialog
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryVM
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryVMF
import com.project.kaamwaala.model.stall_category_product.CreateProductRequest
import com.project.kaamwaala.model.stall_category_product.CreateProductResponse
import com.project.kaamwaala.model.stall_category_product.StoreProductListRequest
import com.project.kaamwaala.model.stall_category_product.StoreProductListResponse
import com.project.kaamwaala.model.stallcategory.CreateCategoryRequest
import com.project.kaamwaala.model.stallcategory.CreateCategoryResponse
import com.project.kaamwaala.OnItemCreateCategory
import com.project.kaamwaala.OnItemCreateProduct
import kotlinx.android.synthetic.main.activity_admin_category_detail.*
import kotlinx.android.synthetic.main.activity_admin_category_detail.ivBack
import kotlinx.android.synthetic.main.activity_admin_category_detail.progressbar
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AdminCategoryDetailActivity : AppCompatActivity(), KodeinAware, OnItemCreateCategory,
    OnItemCreateProduct {

    override val kodein by kodein()
    private val factory: StallCategoryVMF by instance<StallCategoryVMF>()
    private lateinit var VM: StallCategoryVM
    lateinit var callback: OnItemCreateCategory
    lateinit var callback1: OnItemCreateProduct


    var list: MutableList<String> = mutableListOf()

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
    var PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    var PERMISSION_ALL = 4
    var serverResponseCode = 0
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    var builder: AlertDialog.Builder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_category_detail)

        VM = ViewModelProviders.of(this, factory).get(StallCategoryVM::class.java)
        callback = this
        callback1 = this

        builder = AlertDialog.Builder(this)




        Glide.with(this) //1
            .asBitmap()
            .load(intent.getStringExtra("image"))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    ivCategory.setImageBitmap(resource)

                }
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)


                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })






        tvCategory.setText(intent.getStringExtra("name"))


        ivBack.setOnClickListener { finish() }
        ivCategory.setOnClickListener {
            if (checkAndRequestPermissions()) {
                val photoPickerIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    photoPickerIntent, SELECT_PICTURE
                )
            }
        }
        ivAdd.setOnClickListener { createProductDialog() }
        ivEdit.setOnClickListener { createInventoryDialog() }
        ivDelete.setOnClickListener { CategoryDeleteDialog() }



        //====================================API=====================================================//
        ProductDetailApi(StoreProductListRequest("Select","","","",intent.getStringExtra("category_id"),intent.getStringExtra("store_id"),"0"))


    }


    //==============================================Check Permission=========================================//
    private fun checkAndRequestPermissions(): Boolean {
        val camerapermission =
            this?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        val writepermission = this?.let {
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
            this?.let {
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
                        if (this?.let {
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                    it,
                                    Manifest.permission.CAMERA
                                )
                            }!!
                            || this?.let {
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
                                            this?.finish()
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
        this?.let {
            AlertDialog.Builder(it)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
        }
    }

    private fun explain(msg: String) {
        val dialog = this?.let { AlertDialog.Builder(it) }
        dialog?.setMessage(msg)?.setPositiveButton("Yes") { paramDialogInterface, paramInt ->
            //  permissionsclass.requestPermission(type,code);
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:com.example.parsaniahardik.kotlin_marshmallowpermission")
                )
            )
        }?.setNegativeButton("Cancel") { paramDialogInterface, paramInt -> this?.finish() }
        dialog?.show()
    }

    //============================Code for Camera and Gallary===================================//
    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri =
            getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
        val photoURI = FileProvider.getUriForFile(
            this,
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

                ivCategory.setImageBitmap(rotatedBitmap);


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
                val cursor = this!!.contentResolver
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
            //    ivCategory.setImageBitmap(bitmap);
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

                    ivCategory.setImageBitmap(rotatedBitmap);

                    staff_image_url = getEncoded64ImageStringFromBitmap(rotatedBitmap)

                    Log.v("dkhhdhdhd", staff_image_url);

                    CreateCategoryApi(
                        CreateCategoryRequest(
                            "ImagePath",
                            "",
                            intent.getStringExtra("category_id"),
                            staff_image_url,
                            intent.getStringExtra("store_id"),"0"
                        )
                    )

                    val ba = bao.toByteArray()
                } catch (e: Exception) {
                    Log.e("Exception", e.toString())
                }
            } else {

                // Snackbar.make(getActivity(), "Please enter Username.", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(this, "Unable to Select the Image.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }



    //==========================Dialog==================================================//
    private fun createInventoryDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment =
            CreateCategoryDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callback)
    }

    private fun createProductDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment =
            CreateCategoryProductDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callback1)
    }

    private fun CategoryDeleteDialog()
    {
        builder!!.setMessage("Are you sure you want to delete this"+ tvCategory.text.toString()+" and all its products?")
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id ->
                dialog.dismiss()
                progressbar.visibility=View.VISIBLE
                  DeleteCategoryApi(CreateCategoryRequest("Delete", "", intent.getStringExtra("category_id"), "", "","")
                )

            }.setNegativeButton("Cancel")
            {
                    dialog, which -> dialog.dismiss()
            }
        val alert = builder!!.create()
        //Setting the title manually
        alert.setTitle("Delete Product")
        alert.show()
    }

    //================================API===========================================================//
    private fun CreateCategoryApi(createCategoryRequest: CreateCategoryRequest) {
        VM?.responseCreateCategory = MutableLiveData()
        VM?.responseCreateCategory?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CreateCategoryResponse)
            if (response.Status) {

            } else {


            }

        })
        VM.createcategory(createCategoryRequest)
    }

    private fun DeleteCategoryApi(createCategoryRequest: CreateCategoryRequest) {
        VM?.responseCreateCategory = MutableLiveData()
        VM?.responseCreateCategory?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CreateCategoryResponse)
            if (response.Status) {
                  finish()
            } else {


            }

        })
        VM.createcategory(createCategoryRequest)
    }

    private fun CreateProductApi(createProductRequest: CreateProductRequest) {
        VM?.responseCreateProduct = MutableLiveData()
        VM?.responseCreateProduct?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CreateProductResponse)
            if (response.Status) {

                ProductDetailApi(StoreProductListRequest("Select","","","",intent.getStringExtra("category_id"),"","0"))

            } else {


            }

        })
        VM.createproduct(createProductRequest)
    }

    private fun ProductDetailApi(storeProductListRequest: StoreProductListRequest) {
        VM?.responseProductList = MutableLiveData()
        VM?.responseProductList?.observe(this, androidx.lifecycle.Observer {

            val response = (it as StoreProductListResponse)
            if (response.Status) {
                rvProduct.apply {
                    rvProduct.layoutManager = LinearLayoutManager(context)
                    adapter = AdminProductListAdapter(response.ProductItem)
                }
                progressbar.visibility=View.GONE
            }

            else {
                progressbar.visibility=View.GONE

            }

        })
        VM.productlist(storeProductListRequest)
    }


    //===================Dialog On CLick Event========================================//
    override fun onClickCategory(id: String) {
        tvCategory.setText(id)

        CreateCategoryApi(
            CreateCategoryRequest(
                "CategoryName",
                id,
                intent.getStringExtra("category_id"),
                "",
                intent.getStringExtra("store_id"),"0"
            )
        )
    }

    override fun onClickProduct(name: String) {
        progressbar.visibility=View.VISIBLE
        CreateProductApi(CreateProductRequest("Insert","","",name,intent.getStringExtra("category_id"),intent.getStringExtra("store_id")))
    }

    override fun onResume() {
        super.onResume()
        progressbar.visibility=View.VISIBLE
        ProductDetailApi(StoreProductListRequest("Select","","","",intent.getStringExtra("category_id"),intent.getStringExtra("store_id"),"0"))
    }

}