package com.example.mylib.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.model.Riwayat
import org.w3c.dom.Text

class RiwayatAdapter: RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    private  var riwayatList = mutableListOf<Riwayat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.custom_list_riwayat, parent,false)
        return RiwayatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val riwayat = riwayatList[position]
        holder.setItem(riwayat)
    }

    override fun getItemCount(): Int {
        return riwayatList.size
    }

    fun setItems(list: MutableList<Riwayat>){
        this.riwayatList =list
        notifyDataSetChanged()
    }

    class RiwayatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var tvJudulBuku: TextView? = null
        private var tvTanggalPengembalian: TextView? = null
        private var tvKeterangan: TextView?= null

        private var actionView: ImageView? = null
        private var onClickView:((View)->Unit)? = null

        fun setItem(data: Riwayat){
            tvJudulBuku = itemView.findViewById(R.id.tx_judul_buku)
            tvTanggalPengembalian = itemView.findViewById(R.id.tx_tanggal_pengembalian)
            tvKeterangan = itemView.findViewById(R.id.tx_status)

            tvJudulBuku?.text = data.judul
            tvKeterangan?.text = data.keterangan
            tvTanggalPengembalian?.text = data.tanggal_pengembalian
        }
    }
}