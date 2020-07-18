package com.project.kaamwaala.activity.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.project.kaamwaala.R
import java.util.*


class PlaceAutoCompleteActivity : AppCompatActivity()
    {
    private var mMap: GoogleMap? = null
    val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_auto_complete)




        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),getString(R.string.map_key));
        }

        val fields =
            Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME
            )
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)




    }





    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place =
                    Autocomplete.getPlaceFromIntent(data!!)
                Log.i(
                   "test1234",
                    "Place: " + place.name + ", " + place.id
                )
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status =
                    Autocomplete.getStatusFromIntent(data!!)
                Log.i("test1234", status.statusMessage)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}