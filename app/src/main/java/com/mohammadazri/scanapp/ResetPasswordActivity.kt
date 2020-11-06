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
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private var userEmailEditText:EditText?=null
    private var resetButton:Button?=null
    private var firebaseAuth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        userEmailEditText = findViewById(R.id.user_email_resetPage)
        resetButton = findViewById(R.id.reset_btn)
        firebaseAuth = FirebaseAuth.getInstance()

        resetButton?.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        var email_text = userEmailEditText?.text.toString().trim()
        if(TextUtils.isEmpty(email_text)) {
            Toast.makeText(applicationContext, "Please enter your email address", Toast.LENGTH_SHORT).show()
        } else {
            firebaseAuth?.sendPasswordResetEmail(email_text)?.addOnCompleteListener(object:OnCompleteListener<Void>{
                override fun onComplete(task: Task<Void>) {

                    if(task.isSuccessful){
                        Toast.makeText(applicationContext, "Please check your email", Toast.LENGTH_SHORT).show()
                    } else {
                        val error = task.exception?.message
                        Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
    }

    fun goToLoginPage(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun goToSignUpPage(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}