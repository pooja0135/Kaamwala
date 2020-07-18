package com.project.kaamwaala.activity.customer.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.SpinnerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.BaseActivity
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.product.OrderListActivity

import com.project.kaamwaala.adapter.CategoryListAdapter
import com.project.kaamwaala.adapter.admin.customer.CitySpinnerAdapter
import com.project.kaamwaala.adapter.customer.CardPagerAdapter
import com.project.kaamwaala.adapter.customer.NewlyLaunchAdapter
import com.project.kaamwaala.adapter.customer.OffersAdapter
import com.project.kaamwaala.dialog.admin.AdminModeDialog
import com.project.kaamwaala.dialog.admin.LogoutDialog
import com.project.kaamwaala.model.category.CategoryItem
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.city.CityItem
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.model.customer.dashboard.DashboardRequest
import com.project.kaamwaala.model.customer.dashboard.DashboardResponse
import com.project.kaamwaala.network.PrefsConstants
import com.project.kaamwaala.utils.C
import com.project.kaamwaala.utils.Utils

import com.project.kaamwaala.utils.toast
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*


class DashboardActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()

    private val factory: DashboardVMF by instance<DashboardVMF>()
    private var viewModel: DashboardVM? = null
    var list: MutableList<String> = mutableListOf()
    var categorylist: MutableList<CategoryItem> = mutableListOf()
    var servicelist: MutableList<CategoryItem> = mutableListOf()
    var citylist: MutableList<CityItem> = mutableListOf()
    val DELAY_MS: Long = 3000
    val PERIOD_MS: Long = 6000
    var currentPage = 0
    var timer: Timer? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewModel = ViewModelProviders.of(this, factory).get(DashboardVM::class.java)

        Glide.with(this) //1
            .load(Prefs.getString(PrefsConstants.UserImage,""))
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(ivUser)

        tabLayout.setupWithViewPager(viewpager, true)

        ivOrder.setOnClickListener { startActivity(Intent(this, OrderListActivity::class.java)) }
        llLocation.setOnClickListener {
           // startActivity(Intent(this, PlaceAutoCompleteActivity::class.java))
        }
        ivLogo.setOnClickListener { AdminMode() }
        ivUser.setOnClickListener { Logout() }


        spinnerLocation.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                Prefs.putString(PrefsConstants.CityId, citylist.get(spinnerLocation.selectedItemPosition).Id)
                Prefs.putString(PrefsConstants.CityName, citylist.get(spinnerLocation.selectedItemPosition).Name)
                progressbar.visibility= View.VISIBLE
                GetDashboard(DashboardRequest("Dashboard",citylist.get(spinnerLocation.selectedItemPosition).Id,""))

            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        getPermission()

        //===============================API=======================================//

        GetCityApi()



    }


    private fun Logout() {
        val ft = this.supportFragmentManager.beginTransaction()
        val newFragment = LogoutDialog()
        newFragment.show(ft, "dialog")
    }

    private fun AdminMode() {
        val ft = this.supportFragmentManager.beginTransaction()
        val newFragment = AdminModeDialog()
        newFragment.show(ft, "dialog")
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun performCategory() {
        viewModel?.responseCategory = MutableLiveData()
        viewModel?.responseCategory?.observe(this, Observer {

            val response = (it as CategoryListResponse)


            if (response.Status) {
                for (i in 0 until response.CategoryListItem.size) {
                    if (response.CategoryListItem[i].IsService.equals("False"))
                    {
                        categorylist.add(response.CategoryListItem[i])
                    }
                    else
                    {
                        servicelist.add(response.CategoryListItem[i])
                    }

                }

                recyclerview.apply {
                    recyclerview.layoutManager = GridLayoutManager(context,3)
                    adapter = CategoryListAdapter(categorylist) }

                rvServices.apply {
                    rvServices.layoutManager = GridLayoutManager(context,3)
                    adapter = CategoryListAdapter(servicelist)
                    progressbar.visibility= View.GONE
                }

            } else {
                progressbar.visibility= View.GONE
                //  activity?.toast("Something went wrong.")
            }
        })

        viewModel?.getCategory()
    }


    private fun GetCityApi() {
        viewModel?.responseCity = MutableLiveData()
        viewModel?.responseCity?.observe(this, Observer {

            val response = (it as CityResponse)

            if (response.Status) {



                var spinnerAdapter: SpinnerAdapter = CitySpinnerAdapter(this,response.CityItem )
                spinnerLocation?.adapter = spinnerAdapter

                for (i in 0 until response.CityItem.size) {

                    citylist.add(response.CityItem[i])
                }

                Prefs.putString(PrefsConstants.CityId, citylist.get(spinnerLocation.selectedItemPosition).Id)
                Prefs.putString(PrefsConstants.CityName, citylist.get(spinnerLocation.selectedItemPosition).Name)

                GetDashboard(DashboardRequest("Dashboard",citylist.get(spinnerLocation.selectedItemPosition).Id,""))

                performCategory()
            }
            else {

                performCategory()
                //  activity?.toast("Something went wrong.")
            }
        })

        viewModel?.getCity()
    }

    private fun GetDashboard(dashboardRequest: DashboardRequest) {
        viewModel?.responseDashboard = MutableLiveData()
        viewModel?.responseDashboard?.observe(this, Observer {

            val response = (it as DashboardResponse)


            if (response.Status) {

                if(response.BannerList.isEmpty())
                {

                }
                else
                {
                    viewpager.adapter= CardPagerAdapter(this,response.BannerList)
                    val NUM_PAGES: Int = response.BannerList.size
                    val handler = Handler()
                    val Update = Runnable {
                        if (currentPage == NUM_PAGES) {
                            currentPage = 0
                        }
                        viewpager.setCurrentItem(currentPage++, true)
                    }

                    timer = Timer() // This will create a new Thread

                    timer!!.schedule(object : TimerTask() {
                        // task to be scheduled
                        override fun run() {
                            handler.post(Update)
                        }
                    }, DELAY_MS, PERIOD_MS)
                }

                if(response.NewLaunchedList.isEmpty())
                {

                }
                else
                {
                    llNewLaunch.visibility=View.VISIBLE
                    recyclerviewNewly.apply {
                        recyclerviewNewly.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                        adapter = NewlyLaunchAdapter(response.NewLaunchedList) }

                }


                if(response.OfferList.isEmpty())
                {

                }
                else
                {
                    llOffer.visibility=View.VISIBLE
                    recyclerviewOffer.apply {
                        recyclerviewOffer.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                        adapter = OffersAdapter(response.OfferList) }

                }

                progressbar.visibility= View.GONE
            } else {
                progressbar.visibility= View.GONE

                llOffer.visibility=View.GONE
                llNewLaunch.visibility=View.GONE

            }
        })

        viewModel?.getDashboard(dashboardRequest)
    }


    private fun getPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // do you work nowinitLocation
                        initLocation()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        toast("Permission for location denied")
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }


    private fun initLocation() {
        if (Utils.checkPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                this@DashboardActivity
            ) && Utils.checkPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                this@DashboardActivity
            )
        ) {
            try {
                getLocationUpdates()
                startLocationUpdates()
                //initLocation2()
                //manageFragmentTransaction(FRAG_LIST, null, -1)
              //  locationProgress.visibility = View.VISIBLE
                //startService()
                //startGettingLocation()
            } catch (ex: SecurityException) {
                ex.printStackTrace()
            }

        } else {
            toast("No permission to access location")

        }
    }


    private fun getLocationUpdates() {
       // locationProgress.visibility = View.VISIBLE
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 30000
        locationRequest.fastestInterval = 10000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder: LocationSettingsRequest.Builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener(this, object : OnSuccessListener<LocationSettingsResponse?> {
            override fun onSuccess(locationSettingsResponse: LocationSettingsResponse?) {


            }
        })

        task.addOnFailureListener(this, object : OnFailureListener {
            override fun onFailure(e: Exception) {
                /*Toast.makeText(
                    this@NewDashboardActivity,
                    "addOnFailureListener",
                    Toast.LENGTH_SHORT
                ).show()*/
                if (e is ResolvableApiException) { // Location settings are not satisfied, but this can be fixed
                    try { // Show the dialog by calling startResolutionForResult(),

                        val resolvable: ResolvableApiException = e as ResolvableApiException
                        resolvable.startResolutionForResult(
                            this@DashboardActivity, 1
                            //REQUEST_CHECK_SETTINGS
                        )
                    } catch (sendEx: IntentSender.SendIntentException) { // Ignore the error.
                    }
                }
            }
        })


        //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                stopLocationUpdates()
                if (locationResult.locations.isNotEmpty()) {
                    val location =
                        LatLng(
                            locationResult.locations[0].latitude,
                            locationResult.locations[0].longitude
                        )
                    finalizeAddress(LatLng(location.latitude, location.longitude))
                }


            }
        }
    }

    private fun finalizeAddress(latlng: LatLng) {

        //manageFragmentTransaction(FRAG_LIST, null, -1)

        //Prefs.putString(C.currentAddress, localityName.text.toString())
        Prefs.putDouble(C.currentLat, latlng.latitude)
        Prefs.putDouble(C.currentLon, latlng.longitude)



        /* calculateDistance(
             LatLng(
                 latlng.latitude,
                 latlng.longitude
             )

         )*/
    }

    //start location updates
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}
