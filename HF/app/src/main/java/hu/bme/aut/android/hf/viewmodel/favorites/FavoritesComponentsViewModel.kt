package hu.bme.aut.android.hf.viewmodel.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.ProductModel

@Composable
fun LoadProductData(productId: String, product: MutableState<ProductModel>) {
    LaunchedEffect(productId) {
        Firebase.firestore.collection("data")
            .document("stock").collection("products")
            .document(productId).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(ProductModel::class.java)
                    if (result != null) {
                        product.value = result
                    }
                }
            }
    }
}

@Composable
fun LoadFavoriteStatus(productId: String, isFavorite: MutableState<Boolean>) {
    val userId = Firebase.auth.currentUser?.uid
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

