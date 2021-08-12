package com.example.reba.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.reba.R
import com.example.reba.data.PointsData
import com.example.reba.mlKit.PoseAnalyzer
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlinx.android.synthetic.main.fragment_camera.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    companion object {
        var listPoseCamera = mutableListOf<Pose>()
        var cameraDataClass : PointsData? = PointsData(listPoseCamera)
    }
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var textView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //textView = view!!.findViewById(R.id.text_view_id)
        cameraExecutor = Executors.newSingleThreadExecutor()
        var i: Int = 0
        startCamera()
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

//
//    fun getAngle(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {
//
//        var result = Math.toDegrees(
//            atan2( lastPoint.getPosition().y.toDouble() - midPoint.getPosition().y,
//            lastPoint.getPosition().x.toDouble() - midPoint.getPosition().x)
//                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
//            firstPoint.getPosition().x - midPoint.getPosition().x)
//        )
//        result = Math.abs(result) // Angle should never be negative
//        if (result > 180) {
//            result = 360.0 - result // Always get the acute representation of the angle
//        }
//        return result
//    }

//    fun getNeckAngle(
//        orecchio: PoseLandmark, spalla: PoseLandmark
//    ): Double {
//
//        var result = Math.toDegrees(
//            atan2( spalla.getPosition().y.toDouble() - spalla.getPosition().y,
//            (spalla.getPosition().x + 100 ).toDouble() - spalla.getPosition().x)
//                - atan2(orecchio.getPosition().y - spalla.getPosition().y,
//            orecchio.getPosition().x - spalla.getPosition().x)
//        )
//
//        result = Math.abs(result) // Angle should never be negative
//
//        if (result > 180) {
//            result = 360.0 - result // Always get the acute representation of the angle
//        }
//        return result
//    }

    var i: Int = 0
    private fun onPoseFound(pose: Pose)  {
        try {
            cameraDataClass?.listPose?.add(pose)
            // val allPoseLandmarks = pose.allPoseLandmarks

            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            if(leftShoulder != null)
                Log.i("CAM", "$i found")
            else
                Log.i("CAM", "$i notFound")
            i += 1


            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
            val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
            val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
            val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
            val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
            val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
            val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
            val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
            val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
            val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)

            val occhioSx = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
            val occhioDx = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);

            val orecchioDx = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
            val orecchioSx = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);


            val builder = StringBuilder()
            rect_overlay.clear()

            // disegno il collo come la media tra occhi e orecchie
            if( occhioSx != null && occhioDx != null && leftShoulder != null && rightShoulder != null  ){
                rect_overlay.drawNeck(occhioSx, occhioDx, leftShoulder, rightShoulder);
            }

//            // disegno il collo visto lateralmente da sinistra
//            if(orecchioSx != null && leftShoulder != null){
//                rect_overlay.drawLine(orecchioSx, leftShoulder)
//                var angoloCollo = getNeckAngle(orecchioSx, leftShoulder);
//                builder.append("${90 - angoloCollo.toInt()} collo (da sx) \n")
//            }
//
//            // disegno il collo visto lateralmente da destra
//            if(orecchioDx != null && rightShoulder != null){
//                rect_overlay.drawLine(orecchioDx, rightShoulder)
//                var angoloCollo = getNeckAngle(orecchioDx, rightShoulder);
//                builder.append("${90 - angoloCollo.toInt()} collo (da dx) \n")
//            }
//
//            // angolo busto destra
//            if(rightShoulder != null && rightHip != null && rightKnee != null){
//                var angoloBusto = getAngle(rightShoulder, rightHip, rightKnee);
//                builder.append("${ 180 - angoloBusto.toInt()} busto (da dx) \n")
//            }
//
//            // angolo busto sinistra
//            if(leftShoulder != null && leftHip != null && leftKnee != null){
//                var angoloBusto = getAngle(leftShoulder, leftHip, leftKnee);
//                builder.append("${180 - angoloBusto.toInt()} busto (da sx) \n")
//            }
//
//
//            // angolo gamba destra
//            if( rightHip != null && rightKnee != null  && rightAnkle != null){
//                var angoloBusto = getAngle( rightHip, rightKnee, rightAnkle);
//                builder.append("${ 180 - angoloBusto.toInt()} gamba (da dx) \n")
//            }
//
//            // angolo gamba sinistra
//            if( leftHip != null && leftKnee != null  && leftAnkle != null){
//                var angoloBusto = getAngle( leftHip, leftKnee,leftAnkle);
//                builder.append("${ 180 - angoloBusto.toInt()} gamba (da sx) \n")
//            }


            if(leftShoulder != null && rightShoulder != null){
                rect_overlay.drawLine(leftShoulder, rightShoulder)
            }

            if(leftHip != null &&  rightHip != null){
                rect_overlay.drawLine(leftHip, rightHip)
            }

            if(leftShoulder != null &&  leftElbow != null){
                rect_overlay.drawLine(leftShoulder, leftElbow)
            }

            if(leftElbow != null &&  leftWrist != null){
                rect_overlay.drawLine(leftElbow, leftWrist)
            }

            if(leftShoulder != null &&  leftHip != null){
                rect_overlay.drawLine(leftShoulder, leftHip)
            }

            if(leftHip != null &&  leftKnee != null){
                rect_overlay.drawLine(leftHip, leftKnee)
            }

            if(leftKnee != null &&  leftAnkle != null){
                rect_overlay.drawLine(leftKnee, leftAnkle)
            }

            if(leftWrist != null &&  leftThumb != null){
                rect_overlay.drawLine(leftWrist, leftThumb)
            }

            if(leftWrist != null &&  leftPinky != null){
                rect_overlay.drawLine(leftWrist, leftPinky)
            }

            if(leftWrist != null &&  leftIndex != null){
                rect_overlay.drawLine(leftWrist, leftIndex)
            }

            if(leftIndex != null &&  leftPinky != null){
                rect_overlay.drawLine(leftIndex, leftPinky)
            }

            if(leftAnkle != null &&  leftHeel != null){
                rect_overlay.drawLine(leftAnkle, leftHeel)
            }

            if(leftHeel != null &&  leftFootIndex != null){
                rect_overlay.drawLine(leftHeel, leftFootIndex)
            }

            if(rightShoulder != null &&  rightElbow != null){
                rect_overlay.drawLine(rightShoulder, rightElbow)
            }

            if(rightElbow != null &&  rightWrist != null){
                rect_overlay.drawLine(rightElbow, rightWrist)
            }

            if(rightShoulder != null &&  rightHip != null){
                rect_overlay.drawLine(rightShoulder, rightHip)
            }

            if(rightHip != null &&  rightKnee != null){
                rect_overlay.drawLine(rightHip, rightKnee)
            }

            if(rightKnee != null &&  rightAnkle != null){
                rect_overlay.drawLine(rightKnee, rightAnkle)
            }

            if(rightWrist != null &&  rightThumb != null){
                rect_overlay.drawLine(rightWrist, rightThumb)
            }

            if(rightWrist != null &&  rightPinky != null){
                rect_overlay.drawLine(rightWrist, rightPinky)
            }

            if(rightWrist != null &&  rightIndex != null){
                rect_overlay.drawLine(rightWrist, rightIndex)
            }

            if(rightIndex != null &&  rightPinky != null){
                rect_overlay.drawLine(rightIndex, rightPinky)
            }

            if(rightAnkle != null &&  rightHeel != null){
                rect_overlay.drawLine(rightAnkle, rightHeel)
            }

            if(rightHeel != null &&  rightFootIndex != null){
                rect_overlay.drawLine(rightHeel, rightFootIndex)
            }


            //textView.setText("${builder.toString()}")

        } catch (e: java.lang.Exception) {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
    }



    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, PoseAnalyzer(::onPoseFound))
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

//    private fun getOutputDirectory(): File {
//        val mediaDir = externalMediaDirs.firstOrNull()?.let {
//            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
//        return if (mediaDir != null && mediaDir.exists())
//            mediaDir else filesDir
//    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}