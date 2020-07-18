package com.project.kaamwaala.model.login

data class LoginRequest(
    val Email_id: String,
    val EntryBy: String,
    val Member_Id: String,
    val Name: String,
    val Password: String,
    val ProfileUrl: String,
    val Type: String,
    val fcmId: String
)