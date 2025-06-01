package hu.bme.aut.android.hf.viewmodel.favorites

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.hf.model.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesComponentsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _products = MutableStateFlow<Map<String, ProductModel>>(emptyMap())
    val favProducts: StateFlow<Map<String, ProductModel>> get() = _products

    private val _favoriteStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoriteStates: StateFlow<Map<String, Boolean>> get() = _favoriteStates

    fun fetchProductData(productId: String) {
        firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(ProductModel::class.java)
                product?.let {
                    _products.value = _products.value + (productId to it)
                }
            }
    }

    fun fetchFavoriteState(productId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val favorites = document.get("favoriteItems") as? List<String> ?: emptyList()
                    val isFav = productId in favorites
                    _favoriteStates.value = _favoriteStates.value + (productId to isFav)
                }
        }
    }

    fun toggleFavorite(productId: String) {
        val userId = auth.currentUser?.uid ?: return
        val currentFavorites = _favoriteStates.value
        val isCurrentlyFav = currentFavorites[productId] ?: false

        val userDoc = firestore.collection("users").document(userId)
        if (isCurrentlyFav) {
            userDoc.update("favoriteItems", com.google.firebase.firestore.FieldValue.arrayRemove(productId))
        } else {
            userDoc.update("favoriteItems", com.google.firebase.firestore.FieldValue.arrayUnion(productId))
        }

        _favoriteStates.value = _favoriteStates.value + (productId to !isCurrentlyFav)
    }
}
