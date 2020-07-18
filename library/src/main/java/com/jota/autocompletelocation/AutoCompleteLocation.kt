package com.jota.autocompletelocation

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View.OnTouchListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import com.google.android.gms.appindexing.AppIndex
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places

class AutoCompleteLocation(
    context: Context,
    attrs: AttributeSet?
) :
    AutoCompleteTextView(context, attrs) {
    private val mCloseIcon: Drawable
    private val mGoogleApiClient: GoogleApiClient
    private var mAutoCompleteAdapter: AutoCompleteAdapter? = null
    private var mAutoCompleteLocationListener: AutoCompleteLocationListener? =
        null

    interface AutoCompleteLocationListener {
        fun onTextClear()
        fun onItemSelected(selectedPlace: Place?)
    }

    init {
        val resources = context.resources
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AutoCompleteLocation, 0, 0)
        var background =
            typedArray.getDrawable(R.styleable.AutoCompleteLocation_background_layout)
        if (background == null) {
            background = resources.getDrawable(R.drawable.bg_rounded_white)
        }
        var hintText =
            typedArray.getString(R.styleable.AutoCompleteLocation_hint_text)
        if (hintText == null) {
            hintText = resources.getString(R.string.default_hint_text)
        }
        val hintTextColor = typedArray.getColor(
            R.styleable.AutoCompleteLocation_hint_text_color,
            resources.getColor(R.color.default_hint_text)
        )
        val textColor = typedArray.getColor(
            R.styleable.AutoCompleteLocation_text_color,
            resources.getColor(R.color.default_text)
        )
        val padding = resources.getDimensionPixelSize(R.dimen.default_padding)
        typedArray.recycle()
        setBackground(background)
        hint = hintText
        setHintTextColor(hintTextColor)
        setTextColor(textColor)
        setPadding(padding, padding, padding, padding)
        maxLines = resources.getInteger(R.integer.default_max_lines)
        mCloseIcon = context.resources.getDrawable(R.drawable.ic_close)
        mGoogleApiClient = GoogleApiClient.Builder(context)
            .addApi(Places.GEO_DATA_API)
            .addApi(AppIndex.API)
            .build()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mGoogleApiClient.connect()
        addTextChangedListener(mAutoCompleteTextWatcher)
        setOnTouchListener(mOnTouchListener)
        this.onItemClickListener = mAutocompleteClickListener
        setAdapter(mAutoCompleteAdapter)
        mAutoCompleteAdapter = AutoCompleteAdapter(context, mGoogleApiClient, null, null)
        setAdapter(mAutoCompleteAdapter)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled) {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                if (this@AutoCompleteLocation.text.toString() == "") null else mCloseIcon,
                null
            )
        }
    }

    fun setAutoCompleteTextListener(
        autoCompleteLocationListener: AutoCompleteLocationListener?
    ) {
        mAutoCompleteLocationListener = autoCompleteLocationListener
    }

    private val mAutoCompleteTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            i2: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            i2: Int
        ) {
            this@AutoCompleteLocation.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                if (this@AutoCompleteLocation.text.toString() == "") null else mCloseIcon!!,
                null
            )
            if (mAutoCompleteLocationListener != null) {
                mAutoCompleteLocationListener!!.onTextClear()
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }
    private val mOnTouchListener =
        OnTouchListener { view, motionEvent ->
            if (motionEvent.x
                > (this@AutoCompleteLocation.width
                        - this@AutoCompleteLocation.paddingRight
                        - mCloseIcon!!.intrinsicWidth)
            ) {
                this@AutoCompleteLocation.setText("")
                setCompoundDrawables(null, null, null, null)
            }
            false
        }
    private val mAutocompleteClickListener =
        OnItemClickListener { parent, view, position, id ->
            UIUtils.hideKeyboard(context, this@AutoCompleteLocation)
            val item =
                mAutoCompleteAdapter!!.getItem(position)
            if (item != null) {
                val placeId = item.placeId
                val placeResult =
                    Places.GeoDataApi.getPlaceById(
                        mGoogleApiClient,
                        placeId
                    )
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback)
            }
        }
    private val mUpdatePlaceDetailsCallback: ResultCallback<PlaceBuffer> =
        ResultCallback { places ->
            if (!places.status.isSuccess) {
                places.release()
                return@ResultCallback
            }
            val place = places[0]
            if (mAutoCompleteLocationListener != null) {
                mAutoCompleteLocationListener!!.onItemSelected(place)
            }
            places.release()
        }


}