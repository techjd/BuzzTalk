package com.ssip.buzztalk.ui.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.utils.BottomSheetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class HomeBottomSheetViewModel: ViewModel() {

  private val _items: MutableLiveData<MutableList<BottomSheetOptions>> = MutableLiveData(mutableListOf())
  val items: LiveData<MutableList<BottomSheetOptions>> = _items

  private val _clickEvent: MutableStateFlow<Unit> = MutableStateFlow(Unit)
  val clickEvent: StateFlow<Unit> = _clickEvent

  fun addData(bottomSheetOptions: BottomSheetOptions) {
    _items.value?.add(bottomSheetOptions)
  }

  fun click() {
    viewModelScope.launch {
      _clickEvent.emit(Unit)
    }
  }
}