package com.alien.assignment.model

import android.graphics.Bitmap

data class Student(
    val studentName: String? = null,
    val emailId: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
    val bitmap: Bitmap? = null
)
