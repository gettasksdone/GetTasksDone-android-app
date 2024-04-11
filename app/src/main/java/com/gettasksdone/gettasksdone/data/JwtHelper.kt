package com.gettasksdone.gettasksdone.data

import android.content.Context
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import com.gettasksdone.gettasksdone.util.PreferenceHelper.get

class JwtHelper(private val context: Context){
    private fun getJwt(): String {
        val preferences = PreferenceHelper.defaultPrefs(context)
        return preferences["jwt"]
    }

    private fun decodeJWT(token: String): DecodedJWT {
        return JWT.decode(token)
    }

    public fun getId(): Int {
        val token = getJwt()
        val decodedToken = decodeJWT(token)
        val idClaim = decodedToken.getClaim("id")
        val idString = idClaim.asString()
        return idString.toInt()
    }

    public fun getToken(): String {
        return getJwt()
    }

    public fun getUsername(): String{
        val token = getJwt()
        val decodedToken = decodeJWT(token)
        val subClaim = decodedToken.getClaim("sub")
        return subClaim.toString()
    }
}