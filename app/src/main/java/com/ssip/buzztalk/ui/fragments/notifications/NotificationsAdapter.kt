package com.ssip.buzztalk.ui.fragments.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemNotificationBinding

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
      val notification_body = "${notification.postId.userId.firstName} ${notification.postId.userId.lastName} ${getNotificationType(notification.text)} you in a post"
      notificationBody.text = notification_body
    }
  }

  private fun getNotificationType(type: String): String {
    return when(type) {
      "TAGGED" -> "tagged"
      else -> ""
    }
  }
}