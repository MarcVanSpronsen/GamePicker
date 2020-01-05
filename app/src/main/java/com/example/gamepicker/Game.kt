package com.example.gamepicker

import android.media.Image
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.joda.time.DateTime
import java.lang.reflect.Array
import java.net.URL
import java.util.*

@Parcelize
@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @SerializedName("name") val Name: String,
    @SerializedName("released") val releaseDate: String,
    @SerializedName("website") val websiteUrl: String,
    @SerializedName("background_image") val ImageUrl: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("creation_date") val creationDate: String
) : Parcelable {
}