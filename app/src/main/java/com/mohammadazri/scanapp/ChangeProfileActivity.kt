package com.mohammadazri.scanapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.IOException

class ChangeProfileActivity : AppCompatActivity() {

    private var firstName: EditText? = null
    private var lastName: EditText? = null
    private var userName: EditText? = null
    private var updateButton: Button? = null
    private var userPicture: ImageView?=null
    private var imageUri: Uri?=null

    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null
    private var firebaseDatabase: DatabaseReference? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        firstName = findViewById(R.id.user_fname)
        lastName = findViewById(R.id.user_lname)
        userName = findViewById(R.id.user_username)
        updateButton = findViewById(R.id.user_profile_btn)
        userPicture = findViewById(R.id.user_profilePicture)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage!!.getReference()

        setImageProfile()

        updateButton?.setOnClickListener {
            saveUserInfo()
        }
    }

    private fun setImageProfile() {
        val profileImage = storageReference!!.child("users/"+ firebaseUser!!.getUid()+"/profile.jpg")
        profileImage.getDownloadUrl().addOnSuccessListener(object: OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri?) {
                Picasso.get().load(uri).into(userPicture)
            }
        })
    }

    private fun saveUserInfo() {
        val fName = firstName?.text.toString().trim()
        val lName = lastName?.text.toString().trim()
        val username = userName?.text.toString().trim()

        if (TextUtils.isEmpty(fName)) {
            Toast.makeText(applicationContext, "Please enter your first name", Toast.LENGTH_SHORT)
                    .show()
        } else if (TextUtils.isEmpty(lName)) {
            Toast.makeText(applicationContext, "Please enter your last name", Toast.LENGTH_SHORT)
                    .show()
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(applicationContext, "Please enter your username", Toast.LENGTH_SHORT)
                    .show()
        } else {
            val userInfo = HashMap<String, Any>()
            userInfo.put("firstName", fName)
            userInfo.put("lastName", lName)
            userInfo.put("userName", username)

            firebaseDatabase?.updateChildren(userInfo)?.addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Your profile has been updated", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ChangeProfileActivity, UserProfileActivity::class.java))
                    } else {
                        val error = task.exception?.message
                        Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }

    }

    fun openGallery(view: View) {
        val gallery = Intent()
        gallery.type = "image/*"
        gallery.action = Intent.ACTION_PICK
        startActivityForResult(gallery, 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            try {
                val image = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                uploadImageToFirebase(imageUri)
            } catch (error: IOException) {
                Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            //If You want to make the file name to randomize by Android Studio type this
            //val fileRef = storageReference?.child(UUID.randomUUID().toString())

            val fileRef = storageReference?.child("users/" + firebaseUser!!.getUid() + "/profile.jpg")
            fileRef?.putFile(imageUri)
                    ?.addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                        override fun onComplete(task: Task<UploadTask.TaskSnapshot>) {
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "Image Uploaded", Toast.LENGTH_SHORT)
                                        .show()
                                fileRef.getDownloadUrl().addOnSuccessListener(object :
                                        OnSuccessListener<Uri> {
                                    override fun onSuccess(uri: Uri?) {
                                        Picasso.get().load(uri).into(userPicture)
                                    }

                                })
                            } else {
                                val error = task.exception?.message
                                Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT)
                                        .show()
                            }
                        }
                    })
        }
    }
}