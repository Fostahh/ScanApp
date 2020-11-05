package com.mohammadazri.scanapp

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.firebase.database.*

class CheckProductActivity : AppCompatActivity() {

    private val cameraRequestCode = 101
    
    private var codeScanner:CodeScanner?=null
    private var scannerView:CodeScannerView?=null
    private var productCode:TextView?=null
    private var productName:TextView?=null
    private var productExpireDate:TextView?=null

    private var firebaseDatabase:DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_product)

        scannerView = findViewById(R.id.scanner_view)
        productCode = findViewById(R.id.check_product_code)
        productName = findViewById(R.id.check_product_name)
        productExpireDate = findViewById(R.id.check_product_expire)

        productCode?.visibility = View.GONE
        productName?.visibility = View.GONE
        productExpireDate?.visibility = View.GONE

        setupPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scannerView!!)

        codeScanner?.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    productCode?.text = it.text
                    showProductDetail()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera Initialization Error: ${it.message}")
                }
            }
        }

        scannerView?.setOnClickListener {
            codeScanner?.startPreview()
        }
    }

    private fun showProductDetail() {
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Product").child(productCode?.text as String)
        firebaseDatabase?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("name").value as String
                    val expireDate = snapshot.child("expireDate").value as String

                    productName?.text = name
                    productExpireDate?.text = expireDate

                    productCode?.visibility = View.VISIBLE
                    productName?.visibility = View.VISIBLE
                    productExpireDate?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission!= PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), cameraRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            cameraRequestCode -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the camera permission to be able to use this feature!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}