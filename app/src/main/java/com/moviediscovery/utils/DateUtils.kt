package com.moviediscovery.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val INPUT_DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    private const val OUTPUT_DATE_FORMAT_PATTERN = "dd \nMMM \nyyyy"

    private val inputDateFormat: DateFormat by lazy(mode = LazyThreadSafetyMode.NONE) {
        SimpleDateFormat(INPUT_DATE_FORMAT_PATTERN, Locale.US)
    }

    private val outputDateFormat: DateFormat by lazy(mode = LazyThreadSafetyMode.NONE) {
        SimpleDateFormat(OUTPUT_DATE_FORMAT_PATTERN, Locale.US)
    }

    fun formatDate(date: String): String {
        return try {
            inputDateFormat.parse(date)?.let {
                outputDateFormat.format(it)
            } ?: date
        } catch (e: ParseException) {
            date
        }
    }
}