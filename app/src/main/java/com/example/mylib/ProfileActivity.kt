package com.example.mylib

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mylib.databinding.ActivityAkunBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ProfileActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAkunBinding
    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null
    private lateinit var storage: StorageReference
    private lateinit var bitmap: Bitmap
    private lateinit var localFile:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var noinduk = intent.getStringExtra("no_induk").toString()
        var nama = ""
        var email = ""

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("ap")
        databaseReference?.child(noinduk)?.get()?.addOnSuccessListener {

            val link : String = it.child("foto").value.toString()
            storage = FirebaseStorage.getInstance().getReference().child(link)
            localFile = File.createTempFile("fellyy", "jpg")
            storage.getFile(localFile).addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                var image = findViewById<ImageView>(R.id.imageView)
                image.setImageBitmap(bitmap)
                }

            nama = it.child("nama_lengkap").value.toString()
            email = it.child("email").value.toString()
            noinduk = it.child("no_induk").value.toString()

            binding.txnama.text = nama
            binding.txemail.text = email
            binding.txnoinduk.text = noinduk

        }

        binding.btnedit.setOnClickListener{
            val intent = Intent(applicationContext, EditProfile::class.java).apply {
                putExtra("no_induk",noinduk)
            }
            startActivity(intent)
            true
        }
    }
}