package com.vichayan.pdf_readerapp_from_external_storage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PdfRecyclerViewAdapter(private val context: Context, var arrayList: ArrayList<PdfModel>): RecyclerView.Adapter<PdfRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val text:TextView=itemView.findViewById<TextView>(R.id.pdfText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val view=LayoutInflater.from(context).inflate(R.layout.pdf_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val dataList=arrayList[position]
      holder.text.text=dataList.pdfName
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }
}