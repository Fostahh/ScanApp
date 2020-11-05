package com.mohammadazri.scanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private var userEmail: EditText?=null
    private var userPassword:EditText?=null
    private var login: Button?=null
    private var firebaseAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userEmail = findViewById(R.id.user_email_login)
        userPassword = findViewById(R.id.user_password_login)
        login = findViewById(R.id.loginPage_btn)
        firebaseAuth = FirebaseAuth.getInstance()

        login?.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = userEmail?.text.toString().trim()
        val password = userPassword?.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext,"This field cannot be empty!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext,"This field cannot be empty!", Toast.LENGTH_SHORT).show()
        } else {
            firebaseAuth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener(object:
                OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful) {
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                        val user: FirebaseUser = firebaseAuth!!.currentUser!!

                        if(user.isEmailVerified) {
                            startActivity(Intent(this@LoginActivity, UserProfileActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Account hasn't been verified", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val error = task.exception?.message
                        Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
    }

    fun resetPassword(view: View) {
        startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
    }

    fun goToSignUpPage(view: View) {
        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
    }
}