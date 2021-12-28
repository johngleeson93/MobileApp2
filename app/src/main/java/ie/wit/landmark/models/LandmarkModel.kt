package ie.wit.landmark.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class LandmarkModel(
        var uid: String? = "",
        var message: String = "Add a Landmark",
        var upvotes: Int = 0,
        var profilepic: String = "",
        var email: String? = "joe@bloggs.com")
        : Parcelable
{
        @Exclude
        fun toMap(): Map<String, Any?> {
                return mapOf(
                        "uid" to uid,
                        "message" to message,
                        "upvotes" to upvotes,
                        "profilepic" to profilepic,
                        "email" to email
                )
        }
}


