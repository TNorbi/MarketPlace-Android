package com.example.marketplaceproject.models


import android.text.Html
import com.squareup.moshi.JsonClass

data class User(
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var phone_number: String = ""
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    var username: String,
    var password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    var username: String,
    var email: String,
    var phone_number: String?,
    var token: String,
    var creation_time: Long,
    var refresh_time: Long
)

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    var code: String,
    var message: String,
    var creation_time: Long,
    var timestamp: Long?
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    var username: String,
    var password: String,
    var email: String,
    var phone_number: String,
    var firebase_token: String,
    //var userImage : String
)

@JsonClass(generateAdapter = true)
data class ActivateResponse(
    var code: String?,
    var message: String?,
    var timestamp: Long?,
    var htmlResponse: Html
)

@JsonClass(generateAdapter = true)
data class UserInfoResponse(
    var code: Int,
    var data: List<UserInfoField>
)

@JsonClass(generateAdapter = true)
data class UserInfoField(
    var username: String,
    var phone_number: String?,
    var email: String,
    var creation_time: Long
)

@JsonClass(generateAdapter = true)
data class UserUpdateRequest(
    var username: String,
    var email: String,
    var phone_number: String?
    //var userImage: Image
)

@JsonClass(generateAdapter = true)
data class UserUpdateResponse(
    var code: Int,
    var updatedData: UpdatedDataField
)

@JsonClass(generateAdapter = true)
data class UpdatedDataField(
    var username: String,
    val phone_number: String?,
    var email: String,
    var token: String
)

@JsonClass(generateAdapter = true)
data class ResetPasswordRequest(
    var username: String,
    var email: String
)

@JsonClass(generateAdapter = true)
data class ResetPasswordResponse(
    var code : Int,
    var message: String,
    var timestamp: Long
)