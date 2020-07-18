package com.project.kaamwaala.activity.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.model.login.UpdateLoginRequest
import com.project.kaamwaala.model.login.UpdateloginResponse
import com.project.kaamwaala.network.Coroutines

class LoginVM(
    private val repository: LoginRepo
) : ViewModel() {
    var responseLogin = MutableLiveData<LoginResponse>()
    var responseUpdateLogin = MutableLiveData<UpdateloginResponse>()

    fun login(request: LoginRequest) {
        Coroutines.main {
            responseLogin.postValue(repository.Login(request))
        }
    }

    fun updatelogin(request: UpdateLoginRequest) {
        Coroutines.main {
            responseUpdateLogin.postValue(repository.UpdateLogin(request))
        }
    }
}