package com.klinik.antrean.helper

import android.app.Activity
import android.content.Intent
import com.klinik.antrean.response.ReadResponse
import com.klinik.antrean.response.UserResponse
import com.klinik.antrean.session.UserSession
import com.klinik.antrean.ui.model.MetaData
import com.klinik.antrean.ui.model.User
import com.klinik.antrean.ui.view.HomeActivity
import okhttp3.ResponseBody
import org.json.JSONObject

class UserHelper {
    fun processCreate(data: MetaData?, context: Activity): String? {
        var message: String? = null
        data?.code.let {
            if (it == 200) {
                message = data?.message
            }
        }
        return message
    }
    fun processLogin(data: UserResponse?, context: Activity) {
        data?.response.let {
            if (it != null) {
                UserSession.saveToken(context, it)
            }
        }
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
        context.finish()
    }
    fun processRead(data: ReadResponse?, context: Activity): User? {
        var user: User? = null
        if (data?.metadata?.code == 200) {
            user = data.response
        }
        return user
    }
    fun processDenied(data: ResponseBody?): String? {
        val jsonObject = data?.string()?.let { JSONObject(it) }
        if (jsonObject != null) {

        }
        return jsonObject?.getString("message")
    }
    fun processError(data: ResponseBody?): String? {
        val jsonObject = data?.string()?.let { JSONObject(it) }
        if (jsonObject != null) {

        }
        return jsonObject?.getString("message")
    }
    fun processNotified(data: ResponseBody?): String? {
        val jsonObject = data?.string()?.let { JSONObject(it) }
        if (jsonObject != null) {

        }
        return jsonObject?.getString("message")
    }
}