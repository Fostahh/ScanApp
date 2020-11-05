package com.mohammadazri.scanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChangeEmailActivity : AppCompatActivity() {

    private var email:EditText?=null
    private var password:EditText?=null
    private var newEmail:EditText?=null
    private var updateButton:Button?=null
    private var firebaseAuth:FirebaseAuth?=null
    private var user:FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)

        email = findViewById(R.id.user_email_changePage)
        password = findViewById(R.id.user_password_changePage)
        newEmail = findViewById(R.id.new_user_email_changePage)
        updateButton = findViewById(R.id.update_btn)
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth?.currentUser

        updateButton?.setOnClickListener {
            updateEmail()
        }
    }

    private fun updateEmail() {
        val emailText = email?.text.toString().trim()
        val passwordText = password?.text.toString().trim()
        val newEmailText = newEmail?.text.toString().trim()

        if(TextUtils.isEmpty(emailText)) {
            Toast.makeText(applicationContext,"Please enter your current email address", Toast.LENGTH_SHORT).show()
        } else if(TextUtils.isEmpty(passwordText)) {
            Toast.makeText(applicationContext,"Please enter your password", Toast.LENGTH_SHORT).show()
        } else if(TextUtils.isEmpty(newEmailText)) {
            Toast.makeText(applicationContext,"Please enter your new email address", Toast.LENGTH_SHORT).show()
        } else {
            val userInfo = EmailAuthProvider.getCredential(emailText,passwordText)

            user?.reauthenticate(userInfo)?.addOnCompleteListener(object:OnCompleteListener<Void>{
                override fun onComplete(task: Task<Void>) {
                    user!!.updateEmail(newEmailText).addOnCompleteListener(object:OnCompleteListener<Void>{
                        override fun onComplete(task: Task<Void>) {
                            if(task.isSuccessful) {
                                Toast.makeText(applicationContext, "Update Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@ChangeEmailActivity, UserProfileActivity::class.java))
                            } else {
                                val error = task.exception?.message
                                Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }

            })
        }

    }

}