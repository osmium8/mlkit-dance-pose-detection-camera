package com.example.reba.ui

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView

import android.graphics.Bitmap
import android.os.Environment
import android.widget.Button
import com.example.reba.data.CaptureBitmap
import com.example.reba.data.PointsData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import wseemann.media.FFmpegMediaMetadataRetriever
import android.content.Intent
import com.example.reba.R


class VideoFragment() : Fragment() {

    companion object {
        var listPose = mutableListOf<Pose>()
        var dataClass : PointsData? = PointsData(listPose)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflater = inflater.inflate(R.layout.fragment_video, container, false)
        var videoView: VideoView = inflater.findViewById(R.id.video_view)
        // Inflate the layout for this fragment
        val mediaController = MediaController(context)

        val offlineUri = Uri.parse("android.resource://com.example.reba/${R.raw.spiderman_dance}")

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(offlineUri)
        videoView.requestFocus()
        videoView.start()

        //val videoFile = File(offlineUri.toString())
        val mediaMetaDataRetriever  = MediaMetadataRetriever()
        //mediaMetaDataRetriever.setDataSource(videoFile.absolutePath)
        mediaMetaDataRetriever.setDataSource(context, offlineUri)

        val mmr = FFmpegMediaMetadataRetriever()
        Log.i("VID", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/video.mp4")
        mmr.setDataSource(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/video.mp4")

        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
        val poseDetector = PoseDetection.getClient(options)

        /**
         * @listPose will be passed to our dataclass instance
         */
        val listPose: MutableList<Pose> = mutableListOf()

        val captureBitmap = CaptureBitmap()
        // list of poses 15 @ 4fps
        for(i in 0..8) {
            for(j in 0..750000 step 250000) {

                val time = i*1000000L + j

                /**
                 * Get BITMAP
                 */
                //val nonArgbBitmap = mediaMetaDataRetriever.getFrameAtTime(time)//, OPTION_CLOSEST_SYNC)
                val nonArgbBitmap = mmr.getFrameAtTime(time, FFmpegMediaMetadataRetriever.OPTION_CLOSEST)
                //val nonArgbBitmap = captureBitmap.captureFrame(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/video.mp4", time/1000, 300, 500)

                /**
                 * Convert to ARGB_8888
                 */
                val argbBitmap = nonArgbBitmap?.copy(Bitmap.Config.ARGB_8888,true)
                val image = InputImage.fromBitmap(argbBitmap, 0)
                //saveTempBitmap(argbBitmap)

                //Log.i("VID", "${time/1000000} ..... above $i $j")
                /**
                 * MLkit PoseDetection
                 */
                poseDetector.process(image).addOnSuccessListener { pose ->
                    dataClass?.listPose?.add(pose)
                    onPoseFound(pose)
                    }.addOnFailureListener { error ->
                        Log.d("VID", "Failed to process the image")
                        error.printStackTrace()
                    }
            }
            Log.i("VID", "${dataClass!!.listPose}")
        }

        val button = inflater.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val myIntent = Intent(activity, Result::class.java)
            activity!!.startActivity(myIntent)
        }

        return inflater
    }

    //    fun argb8888(src: Bitmap): Bitmap? {
//        return Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
//    }

    var i: Int = 0
    private fun onPoseFound(pose: Pose)  {
        try {
            //Log.i("VID", "onPoseFound called............")
            //val allPoseLandmarks = pose.allPoseLandmarks

            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            if(leftShoulder != null)
                Log.i("VID", "$i found")
            else
                Log.i("VID", "$i notFound")
            // Log.i("VID", "$i ${Calendar.getInstance().time} ${leftShoulder?.position?: -1}")
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


           // val builder = StringBuilder()
            //rect_overlay.clear()

            // disegno il collo come la media tra occhi e orecchie
//            if( occhioSx != null && occhioDx != null && leftShoulder != null && rightShoulder != null  ){
//                rect_overlay.drawNeck(occhioSx, occhioDx, leftShoulder, rightShoulder);
//            }

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
//
//
//            if(leftShoulder != null && rightShoulder != null){
//                rect_overlay.drawLine(leftShoulder, rightShoulder)
//            }
//
//            if(leftHip != null &&  rightHip != null){
//                rect_overlay.drawLine(leftHip, rightHip)
//            }
//
//            if(leftShoulder != null &&  leftElbow != null){
//                rect_overlay.drawLine(leftShoulder, leftElbow)
//            }
//
//            if(leftElbow != null &&  leftWrist != null){
//                rect_overlay.drawLine(leftElbow, leftWrist)
//            }
//
//            if(leftShoulder != null &&  leftHip != null){
//                rect_overlay.drawLine(leftShoulder, leftHip)
//            }
//
//            if(leftHip != null &&  leftKnee != null){
//                rect_overlay.drawLine(leftHip, leftKnee)
//            }
//
//            if(leftKnee != null &&  leftAnkle != null){
//                rect_overlay.drawLine(leftKnee, leftAnkle)
//            }
//
//            if(leftWrist != null &&  leftThumb != null){
//                rect_overlay.drawLine(leftWrist, leftThumb)
//            }
//
//            if(leftWrist != null &&  leftPinky != null){
//                rect_overlay.drawLine(leftWrist, leftPinky)
//            }
//
//            if(leftWrist != null &&  leftIndex != null){
//                rect_overlay.drawLine(leftWrist, leftIndex)
//            }
//
//            if(leftIndex != null &&  leftPinky != null){
//                rect_overlay.drawLine(leftIndex, leftPinky)
//            }
//
//            if(leftAnkle != null &&  leftHeel != null){
//                rect_overlay.drawLine(leftAnkle, leftHeel)
//            }
//
//            if(leftHeel != null &&  leftFootIndex != null){
//                rect_overlay.drawLine(leftHeel, leftFootIndex)
//            }
//
//            if(rightShoulder != null &&  rightElbow != null){
//                rect_overlay.drawLine(rightShoulder, rightElbow)
//            }
//
//            if(rightElbow != null &&  rightWrist != null){
//                rect_overlay.drawLine(rightElbow, rightWrist)
//            }
//
//            if(rightShoulder != null &&  rightHip != null){
//                rect_overlay.drawLine(rightShoulder, rightHip)
//            }
//
//            if(rightHip != null &&  rightKnee != null){
//                rect_overlay.drawLine(rightHip, rightKnee)
//            }
//
//            if(rightKnee != null &&  rightAnkle != null){
//                rect_overlay.drawLine(rightKnee, rightAnkle)
//            }
//
//            if(rightWrist != null &&  rightThumb != null){
//                rect_overlay.drawLine(rightWrist, rightThumb)
//            }
//
//            if(rightWrist != null &&  rightPinky != null){
//                rect_overlay.drawLine(rightWrist, rightPinky)
//            }
//
//            if(rightWrist != null &&  rightIndex != null){
//                rect_overlay.drawLine(rightWrist, rightIndex)
//            }
//
//            if(rightIndex != null &&  rightPinky != null){
//                rect_overlay.drawLine(rightIndex, rightPinky)
//            }
//
//            if(rightAnkle != null &&  rightHeel != null){
//                rect_overlay.drawLine(rightAnkle, rightHeel)
//            }
//
//            if(rightHeel != null &&  rightFootIndex != null){
//                rect_overlay.drawLine(rightHeel, rightFootIndex)
//            }


            //textView.setText("${builder.toString()}")

        } catch (e: java.lang.Exception) {
            Log.i("VID", "can't detect pose")
        }
    }

    override fun onResume() {
        super.onResume()
    }
}