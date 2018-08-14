package com.example.lilei.iptest

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lilei.iptest.config.ImagePickerConfig
import com.example.lilei.iptest.interfaces.ImageLoader

class MainActivity : AppCompatActivity() {
    private val imageListFragment by lazy { ImageListFragment() }
    private val STORAGE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var configBuilder = ImagePickerConfig.Builder(this, ImageLoader
        { context, imagePath, imageView -> Glide.with(context).load(imagePath).asBitmap().into(imageView) })
        configBuilder.needCamera(false)
        Constant.config = configBuilder.build()


        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    permissions,
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
                    AlertDialog.Builder(this)
                            .setTitle("存储权限不可用").setMessage("please open it")
                            .setPositiveButton("open") { _, _ -> goToAppSetting() }
                            .setNegativeButton("cancel") { _, _ -> finish() }
                            .setCancelable(false).show()
                }
        }
    }

    // 跳转到当前应用的设置界面
    // 仅flame  ... be simple
    private fun goToAppSetting() {
        val intent = Intent();
        intent.action = "com.meizu.safe.security.SHOW_APPSEC";
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra("packageName", packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityForResult(intent, 123);
    }

    override fun onBackPressed() {
        if (imageListFragment.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

}
