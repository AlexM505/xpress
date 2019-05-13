package com.alex.xpress.Utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Utils {

    companion object {

        fun timeString(millisUntilFinished: Long): String {
            var millisUntilFinished: Long = millisUntilFinished
            val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
            millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

            val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
            millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

            val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

            // Format the string
            return String.format(
                Locale.getDefault(),
                "%02d dias %02d:%02d:%02d",
                days, hours, minutes, seconds
            )
        }

        fun timeDays(millisUntilFinished: Long):String{
            val millisUntilFinished: Long = millisUntilFinished
            val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)

            return String.format(Locale.getDefault(), "%02d Dias", days)
        }

        fun timeHoursMinSec(millisUntilFinished: Long):String{
            var millisUntilFinished: Long = millisUntilFinished

            val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
            millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

            val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
            millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

            val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

            // Format the string
            return String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                 hours, minutes, seconds
            )
        }

        fun validateStateProduct(expiration:String):String{
            val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
            val currentDate = sdf.format(Date())
            var estado: String = if(currentDate.compareTo(expiration) > 0 || currentDate.equals(expiration))
                "CADUCADO"
            else
                "VIGENTE"

            return estado
        }

        fun dialogStandar(context: Context, title:String, msg:String){
            val builder = AlertDialog.Builder(context)

            builder.setTitle(title)
            builder.setMessage(msg)
            builder.setPositiveButton("OK"){dialog, which ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        fun validateProductName(name:String):String{
            return if(name.length < 18)
                name.substring(0,name.length)
            else
                name.substring(0,15) + ".."
        }

    }

}