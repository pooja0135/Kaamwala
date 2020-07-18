package com.project.kaamwaala.utils

class StringUtils {
    companion object {
        fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

        fun getNumberList(min: Int, max: Int): ArrayList<String> {
            val list: ArrayList<String> = ArrayList()
            for (i in min..max) {
                list.add(i.toString())
            }
            return list
        }
    }
}