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
    private var qrbarcodeBarang:TextView?=null
    private var kodeBarang:EditText?=null
    private var tahunPerolehan: EditText? = null
    private var nup: EditText? = null
    private var tempatRuangan:EditText?=null
    private var kondisiBarang:EditText?=null
    private var addProductButton: Button? = null


    private var firebaseDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        scannerView = findViewById(R.id.scanner_view)
        qrbarcodeBarang = findViewById(R.id.qrbarcodeAddProduct)
        kodeBarang = findViewById(R.id.kodeBarangAddProduct)
        tahunPerolehan = findViewById(R.id.tahunPerolehanAddProduct)
        nup = findViewById(R.id.nupAddProduct)
        tempatRuangan = findViewById(R.id.tempatRuanganAddProduct)
        kondisiBarang = findViewById(R.id.kondisibarangAddProduct)
        addProductButton = findViewById(R.id.addProductButton)

        setupPermission()
        codeScanner()

        addProductButton?.setOnClickListener {
            addProductToFirebase()
        }
    }

    private fun addProductToFirebase() {
        val qrbarcode = qrbarcodeBarang?.text.toString().trim()
        val kodeBrg = kodeBarang?.text.toString().trim()
        val thnPeroleh = tahunPerolehan?.text.toString().trim()
        val nomorUrutP = nup?.text.toString().trim()
        val tmptRuangan = tempatRuangan?.text.toString().trim()
        val kondisiBrg = kondisiBarang?.text.toString().trim()

        if (TextUtils.isEmpty(qrbarcode)) {
            Toast.makeText(applicationContext, "Scan Barang Terlebih Dahulu", Toast.LENGTH_SHORT)
                    .show()
        } else if (TextUtils.isEmpty(kodeBrg)) {
            Toast.makeText(applicationContext, "Isi Kolom Kode Barang Terlebih Dahulu", Toast.LENGTH_SHORT)
                .show()
        } else if (TextUtils.isEmpty(thnPeroleh)) {
            Toast.makeText(applicationContext, "Isi Kolom Tahun Peroleh Terlebih Dahulu", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(nomorUrutP)) {
            Toast.makeText(applicationContext, "Isi Kolom NUP Terlebih Dahulu", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(tmptRuangan)) {
            Toast.makeText(applicationContext, "Isi Kolom Tempat Ruangan Terlebih Dahulu", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(kondisiBrg)) {
            Toast.makeText(applicationContext, "Isi Kolom Kondisi Barang Terlebih Dahulu", Toast.LENGTH_SHORT).show()
        } else {
            val productInfo = HashMap<String, Any>()
            productInfo.put("kodeBarang", kodeBrg)
            productInfo.put("tahunPeroleh", thnPeroleh)
            productInfo.put("NUP", nomorUrutP)
            productInfo.put("tempatRuangan", tmptRuangan)
            productInfo.put("kondisiBarang", kondisiBrg)

            firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Product").child(qrbarcode)

            firebaseDatabase?.updateChildren(productInfo)?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Data telah ditambahkan", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AddProductActivity, AddProductActivity::class.java))
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
                        this, "Anda harus memberi izin camera untuk menggunakan fitur ini",
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
                    qrbarcodeBarang!!.text = it.text
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