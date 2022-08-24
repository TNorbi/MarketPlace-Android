package com.example.marketplaceproject.viewModels.forgotpassword

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplaceproject.models.ResetPasswordRequest
import com.example.marketplaceproject.models.User
import com.example.marketplaceproject.repository.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

class ForgotPasswordViewModel(val context: Context,val repository: Repository): ViewModel() {
    var user = MutableLiveData<User>()
    var code = MutableLiveData<Int>()

    init {
        user.value = User()
    }

    fun resetUserPassword(){
        viewModelScope.launch {
            val request = ResetPasswordRequest(username = user.value!!.username,email = user.value!!.email)

            try {
                Log.d("xxx","ForgotPassWord: request = $request")
                val response = repository.resetUserPassword(request)
                Log.d("xxx","ForgotPassWord: response = $response")
                code.value = response.code

            }catch (e: retrofit2.HttpException){
                Log.d("xxx","HTTPExceptionben vagyok, e.code() = ${e.code()}, message: ${e.message()}")
                //itt le kell kezeljem majd a hibakat
                if(e.code() == 300){
                    Toast.makeText(context, "Username or email not set in body.", Toast.LENGTH_LONG).show()
                }

                if(e.code() == 301){
                    Toast.makeText(context, "Username or email is wrong.", Toast.LENGTH_LONG).show()
                }

                if(e.code() == 302){
                    Toast.makeText(context, "Mail could not be sent.Please try again!", Toast.LENGTH_LONG).show()
                }

            }catch (e: Exception){
                Log.d("xxx","ForgotPasswordViewmodel: $e")
            }
        }
    }
}