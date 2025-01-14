package com.flab.deepsleep.data.entity.photos

import android.os.Parcel
import android.os.Parcelable
import com.flab.deepsleep.data.entity.room.Photo
import com.google.gson.annotations.SerializedName

data class SinglePhoto(
    @SerializedName("blur_hash")
    val blurHash: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean?,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("public_domain")
    val publicDomain: Boolean?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("width")
    val width: Int,
    @SerializedName("exif")
    val exif: Exif?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("user")
    val user: User?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readInt(),
        parcel.readParcelable(Exif::class.java.classLoader),
        parcel.readParcelable(Urls::class.java.classLoader),
        parcel.readParcelable(User::class.java.classLoader)
    )
    companion object CREATOR : Parcelable.Creator<SinglePhoto> {
        override fun createFromParcel(parcel: Parcel): SinglePhoto {
            return SinglePhoto(parcel)
        }

        override fun newArray(size: Int): Array<SinglePhoto?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(blurHash)
        parcel.writeString(color)
        parcel.writeString(createdAt)
        parcel.writeString(description)
        parcel.writeInt(downloads)
        parcel.writeInt(height)
        parcel.writeString(id)
        parcel.writeValue(likedByUser)
        parcel.writeInt(likes)
        parcel.writeValue(publicDomain)
        parcel.writeString(updatedAt)
        parcel.writeInt(width)
        parcel.writeParcelable(exif, flags)
        parcel.writeParcelable(urls, flags)
        parcel.writeParcelable(user, flags)
    }
}

fun SinglePhoto.toPhoto(): Photo {
    return Photo(
        id = this.id?.toIntOrNull() ?: 0,
        likes = this.likes,
        urls = this.urls?.raw,
        createdAt = this.createdAt,
        username = this.user?.username,
        isLike = true
    )
}
