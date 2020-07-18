package com.project.kaamwaala.utils

import android.content.Context
import android.content.pm.PackageManager


class Utils {
    companion object{
        fun checkPermissions(permission: String, context: Context): Boolean {
            val res = context.checkCallingOrSelfPermission(permission)
            return res == PackageManager.PERMISSION_GRANTED
        }
    }
}