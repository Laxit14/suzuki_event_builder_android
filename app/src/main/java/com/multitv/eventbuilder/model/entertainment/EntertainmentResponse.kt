import com.google.gson.annotations.SerializedName

data class EntertainmentResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("page_tittle")
    val pageTitle: String,

    @SerializedName("data")
    val data: EntertainmentData
)

data class EntertainmentData(
    @SerializedName("entertainment")
    val entertainment: List<EntertainmentItem>
)

data class EntertainmentItem(
    @SerializedName("image1")
    val image1: String,

    @SerializedName("hide")
    val hide: String


)
