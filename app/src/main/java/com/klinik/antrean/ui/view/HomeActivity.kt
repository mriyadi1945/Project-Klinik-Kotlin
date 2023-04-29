package com.klinik.antrean.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.klinik.antrean.R
import com.klinik.antrean.databinding.ActivityHomeBinding
import com.klinik.antrean.helper.UserHelper
import com.klinik.antrean.response.BaseResponse
import com.klinik.antrean.session.UserSession
import com.klinik.antrean.ui.model.User
import com.klinik.antrean.ui.viewModel.UserViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var context: Context? = null
    private val viewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        context = this.applicationContext
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.btnLogout.setOnClickListener { showAppClosingDialog() }

        binding.btnShow.setOnClickListener { read() }

        viewModel.readResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {

                }

                is BaseResponse.Success -> {
                    var data = UserHelper().processRead(it.data, this)
                    if(it.data?.response?.id?.isNotEmpty() == true){
                        if (data != null) {
                            showData(data)
                        }
                    }
                }

                is BaseResponse.Denied -> {

                }

                is BaseResponse.Error -> {
                }

                is BaseResponse.Notified -> {
                }

                else -> {
                }
            }
        }
    }

    private fun showData(data: User) {
        val dialogBuilder = MaterialAlertDialogBuilder(this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setTitle("Data User")
        dialogBuilder.setMessage(HtmlCompat.fromHtml("<big>Nama: ${data.name}</big><br/><big>Email: ${data.email}</big><br/><big>Hak Akses: ${data.hakakses}</big><br/>", HtmlCompat.FROM_HTML_MODE_LEGACY))
        dialogBuilder.setNegativeButton("Tutup") { dialog,_ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = 900
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        alertDialog.window!!.attributes = layoutParams
    }

    private fun read() {
        val token = UserSession.getDataString(this, "token")
        token.let {
            viewModel.readUser(token = token!!)
        }
    }

    private fun showAppClosingDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setMessage(HtmlCompat.fromHtml("<big>Apakah anda ingin keluar dari aplikasi?</big><br/>", HtmlCompat.FROM_HTML_MODE_LEGACY))
        dialogBuilder.setPositiveButton("Ya"){ _, _ ->
            logout()
        }
        dialogBuilder.setNegativeButton("Batal") { dialog,_ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = 900
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        alertDialog.window!!.attributes = layoutParams

    }

    private fun logout(){
        context?.let { it -> UserSession.clearData(it) }
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }

    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }
}