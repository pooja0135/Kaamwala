package com.project.kaamwaala.utils


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.michaldrabik.classicmaterialtimepicker.CmtpDateDialogFragment
import com.michaldrabik.classicmaterialtimepicker.utilities.setOnDatePickedListener
import com.project.kaamwaala.R

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


fun Context.toast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    makeGone()
}

fun View.makeVisibile() {
    this.visibility = View.VISIBLE
}

fun View.makeInVisibile() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }
    }.show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/*
fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}
*/

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.fetchColor(id: Int): Int = ContextCompat.getColor(this, id)

fun ImageView.setImageFromUrl(url: String) {
    val requestOption = RequestOptions()
    Glide.with(context).load(url)
        .apply(requestOption)
        .placeholder(R.drawable.ic_done_white_48dp)
        .into(this)
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}


fun TextView.openDateChooser(minDatee: Long) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context as AppCompatActivity,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            text = ("" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year)
        },
        year,
        month,
        day
    )
    if (minDatee == 0L) {
        dpd.datePicker.minDate = minDatee - 2000
    } else {
        dpd.datePicker.minDate = minDatee
    }
    dpd.show()
}

fun TextView.openDateChooserString(minDatee: Long, string: String) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context as Activity,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            text = ("" + +dayOfMonth + "-" + getMonthForInt(monthOfYear) + "-" + year)
        },
        year,
        month,
        day
    )
    dpd.setTitle(string)
    if (minDatee != 0L) {
        dpd.datePicker.minDate = minDatee - 2000
    }
    dpd.show()
}


fun TextView.OpenDatePicker(minDatee: Long) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context as Activity,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            text = sdf.format(c.time)

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                text = sdf.format(c.time)+"\n"+SimpleDateFormat("HH:mm a").format(cal.time)
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        },
        year,
        month,
        day
    )
    if (minDatee != 0L) {
        dpd.datePicker.minDate = minDatee - 2000
    }
    dpd.show()
}

fun EditText.OpenDateCalendar(minDatee: Long, context: FragmentActivity) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context as Activity,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)

            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)


            setText(sdf.format(c.time))
        },
        year,
        month,
        day
    )
    if (minDatee == 0L) {
        dpd.datePicker.minDate = minDatee - 1000
    } else {
        dpd.datePicker.minDate = minDatee
    }
    dpd.show()
}


fun getMiliSeconds(date: Int, month: Int, year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar[Calendar.YEAR] = year
    calendar[Calendar.MONTH] = month
    calendar[Calendar.DAY_OF_MONTH] = date
    return calendar.timeInMillis
}

fun getMiliSeconds(date1: String): Long {
    val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
    val date = sdf.parse(date1)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    return calendar.timeInMillis
}

fun EditText.setErrorWithFocus(message: String) {
    requestFocus()
    error = message
}

fun EditText.checkIfEmpty(): Boolean {
    return text.toString().isEmpty()
}

fun EditText.isValidEmail(target: CharSequence): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
}




fun TextView.openDateChooser1(minDatee: Long, context: FragmentActivity) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            text = ("" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year)
        },
        year,
        month,
        day
    )
    if (minDatee == 0L) {
        dpd.datePicker.minDate = minDatee - 2000
    }
    dpd.show()
}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun TextView.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun getMonthForInt(num: Int): String? {
    var month = "wrong"
    val dfs = DateFormatSymbols()
    val months: Array<String> = dfs.months
    if (num in 0..11) {
        month = months[num]
    }
    return month.substring(0, 3)
}

/***
 * date picker dialog.. returns in dd-MMM-yyyy
 */
fun TextView.showDatePickerNew(fragmentManager: FragmentManager, init: Long) {

    val dialog = CmtpDateDialogFragment.newInstance()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = init
    dialog.setInitialDate(
        calendar[Calendar.DATE],
        calendar[Calendar.MONTH] + 1,
        calendar[Calendar.YEAR]
    )
    //dialog.setCustomYearRange(1990, 2020)
    dialog.setCustomSeparator("/")
    dialog.setOnDatePickedListener {
        this.text = "" + it.day + "-" + getMonthForInt(it.month-1) + "-" + it.year
    }
    dialog.show(fragmentManager, "TimePicker")
}


/*fun TextView.openDateChooser(context: Context, minDatee: Long) {
    val newFragment = SelectDateFragment(this, minDatee)
    newFragment.show(
        (context as BaseActivity).supportFragmentManager.beginTransaction(),
        "DatePicker"
    )
}*/


/*fun TextView.openSpinnerList(list : ArrayList<String>, ft: FragmentTransaction , name: String){
    val newFragment = SpinnerDialog.newInstance(list, name)
    newFragment.show(ft, "Spinner Dialog")
}*/
