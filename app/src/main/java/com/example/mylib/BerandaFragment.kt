package com.example.mylib

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mylib.adapter.BukuAdapter
import com.example.mylib.databinding.FragmentBerandaBinding
import com.example.mylib.model.Buku
import com.google.firebase.database.*
import java.sql.Date
import java.text.SimpleDateFormat


class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null
    private var databaseReference1: DatabaseReference? = null

    private var list = mutableListOf<Buku>()

    private var adapter: BukuAdapter? = null

    private var noinduk=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        noinduk = getActivity()?.getIntent()?.getStringExtra("no_induk").toString()
        //Toast.makeText(activity, "nim yang dilempar di fragment $noinduk", Toast.LENGTH_LONG).show()

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("meminjam")
        databaseReference1 = firebaseDataBase?.getReference("ap")
        getData()

        binding.profile.setOnClickListener{
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra("apa","babanana")
                putExtra("no_induk",noinduk)
            }
            context?.startActivity(intent)
            true
        }
    }

    private fun initRecyclerView() {
        adapter = BukuAdapter()
        binding.apply {
            recyclerView.layoutManager =  LinearLayoutManager(activity)
            recyclerView.adapter = adapter
        }

        adapter?.setOnClickView {
            Toast.makeText(activity,"${it.id}", Toast.LENGTH_SHORT).show()

        }

    }

    private fun getData() {
        databaseReference?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Log.e("oooo", "oDataChange :$snapshot")
                databaseReference1?.child(noinduk)?.get()?.addOnSuccessListener {
                    var noduk = it.child("no_induk").value.toString()
                    //Toast.makeText(activity, "${noduk}", Toast.LENGTH_SHORT).show()

                    val status = "Meminjam"
                    list.clear()
                    for (ds in snapshot.children){
                        if (status.equals(ds.child("status").value.toString()) and  noduk.equals(ds.child("no_induk").value.toString())){

                            //Toast.makeText(activity, "${noduk}", Toast.LENGTH_SHORT).show()

                            val id = ds.key
                            val kode_buku = ds.child("kode_buku").value.toString()
                            val judul = ds.child("judul").value.toString()
                            val no_induk = ds.child("no_induk").value.toString()
                            val tanggal_peminjaman = ds.child("tanggal_peminjaman").value.toString()
                            val tanggal_pengembalian = ds.child("tanggal_pengembalian").value.toString()

                            //UBAH MILLIS KE BENTUK TANGGAL
                            val bentuk = SimpleDateFormat("dd MMM yyyy")
                            val hasilpengembalian = Date(tanggal_pengembalian.toLong())


                            //val date = Date(tanggal_pengembalian.toLong())
                            //Log.e("convert","size:${sdf.format(date)} ")

                            val buku = Buku(id = id, kode_buku = kode_buku,
                                judul = judul,
                                no_induk = no_induk,
                                tanggal_peminjaman = tanggal_peminjaman ,
                                tanggal_pengembalian = bentuk.format(hasilpengembalian).toString())
                            list.add(buku)
                        }else{

                        }


                    }
                    Log.e("ooooo","size:${list.size} ")
                    adapter?.setItems(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("oooo", "onCancelled :${error.toException()}")
            }
        })

    }
}
