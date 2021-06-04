package com.example.testing

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView;
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "appDebug"
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var placeholderAdapter: PlaceholderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        primaryActionBtn.setOnClickListener {
            takePhoto()
        }
        this.transparentStatusBar()
        animZoom()
    }

    fun Activity.transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor = Color.TRANSPARENT
        } else
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    private fun createItems() : List<TestItem> =
        listOf(
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
            TestItem("", 123),
        )

//    private fun initRecyclerView() {
//        placeHolderRv.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
//            placeholderAdapter = PlaceholderAdapter()
//            adapter = placeholderAdapter
//        }
//
//        placeholderAdapter.submitItems(createItems())
//    }

    private fun animZoom() {
        val zoom = AnimationUtils.loadAnimation(this, R.anim.zoom)
        val zoom1 = AnimationUtils.loadAnimation(this, R.anim.zoom1)
        val zoom2 = AnimationUtils.loadAnimation(this, R.anim.zoom2)
        val zoom3 = AnimationUtils.loadAnimation(this, R.anim.zoom3)

        var imgButton = findViewById(R.id.roundButton) as ImageView
        var anim1 = findViewById(R.id.anim1) as ImageView
        var anim2 = findViewById(R.id.anim2) as ImageView
        var anim3 = findViewById(R.id.anim3) as ImageView



        zoom.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                imgButton.alpha = 1f
            }

            override fun onAnimationEnd(animation: Animation?) {
                animation?.start()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                Log.d(TAG, "onAnimationRepeat: ZOOM  REPEAT $animation")
            }
        })
        zoom1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                anim1.alpha = 1f
                Log.d(TAG, "onAnimationStart: ZOOM 1 STARTED")
            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d(TAG, "onAnimationStart: ZOOM1  END")
                animation?.start()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        })
        zoom2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                Log.d(TAG, "onAnimationStart: ZOOM 2 STARTED")
                anim2.alpha = 1f
            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d(TAG, "onAnimationStart: ZOOM2  END")
                animation?.start()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        })
        zoom3.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                Log.d(TAG, "onAnimationStart: ZOOM 3 STARTED")
                anim3.alpha = 1f
            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d(TAG, "onAnimationStart: ZOOM3  END")
                animation?.start()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        })
        imgButton.startAnimation(zoom)
        anim1.startAnimation(zoom1)
      //  anim2.startAnimation(zoom2)
       // anim3.startAnimation(zoom3)
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            this.packageManager?.let { it ->
                takePictureIntent.resolveActivity(it)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }

                    photoFile?.also { file ->
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.testing",
                            file
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: ok")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}