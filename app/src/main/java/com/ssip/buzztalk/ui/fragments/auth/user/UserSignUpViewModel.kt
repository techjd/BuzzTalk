package com.ssip.buzztalk.ui.fragments.auth.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.api.AuthAPI
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import com.ssip.buzztalk.models.auth.user.register.response.UserRegisterResponse
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserSignUpViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val errorResponse: ErrorResponse,
    private val authAPI: AuthAPI
): ViewModel() {
    private val _firstName = MutableLiveData("")
    private val _secondName = MutableLiveData("")
    private val _email = MutableLiveData("")
    private val _password = MutableLiveData("")
    private val _userName = MutableLiveData("")
    val firstName: LiveData<String> = _firstName
    val secondName: LiveData<String> = _secondName
    val userName :LiveData<String> = _userName
    val email: LiveData<String> = _email
    val password: LiveData<String> = _password

    private val _registerResponse: MutableLiveData<NetworkResult<UserRegisterResponse>> = MutableLiveData()
    val registerResponse: LiveData<NetworkResult<UserRegisterResponse>> = _registerResponse

    fun saveFirstNameAndSecond(firstName: String, secondName: String) {
        _firstName.value = firstName
        _secondName.value = secondName
    }

    fun saveFirstName(firstName: String) {
        _firstName.value = firstName
    }

    fun saveSecondName(secondName: String) {
        _secondName.value = secondName
    }

    fun getName(): String? {
        return firstName.value
    }

    fun getEmail(): String? {
        return email.value
    }

    fun saveEmail(email: String) {
        _email.value = email
    }

    fun saveUserName(userName: String) {
        _userName.value = userName
    }

    fun savePassword(password: String) {
        _password.value = password
    }

    fun register(userRequestRegister: UserRequestRegister) {
        viewModelScope.launch {
            _registerResponse.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = authAPI.register(userRequestRegister)
                    if (data.isSuccessful) {
                        _registerResponse.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _registerResponse.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _registerResponse.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _registerResponse.postValue(NetworkResult.Error("Network Failure"))
                    else -> _registerResponse.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }
}