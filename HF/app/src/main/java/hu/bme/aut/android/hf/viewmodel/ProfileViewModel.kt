// ProfileViewModel.kt
package hu.bme.aut.android.hf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow(auth.currentUser?.email ?: "")
    val email: StateFlow<String> = _email

    private val _comments = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val comments: StateFlow<List<Pair<String, String>>> = _comments

    private var nameListenerRegistration: ListenerRegistration? = null

    init {
        fetchUserName()
        fetchUserEmail()
        fetchUserComments()
    }

    private fun fetchUserName() {
        val uid = auth.currentUser?.uid ?: return

        nameListenerRegistration?.remove() // remove old listener if it exists

        nameListenerRegistration = firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, _ ->
                val name = snapshot?.getString("name") ?: ""
                viewModelScope.launch {
                    _name.emit(name)
                }
            }

    }
    override fun onCleared() {
        super.onCleared()
        nameListenerRegistration?.remove()
    }


    private fun fetchUserEmail() {
        auth.currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    _email.value = doc.getString("email") ?: ""
                }
        }
    }

    private fun fetchUserComments() {
        auth.currentUser?.uid?.let { uid ->
            firestore.collection("data").document("stock")
                .collection("products").get()
                .addOnSuccessListener { products ->
                    val userComments = mutableListOf<Pair<String, String>>()
                    for (product in products) {
                        val commentMap = product.get("comments") as? Map<*, *>
                        val comment = commentMap?.get(uid) as? String
                        if (comment != null) {
                            userComments.add(product.getString("title").orEmpty() to comment)
                        }
                    }
                    _comments.value = userComments
                }
        }
    }
}
