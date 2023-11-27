package com.example.project9

import com.google.firebase.firestore.PropertyName

/**
 * Data class representing an Image entity for Firebase Firestore.
 * This class is used to model an Image object with a corresponding property for the image URL.
 */
data class Image(
    @get:PropertyName("image_url") @set:PropertyName("image_url")
    var imageUrl: String = "",
)
