package com.ssip.buzztalk.ui.fragments.chat

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssip.buzztalk.ui.fragments.chat.mychat.MyChatsFragment
import com.ssip.buzztalk.ui.fragments.chat.mygroup.MyGroupsFragment

class ConversationsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
  override fun getItemCount(): Int {
    return 2
  }

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> MyChatsFragment()
      else -> MyGroupsFragment()
    }
  }
}