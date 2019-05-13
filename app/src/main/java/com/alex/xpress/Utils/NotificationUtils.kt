package com.alex.xpress.Utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.alex.xpress.R
import com.alex.xpress.views.ExpiredActivity
import com.alex.xpress.views.ProductsActivity

class NotificationUtils {

    companion object{
        private const val CHANNEL_ID_TIMER = "channel_xpress"
        private const val CHANNEL_NAME_TIMER = "Xpress App"
        private const val TIMER_ID = 0

        @TargetApi(24)
        private fun NotificationManager.createNotificationChannel(channelID: String, channelName: String, playSound: Boolean){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelImportance = if(playSound) NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW

                val nChannel = NotificationChannel(channelID,channelName,channelImportance)
                nChannel.enableLights(true)
                nChannel.enableVibration(true)
                nChannel.lightColor = Color.BLUE
                this.createNotificationChannel(nChannel)
            }
        }

        fun hideTimerNotification(context: Context){
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }

        private fun getBasicNotificationBuilder(context: Context, channelId: String, playSound: Boolean): NotificationCompat.Builder {
            val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val nBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_timer_off_black_24dp)
                .setAutoCancel(true)
                .setDefaults(0)

            if(playSound) nBuilder.setSound(notificationSound)

            return nBuilder
        }

        fun showTimerExpired(context: Context){
            val notificationIntent = Intent(context, ExpiredActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

            //Crear notification
            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("Producto Expirado!")
                .setContentText("Favor revisar la lista de productos vencidos!!")
                .setContentIntent(pendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }
    }
}