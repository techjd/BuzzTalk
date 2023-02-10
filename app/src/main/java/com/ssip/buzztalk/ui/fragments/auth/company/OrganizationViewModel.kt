package com.ssip.buzztalk.ui.fragments.auth.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.organizations.request.LoginRequest
import com.ssip.buzztalk.models.organizations.request.RegisterRequest
import com.ssip.buzztalk.models.organizations.response.info.OrgInfo
import com.ssip.buzztalk.models.organizations.response.login.OrgLoginInfo
import com.ssip.buzztalk.models.organizations.response.register.OrgRegisterResponse
import com.ssip.buzztalk.repository.CompanyRepository
import com.ssip.buzztalk.utils.NetworkResult
import com.ssip.buzztalk.utils.OrgTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class OrganizationViewModel @Inject constructor(
  private val companyRepository: CompanyRepository
): ViewModel() {
  private val _orgName = MutableLiveData("")
  private val _orgUserName = MutableLiveData("")
  private val _orgMotto = MutableLiveData("")
  private val _orgLink = MutableLiveData("")
  private val _orgPhone = MutableLiveData("")
  private val _orgEmail = MutableLiveData("")
  private val _orgPassword = MutableLiveData("")
  private val _orgType = MutableLiveData(OrgTypes.DEFAULT)

  val orgName= _orgName
  val orgUserName= _orgUserName
  val orgMotto= _orgMotto
  val orgLink= _orgLink
  val orgPhone= _orgPhone
  val orgEmail = _orgEmail
  val orgPassword= _orgPassword
  val orgType= _orgType

  private val _registerResponse: MutableLiveData<NetworkResult<OrgRegisterResponse>> = MutableLiveData()
  val registerResponse: LiveData<NetworkResult<OrgRegisterResponse>> = _registerResponse

  private val _loginResponse: MutableLiveData<NetworkResult<OrgLoginInfo>> = MutableLiveData()
  val loginResponse: LiveData<NetworkResult<OrgLoginInfo>> = _loginResponse

  private val _orgInfo: MutableLiveData<NetworkResult<OrgInfo>> = MutableLiveData()
  val orgInfo: LiveData<NetworkResult<OrgInfo>> = _orgInfo

  fun saveNameUserNameMotto(name: String, userName: String, bio: String) {
    _orgName.value = name
    _orgUserName.value = userName
    _orgMotto.value = bio
  }

  fun saveWebAndMobileNum(webSite: String, mobile: String) {
    _orgLink.value = webSite
    _orgPhone.value = mobile
  }

  fun saveEmailPassword(email: String, password: String) {
    _orgEmail.value = email
    _orgPassword.value = password
  }

  fun saveUserType(orgTypes: OrgTypes) {

  }

  fun getUserName(): String? {
    return orgUserName.value
  }

  fun getName(): String? {
    return orgName.value
  }

  fun getEmail(): String? {
    return orgEmail.value
  }

  fun getBio(): String? {
    return orgMotto.value
  }

  fun getPassword(): String? {
    return orgPassword.value
  }

  fun getLink(): String? {
    return orgLink.value
  }

  fun getPhone(): String? {
    return orgPhone.value
  }

  fun registerOrg() {
    val registerReq = RegisterRequest(
      orgName = orgName.value!!,
      orgType = orgType.value!!.name,
      orgBio = if (orgMotto.value == null) orgType.value!!.name else orgMotto.value!!,
      orgWebSite = orgLink.value!!,
      orgPhone = orgPhone.value!!,
      orgEmail = orgEmail.value!!,
      orgPwd = orgPassword.value!!,
      orgUserName = orgUserName.value!!
    )
    viewModelScope.launch {
      _registerResponse.postValue(NetworkResult.Loading())
      _registerResponse.postValue(companyRepository.registerOrg(registerReq))
    }
  }

  fun loginOrg() {
    val loginReq = LoginRequest(
      orgEmail = orgEmail.value!!,
      orgPwd = orgPassword.value!!
    )
    viewModelScope.launch {
      _loginResponse.postValue(NetworkResult.Loading())
      _loginResponse.postValue(companyRepository.loginOrg(loginReq))
    }
  }

  fun getUserInfo() {
    viewModelScope.launch {
      _orgInfo.postValue(NetworkResult.Loading())
      _orgInfo.postValue(companyRepository.getOrgInfo())
    }
  }
}