package hu.bme.aut.android.hf.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow(auth.currentUser?.email ?: "")
    val email: StateFlow<String> = _email

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        auth.currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    _name.value = doc.getString("name") ?: ""
                    _email.value = doc.getString("email") ?: auth.currentUser?.email.orEmpty()
                }
        }
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

//    fun updateEmail(newEmail: String) {
//        _email.value = newEmail
//    }

     fun saveNameToFirestore() {
        val uid = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        firestore.collection("users").document(uid)
            .update("name", _name.value)

    }

    fun saveNameToAuthAndFirestore(
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val user = auth.currentUser
        val newName = _name.value

        if (user == null) {
            onFailure(Exception("User not logged in"))
            return
        }

        val profileUpdates = userProfileChangeRequest {
            displayName = newName
        }

        user.updateProfile(profileUpdates)
            .addOnSuccessListener {
                firestore.collection("users").document(user.uid)
                    .update("name", newName)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }

//    suspend fun saveEmailToAuthAndFirestore(
//        password: String
//    ) {
//        val user = auth.currentUser ?: throw IllegalStateException("User not logged in")
//        val newEmail = _email.value
//
//        if (user.email.isNullOrEmpty()) {
//            throw IllegalStateException("Current email is null")
//        }
//
//        val credential = EmailAuthProvider.getCredential(user.email!!, password)
//
//        // Reauthenticate
//        user.reauthenticate(credential).await()
//
//        // Update email in Firebase Auth
//        user.updateEmail(newEmail).await()
//
//        // Update email in Firestore
//        firestore.collection("users").document(user.uid)
//            .update("email", newEmail)
//            .await()
//    }

//    // Optional: Wrapper for save logic (use this from UI if preferred)
//    fun updateProfile(
//        password: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        viewModelScope.launch {
//            try {
//                saveNameToFirestore()
//                saveEmailToAuthAndFirestore(password)
//                onSuccess()
//            } catch (e: Exception) {
//                Log.e("FirebaseUpdate", "Update failed: ${e.message}", e)
//                onFailure(e)
//            }
//        }
//    }

    ////////////////////


//    private val _emailUpdateState = mutableStateOf<String?>(null)
//    val emailUpdateState: State<String?> = _emailUpdateState
//
//    fun updateEmail1(newEmail: String) {
//        val user = Firebase.auth.currentUser
//        if (user != null) {
//            user!!.updateEmail(newEmail)
//                .addOnSuccessListener {
//                    _emailUpdateState.value = "Email updated successfully"
//                }
//                .addOnFailureListener {
//                    _emailUpdateState.value = "Failed: ${it.message}"
//                }
//        } else {
//            _emailUpdateState.value = "No user signed in"
//        }
//    }
//
//
//    fun saveEmailToAuthAndFirestore1(
//        password: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val user = auth.currentUser
//        val newEmail = _email.value ?: return onFailure(Exception("Email is null"))
//
//        if (user != null && user.email != null) {
//            val credential = EmailAuthProvider.getCredential(user.email!!, password)
//
//            user.reauthenticate(credential)
//                .addOnSuccessListener {
//                    user.updateEmail(newEmail)
//                        .addOnSuccessListener {
//                            firestore.collection("users").document(user.uid)
//                                .update("email", newEmail)
//                                .addOnSuccessListener { onSuccess() }
//                                .addOnFailureListener { onFailure(it) }
//                        }
//                        .addOnFailureListener { onFailure(it) }
//                }
//                .addOnFailureListener { onFailure(it) }
//        } else {
//            onFailure(Exception("User not signed in"))
//        }
//    }
//
//
//
//    fun saveEmailToAuthAndFirestore2(
//        password: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val user = auth.currentUser
//        val newEmail = _email.value
//
//        if (user == null || newEmail.isBlank()) {
//            onFailure(Exception("User or new email is null"))
//            return
//        }
//
//        val currentEmail = user.email
//        if (currentEmail.isNullOrEmpty()) {
//            onFailure(Exception("Current email is not available"))
//            return
//        }
//
//        val credential = EmailAuthProvider.getCredential(currentEmail, password)
//
//        user.reauthenticate(credential)
//            .addOnSuccessListener {
//                user.updateEmail(newEmail)
//                    .addOnSuccessListener {
//                        Log.d("AUTH", "Re-authentication successful")
//                        user.reload() // refresh the currentUser instance
//                        firestore.collection("users").document(user.uid)
//                            .update("email", newEmail)
//                            .addOnSuccessListener { onSuccess() }
//                            .addOnFailureListener { onFailure(it) }
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.e("AUTH", "Re-authentication failed", exception)
//                        onFailure(exception)
//                    }
//                    .addOnFailureListener { onFailure(it) }
//            }
//    }

}
