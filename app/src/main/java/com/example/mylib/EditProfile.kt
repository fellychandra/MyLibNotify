package com.example.mylib

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mylib.databinding.ActivityGantipasswordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_gantipassword.*
import kotlinx.android.synthetic.main.activity_masuk.*

class EditProfile: AppCompatActivity() {
    private lateinit var binding: ActivityGantipasswordBinding
    private var firebaseDataBase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null


    var noinduk=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGantipasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noinduk = intent.getStringExtra("no_induk").toString()
        edit()
    }

    private fun edit() {
        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("ap")
        databaseReference?.child(noinduk)?.get()?.addOnSuccessListener {

            val pass : String = it.child("password").value.toString()
            binding.edtnim.setText(it.child("no_induk").value.toString())
            binding.edtnama.setText(it.child("nama_lengkap").value.toString())
            binding.edtemail.setText(it.child("email").value.toString())
            //binding.edtpasslama.setText(it.child("password").value.toString())


            binding.edtnim.isEnabled=false
            binding.edtnama.isEnabled=false
            binding.edtemail.isEnabled=false
            //binding.edtpasslama.isEnabled=false


            binding.btnsimpan.setOnClickListener{
                if(binding.edtpasslama.length() < 8 ){
                    binding.edtpasslama.requestFocus()
                    binding.edtpasslama.error = "Password Kurang dari 8 Karakter"
                }
                if(binding.edtpassbaru.length() < 8 ){
                    binding.edtpassbaru.requestFocus()
                    binding.edtpassbaru.error = "Password Kurang dari 8 Karakter"
                }else{
                    if (binding.edtpasslama.text.toString().equals(pass)){
                        databaseReference?.child(noinduk)?.child("password")?.setValue(binding.edtpassbaru.text.toString())
                        val intent = Intent(applicationContext, ProfileActivity::class.java).apply {
                            putExtra("apa","babanana")
                            putExtra("no_induk",noinduk)
                        }
                        Toast.makeText(applicationContext, "Password Berhasil diupdate", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        true
                    }else{
                        Toast.makeText(applicationContext, "Password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            //Toast.makeText(applicationContext, "${it.value.toString()}", Toast.LENGTH_SHORT).show()
        }
    }
}