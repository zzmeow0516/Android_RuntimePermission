package com.example.runtimepermission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val TAG = "mylog_RuntimePermission"

    private val requestCode_CALL_PHONE = 1

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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call()
                } else {
                    Toast.makeText(this, "permission have not been granted!",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun call() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:10086")
        startActivity(intent)
    }
}