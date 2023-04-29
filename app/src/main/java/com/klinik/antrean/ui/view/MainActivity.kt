package com.klinik.antrean.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.klinik.antrean.helper.UserHelper
import com.klinik.antrean.ui.viewModel.UserViewModel
import com.klinik.antrean.databinding.ActivityMainBinding
import com.klinik.antrean.response.BaseResponse
import com.klinik.antrean.session.UserSession

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var context: Context? = null
    private val viewModel by viewModels<UserViewModel>()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        context = this.applicationContext
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val token = UserSession.getDataString(this, "token")

        if (token?.isNotEmpty() == true){
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
        }

        binding.etUser.setOnTouchListener { v, _ ->
            v.isFocusable = false
            v.isFocusableInTouchMode = true
            false
        }
        binding.etUser.hint = "Username"

        binding.etPassword.setOnTouchListener { v, _ ->
            v.isFocusable = false
            v.isFocusableInTouchMode = true
            false
        }
        binding.etPassword.hint = "Password"

        binding.btnLogin.setOnClickListener {
            if (isNetworkAvailable(this)) {
                doLogin()
            }
            else{
                Toast.makeText(this, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loginResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.llProgressBar.preload.visibility = View.VISIBLE
                }

                is BaseResponse.Success -> {
                    UserHelper().processLogin(it.data, this)
                }

                is BaseResponse.Denied -> {
                    binding.llProgressBar.preload.visibility = View.GONE
                    binding.tvNotif.visibility = view.visibility
                    binding.tvNotif.text = UserHelper().processDenied(it.data)
                }

                is BaseResponse.Error -> {
                    binding.llProgressBar.preload.visibility = View.GONE
                    binding.tvNotif.visibility = View.VISIBLE
                    binding.tvNotif.text = "ERROR"
                }

                is BaseResponse.Notified -> {
                    binding.llProgressBar.preload.visibility = View.GONE
                    binding.tvNotif.visibility = View.VISIBLE
                    binding.tvNotif.text = "NO Response"
                }

                else -> {
                    binding.tvNotif.visibility = View.VISIBLE
                    binding.tvNotif.text = "Something Wrong!"
                    binding.llProgressBar.preload.visibility = View.GONE
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun doLogin() {
        val username = binding.etUser.text.toString()
        val password = binding.etPassword.text.toString()
        if(username.isNotEmpty() && password.isNotEmpty()){
            username.let {
                password.let { viewModel.loginUser(username = username, password = password) }
            }
        }
        else{
            Toast.makeText(this, "Username & Password required!", Toast.LENGTH_LONG).show()
        }
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