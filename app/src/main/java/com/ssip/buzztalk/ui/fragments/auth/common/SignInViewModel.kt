package com.ssip.buzztalk.ui.fragments.auth.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.api.AuthAPI
import com.ssip.buzztalk.models.auth.company.CompanyLoginResponse
import com.ssip.buzztalk.models.auth.university.UniversityLoginRespons
import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.models.auth.user.login.response.UserLoginResponse
import com.ssip.buzztalk.models.auth.user.register.response.UserRegisterResponse
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val errorResponse: ErrorResponse,
    private val authAPI: AuthAPI
): ViewModel() {
    private val _loginResponse: MutableLiveData<NetworkResult<UserLoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<NetworkResult<UserLoginResponse>> = _loginResponse

    private val _loginResponseUniversity: MutableLiveData<NetworkResult<UniversityLoginRespons>> = MutableLiveData()
    val loginResponseUniversity: LiveData<NetworkResult<UniversityLoginRespons>> = _loginResponseUniversity

    private val _loginResponseCompany: MutableLiveData<NetworkResult<CompanyLoginResponse>> = MutableLiveData()
    val loginResponseCompany: LiveData<NetworkResult<CompanyLoginResponse>> = _loginResponseCompany

    fun login(userRequestLogin: UserRequestLogin) {
        viewModelScope.launch {
            _loginResponse.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = authAPI.logIn(userRequestLogin)
                    if (data.isSuccessful) {
                        _loginResponse.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _loginResponse.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _loginResponse.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _loginResponse.postValue(NetworkResult.Error("Network Failure"))
                    else -> _loginResponse.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }

    fun loginCompany(userCompanyRequest: UserCompanyRequest) {
        viewModelScope.launch {
            _loginResponseCompany.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = authAPI.logInCompany(userCompanyRequest)
                    if (data.isSuccessful) {
                        _loginResponseCompany.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _loginResponseCompany.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _loginResponseCompany.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _loginResponseCompany.postValue(NetworkResult.Error("Network Failure ${t.message}"))
                    else -> _loginResponseCompany.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }

    fun loginUniversity(userUniversityRequest: UserUniversityRequest) {
        viewModelScope.launch {
            _loginResponseUniversity.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = authAPI.loginUniversity(userUniversityRequest)
                    if (data.isSuccessful) {
                        _loginResponseUniversity.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _loginResponseUniversity.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _loginResponseUniversity.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _loginResponseUniversity.postValue(NetworkResult.Error("Network Failure ${t.message}"))
                    else -> _loginResponseUniversity.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }
}

data class UserCompanyRequest(
    val companyEmail: String,
    val companyPassword: String
)

data class UserUniversityRequest(
    val universityEmail: String,
    val universityPassword: String
)