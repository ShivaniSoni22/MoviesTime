package com.movies.moviestime.database.entity.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "searched_movies")
@Parcelize
data class Search(
    @SerializedName("Type")
    var type: String,

    @SerializedName("Year")
    var year: String,

    @PrimaryKey
    @SerializedName("imdbID")
    var imdbID: String,

    @SerializedName("Poster")
    var poster: String,

    @SerializedName("Title")
    var title: String
) : Parcelable