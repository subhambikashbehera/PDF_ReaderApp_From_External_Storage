package com.vichayan.pdf_readerapp_from_external_storage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.lang.Exception

class PdfRecyclerViewAdapter(private val context: Context, var arrayList: ArrayList<PdfModel>) :
    RecyclerView.Adapter<PdfRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById<TextView>(R.id.pdfText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pdf_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataList = arrayList[position]
        holder.text.text = dataList.pdfName

        holder.itemView.setOnClickListener {
            shared(dataList.uri)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }



    fun shared(file: String){

        val uri: Uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider",File(file))
        val shareIntent = Intent()
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.action = Intent.ACTION_VIEW
        shareIntent.setDataAndType(uri,"application/pdf");
        context.startActivity(shareIntent)

    }
}