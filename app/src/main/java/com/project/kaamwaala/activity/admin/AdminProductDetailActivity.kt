package com.project.kaamwaala.activity.admin

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.*
import com.project.kaamwaala.adapter.admin.Price.PriceListAdapter
import com.project.kaamwaala.adapter.admin.ProductImage.ProductImageListAdapter
import com.project.kaamwaala.dialog.admin.*
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryVM
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryVMF
import com.project.kaamwaala.model.stall_category_product.*
import kotlinx.android.synthetic.main.activity_admin_product_detail.*
import kotlinx.android.synthetic.main.activity_admin_product_detail.ivBack
import kotlinx.android.synthetic.main.product_description_dialog.*

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class AdminProductDetailActivity : AppCompatActivity(), KodeinAware, OnItemCreateProduct,
    OnItemProductPrice, OnItemEditPrice, OnItemDeletePrice, OnItemStock, OnAddImageUrl,
    OnItemClickImage, OnItemDeleteImage, OnItemProductDescription {

    override val kodein by kodein()
    var list: MutableList<String> = mutableListOf()

    private val factory: StallCategoryVMF by instance<StallCategoryVMF>()
    private lateinit var VM: StallCategoryVM
    var productid: String=""
    lateinit var callbackPrice: OnItemProductPrice

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


    lateinit var callback1: OnItemCreateProduct
    lateinit var callbackEditPrice: OnItemEditPrice
    lateinit var callbackDeletePrice: OnItemDeletePrice
    lateinit var callbackStock: OnItemStock
    lateinit var callbackAddImage: OnAddImageUrl
    lateinit var callbackImage: OnItemClickImage
    lateinit var callbackDeleteImage: OnItemDeleteImage
    lateinit var callbackDescription: OnItemProductDescription

    var builder: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_product_detail)

        callback1=this
        callbackPrice=this
        callbackEditPrice=this
        callbackDeletePrice=this
        callbackStock=this
        callbackAddImage=this
        callbackImage=this
        callbackDeleteImage=this
        callbackDescription=this

        builder = AlertDialog.Builder(this)


        VM = ViewModelProviders.of(this, factory).get(StallCategoryVM::class.java)


         //SetOnCLickListener
        ivBack.setOnClickListener { finish() }
        ivEditName.setOnClickListener {  UpdateProductDialog()}
        tvAddNew.setOnClickListener {  UpdatePriceVariantDialog()}
        tvAddmore.setOnClickListener { AddUrlDialog()}
        ivEditDescription.setOnClickListener { DescriptionDialog()}
        ivDelete.setOnClickListener { ProductDeleteDialog()}
        tvUpdateNew.setOnClickListener {
            if (checkAndRequestPermissions()) {
                val photoPickerIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    photoPickerIntent, SELECT_PICTURE
                )
            }
        }


        //===================Api===============================//

        ProductDetailApi(StoreProductDetailRequest(intent.getStringExtra("productid")))



    }

    //====================================Dialog=========================================//
    private fun openMenu(price: String, saleprice: String, describe: String, id: String,action:String) {
        val ft =supportFragmentManager.beginTransaction()
        val newFragment = PriceVariantDialog.newInstance(price, saleprice, describe, id,action)
        newFragment.show(ft, "update")
        newFragment.setCallBack(callbackPrice)
    }
    private fun UpdateProductDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment =
            CreateCategoryProductDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callback1)
    }
    private fun UpdatePriceVariantDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment =
            PriceVariantDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callbackPrice)
    }
    private fun AddUrlDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment =
            AddUrlImageDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callbackAddImage)
    }
    private fun DescriptionDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment =
            ProductDescriptionDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callbackDescription)
    }
    private fun ProductImageDialog(imageurl:String,id:String) {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment =
            ImageDialog.newInstance(imageurl,id)
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callbackDeleteImage)
    }

    private fun ProductDeleteDialog()
    {
        builder!!.setMessage("Are you sure you want to delete this Product?")
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id ->
                dialog.dismiss()
                progressbar.visibility=View.VISIBLE
                DeleteProductApi(CreateProductRequest("Delete",intent.getStringExtra("productid"),"","","",""))


            }.setNegativeButton("Cancel")
            {
                    dialog, which -> dialog.dismiss()
            }
        val alert = builder!!.create()
        //Setting the title manually
        alert.setTitle("Delete Product")
        alert.show()
    }


    //==========================Click Event=========================================================//
    override fun onClickProduct(name: String) {
        tvName.setText(name)
        progressbar.visibility=View.VISIBLE
        CreateProductApi(CreateProductRequest("Name",intent.getStringExtra("productid"),"",name,"",""))
    }
    override fun onClickProductPrice(price: String, saleprice: String, describe: String,action:String,id:String) {

        progressbar.visibility=View.VISIBLE

        InsertProductPriceApi(InsertProductPriceRequest(action,id,describe,price,intent.getStringExtra("productid"),saleprice,"0"))
    }
    override fun onClickEditPrice(price: String, saleprice: String, describe: String, id: String) {
      openMenu(price,saleprice,describe,id,"Update")
    }

    override fun onClickDeletePrice(id: String) {
        progressbar.visibility=View.VISIBLE
        InsertProductPriceApi(InsertProductPriceRequest("Delete",id,"","",intent.getStringExtra("productid"),"",""))

    }

    override fun onClickStock(id: String, value: String,price:String) {
        progressbar.visibility=View.VISIBLE
        InsertProductPriceApi(InsertProductPriceRequest("IsStock",id,"",price,intent.getStringExtra("productid"),"",value))
    }
    //For Add Image Url

    override fun onClickImage(url: String) {
        progressbar.visibility= View.VISIBLE
        InsertProductImageApi(InsertProductImageRequest("Insert","","",intent.getStringExtra("productid"),"",url))

    }

    //for Image Dialog
    override fun onClickImageClick(url:String,id:String ) {
        Log.v("hfhfhffh",url)
        ProductImageDialog(url,id)
    }

    override fun onClickDeleteImage(id: String, productid: String, imageurl: String) {
        progressbar.visibility= View.VISIBLE
        InsertProductImageApi(InsertProductImageRequest("Delete",id,"",intent.getStringExtra("productid"),"",""))

    }
    override fun onClickDescription(value: String) {
        progressbar.visibility= View.VISIBLE
        tvDescription.setText(value)
        CreateProductApi(CreateProductRequest("Description",intent.getStringExtra("productid"),value,"","",""))

    }


    //==========================================Check Permission=======================================
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



    //======================================Code For Gallery Image=======================================//
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
         if (requestCode == SELECT_PICTURE) {
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
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options)

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

                    //  ivCategory.setImageBitmap(rotatedBitmap);

                    staff_image_url = getEncoded64ImageStringFromBitmap(rotatedBitmap)

                    progressbar.visibility= View.VISIBLE
                    InsertProductImageApi(InsertProductImageRequest("Insert","","",intent.getStringExtra("productid"),staff_image_url,""))

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


    //=====================================API===================================================//
    private fun CreateProductApi(createProductRequest: CreateProductRequest) {
        VM?.responseCreateProduct = MutableLiveData()
        VM?.responseCreateProduct?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CreateProductResponse)
            if (response.Status) {


                ProductDetailApi(StoreProductDetailRequest(intent.getStringExtra("productid")))


            } else {

                progressbar.visibility=View.GONE
            }


        })
        VM.createproduct(createProductRequest)
    }
    private fun DeleteProductApi(createProductRequest: CreateProductRequest) {
        VM?.responseCreateProduct = MutableLiveData()
        VM?.responseCreateProduct?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CreateProductResponse)
            if (response.Status) {
                finish()
            } else {

                progressbar.visibility=View.GONE
            }


        })
        VM.createproduct(createProductRequest)
    }
    private fun InsertProductImageApi(insertProductPriceRequest: InsertProductImageRequest) {
        VM?.responseInsertProductImage = MutableLiveData()
        VM?.responseInsertProductImage?.observe(this, androidx.lifecycle.Observer {

            val response = (it as InsertProductImageResponse)
            if (response.Status) {
                ProductDetailApi(StoreProductDetailRequest(intent.getStringExtra("productid")))
            } else {


            }

        })
        VM.insertproductimage(insertProductPriceRequest)
    }
    private fun InsertProductPriceApi(insertProductPriceRequest: InsertProductPriceRequest) {
        VM?.responseInsertProductPrice = MutableLiveData()
        VM?.responseInsertProductPrice?.observe(this, androidx.lifecycle.Observer {

            val response = (it as InsertProductPriceResponse)
            if (response.Status) {
                ProductDetailApi(StoreProductDetailRequest(intent.getStringExtra("productid")))
            } else {


            }

        })
        VM.insertproductprice(insertProductPriceRequest)
    }
    private fun ProductDetailApi(storeProductDetailRequest: StoreProductDetailRequest) {
        VM?.responseProductDetail = MutableLiveData()
        VM?.responseProductDetail?.observe(this, androidx.lifecycle.Observer {

            val response = (it as StoreProductDetailResponse)
            if (response.Status) {
                productid=response.Code
                tvName.setText(response.Name)
                tvDescription.setText(response.Description)


                rvImage.apply {
                    rvImage.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
                    adapter = ProductImageListAdapter(response.ProductImagesItem,callbackImage,callbackDeleteImage) }


                rvPrice.apply {
                    rvPrice.layoutManager = LinearLayoutManager(context)
                    adapter = PriceListAdapter(response.ProductPriceItem,callbackDeletePrice,callbackEditPrice,callbackStock) }



                progressbar.visibility= View.GONE
            }

            else {
                progressbar.visibility= View.GONE

            }

        })
        VM.productdetail(storeProductDetailRequest)
    }




}