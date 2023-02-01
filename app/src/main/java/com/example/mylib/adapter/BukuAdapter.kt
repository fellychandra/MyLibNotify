package com.example.mylib.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.DetailPeminjamanActivity
import com.example.mylib.R
import com.example.mylib.model.Buku
import kotlinx.android.synthetic.main.custom_list.view.*

class BukuAdapter:RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    private  var bukuList = mutableListOf<Buku>()

    private var onClickView: ((Buku) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.custom_list, parent,false)
        return BukuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = bukuList[position]
        holder.setItem(buku)

        holder.setOnClickView {
            onClickView?.invoke(it)
        }

        holder.nJudulBukuView.text = buku.judul
        holder.nTanggalPengembalian.text = buku.tanggal_pengembalian


        holder.itemView.setOnClickListener(){ v ->
            val intent = Intent(v.context, DetailPeminjamanActivity::class.java).apply {
                putExtra("id", buku.id)
                putExtra("kode_buku", buku.kode_buku)
                putExtra("judul", buku.judul)
                putExtra("tanggal_peminjaman", buku.tanggal_peminjaman)
                putExtra("tanggal_pengembalian", buku.tanggal_pengembalian)
            }
            v.context.startActivity(intent)

            true
//            Toast.makeText(v.context,"click action view ${buku.id}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return bukuList.size
    }

    fun setItems(list: MutableList<Buku>){
        this.bukuList =list
        notifyDataSetChanged()
    }

    fun setOnClickView(callback:(Buku)-> Unit ){
        this.onClickView = callback
    }

    class BukuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var tvJudulBuku: TextView? = null
        private var tvTanggalPengembalian: TextView? = null

        private var actionView: ImageView? = null
        private var onClickView:((Buku)->Unit)? = null
        private var tvDetail: TextView?= null

        val nJudulBukuView: TextView = itemView.tx_judul_buku
        val nTanggalPengembalian: TextView = itemView.tx_tanggal_pengembalian

        fun setItem(data: Buku){
            tvJudulBuku = itemView.findViewById(R.id.tx_judul_buku)
            tvTanggalPengembalian = itemView.findViewById(R.id.tx_tanggal_pengembalian)
            actionView = itemView.findViewById(R.id.shapeableImageView)
            tvDetail = itemView.findViewById(R.id.tx_lihat_detail)


            tvJudulBuku?.text = data.judul
            tvTanggalPengembalian?.text = data.tanggal_pengembalian


            actionView?.setOnClickListener{
                onClickView?.invoke(data)}
            tvDetail?.setOnClickListener{
                onClickView?.invoke(data)
            }
        }
        fun setOnClickView(callback:(Buku)-> Unit ){
            this.onClickView = callback
        }
    }
}