/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity

    /**
     * PendingIntent grants rights to another application or the system to perform an operation on
     * behalf of your application. A PendingIntent itself is simply a reference to a token maintained
     * by the system describing the original data used to retrieve it. This means that, even if its owning
     * application's process is killed, the PendingIntent itself will remain usable from other processes
     * it has been given to. In this case, the system will use the pending intent to open the app on behalf of you,
     * regardless of whether or not the timer app is running.
     */
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)

    val snoozePendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )

    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle().bigPicture(eggImage)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    )
    /**
    Notification Channels are a way to group notifications. By grouping together similar types of notifications,
    developers and users can control all of the notifications in the channel.
    Once a channel is created, it can be used to deliver any number of notifications.
     */

    builder.setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true) //Also set setAutoCancel() to true, so that when the user taps on the notification, the notification dismisses itself as it takes them to the app.
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)
        .setPriority(NotificationCompat.PRIORITY_HIGH) /**To support devices running Android 7.1 (API level 25) or lower, you must also call setPriority() for each notification, using a priority constant from the NotificationCompat class. */
        .addAction(
            R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )  /** Next, call the addAction() function on the notificationBuilder. This function expects an icon and a text to describe your action to the user. You also need to add the snoozeIntent. This intent will be used to trigger the right boadcastReceiver when your action is clicked.*/


    /**
    This ID represents the current notification instance and is needed for updating or canceling this notification.
    Since your app will only have one active notification at a given time, you can use the same ID for all your
    notifications. You are already given a constant for this purpose called NOTIFICATION_ID in NotificationUtils.kt
    Notice that you can directly call notify() since you are performing the call from an extension function on
    the same class.
     */

    notify(NOTIFICATION_ID, builder.build())


}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}

/**
 * NotificationCompat offers built-in styles for:

BigTextStyle, which can display a large block of text, such as showing the contents of an email when expanded.
BigPictureStyle, which shows large-format notifications that include a large image attachment.
InboxStyle, which shows a conversation style text content.
MediaStyle, which shows controls for media playback.
MessagingStyle, which shows large-format notifications that include multiple messages between any number of people.
 */
