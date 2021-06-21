package com.example.testing

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

const val TAG = "appDebug"
const val REQUEST_IMAGE_CAPTURE = 1
const val LOCATION_INTERVAL = 5000L
const val LOCATION_FASTEST_INTERVAL = 3000L
const val DEVICE_ID = "I3PB1OcmWEfB7MKSxXBK"
const val USERNAME = "rolloapp@rolloapp.fi"
const val PASSWORD = "rolloapp"

fun hasLocationPermissions(context: Context) =
    EasyPermissions.hasPermissions(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )



