package hu.bme.aut.android.hf.viewmodel.product

import android.content.Context
import androidx.compose.runtime.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.ProductModel

@Composable
fun ObserveProduct(productId: String, onProductChange: (ProductModel) -> Unit) {
    val productRef = Firebase.firestore.collection("data").document("stock")
        .collection("products")
        .document(productId)

    DisposableEffect(productId) {
        val listenerRegistration = productRef.addSnapshotListener { snapshot, _ ->
            val updatedProduct = snapshot?.toObject(ProductModel::class.java)
            if (updatedProduct != null) {
                onProductChange(updatedProduct)
            }
        }

        onDispose {
            listenerRegistration.remove()
        }
    }
}

@Composable
fun ObserveFavoriteStatus(productId: String, onStatusChange: (Boolean) -> Unit) {
    val userId = Firebase.auth.currentUser?.uid

    LaunchedEffect(productId) {
        if (userId != null) {
            Firebase.firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val favorites = document.get("favoriteItems") as? List<String> ?: emptyList()
                    onStatusChange(productId in favorites)
                }
        }
    }
}

@Composable
fun ObserveTastedStatus(productId: String, onStatusChange: (Boolean) -> Unit) {
    val userId = Firebase.auth.currentUser?.uid

    LaunchedEffect(productId) {
        if (userId != null) {
            Firebase.firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val tasted = document.get("tastedItems") as? List<String> ?: emptyList()
                    onStatusChange(productId in tasted)
                }
        }
    }
}

@Composable
fun LoadCommentUserNames(
    commentUserIds: Set<String>,
    existingNames: MutableMap<String, String>,
    onNameLoaded: (String, String) -> Unit
) {
    LaunchedEffect(commentUserIds) {
        for (userId in commentUserIds) {
            if (!existingNames.containsKey(userId)) {
                Firebase.firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { userDoc ->
                        val name = userDoc.getString("name") ?: userId
                        onNameLoaded(userId, name)
                    }
            }
        }
    }
}
