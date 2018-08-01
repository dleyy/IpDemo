package com.example.lilei.iptest

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val imageListFragment by lazy { ImageListFragment() }
    private val STORAGE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    { Manifest.permission.WRITE_EXTERNAL_STORAGE } as Array<String>,
                    STORAGE_REQUEST_CODE)
        } else {
            supportFragmentManager.beginTransaction().add(imageListFragment, "ImageF")
                    .replace(R.id.content, imageListFragment).commit()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    supportFragmentManager.beginTransaction().add(imageListFragment, "ImageF")
                            .replace(R.id.content, imageListFragment).commit()
                } else {
                    Toast.makeText(this, "打开存储权限", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
