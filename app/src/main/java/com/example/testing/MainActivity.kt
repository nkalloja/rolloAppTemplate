package com.example.testing

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BlurMaskFilter
import android.os.*
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieListener
import com.example.testing.R.id.primaryActionBtn
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext


class MainActivity : AppCompatActivity() {
    
    private lateinit var lottieAnim: LottieAnimationView
    private lateinit var camButton: ImageView
    private lateinit var textGuide: TextView


    companion object {
        const val TAG = "appDebug"
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var placeholderAdapter: PlaceholderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roundButton.setOnClickListener {
            buttonPressAnim()
            takePhoto()
        }
        this.transparentStatusBar()

        camButton = findViewById<ImageView>(R.id.roundButton)
        textGuide = findViewById(R.id.textGuide)

        startAnimations()

    }

    private fun startAnimations(){
        slideUp()

    }

    private fun slideUp(){
        val buttonBackground = findViewById<ImageView>(R.id.button_background)

        val slide = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        buttonBackground.startAnimation(slide)

        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                playZoom()
                playTextPop()
            }
            override fun onAnimationEnd(animation: Animation?) {
                playLottie()
            }
            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }

    private fun playTextPop(){

        val popUp = AnimationUtils.loadAnimation(this, R.anim.text_pop)


        textGuide.startAnimation(popUp)

    }

    private fun playLottie(){
        lottieAnim = findViewById(R.id.primaryActionBtn)
        lottieAnim.setMaxFrame(128)

        lottieAnim.playAnimation()

        lottieAnim.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(5000)
                    lottieAnim.setMinFrame(30)
                    playAnimation()
                }

            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }

    private fun playZoom(){
        val zoom = AnimationUtils.loadAnimation(this, R.anim.zoom)
        camButton.startAnimation(zoom)

        zoom.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                Log.d(TAG, "onAnimationStart: ZOOM STARTED")
            }
            override fun onAnimationEnd(animation: Animation?) {
                playZoom()
                Log.d(TAG, "onAnimationStart: ZOOM ENDED")
            }
            override fun onAnimationRepeat(animation: Animation?)  {
            }
        })
    }

    private fun playAnimation() {
        lottieAnim.playAnimation()
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


    private fun buttonPressAnim(){
        val openCam = AnimationUtils.loadAnimation(this, R.anim.button_press)
        camButton.startAnimation(openCam)
    }

    private fun animZoom() {
        val zoom = AnimationUtils.loadAnimation(this, R.anim.zoom)
        var imgButton = findViewById(R.id.roundButton) as ImageView

        imgButton.startAnimation(zoom)

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

