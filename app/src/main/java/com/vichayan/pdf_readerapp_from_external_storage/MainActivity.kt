package com.vichayan.pdf_readerapp_from_external_storage

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
                android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                   resultLauncher1.launch(intent)
                } catch (e: Exception) {
                  e.printStackTrace()
                }
            } else {
                resultLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
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


    private val resultLauncher1=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (it.resultCode==Activity.RESULT_OK) {
                showPdf(dirs)
            } else {
                Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}