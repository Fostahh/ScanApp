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

class SignUpActivity : AppCompatActivity() {

    private var signUp: Button?= null
    private var userEmail: EditText?=null
    private var userPassword:EditText?=null
    private var firebaseAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUp = findViewById(R.id.signUp_btn)
        userEmail = findViewById(R.id.user_email)
        userPassword = findViewById(R.id.user_password)
        firebaseAuth = FirebaseAuth.getInstance()

        signUp?.setOnClickListener {
            registerAccount()
        }
    }

    fun goToLoginPage(view: View) {
        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
    }

    private fun registerAccount() {
        val email = userEmail?.text.toString().trim()
        val password = userPassword?.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext,"This field cannot be empty!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext,"This field cannot be empty!", Toast.LENGTH_SHORT).show()
        } else {
            firebaseAuth?.createUserWithEmailAndPassword(email,password)?.addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Account has been created!", Toast.LENGTH_SHORT).show()
                        val user:FirebaseUser = firebaseAuth!!.currentUser!!
                        user.sendEmailVerification().addOnCompleteListener(object:
                            OnCompleteListener<Void> { override fun onComplete(task: Task<Void>) {
                            if(task.isSuccessful) {
                                Toast.makeText(applicationContext, "Please check your email", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            } else {
                                val error = task.exception?.message
                                Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
                            }
                        }

                        })
                    } else {
                        val error = task.exception?.message
                        Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
    }

}