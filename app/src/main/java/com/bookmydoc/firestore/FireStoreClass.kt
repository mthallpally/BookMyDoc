package com.bookmydoc.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.bookmydoc.model.*
import com.bookmydoc.Constants
import com.bookmydoc.view.LoginActivity
import com.bookmydoc.view.RegisterActivity
import com.bookmydoc.view.SettingsActivity
import com.bookmydoc.view.UserProfileActivity
import com.google.firebase.auth.FirebaseAuth

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, user: User) {
        mFireStore.collection(Constants.USERS)
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }.addOnFailureListener { exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, exception.message.toString())
            }

    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }


    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {

                    val sharedPref = activity.getSharedPreferences(
                        Constants.MY_SHOP_PAL_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.fullName}"
                    )
                    editor.apply()

                    when (activity) {
                        is LoginActivity -> activity.userLoggedInSuccess(user)
                        is RegisterActivity -> activity.userLoggedInSuccess(user)
                       // is SettingsActivity -> activity.userDetailSuccess(user)
                    }
                }

            }.addOnFailureListener { exception ->
                when (activity) {
                    is LoginActivity -> activity.hideProgressDialog()
                    is RegisterActivity -> activity.hideProgressDialog()
                  //  is SettingsActivity -> activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName, exception.message.toString())
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> activity.userProfileUpdateSuccess()
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName, "Error while updating profile", e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?, imageType: String) {
        val shrf: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType
                    + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity, imageFileUri)
        )

        shrf.putFile(imageFileUri!!)
            .addOnSuccessListener { snapShot ->
                Log.e("Firebase Image Url", snapShot.metadata!!.reference!!.downloadUrl.toString())
                snapShot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Image Url", uri.toString())

                        when (activity) {
                            is UserProfileActivity -> activity.imageUploadSuccess(uri.toString())
                        }
                    }
            }.addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> activity.hideProgressDialog()
                }
                Log.e("Error while uploading", "Error while uploading image to db", e)
            }
    }


}