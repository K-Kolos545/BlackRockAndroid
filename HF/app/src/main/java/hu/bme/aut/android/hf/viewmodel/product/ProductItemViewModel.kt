package hu.bme.aut.android.hf.viewmodel.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun LoadFavorite(productId: String, isFavorite: MutableState<Boolean>) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(productId) {
        if (userId != null) {
            Firebase.firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val favorites = document.get("favoriteItems") as? List<String> ?: emptyList()
                    isFavorite.value = productId in favorites
                }
        }
    }
}