package com.mohammadazri.scanapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddProductActivity : AppCompatActivity() {

    private val cameraRequestCode = 101
    private var codeScanner: CodeScanner?=null
    private var scannerView: CodeScannerView?=null
    private var productName: EditText? = null
    private var productCode:TextView?=null
    private var productExpireDate: EditText? = null
    private var addProductButton: Button? = null
    
    private var firebaseDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        scannerView = findViewById(R.id.scanner_view)
        productName = findViewById(R.id.product_name)
        productExpireDate = findViewById(R.id.product_expired)
        addProductButton = findViewById(R.id.addProduct_btn)
        productCode = findViewById(R.id.product_code)

        setupPermission()
        codeScanner()

        addProductButton?.setOnClickListener {
            addProductToFirebase()
        }
    }

    private fun addProductToFirebase() {
        val code = productCode?.text.toString().trim()
        val name = productName?.text.toString().trim()
        val expired = productExpireDate?.text.toString().trim()

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(applicationContext, "Please scan a product!", Toast.LENGTH_SHORT)
                .show()
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(applicationContext, "Please fill the product name!", Toast.LENGTH_SHORT)
                .show()
        } else if (TextUtils.isEmpty(expired)) {
            Toast.makeText(applicationContext, "Please fill the expire date of the product!", Toast.LENGTH_SHORT).show()
        } else {
            val productInfo = HashMap<String, Any>()
            productInfo.put("code", code)
            productInfo.put("name", name)
            productInfo.put("expireDate", expired)

            firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Product").child(code)

            firebaseDatabase?.updateChildren(productInfo)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Data has been added",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@AddProductActivity, ProfileFragment::class.java))
                        } else {
                            val error = task.exception?.message
                            Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                })

        }
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission!= PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            cameraRequestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when(requestCode) {
            cameraRequestCode -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this, "You need the camera permission to be able to use this feature!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
                    productCode!!.text = it.text
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

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }
}