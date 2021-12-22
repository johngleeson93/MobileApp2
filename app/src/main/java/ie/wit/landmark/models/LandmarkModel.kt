package ie.wit.landmark.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LandmarkModel(var id: Long = 0,
                         val paymentmethod: String = "N/A",
                         val amount: Int = 0) : Parcelable