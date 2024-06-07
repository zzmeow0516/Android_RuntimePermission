package com.example.runtimepermission

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val buttonMakeCall = findViewById<Button>(R.id.makeCall)
        buttonMakeCall.setOnClickListener {
            try {
                //提示需要权限requires android.permission.CALL_PHONE
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:10086")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}