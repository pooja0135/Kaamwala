package com.project.kaamwaala.activity.login

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.dashboard.DashboardActivity
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.PrefsConstants
import com.project.kaamwaala.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream


class LoginActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: LoginVMF by instance<LoginVMF>()
    private var viewModel: LoginVM? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    var profile_image=""

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this, factory).get(LoginVM::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
            .build()
        
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        ivGoogle.setOnClickListener {

          //  startActivity(Intent(this, DashboardActivity::class.java))
            signIn()
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

      //  Log.v("fghhgjhgjghgjh", data.toString())
    }



    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )
    }
    // [END signin]

    private fun signOut() {
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }





    }

    private fun revokeAccess() {
        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: GoogleSignInAccount?) {

        if (user != null) {


           // Log.v("fghhgjhgjghgjh", user.id)
           // Log.v("fghhgjhgjghgjh", user.displayName)
          //  Log.v("fghhgjhgjghgjh", user.email)
          //  Log.v("fghhgjhgjghgjh", user.photoUrl.toString())


            progressbar.visibility= View.VISIBLE
            ivGoogle.visibility= View.INVISIBLE

        /*    Glide.with(this) //1
                .asBitmap()
                .load(user.photoUrl)
                 .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        ivUser.setImageBitmap(resource)

                        profile_image= getEncoded64ImageStringFromBitmap(resource).toString()


                    }

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)


                    }
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })*/
            Handler().postDelayed({
                performLogin(
                    LoginRequest(
                        user.email.toString(),"","",user.displayName.toString(),"",profile_image,"Customer",""
                    )
                )
            }, 2000)





        } else {

        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.


            Log.v("fghhgjhgjghgjh", e.toString())

            updateUI(null)
        }
    }




//========================API====================================================//
private fun performLogin(loginRequest: LoginRequest) {
    viewModel?.responseLogin = MutableLiveData()
    viewModel?.responseLogin?.observe(this, Observer {

        val response = (it as LoginResponse)
        if (response.Status) {
            progressbar.visibility= View.GONE

            startActivity(Intent(this, DashboardActivity::class.java))

            Prefs.putBoolean(PrefsConstants.KEY_ISLOGGED_IN, true)
            Prefs.putString(PrefsConstants.UserName, response.Name)
            Prefs.putString(PrefsConstants.UserId, response.Member_Id)
            Prefs.putString(PrefsConstants.UserImage, response.ProfileUrl)
            Prefs.putString(PrefsConstants.UserEmail, response.Email_id)
            Prefs.putString(PrefsConstants.UserType, response.Type)
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            (this as AppCompatActivity).finish()


            signOut()

        } else {

            this.toast("Something went wrong.")
        }
    })

    viewModel?.login(loginRequest)
}



    //Cnvert in Base64
    private fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val byteFormat = stream.toByteArray()
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)


    }
}
