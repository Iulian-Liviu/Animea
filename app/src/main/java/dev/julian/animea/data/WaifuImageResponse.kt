package dev.julian.animea.data


import com.google.gson.annotations.SerializedName

data class WaifuImageResponse(
    val images: List<Image> = listOf()
) {
    data class Image(
        @SerializedName("byte_size")
        val byteSize: Int = 0, // 535743
        @SerializedName("dominant_color")
        val dominantColor: String = "", // #393146
        val extension: String = "", // .jpeg
        val favorites: Int = 0, // 1
        val height: Int = 0, // 3401
        @SerializedName("image_id")
        val imageId: Int = 0, // 3911
        @SerializedName("is_nsfw")
        val isNsfw: Boolean = false, // false
        @SerializedName("liked_at")
        val likedAt: Any? = null, // null
        @SerializedName("preview_url")
        val previewUrl: String = "", // https://www.waifu.im/preview/3911/
        val signature: String = "", // d07c994be362d2a9
        val source: String = "", // https://reddit.com/n9zd6s/
        val tags: List<Tag> = listOf(),
        @SerializedName("uploaded_at")
        val uploadedAt: String = "", // 2021-11-02T12:16:19.048684+01:00
        val url: String = "", // https://cdn.waifu.im/3911.jpeg
        val width: Int = 0 // 1914
    ) {
        data class Tag(
            val description: String = "", // Girls wearing any kind of uniform, cosplay etc... 
            @SerializedName("is_nsfw")
            val isNsfw: Boolean = false, // false
            val name: String = "", // uniform
            @SerializedName("tag_id")
            val tagId: Int = 0 // 11
        )
    }
}