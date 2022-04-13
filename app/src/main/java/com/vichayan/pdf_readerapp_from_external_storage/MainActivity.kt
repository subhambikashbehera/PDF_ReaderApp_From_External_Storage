package com.vichayan.pdf_readerapp_from_external_storage

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var pdfRecyclerViewAdapter: PdfRecyclerViewAdapter
    lateinit var recyclerView:RecyclerView
    var pdfList = arrayListOf<PdfModel>()
    private val dirs: File = Environment.getExternalStorageDirectory()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView=findViewById(R.id.recyclerView)
        initializeRecyclerView()

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            resultLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            showPdf(dirs)
        }





    }

    private fun initializeRecyclerView(){
        recyclerView.layoutManager=LinearLayoutManager(this)
        pdfRecyclerViewAdapter= PdfRecyclerViewAdapter(this,pdfList)
        recyclerView.adapter=pdfRecyclerViewAdapter
        pdfRecyclerViewAdapter.notifyDataSetChanged()
    }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                showPdf(dirs)
            } else {
                Toast.makeText(this, "permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showPdf(dirs: File) {
        val pattern = ".pdf"
        val filesDirsList: ArrayList<File> = dirs.listFiles()!!.toCollection(ArrayList())

        val thread = object : Thread() {
            override fun run() {
                for (i in 0 until filesDirsList.size) {
                    if (filesDirsList[i].isDirectory) {
                        showPdf(filesDirsList[i])
                    } else {
                        if (filesDirsList[i].name.endsWith(pattern)) {
                            pdfList.add(PdfModel(filesDirsList[i].absolutePath,filesDirsList[i].name))
                        }
                    }
                }
            }
        }.start()
    }
}