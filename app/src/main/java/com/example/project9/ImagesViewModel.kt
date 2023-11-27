package com.example.project9

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.example.project9.Image
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
/**
 * ViewModel for managing image data and authentication in the application.
 * This ViewModel handles the retrieval of image data from Firebase Realtime Database
 * and provides methods for user authentication (sign-in, sign-up, sign-out).
 */
class ImagesViewModel : ViewModel() {
    // Tag for logging purposes
    val TAG = "ImagesViewModel"
    // Current signed-in user
    var signedInUser: User? = null
    // LiveData for the list of images
    private val _images: MutableLiveData<MutableList<Image>> = MutableLiveData()
    val images: LiveData<List<Image>>
        get() = _images as LiveData<List<Image>>
    // LiveData for the selected image URL
    private val _selectedImageUrl: MutableLiveData<String?> = MutableLiveData()
    val selectedImageUrl: LiveData<String?>
        get() = _selectedImageUrl as LiveData<String?>
    // User information and password verification
    var user: User = User()
    var verifyPassword = ""

    /**
     * Initialization block to fetch image data from Firebase Realtime Database.
     */
    init {

        val dbRefs = com.google.firebase.Firebase.database.reference
        val dbImageRefs = dbRefs.child("images")

        dbImageRefs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imageList = mutableListOf<Image>()
                for (childSnapshot in snapshot.children) {
                    val imageUrl = childSnapshot.getValue(String::class.java)
                    imageList.add(Image(imageUrl = imageUrl.toString()))
                }
                _images.value = imageList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if needed
            }

        })

    }


    fun setSelectedImage(image: Image){
        _selectedImageUrl.value = image.imageUrl
    }
    // LiveData for handling errors
    private val _errorHappened = MutableLiveData<String?>()
    val errorHappened: LiveData<String?>
        get() = _errorHappened
    // LiveData for navigation events
    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    fun getAll(): LiveData<List<Image>> {
        return images
    }

    fun onNavigatedToList() {
        _navigateToList.value = false
    }

    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }

    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }
    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }
    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }
    /**
     * Signs in the user with the provided email and password.
     */
    fun signIn() {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {

                _navigateToList.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }
    /**
     * Signs up the user with the provided email and password.
     */
    fun signUp() {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        if (user.password != verifyPassword) {
            _errorHappened.value = "Password and verify do not match."
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                _navigateToSignIn.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    /**
     * Signs out the current user and navigates to the sign-in screen.
     */
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _navigateToSignIn.value = true
        signedInUser = null
    }
}