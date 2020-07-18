package com.project.kaamwaala.model.login

data class LoginResponse(
    val Email_id: String,
    val Member_Id: String,
    val Message: String,
    val Name: String,
    val ProfileUrl: String,
    val Status: Boolean,
    val Type: String
)