package com.example.testing

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.w3c.dom.Text
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var lottieAnim: LottieAnimationView
    private lateinit var userAddressTextView: TextView
    private lateinit var camButton: ImageView
    private lateinit var textGuide: TextView
    private lateinit var currentPhotoPath: String
    private lateinit var randomSentenceView: TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var userLastLat: Double? = 0.0
    private var userLastLon: Double? = 0.0
    private val bundle = Bundle()
    var userToken: String? = null
    var userRefreshToken: String? = null

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        roundButton.setOnClickListener {
            buttonPressAnim()
        }
        this.transparentStatusBar()

        camButton = findViewById<ImageView>(R.id.roundButton)
        textGuide = findViewById(R.id.textGuide)
        userAddressTextView = findViewById(R.id.textView3)
        randomSentenceView = findViewById(R.id.textGuide)
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        geocoder = Geocoder(this)
        requestPermissions()
        startAnimations()
        setRandomSentence()

        if (hasLocationPermissions(this)) {
            setupLocationUpdates()
            userAddressTextView.visibility = View.VISIBLE
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = RetrofitClient.getRetrofitObject()?.create(ApiService::class.java)
                    ?.login(USERNAME, PASSWORD)
                RetrofitClient.token = res?.token

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val locations = p0.locations
            for (location in locations) {
                try {
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    userLastLat = location.latitude
                    userLastLon = location.longitude
                    for (address in addresses) {
                        val completeUserAddress = address.getAddressLine(0).substringBeforeLast(",")
                        val userStreetAddress = completeUserAddress.substringBefore(",")
                        userAddressTextView.text = userStreetAddress
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onRestart() {
        setRandomSentence()
        super.onRestart()
        if (hasLocationPermissions(this)) {
            setupLocationUpdates()
            userAddressTextView.visibility = View.VISIBLE
        } else {
            requestPermissions()
        }
    }

    private fun setRandomSentence(){

        val randomInt = Random().nextInt(7) + 1

        val sentence = when (randomInt) {
            1 -> R.string.sentence_1
            2 -> R.string.sentence_2
            3 -> R.string.sentence_3
            4 -> R.string.sentence_4
            5 -> R.string.sentence_5
            6 -> R.string.sentence_6
            else -> R.string.sentence_7
        }

        randomSentenceView.setText(sentence)
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationUpdates() {
        val request = LocationRequest().apply {
            interval = LOCATION_INTERVAL
            fastestInterval = LOCATION_FASTEST_INTERVAL
            priority = PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun startAnimations() {
        slideUp()
    }

    private fun slideUp() {
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

    private fun playTextPop() {
        val popUp = AnimationUtils.loadAnimation(this, R.anim.text_pop)
        textGuide.startAnimation(popUp)
    }

    private fun playLottie() {
        lottieAnim = findViewById(R.id.primaryActionBtn)
        lottieAnim.setMaxFrame(128)
        lottieAnim.playAnimation()

        lottieAnim.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

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

    private fun playZoom() {
        val zoom = AnimationUtils.loadAnimation(this, R.anim.zoom)
        camButton.startAnimation(zoom)

        zoom.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                playZoom()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun playAnimation() {
        lottieAnim.playAnimation()
    }

    private fun Activity.transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun requestPermissions() {
        if (hasLocationPermissions(this)) {
            userAddressTextView.visibility = View.VISIBLE
            setupLocationUpdates()
            return
        }

        EasyPermissions.requestPermissions(
            this,
            "Käyttääksesi sovellusta sinun täytyy hyväksyä lupa sijainnin käyttöön",
            0,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }


    private fun buttonPressAnim() {
        if (hasLocationPermissions(this)) {
            val openCam = AnimationUtils.loadAnimation(this, R.anim.button_press)
            camButton.startAnimation(openCam)
            openCam.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    takePhoto()
                }

                override fun onAnimationEnd(animation: Animation?) {

                }

                override fun onAnimationRepeat(animation: Animation?) {}

            })
        } else {
            requestPermissions()
        }

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
            val file = File(currentPhotoPath)
            val requestFile: RequestBody =
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            val test = MultipartBody.Part.createFormData("image", file.name, requestFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val res = RetrofitClient.getRetrofitObject()
                        ?.create(ApiService::class.java)
                        ?.postImage(
                            test,
                            "kuvauskenttä -- tähän jotain tulevaisuudessa ehkä?".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            userLastLat.toString()
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            userLastLon.toString()
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        )
                    Log.d(TAG, "onActivityResult: ${res?.imgId}")

                    res?.imgId?.let { imgId ->
                        val telemetryJson = JsonObject()

                        telemetryJson.addProperty("desc", "coming soon...")
                        telemetryJson.addProperty("lat", userLastLat)
                        telemetryJson.addProperty("lon", userLastLon)
                        telemetryJson.addProperty("imgId", imgId)
                        telemetryJson.addProperty("timestamp", System.currentTimeMillis())

                        if (RetrofitClient.token?.isNotEmpty() == true) {
                            RetrofitClient.getRetrofitObject()?.create(ApiService::class.java)
                                ?.saveDeviceAttributes(
                                    DEVICE_ID,
                                    telemetryJson
                                )
                        }
                        showSnackbar(R.string.snackSuccess)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "${e.printStackTrace()}")
                    showSnackbar(R.string.snackSuccess)
                }
            }
        }
    }

    fun showSnackbar(message: Int) {
        val hephoi = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(hephoi, message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.BLACK)
            .setBackgroundTint(resources.getColor(R.color.colorFont))
        snackbar.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
            bundle.putString("currentPhotoPath", currentPhotoPath)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        userAddressTextView.visibility = View.VISIBLE
        setupLocationUpdates()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("Luvat vaaditaan")
                .setRationale("Sovellus ei toimi ilman pyydettyjä lupia. Avaa sovelluksen asetukset muuttaaksesi lupia.")
                .build()
                .show()
            userAddressTextView.visibility = View.GONE
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}

