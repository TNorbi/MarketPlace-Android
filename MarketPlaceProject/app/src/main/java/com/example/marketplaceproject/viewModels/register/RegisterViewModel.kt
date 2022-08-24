package com.example.marketplaceproject.viewModels.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplaceproject.models.RegisterRequest
import com.example.marketplaceproject.models.User
import com.example.marketplaceproject.repository.Repository
import kotlinx.coroutines.launch

class RegisterViewModel(val context: Context,val repository: Repository) : ViewModel() {
    var registerResponse: MutableLiveData<String> = MutableLiveData()
    var activateResponse: MutableLiveData<String> = MutableLiveData()
    var user = MutableLiveData<User>()

    init {
        user.value = User()
    }

    fun register(){
        viewModelScope.launch {
            val request =
                RegisterRequest(username = user.value!!.username, password = user.value!!.password, email = user.value!!.email, phone_number = user.value!!.phone_number, firebase_token = "token")
            try {
                val result = repository.register(request)
                registerResponse.value = result.creation_time.toString()
                Log.d("xxx","User successfully created!")
                //itt kene atvaltsak egy ablakra,ahol megjelenitem a Usernek,
                // hogy a Regisztracio sikeres lett eshogy az email postajat megnezve tudja majd aktivalni a fiokjat
            } catch (e: retrofit2.HttpException) {
                Log.d("xxx", "RegisterViewModel - exception: $e, code : ${e.code()}")

                //itt lekezelem a HTTP hibakat
                if(e.code() == 300){
                    Toast.makeText(context,"One of the following  username , password ,\n" +
                            "email , phone_number, userImage are either\n" +
                            "empty or missing.",Toast.LENGTH_LONG).show()
                }

                if(e.code() == 301){
                    Toast.makeText(context,"Wrong file format. Only jpeg or png are allowed.",Toast.LENGTH_LONG).show()
                }

                if(e.code() == 302){
                    Toast.makeText(context,"Email incorrect. You need to enter another\n" +
                            "email.",Toast.LENGTH_LONG).show()
                }

                if(e.code() == 303){
                    Toast.makeText(context,"Username or email already used.",Toast.LENGTH_LONG).show()
                }

            }catch (e: Exception){
                Log.d("xxx", "RegisterViewModel - exception: $e")
            }
        }
    }

    fun activateUser(){
        viewModelScope.launch {
            try{
                Log.d("xxx","Username: ${user.value!!.username}")
                val result = repository.activateUser(user.value!!.username)
                activateResponse.value = result.toString()
            }catch (e: retrofit2.HttpException){

                if(e.code() == 301){
                    Log.d("xxx","When the username param is not sent.")
                }

                if(e.code() == 300){
                    Log.d("xxx","User not in database.")
                }

            }catch (e: Exception){
                Log.d("xxx", "RegisterViewModel - exception: $e")
            }
        }
    }
}