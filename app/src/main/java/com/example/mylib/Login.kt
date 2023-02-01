package com.example.mylib

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mylib.databinding.ActivityMasukBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_masuk.*

class Login : AppCompatActivity(){
    lateinit var binding : ActivityMasukBinding

    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasukBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("ap")

         login()
    }

    private fun login() {
        btn_masuk.setOnClickListener(View.OnClickListener {
            val nip: String = TextNIPNIM.text.toString()
            val pass: String = TextPassword.text.toString()

            if (nip.isEmpty()) {
                TextNIPNIM.error = "No Induk tidak boleh kosong"
                TextNIPNIM.requestFocus()
            } else if (pass.isEmpty()) {
                TextPassword.error = "Password tidak boleh kosong"
                TextPassword.requestFocus()
            }else if(pass.length < 8){
                TextPassword.error = "Password kurang dari 8 karakter"
                TextPassword.requestFocus()
            }
            else{
                databaseReference?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children){
                            //////////////////////
                            if (nip.equals(ds.child("no_induk").value.toString())){
                                val id = ds.key
                                val nips = ds.child("no_induk").value.toString()
                                val passwords = ds.child("password").value.toString()
                                //Toast.makeText(applicationContext, "nimssssss $nips", Toast.LENGTH_LONG).show()
                                if (pass.equals(passwords)){
                                    val intent = Intent(this@Login, MainActivity::class.java).apply {
                                        putExtra("no_induk", nip)
                                    }
                                    startActivity(intent)
                                }else{
                                    TextPassword.error = "Paasword anda salah!"
                                    TextPassword.requestFocus()
                                }
                            }else{
                            }

                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        })
    }
}