package com.example.lilei.iptest

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lilei.iptest.config.ImagePickerConfig
import com.example.lilei.iptest.interfaces.ImageLoader
import com.example.lilei.iptest.interfaces.OnImageClickedListener
import com.example.lilei.iptest.model.Image
import com.example.lilei.iptest.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(), OnImageClickedListener {

    private val imageListFragment by lazy { ImageListFragment() }
    private val STORAGE_REQUEST_CODE = 1
    private val CAMERA_REQUEST_CODE = 2
    private val IMAGE_CROP_CODE = 3
    private val REQUEST_CAMERA = 4;
    private var cropImagePath = ""
    private var cameraFile = File("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        initConfigs()

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

    //创建config
    private fun initConfigs() {
        var configBuilder = ImagePickerConfig.Builder(this, ImageLoader
        { context, imagePath, imageView -> Glide.with(context).load(imagePath).asBitmap().into(imageView) })
        configBuilder.needCamera(true)
        Constant.config = configBuilder.build()
    }

    private fun initViews() {
        iv_back.setOnClickListener { finish() }

        b_sure.setOnClickListener {
            if (Constant.selectedImg.size == 1) {
                crop(Constant.selectedImg[0].path)
            }
        }
    }

    fun crop(imagePath: String) {
        val file = File(Utils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg")
        cropImagePath = file.absolutePath
        var intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(getImageContentUri(File(imagePath)), "image/*")
        intent.putExtra("crop", true)
        intent.putExtra("aspectX", Constant.config.aspectX)
        intent.putExtra("aspectY", Constant.config.aspectY)
        intent.putExtra("outputX", Constant.config.outputX)
        intent.putExtra("outputY", Constant.config.outputY)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)
        startActivityForResult(intent, IMAGE_CROP_CODE)
    }

    private fun getImageContentUri(imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                return null
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraFile = File(Utils.createRootPath(this) + "/"
                + System.currentTimeMillis() + ".jpg")
        Utils.createFile(cameraFile)
        //authority 在Manifest中已经注册
        val imgUri = FileProvider.getUriForFile(this, "ip.provider", cameraFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }

    private fun showBigImages(imagePath: String) {

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
            CAMERA_REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

    override fun onImageClicked(position: Int, image: Image) {
        val path = image.path
        if (!Constant.config.needCamera) {
            showBigImages(path)
        } else {
            if (position == 0) {
                checkCameraPermission()
            } else {
                showBigImages(path)
            }
        }
    }

    override fun onImageChecked(position: Int) {
        when (Constant.selectedImg.size) {
            0 -> {
                b_sure.text = "确定"
                b_sure.isClickable = false
            }

            in 1..9 -> {
                b_sure.isClickable = true
                b_sure.text = String.format(resources.getString(R.string.sure_btn_text),
                        Constant.selectedImg.size, Constant.config.maxNum)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_CROP_CODE -> {
                    Log.e("ok", "passImagePath =" +
                            if (Constant.selectedImg.isEmpty())
                                "null"
                            else
                                Constant.selectedImg[0].path
                    )

                    Log.e("ok", "passImagePath =" + "\n"
                            + "cropImagePath" + cropImagePath)
                }

                REQUEST_CAMERA -> {
                    crop(cameraFile.toString())
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            openCamera()
        }
    }

}
