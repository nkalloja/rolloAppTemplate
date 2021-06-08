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
import android.view.animation.Animation
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieListener
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext


class MainActivity : AppCompatActivity() {
    
    private lateinit var lottieAnim: LottieAnimationView


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

//        applyBlurMaskFilter()
        animZoom()
        lottieAnim = findViewById(R.id.primaryActionBtn)
        lottieAnim.playAnimation()
        lottieAnim.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                lottieAnim.setMaxFrame(128)
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

//    private fun blurEffect(){


//        var blurryImage = findViewById<ImageView>(R.id.backgroundColor1)
//        Blurry.with(this)
//            .radius(100)
//            .sampling(2)
//            .onto(blurryImage)

//        blurryImage.post {
//            Blurry.with(this)
//                .radius(25)
//                .sampling(2)
//                .async()
//                .animate(300)
//                .onto(blurryImage)
//        }
//        var blurryImage = findViewById<ImageView>(R.id.backgroundColor1)
//        Blurry.with(this)
//            .radius(25)
//            .sampling(4)
//            .animate(500)
//            .async()
//            .onto(blurryImage)
//
//        Blurry.with(this)
//            .radius(25)
//            .sampling(1)
////            .color(Color.argb(66, 0, 255, 255))
//            .async()
////            .capture(findViewById(R.id.backgroundImage))
//            .onto(findViewById(R.id.backgroundImage))


//        val radius = 20f
//
//        val decorView = window.decorView
//        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
//        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
//        val rootView = decorView.findViewById<View>(R.id.backgroundImage) as ImageView
//        //Set drawable to draw in the beginning of each blurred frame (Optional).
//        //Can be used in case your layout has a lot of transparent space and your content
//        //gets kinda lost after after blur is applied.
//        //Set drawable to draw in the beginning of each blurred frame (Optional).
//        //Can be used in case your layout has a lot of transparent space and your content
//        //gets kinda lost after after blur is applied.
//        val windowBackground = decorView.backgroundImage
//
//        blurView.setupWith(rootView)
//            .setFrameClearDrawable(windowBackground)
//            .setBlurAlgorithm(RenderScriptBlur(this))
//            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)
//            .setHasFixedTransformationMatrix(true) // Or false if it's in a scrolling container or might be animated
//
//    private fun applyBlurMaskFilter() {
////        var blurImage = findViewById<ImageView>(R.id.backgroundImage)
//////        blurImage.(applyBlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL)
//        backgroundImage.paint.maskFilter = BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL)
//        backgroundImage.postInvalidate()
//    }

    private fun buttonPressAnim(){
        val openCam = AnimationUtils.loadAnimation(this, R.anim.button_press)
        var camButton = findViewById(R.id.roundButton) as ImageView
        camButton.startAnimation(openCam)
    }
    private fun animZoom() {
        val zoom = AnimationUtils.loadAnimation(this, R.anim.zoom)
//        val zoom1 = AnimationUtils.loadAnimation(this, R.anim.zoom1)
//        val zoom2 = AnimationUtils.loadAnimation(this, R.anim.zoom2)
//        val zoom3 = AnimationUtils.loadAnimation(this, R.anim.zoom3)

        var imgButton = findViewById(R.id.roundButton) as ImageView
//        val anim1 = this.findViewById(R.id.anim1) as ImageView
//        val anim2 = findViewById(R.id.anim2) as ImageView
//        val anim3 = findViewById(R.id.anim3) as ImageView

//
//        zoom.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {
//                imgButton.alpha = 1f
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                animation?.start()
//            }
//
//            override fun onAnimationRepeat(animation: Animation?) {
//                Log.d(TAG, "onAnimationRepeat: ZOOM  REPEAT $animation")
//            }
//        })
//        zoom1.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {
//                anim1.alpha = 1f
//                Log.d(TAG, "onAnimationStart: ZOOM 1 STARTED")
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                Log.d(TAG, "onAnimationStart: ZOOM1  END")
//                animation?.start()
//            }
//
//            override fun onAnimationRepeat(animation: Animation?) {
//                TODO("Not yet implemented")
//            }
//        })
//        zoom2.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {
//                Log.d(TAG, "onAnimationStart: ZOOM 2 STARTED")
//                anim2.alpha = 1f
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                Log.d(TAG, "onAnimationStart: ZOOM2  END")
//                animation?.start()
//            }
//
//            override fun onAnimationRepeat(animation: Animation?) {
//                TODO("Not yet implemented")
//            }
//        })
//        zoom3.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {
//                Log.d(TAG, "onAnimationStart: ZOOM 3 STARTED")
//                anim3.alpha = 1f
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                Log.d(TAG, "onAnimationStart: ZOOM3  END")
//                animation?.start()
//            }
//
//            override fun onAnimationRepeat(animation: Animation?) {
//                TODO("Not yet implemented")
//            }
//        })
        imgButton.startAnimation(zoom)
//        anim1.startAnimation(zoom1)
//        anim2.startAnimation(zoom2)
//        anim3.startAnimation(zoom3)

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

