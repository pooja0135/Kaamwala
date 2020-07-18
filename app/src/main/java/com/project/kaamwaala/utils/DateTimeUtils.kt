package com.sacredheartballia.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class DateTimeUtils {

    companion object {


        fun getBetweenDays(d1: String, d2: String): Int {
            val cal1 = getDate(d1)
            val cal2 = getDate(d2)
            val end: Long = cal2.timeInMillis
            val start: Long = cal1.timeInMillis
            val days = TimeUnit.MILLISECONDS.toDays(abs(end - start))
            return days.toInt()
        }

        fun getDate(value: String): Calendar {
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
            val date: Date = sdf.parse(value)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }


        fun getBetweenDays2(d1: String, d2: String): Int {
            val cal1 = getDate2(d1)
            val cal2 = getDate2(d2)
            val end: Long = cal2.timeInMillis
            val start: Long = cal1.timeInMillis
            val days = TimeUnit.MILLISECONDS.toDays(abs(end - start))
            return days.toInt()
        }

        fun getDate2(value: String): Calendar {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date: Date = sdf.parse(value)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }
    }
}