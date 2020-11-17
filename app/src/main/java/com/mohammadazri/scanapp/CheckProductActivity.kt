package com.mohammadazri.scanapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
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
import com.google.firebase.database.*

class CheckProductActivity : AppCompatActivity() {

    private val cameraRequestCode = 101
    private var codeScanner: CodeScanner? = null
    private var scannerView: CodeScannerView? = null
    private var qrbarcodeBarang: TextView? = null
    private var nupBarang: TextView? = null
    private var kodeBarang: TextView? = null
    private var tempatRuanganBarang: TextView? = null
    private var tahunPerolehanBarang: TextView? = null
    private var kondisiBarang: EditText? = null
    private var updateProduct: Button? = null
    private var checkAgain:Button?=null

    private var firebaseDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_product)

        scannerView = findViewById(R.id.scanner_view)
        qrbarcodeBarang = findViewById(R.id.qrbarcodeCheckProduct)
        nupBarang = findViewById(R.id.nupCheckProduct)
        kodeBarang = findViewById(R.id.kodeBarangCheckProduct)
        tempatRuanganBarang = findViewById(R.id.tempatRuanganCheckProduct)
        tahunPerolehanBarang = findViewById(R.id.tahunPerolehanCheckProduct)
        kondisiBarang = findViewById(R.id.kondisiBarangEditable)
        updateProduct = findViewById(R.id.updateCheckProduct)

        nupBarang?.visibility = View.GONE
        kodeBarang?.visibility = View.GONE
        tahunPerolehanBarang?.visibility = View.GONE
        tempatRuanganBarang?.visibility = View.GONE
        kondisiBarang?.visibility = View.GONE

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
                    qrbarcodeBarang?.text = it.text
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
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Product").child(qrbarcodeBarang?.text.toString().trim())
        firebaseDatabase?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nup = snapshot.child("NUP").value as String
                    val kode = snapshot.child("kodeBarang").value as String
                    val kondisi = snapshot.child("kondisiBarang").value as String
                    val thnPeroleh = snapshot.child("tahunPeroleh").value as String
                    val tmptRuangan = snapshot.child("tempatRuangan").value as String

                    nupBarang?.text = nup
                    kodeBarang?.text = kode
                    kondisiBarang?.setText(kondisi)
                    tahunPerolehanBarang?.text = thnPeroleh
                    tempatRuanganBarang?.text = tmptRuangan

                    nupBarang?.visibility = View.VISIBLE
                    kodeBarang?.visibility = View.VISIBLE
                    kondisiBarang?.visibility = View.VISIBLE
                    tahunPerolehanBarang?.visibility = View.VISIBLE
                    tempatRuanganBarang?.visibility = View.VISIBLE

                    updateProduct?.setOnClickListener {
                        if (kondisiBarang?.text.toString().trim() != kondisi) {
                            val updateKondisi = HashMap<String, Any>()
                            updateKondisi.put("kondisiBarang", kondisiBarang?.text.toString().trim())
                            firebaseDatabase?.updateChildren(updateKondisi)?.addOnCompleteListener(object:OnCompleteListener<Void>{
                                override fun onComplete(task: Task<Void>) {
                                    if (task.isSuccessful) {
                                        Toast.makeText(applicationContext, "Kondisi barang berhasil diupdate", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(applicationContext, MainMenuActivity::class.java))
                                    } else {
                                        val error = task.exception?.message
                                        Toast.makeText(applicationContext, "Error " + error, Toast.LENGTH_SHORT).show()
                                    }
                                }

                            })
                        } else {
                            Toast.makeText(applicationContext, "Update kondisi barang terlebih dahulu!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, CheckProductActivity::class.java))
                        }
                    }
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

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), cameraRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            cameraRequestCode -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Anda harus memberi izin camera untuk menggunakan fitur ini", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}