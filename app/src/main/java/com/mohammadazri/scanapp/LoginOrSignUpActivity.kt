package com.mohammadazri.scanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginOrSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_or_sign_up)
        supportActionBar?.hide()
    }

    fun goToSignUp(view: View) {
        startActivity(Intent(this@LoginOrSignUpActivity, SignUpActivity::class.java))
    }

    fun goToLoginPage(view: View) {
        startActivity(Intent(this@LoginOrSignUpActivity, LoginActivity::class.java))
    }
}