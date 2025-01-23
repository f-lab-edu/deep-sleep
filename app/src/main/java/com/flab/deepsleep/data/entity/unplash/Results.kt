package com.flab.deepsleep.data.entity.unplash
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("blur_hash")
    val blurHash: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("user")
    val user: SearchUser?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        TODO("user")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(blurHash)
        parcel.writeString(color)
        parcel.writeString(createdAt)
        parcel.writeString(description)
        parcel.writeInt(height)
        parcel.writeString(id)
        parcel.writeByte(if (likedByUser) 1 else 0)
        parcel.writeInt(likes)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Results> {
        override fun createFromParcel(parcel: Parcel): Results {
            return Results(parcel)
        }

        override fun newArray(size: Int): Array<Results?> {
            return arrayOfNulls(size)
        }
    }
}
