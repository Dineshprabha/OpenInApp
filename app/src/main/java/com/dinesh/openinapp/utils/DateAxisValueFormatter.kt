package com.dinesh.openinapp.utils

import android.icu.util.Calendar
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class DateAxisValueFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = value.toLong()
        return dateFormat.format(calendar.time)
    }
}