package com.example.mylib

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mylib.adapter.MeminjamAdapter
import com.example.mylib.adapter.RiwayatAdapter
import com.example.mylib.databinding.FragmentPinjamBinding
import com.example.mylib.model.Meminjam
import com.example.mylib.model.Riwayat
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class PinjamFragment : Fragment() {

    private lateinit var binding: FragmentPinjamBinding

    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null
    private var list = mutableListOf<Meminjam>()

    private var adapter: MeminjamAdapter? = null
    var noinduk=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinjamBinding.inflate(inflater, container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shapeableImageView.setOnClickListener {
            scanQRCode()

        }

        initRecyclerView()

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("meminjam")

        noinduk = getActivity()?.getIntent()?.getStringExtra("no_induk").toString()
        getData()
        //Toast.makeText(activity, "fragment pinjam $noinduk", Toast.LENGTH_LONG).show()
    }
    private fun scanQRCode() {
        val intent = Intent(context, Scanner::class.java).apply {
            putExtra("no_induk",noinduk)
        }

        startActivity(intent)
    }

    private fun initRecyclerView() {
        adapter = MeminjamAdapter()
        binding.apply {
            recyclerViewScanner.layoutManager =  LinearLayoutManager(activity)
            recyclerViewScanner.adapter = adapter
        }
    }

    private fun getData() {
        databaseReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = "Mengajukan"
                list.clear()
                for (ds in snapshot.children){
                    if (status.equals(ds.child("status").value.toString()) and noinduk.equals(ds.child("no_induk").value.toString()) ){
                        val id = ds.key

                        val kodebuku = ds.child("kode_buku").value.toString()
                        val judul = ds.child("judul").value.toString()
                        val pengarang = ds.child("pengarang").value.toString()
                        val norak = ds.child("no_rak").value.toString()

                        val noinduk = ds.child("no_induk").value.toString()
                        val email = ds.child("email").value.toString()
                        val namalengkap = ds.child("nama_lengkap").value.toString()
                        val password = ds.child("password").value.toString()
                        val tahunterdaftar = ds.child("tahun_terdaftar").value.toString()

                        val status = ds.child("status").value.toString()
                        val tanggalpeminjaman = ds.child("tanggal_peminjaman").value.toString()
                        val tanggalpengembalian = ds.child("tanggal_pengembalian").value.toString()


                        //UBAH MILLIS KE BENTUK TANGGAL
                        val bentuk = SimpleDateFormat("dd MMM yyyy")
                        val hasilpengembalian = Date(tanggalpengembalian.toLong())


                        val meminjam = Meminjam(
                            kode_buku = kodebuku,
                            judul = judul,
                            pengarang = pengarang,
                            no_rak = norak,

                            no_induk = noinduk,
                            email = email,
                            nama_lengkap = namalengkap,
                            password = password,
                            tahun_terdaftar = tahunterdaftar,

                            status = status,
                            tanggal_peminjaman = tanggalpeminjaman ,
                            tanggal_pengembalian = bentuk.format(hasilpengembalian).toString())
                        list.add(meminjam)
                    }
                }
                Log.e("aaaaaaa","size:${list.size} ")
                adapter?.setItems(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}