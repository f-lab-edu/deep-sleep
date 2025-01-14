package com.flab.deepsleep.data.entity.photos

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Exif(
    @SerializedName("aperture")
    val aperture: String?,
    @SerializedName("exposure_time")
    val exposureTime: String?,
    @SerializedName("focal_length")
    val focalLength: String?,
    @SerializedName("iso")
    val iso: Int,
    @SerializedName("make")
    val make: String?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("name")
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(aperture)
        parcel.writeString(exposureTime)
        parcel.writeString(focalLength)
        parcel.writeInt(iso)
        parcel.writeString(make)
        parcel.writeString(model)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exif> {
        override fun createFromParcel(parcel: Parcel): Exif {
            return Exif(parcel)
        }

        override fun newArray(size: Int): Array<Exif?> {
            return arrayOfNulls(size)
        }
    }
}
