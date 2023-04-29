package com.klinik.antrean.session

import android.content.Context
import android.content.SharedPreferences
import com.klinik.antrean.R
import com.klinik.antrean.ui.model.Token

object UserSession {
    private const val Token = "token"

    fun saveToken(context: Context, value: Token) {
        val prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(Token , value.token)
        editor.apply()
    }

    private fun getDataStringPrivate(context: Context, key: String): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    fun getDataString(context: Context, data: String): String? {
        return getDataStringPrivate(context, data)
    }

    fun clearData(context: Context){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}