package com.project.kaamwaala.activity.login

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.model.login.UpdateLoginRequest
import com.project.kaamwaala.model.login.UpdateloginResponse
import com.project.kaamwaala.network.MyApi
import com.project.kaamwaala.network.SafeApiRequest

class LoginRepo(
    private val api: MyApi
) : SafeApiRequest() {


    suspend fun Login(request: LoginRequest): LoginResponse {
        var response: LoginResponse? = LoginResponse(
            "","","","","",
            false,""
        )
        try {
            val jsonObject = Gson().toJson(request, LoginRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.login(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun UpdateLogin(request: UpdateLoginRequest): UpdateloginResponse {
        var response: UpdateloginResponse? = UpdateloginResponse(
            "","",false

        )
        try {
            val jsonObject = Gson().toJson(request, UpdateLoginRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.updatelogin(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }



}