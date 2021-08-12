package com.example.reba.data

import android.os.Parcelable
import com.google.mlkit.vision.pose.Pose
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable

@Parcelize
data class PointsData(
    val listPose :  @RawValue MutableList<Pose>
) : Parcelable
