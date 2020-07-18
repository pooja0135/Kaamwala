package com.project.kaamwaala.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.admin.AdminDashboardActivity
import com.project.kaamwaala.activity.customer.dashboard.DashboardActivity
import com.project.kaamwaala.activity.login.LoginActivity
import com.project.kaamwaala.network.PrefsConstants

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startSplashTimer()
    }


        private fun startSplashTimer() {
        Handler().postDelayed({
            gotoNextActivity()
        }, SPLASH_TIME_OUT)
        }

    private fun gotoNextActivity() {
       if (Prefs.getBoolean(PrefsConstants.KEY_ISLOGGED_IN, false))
        {
            if (Prefs.getString(PrefsConstants.UserType,"").equals("Admin"))
            {
                startActivity(Intent(this, AdminDashboardActivity::class.java))
            }
            else
            {
                startActivity(Intent(this, DashboardActivity::class.java))
            }
        }
       else
        {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()


    }
}
