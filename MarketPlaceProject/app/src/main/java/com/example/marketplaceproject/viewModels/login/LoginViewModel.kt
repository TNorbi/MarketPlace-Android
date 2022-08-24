package com.example.marketplaceproject.viewModels.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplaceproject.TokenApplication
import com.example.marketplaceproject.models.LoginRequest
import com.example.marketplaceproject.models.User
import com.example.marketplaceproject.repository.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(val context: Context, val repository: Repository) : ViewModel() {
    var token: MutableLiveData<String> = MutableLiveData()
    var user = MutableLiveData<User>()

    init {
        user.value = User()
    }

    //itt a ViewModel fogja elinditani a coroutine-t es nem a fragment!!!
    fun login() {
        viewModelScope.launch {
            val request =
                LoginRequest(username = user.value!!.username, password = user.value!!.password)
            try {
                val result = repository.login(request)
                TokenApplication.token = result.token
                TokenApplication.username = result.username
                token.value = result.token
                Log.d("xxx", "MyApplication - token:  ${TokenApplication.token}")
            } catch (e: retrofit2.HttpException) {
               //itt le fogom kezelni a HTTP Error Code-okat!

                if(e.code() == 300){
                    Toast.makeText(context,"Username or password missing.",Toast.LENGTH_LONG).show()
                }

                if(e.code() == 301){
                    Toast.makeText(context,"Account not activated.Check your email to activate your account!",Toast.LENGTH_LONG).show()
                }

                if(e.code() == 302){
                    Toast.makeText(context,"No such user!",Toast.LENGTH_LONG).show()
                }

            }catch (e : Exception){
                Log.d("xxx", "LoginViewModel - exception: $e")
            }
        }
    }
}
