package com.mohammadazri.scanapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class UserProfileActivity : AppCompatActivity() {

    private var fName:TextView?=null
    private var lName:TextView?=null
    private var username:TextView?=null
    private var userPicture:ImageView?=null
    private var scanner:Button?=null
    private var addProduct:Button?=null

    private var firebaseAuth:FirebaseAuth?=null
    private var firebaseDatabase:DatabaseReference?=null
    private var firebaseUser:FirebaseUser?=null
    private var firebaseStorage:FirebaseStorage?=null
    private var storageReference:StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        fName = findViewById(R.id.fName_textView)
        lName = findViewById(R.id.lName_textView)
        username = findViewById(R.id.username_textView)
        userPicture = findViewById(R.id.user_profilePicture)
        scanner = findViewById(R.id.scanner_btn)
        addProduct = findViewById(R.id.addProduct_btn)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage!!.getReference()

        setImageProfile()
        profileInformation()
    }

    private fun profileInformation() {
        firebaseDatabase?.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val firstName = snapshot.child("firstName").value as String
                    val lastName = snapshot.child("lastName").value as String
                    val userName = snapshot.child("userName").value as String

                    fName?.text = firstName
                    lName?.text = lastName
                    username?.text = userName
                }
            }
        })
    }

    private fun setImageProfile() {
        var profileImage = storageReference!!.child("users/"+ firebaseUser!!.getUid()+"/profile.jpg")
        profileImage?.getDownloadUrl()?.addOnSuccessListener(object: OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri?) {
                Picasso.get().load(uri).into(userPicture)
            }
        })
    }

    fun changeProfile(view: View) {
        startActivity(Intent(this@UserProfileActivity, ChangeProfileActivity::class.java))
    }

    fun changeEmail(view: View) {
        startActivity(Intent(this@UserProfileActivity, ChangeEmailActivity::class.java))
    }

    fun goToAddProductPage(view: View) {
        startActivity(Intent(this@UserProfileActivity, AddProductActivity::class.java))
    }

    fun goToCheckProductPage(view: View) {
        startActivity(Intent(this@UserProfileActivity, CheckProductActivity::class.java))
    }
}