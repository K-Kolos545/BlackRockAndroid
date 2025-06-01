package hu.bme.aut.android.hf.viewmodel

import android.R.attr.name
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class HeaderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel(){

    private val _header = MutableStateFlow("")
    val header: StateFlow<String> get()= _header

    fun fetchUserName(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            Firebase.firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val fullName = document.getString("name") ?: ""
                    val firstName = fullName.split(" ").firstOrNull() ?: ""
                    _header.value = firstName

                }
        }
    }
}