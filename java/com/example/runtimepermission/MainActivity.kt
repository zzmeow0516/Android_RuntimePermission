package com.example.runtimepermission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val TAG = "mylog_RuntimePermission"

    private val requestCode_CALL_PHONE = 1
    private val requestCode_READ_CONTACTS = 2

    private val contactArray = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonMakeCall = findViewById<Button>(R.id.makeCall)
        buttonMakeCall.setOnClickListener {
            try {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CALL_PHONE), requestCode_CALL_PHONE)
                } else {
                    call()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //初始化adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactArray)
        //使用adapter
        val listviewContact = findViewById<ListView>(R.id.contactsView)
        listviewContact.adapter = adapter

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),
                requestCode_READ_CONTACTS)
        } else {
            readContact()
        }

    }

    //ActivityCompat.requestPermissions的结果都会回调到onRequestPermissionsResult这个方法
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            requestCode_CALL_PHONE ->
                //授权结果竟然是个数组，负责一次性传输多个授权结果？好像也行，反正就几个权限，也都写在一块了
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call()
                } else {
                    Toast.makeText(this, "$permissions[0] have not been granted!",
                        Toast.LENGTH_SHORT).show()
                }

            requestCode_READ_CONTACTS ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContact()
                } else {
                    Toast.makeText(this, "$permissions[0] have not been granted!",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun call() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:10086")
        startActivity(intent)
    }

    private fun readContact() {
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
            null, null)?.apply {
                while (moveToNext()) {
                    val contactName = getString(getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val contactNumber = getString(getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    Log.v(TAG, "contactName = $contactName, contactNumber = $contactNumber")
                    contactArray.add("$contactName\n $contactNumber")
                }
            adapter.notifyDataSetChanged()
            close()
        }
    }

}