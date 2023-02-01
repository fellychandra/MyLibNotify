package com.example.mylib

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.mylib.adapter.MeminjamAdapter
import com.example.mylib.databinding.ActivityMasukpasswordBinding
import com.example.mylib.databinding.ActivityScannerBinding
import com.example.mylib.model.Ap
import com.example.mylib.model.Buku
import com.example.mylib.model.Meminjam
import com.example.mylib.model.Riwayat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_masukpassword.view.*

class Scanner() : AppCompatActivity() {

    lateinit var binding : ActivityScannerBinding
    lateinit var codeScanner : CodeScanner
    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference1: DatabaseReference? = null
    private var databaseReference2: DatabaseReference? = null
    private var databaseReference3: DatabaseReference? = null
    private var kodeBuku: String? = null

//    private var list = mutableListOf<Meminjam>()
//    private var adapter: MeminjamAdapter? = null
    var noinduk = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference1 = firebaseDataBase?.getReference("buku")
        databaseReference2 = firebaseDataBase?.getReference("ap")
        databaseReference3 = firebaseDataBase?.getReference("meminjam")


        codeScanner()
        setPermission()



    }

    private fun codeScanner() {
        noinduk = intent.getStringExtra("no_induk").toString()
        Toast.makeText(applicationContext, "$noinduk", Toast.LENGTH_SHORT).show()
//        //buku
        var kode = ""
//        var judul =""
//        var norak = ""
//        var pengarang = ""
//
//        //ap
        var email=""
        var namalengkap=""

        var password = ""
        var tahunterdaftar = ""
//
//        //meminjam
//        var status = ""
        var tanggalpeminjaman = System.currentTimeMillis()
        var tanggalpengembalian = tanggalpeminjaman+604800000

        codeScanner = CodeScanner(this, binding.scanner)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    binding.tvOutput.text = it.text

                    kode = it.text
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }

            binding.scanner.setOnClickListener {
                codeScanner.startPreview()
            }
        }

        var arrayList: ArrayList<String> = arrayListOf()
        var id : String? = null
        var a : Int = 0
        databaseReference1?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    id = ds.key
                    arrayList.add(id.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("oooo", "onCancelled :${error.toException()}")
            }
        })

        binding.btnOke.setOnClickListener {
            var i : Int = 0
            //Toast.makeText(applicationContext, "idnya ${kode}" , Toast.LENGTH_SHORT).show()
            while (i < arrayList.size) {
                if (arrayList[i].equals(kode)) {
                    a = 1

                    //Toast.makeText(applicationContext, "Pliz bs y" , Toast.LENGTH_SHORT).show()
                    break
                }
                i++
            }
            if (a == 1) {
                val builder = AlertDialog.Builder(this@Scanner)
                val inflater = LayoutInflater.from(this@Scanner)
                val view = inflater.inflate(R.layout.activity_masukpassword, null)
                builder.setView(view)
                val alert = builder.create()
                alert.show()
                view.btnsimpan.setOnClickListener {
                    databaseReference2?.child(noinduk)?.get()?.addOnSuccessListener {
                        if (it.exists()) {
                            val pass = it.child("password").value.toString()
                            val inputpass = view.editmasukpass.text.toString()
                            email = it.child("email").value.toString()
                            namalengkap = it.child("nama_lengkap").value.toString()
                            noinduk = it.child("no_induk").value.toString()

                            Toast.makeText(applicationContext,"${namalengkap}", Toast.LENGTH_SHORT).show()
                            if (pass.equals(inputpass)) {
                                //Toast.makeText(applicationContext, "OK" , Toast.LENGTH_SHORT).show()
                                databaseReference1?.child(kode)?.get()?.addOnSuccessListener {
                                    //it.child("judul").value.toString()
                                    //Toast.makeText(applicationContext, "" , Toast.LENGTH_SHORT).show()
                                    //AP
                                    val emails = email
                                    val namalengkaps= namalengkap
                                    val passwords=pass
                                    val tahunterdaftars= 2020
                                    val noinduks= noinduk

                                    //BUKU
                                    val juduls=it.child("judul").value.toString()
                                    val kodebukus=kode
                                    val noraks= it.child("no_rak").value.toString()
                                    val pengarangs = it.child("pengarang").value.toString()

                                    //MEMINJAM
                                    val statuss= "Mengajukan"
                                    val tanggalpeminjamans = tanggalpeminjaman
                                    val tanggalpengembalians = tanggalpengembalian
                                    val perpanjangan = "false"

                                    view.btnsimpan.setOnClickListener { v ->
                                        val meminjam = Meminjam(
                                            kodebukus.toString(),
                                            juduls.toString(),
                                            pengarangs.toString(),
                                            noraks.toString(),

                                            noinduks.toString(),
                                            emails.toString(),
                                            namalengkaps.toString(),
                                            passwords.toString(),
                                            tahunterdaftars.toString(),

                                            statuss.toString(),
                                            tanggalpeminjamans.toString(),
                                            tanggalpengembalians.toString(),
                                            perpanjangan.toString()
                                        )



                                        val idmemimjam = databaseReference3?.push()!!.key

                                        //databaseReference3?.child(idmemimjam!!)?.child("perpanjangan")
                                        databaseReference3?.child(idmemimjam!!)?.setValue(meminjam)
                                        Toast.makeText(applicationContext, "Pengajuan berhasil" , Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                            }
                            else {
                                Toast.makeText(applicationContext, "Password salah ygy" , Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            Toast.makeText(applicationContext, "G OK" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.startPreview()
    }

    private fun setPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeReq()
        }
    }

    private fun makeReq() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA), 101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            101 -> {
                if (grantResults. isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Dibutuhkan", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

