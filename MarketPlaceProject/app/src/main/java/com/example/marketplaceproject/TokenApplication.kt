package com.example.marketplaceproject

import android.app.Application
import com.example.marketplaceproject.utils.SessionManager

//ez nem tul elegans, mindig be kell jelentkezni majd, mert a token elveszlodik (applikacio hatokoreben van eltarolva a token!)
class TokenApplication:Application() {
    companion object{
        var token: String =""
        var username: String = ""
    }
}