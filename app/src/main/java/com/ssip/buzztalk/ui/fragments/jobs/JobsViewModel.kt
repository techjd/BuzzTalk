package com.ssip.buzztalk.ui.fragments.jobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.chat.response.messages.Messages
import com.ssip.buzztalk.models.opportunities.response.AllOppo
import com.ssip.buzztalk.repository.CompanyRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class JobsViewModel @Inject constructor(
  private val companyRepository: CompanyRepository
): ViewModel() {

  private val _allOpportunities: MutableLiveData<NetworkResult<AllOppo>> = MutableLiveData()
  val allOpportunities: LiveData<NetworkResult<AllOppo>> = _allOpportunities

  fun getAllOppo() {
    viewModelScope.launch {
      _allOpportunities.postValue(NetworkResult.Loading())
      _allOpportunities.postValue(companyRepository.getAllOppo())
    }
  }

}