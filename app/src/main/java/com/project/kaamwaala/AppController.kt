package com.project.kaamwaala

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.BuildConfig
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.activity.customer.category.CustomerCategoryListRepo
import com.project.kaamwaala.activity.customer.category.CustomerCategoryListVMF
import com.project.kaamwaala.activity.customer.dashboard.DashboardRepo
import com.project.kaamwaala.activity.customer.dashboard.DashboardVMF
import com.project.kaamwaala.activity.customer.product.CustomerProductListRepo
import com.project.kaamwaala.activity.customer.product.CustomerProductListVMF
import com.project.kaamwaala.activity.customer.stall.CustomerStallListRepo
import com.project.kaamwaala.activity.customer.stall.CustomerStallListVMF
import com.project.kaamwaala.fragment.admin.adminorder.AdminOrderRepo
import com.project.kaamwaala.fragment.admin.adminorder.AdminOrderVMF
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallRepo
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallVMF
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryRepo
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryVMF
import com.project.kaamwaala.activity.login.LoginRepo
import com.project.kaamwaala.activity.login.LoginVMF
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.paperdb.Paper
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

import com.project.kaamwaala.network.MyApi
import com.project.kaamwaala.network.NetworkConnectionInterceptor
import org.kodein.di.generic.provider

import timber.log.Timber

class AppController : Application(), KodeinAware {


    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppController))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }

        bind() from singleton { LoginRepo(instance()) }
        bind() from provider { LoginVMF(instance()) }

        bind() from singleton { AdminStallRepo(instance()) }
        bind() from provider { AdminStallVMF(instance()) }

        bind() from singleton { StallCategoryRepo(instance()) }
        bind() from provider { StallCategoryVMF(instance()) }

        bind() from singleton { DashboardRepo(instance()) }
        bind() from provider { DashboardVMF(instance()) }

        bind() from singleton { CustomerStallListRepo(instance()) }
        bind() from provider { CustomerStallListVMF(instance()) }

        bind() from singleton { CustomerCategoryListRepo(instance()) }
        bind() from provider { CustomerCategoryListVMF(instance()) }

        bind() from singleton { CustomerProductListRepo(instance()) }
        bind() from provider { CustomerProductListVMF(instance()) }

        bind() from singleton { AdminOrderRepo(instance()) }
        bind() from provider { AdminOrderVMF(instance()) }
    }


    override fun onCreate() {
        super.onCreate()

        initPreferences()

        initCalligraphyLibrary()

        initTimber()

        Paper.init(this);


    }


    /**
     * Initializing calligraphy library which is used to add custom fonts
     */
    private fun initCalligraphyLibrary() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setFontAttrId(R.attr.fontPath)
                            .setDefaultFontPath("fonts/MU-R.ttf")
                            .build()
                    )
                )
                .build()
        )
    }


    /**
     * Initialize the Prefs class
     */
    private fun initPreferences() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

    /**
     * Initializes the Timber libarary
     */
    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        const val base_url = "http://kaamvalapi.sigmasoftwares.net/"
       // const val base_url = "http://solidplus.sigmasoftwares.net/"
    //    const val hybrid_url = "http://www.sacredheartschoolballia.com/webadmin/login.aspx?u%="
     //   const val url = "http://www.sacredheartschoolballia.com/"
      //  const val hybrid_url = "http://idcard.sigmasoftwares.org/webadmin/login.aspx?u%="
        const val image_base_url = "http://kaamvalapi.sigmasoftwares.net/"
    }
}
