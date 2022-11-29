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
import com.bookmydoc.Constants.BOOKING
import com.bookmydoc.view.*
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

    fun addDoctor(activity: DashboardActivity, user: Doctors) {
        mFireStore.collection("doctors")
            .document(user.doctor_id)
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
                        is SettingsActivity -> activity.userDetailSuccess(user)
                        is DashboardActivity -> activity.userDetailSuccess(user)
                        is DocotrDetailActivity -> activity.userDetailSuccess(user)
                    }
                }

            }.addOnFailureListener { exception ->
                when (activity) {
                    is LoginActivity -> activity.hideProgressDialog()
                    is RegisterActivity -> activity.hideProgressDialog()
                    is SettingsActivity -> activity.hideProgressDialog()
                    is DashboardActivity -> activity.hideProgressDialog()
                    is DocotrDetailActivity -> activity.hideProgressDialog()
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

    fun getCategoriesList(activity: Activity) {
        mFireStore.collection(Constants.CATEGORIES)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val productList: ArrayList<Categories> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Categories::class.java)!!
                    //product.categories_id = i.id

                    productList.add(product)
                }
                when (activity) {
                    is CategoryActivity -> activity.successCategoryListFromFirestore(productList)
                    is DashboardActivity -> activity.successCategoryListFromFirestore(productList)

                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CategoryActivity -> activity.hideProgressDialog()
                }
                Log.e("Error :: ", e.message.toString())
            }
    }

    fun getDoctorList(activity: Activity, cat_id: Int) {
        mFireStore.collection(Constants.DOCTOR)
            .whereEqualTo(Constants.CATEGORIES_ID, cat_id)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val productList: ArrayList<Doctors> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Doctors::class.java)!!
                    product.doctor_id = i.id
                    productList.add(product)
                }
                when (activity) {
                    is DoctorListActivity -> activity.successDoctorListFromFirestore(productList)
                    is DashboardActivity -> activity.successDoctorListFromFirestore(productList)

                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is DoctorListActivity -> activity.hideProgressDialog()
                    is DashboardActivity -> activity.hideProgressDialog()
                }
                Log.e("Error :: ", e.message.toString())
            }
    }

    fun getAllDoctorList(activity: Activity, cat_id: Int) {
        mFireStore.collection(Constants.DOCTOR)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val productList: ArrayList<Doctors> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Doctors::class.java)!!
                    product.doctor_id = i.id
                    productList.add(product)
                }
                when (activity) {
                    is DoctorListActivity -> activity.successDoctorListFromFirestore(productList)
                    is DashboardActivity -> activity.successDoctorListFromFirestore(productList)

                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is DoctorListActivity -> activity.hideProgressDialog()
                    is DashboardActivity -> activity.hideProgressDialog()
                }
                Log.e("Error :: ", e.message.toString())
            }
    }


    fun getTopDoctorList(activity: Activity, cat_id: Double) {
        mFireStore.collection(Constants.DOCTOR)
            .whereGreaterThan(Constants.RATING, cat_id)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val productList: ArrayList<Doctors> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Doctors::class.java)!!
                    product.doctor_id = i.id
                    productList.add(product)
                }
                when (activity) {
                    is DoctorListActivity -> activity.successDoctorListFromFirestore(productList)
                    is DashboardActivity -> activity.successDoctorListFromFirestore(productList)

                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is DoctorListActivity -> activity.hideProgressDialog()
                    is DashboardActivity -> activity.hideProgressDialog()
                }
                Log.e("Error :: ", e.message.toString())
            }
    }

    fun geDoctorDetails(activity: Activity, doctorID: String) {

        mFireStore.collection(Constants.DOCTOR)
            .document(doctorID)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val product = document.toObject(Doctors::class.java)

                if (product != null) {
                    when (activity) {
                        is DocotrDetailActivity -> activity.doctorDetailsSuccess(product)
                        is DocotrLoginActivity -> activity.doctorDetailsSuccess(product)

                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is DocotrDetailActivity -> activity.hideProgressDialog()
                    is DocotrLoginActivity -> activity.hideProgressDialog()

                }
                Log.e(activity.javaClass.simpleName, e.message.toString())
            }
    }

    fun addBooking(activity: DocotrDetailActivity, user: Booking) {
        mFireStore.collection(BOOKING)
            .document(user.booking_id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userBookingSuccess()
            }.addOnFailureListener { exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, exception.message.toString())
            }

    }

    fun getMyBookingList(activity: MyBookingActivity, uer_id: String) {
        activity.showProgressDialog("")
        mFireStore.collection(Constants.BOOKING)
            .whereEqualTo(Constants.USER_ID, uer_id)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val productList: ArrayList<Booking> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Booking::class.java)!!
                    product.booking_id = i.id
                    productList.add(product)
                }
                activity.successBookingListFromFirestore(productList)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e("Error :: ", e.message.toString())
            }
    }

    fun getDoctorBookingList(activity: DoctorDashboardActivity, uer_id: String) {
        activity.showProgressDialog("")
        mFireStore.collection(Constants.BOOKING)
            .whereEqualTo(Constants.DOCTOR_ID, uer_id)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val productList: ArrayList<Booking> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Booking::class.java)!!
                    product.booking_id = i.id
                    productList.add(product)
                }
                activity.successBookingListFromFirestore(productList)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e("Error :: ", e.message.toString())
            }
    }
}