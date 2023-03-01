package com.ssip.buzztalk.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun getDateFromISO(date: String): Date {
  val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  inputFormat.timeZone = TimeZone.getTimeZone("UTC")
  return inputFormat.parse(date)
}