package com.example.reba.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.reba.R
import com.example.reba.data.PointsData
import com.google.mlkit.vision.pose.Pose

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if(!allPermissionsGranted()) {
//            ActivityCompat.requestPermissions(
//                this, MainActivity.REQUIRED_PERMISSIONS, MainActivity.REQUEST_CODE_PERMISSIONS
//            )
//        }

        setContentView(R.layout.activity_main)

        /**
         * Video Fragment
         */
//        val listVideoData = mutableListOf<Pose>()
//        val videoPoseData = PointsData(listVideoData)
//        val bundle = Bundle()
//        bundle.putParcelable("videoDataClass", videoPoseData)
        val videoFragment = VideoFragment()
        //videoFragment.arguments = bundle

        /**
         * Camera Fragment
         */
        val cameraFragment = CameraFragment()

        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().replace(R.id.fragmentCamera, cameraFragment).commit()
        fm.beginTransaction().replace(R.id.fragmentVideo, videoFragment).commit()

    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this,
                    "Permissions granted by the user.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}