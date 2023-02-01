package com.example.mylib.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.model.Meminjam

class MeminjamAdapter: RecyclerView.Adapter<MeminjamAdapter.MeminjamViewHolder>() {

    private  var meminjamList = mutableListOf<Meminjam>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeminjamViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.custom__list_scanner, parent,false)
        return MeminjamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeminjamViewHolder, position: Int) {
        val meminjam = meminjamList[position]
        holder.setItem(meminjam)
    }

    override fun getItemCount(): Int {
        return meminjamList.size
    }

    fun setItems(list: MutableList<Meminjam>){
        this.meminjamList =list
        notifyDataSetChanged()
    }

    class MeminjamViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var tvJudulBuku: TextView? = null
        private var tvTanggalPengembalian: TextView? = null
        private var tvStatus: TextView?=null

        fun setItem(data: Meminjam){
            tvJudulBuku = itemView.findViewById(R.id.tx_judul_buku)
            tvTanggalPengembalian = itemView.findViewById(R.id.tx_tanggal_pengembalian)
            tvStatus = itemView.findViewById(R.id.tx_status)


            tvJudulBuku?.text = data.judul
            tvTanggalPengembalian?.text = data.tanggal_pengembalian
            tvStatus?.text= data.status
        }
    }
}