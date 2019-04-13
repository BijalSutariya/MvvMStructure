package com.example.mvvmstructure.domainlayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Movie(
    @field:SerializedName("Title")
    var title: String, @field:SerializedName("Year")
    var year: String, @field:SerializedName("imdbID")
    @field:PrimaryKey
    var imdbID: String, @field:SerializedName("Type")
    var type: String, @field:SerializedName("Poster")
    var poster: String
)
