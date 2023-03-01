package com.ssip.buzztalk.ui.components

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.utils.BottomSheetOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeBottomSheetViewModel: ViewModel() {

  private val _items: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
  val items: LiveData<MutableList<String>> = _items

  private val _clickEvent: MutableStateFlow<Boolean> = MutableStateFlow(true)
  val clickEvent: StateFlow<Boolean> = _clickEvent.asStateFlow()


  private val _eventChannel = Channel<Event>(Channel.BUFFERED)
  val events = _eventChannel.receiveAsFlow()

  val flow: Flow<Int> = flowOf()
  fun addData(choice: String) {
    _items.value?.add(choice)
  }

  fun click() {
    viewModelScope.launch {
      // _clickEvent.emit(false)
      _eventChannel.send(Event.Click)
    }
  }
}

data class ClickEvent(
  val click: Int = 0
)

sealed class Event {
  object Click: Event()
}