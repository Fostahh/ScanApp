package com.mohammadazri.scanapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_profile, container, false)

        var fName:TextView
        var lName:TextView
        var username:TextView

        fName = layout.findViewById(R.id.fName_textView)
        lName = layout.findViewById(R.id.lName_textView)
        username = layout.findViewById(R.id.username_textView)
        val userPicture:ImageView = layout.findViewById(R.id.user_profilePicture)
        val updateProfileButton:Button = layout.findViewById(R.id.updateProfileButton)

        val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val firebaseDatabase:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(
            firebaseUser!!.uid
        )
        val firebaseStorage:StorageReference = FirebaseStorage.getInstance().getReference()

        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val firstName = snapshot.child("firstName").value as String
                    val lastName = snapshot.child("lastName").value as String
                    val userName = snapshot.child("userName").value as String

                    fName?.text = firstName
                    lName?.text = lastName
                    username?.text = userName
                }
            }
        })

        val profileImage = firebaseStorage.child("users/" + firebaseUser.getUid() + "/profile.jpg")
        profileImage.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri?) {
                Picasso.get().load(uri).into(userPicture)
            }
        })

        updateProfileButton.setOnClickListener {
            val myIntent = Intent(activity, ChangeProfileActivity::class.java)
            activity!!.startActivity(myIntent)
        }

        return layout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}