package com.klinik.antrean.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.klinik.antrean.R
import com.klinik.antrean.databinding.ActivityRegisterBinding
import com.klinik.antrean.helper.UserHelper
import com.klinik.antrean.response.BaseResponse
import com.klinik.antrean.ui.viewModel.UserViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var context: Context? = null
    private val viewModel by viewModels<UserViewModel>()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        context = this.applicationContext
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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

        binding.etName.setOnTouchListener { v, _ ->
            v.isFocusable = false
            v.isFocusableInTouchMode = true
            false
        }
        binding.etPassword.hint = "Nama Lengkap"

        binding.etHakakses.setOnTouchListener { v, _ ->
            v.isFocusable = false
            v.isFocusableInTouchMode = true
            false
        }
        binding.etPassword.hint = "Hak Akses"

        binding.etKdklinik.setOnTouchListener { v, _ ->
            v.isFocusable = false
            v.isFocusableInTouchMode = true
            false
        }
        binding.etPassword.hint = "Kode Klinik"

        binding.etKdcabang.setOnTouchListener { v, _ ->
            v.isFocusable = false
            v.isFocusableInTouchMode = true
            false
        }
        binding.etPassword.hint = "Kode Cabang"

        binding.etEmail.setOnTouchListener { v, _ ->
            v.isFocusable = false
            v.isFocusableInTouchMode = true
            false
        }
        binding.etPassword.hint = "Email"

        binding.btnRegister.setOnClickListener {
            if (isNetworkAvailable(this)) {
                doRegister()
            }
            else{
                Toast.makeText(this, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        viewModel.createResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.llProgressBar.preload.visibility = View.VISIBLE
                }

                is BaseResponse.Success -> {
                    var message = UserHelper().processCreate(it.data, this)
                    registerSuccess(message)
                }

                is BaseResponse.Denied -> {
                    binding.llProgressBar.preload.visibility = View.GONE
                    binding.tvNotif.visibility = view.visibility
                    binding.tvNotif.text = UserHelper().processDenied(it.data)
                }

                is BaseResponse.Error -> {
                    binding.llProgressBar.preload.visibility = View.GONE
                    binding.tvNotif.visibility = View.VISIBLE
                    binding.tvNotif.text = it.msg
                }

                is BaseResponse.Notified -> {
                    binding.llProgressBar.preload.visibility = View.GONE
                    binding.tvNotif.visibility = View.VISIBLE
                    binding.tvNotif.text = it.msg
                }

                else -> {
                    binding.tvNotif.visibility = View.VISIBLE
                    binding.tvNotif.text = "Something Wrong!"
                    binding.llProgressBar.preload.visibility = View.GONE
                }
            }
        }
    }

    private fun registerSuccess(message: String?) {
        val dialogBuilder = MaterialAlertDialogBuilder(this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setMessage(HtmlCompat.fromHtml("<big>Register Berhasil!</big><br/>", HtmlCompat.FROM_HTML_MODE_LEGACY))
        dialogBuilder.setPositiveButton("Ok"){ _, _ ->
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = 900
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        alertDialog.window!!.attributes = layoutParams
    }

    private fun doRegister() {
        val username = binding.etUser.text.toString()
        val password = binding.etPassword.text.toString()
        val name = binding.etName.text.toString()
        val akses = binding.etHakakses.text.toString()
        val klinik = binding.etKdklinik.text.toString()
        val cabang = binding.etKdcabang.text.toString()
        val email = binding.etEmail.text.toString()
        if(username.isNotEmpty()
            && password.isNotEmpty()
            && name.isNotEmpty()
            && akses.isNotEmpty()
            && klinik.isNotEmpty()
            && cabang.isNotEmpty()
            && email.isNotEmpty()
        ){
            username.let {
                password.let { viewModel.createUser(username = username,
                    password = password,
                    name = name,
                    akses = akses,
                    klinik = klinik,
                    cabang = cabang,
                    email = email
                ) }
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