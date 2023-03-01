package com.ssip.buzztalk.ui.fragments.notifications

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemNotificationBinding
import com.ssip.buzztalk.utils.getDateFromISO

class NotificationsAdapter(
  private val onNotificationClick: (postId: String) -> Unit
): RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

  var notifications: MutableList<com.ssip.buzztalk.models.notifications.response.Data> = mutableListOf()

  inner class NotificationsViewHolder(val notificationBinding: ItemNotificationBinding) : RecyclerView.ViewHolder(notificationBinding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): NotificationsViewHolder {
    val binding =
      ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return NotificationsViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return notifications.size
  }

  override fun onBindViewHolder(
    holder: NotificationsViewHolder,
    position: Int
  ) {
    val notification = notifications[position]
    with(holder.notificationBinding) {
      val checkType = getNotificationType(notification.text)
      val notification_body = "${notification.postId.userId.firstName} ${notification.postId.userId.lastName} ${checkType} ${
        if (checkType == "commented") "on your" else "you in a"
      } post"
      notificationBody.text = notification_body
      val dt = getDateFromISO(notification.createdAt)
      // Log.d("DATE ", "onBindViewHolder: ${notification.createdAt}")
      // val dateValue = "${dt!!.date}-${dt!!.month}-${dt!!.year} ${dt.hours}:${dt.minutes}"
      date.text = "${notification.createdAt.substring(0, 10)}"
      root.setOnClickListener {
        onNotificationClick(notification.postId._id)
      }
    }
  }

  private fun getNotificationType(type: String): String {
    return when(type) {
      "TAGGED" -> "tagged"
      "COMMENTTED" -> "commented"
      else -> ""
    }
  }
}