package com.example.kotlinroom.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Homework (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val mapel: String,
    val desc: String

    ) : Parcelable