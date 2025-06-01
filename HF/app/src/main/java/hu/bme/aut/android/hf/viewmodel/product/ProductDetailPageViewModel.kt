package hu.bme.aut.android.hf.viewmodel.product

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.hf.model.ProductModel
import hu.bme.aut.android.hf.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class ProductDetailPageViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _product = MutableStateFlow<ProductModel?>(null)
    val product: StateFlow<ProductModel?> get() = _product

    private val _favoriteStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoriteStates: StateFlow<Map<String, Boolean>> get() = _favoriteStates

    private val _tastedStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val tastedStates: StateFlow<Map<String, Boolean>> get() = _tastedStates

    private val _userNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val userNames: StateFlow<Map<String, String>> get() = _userNames

    private var listenerRegistration: ListenerRegistration? = null

    fun observeProduct(productId: String) {
        val productRef = firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)

        listenerRegistration?.remove() // In case it's already listening
        listenerRegistration = productRef.addSnapshotListener { snapshot, _ ->
            val updatedProduct = snapshot?.toObject(ProductModel::class.java)
            _product.value = updatedProduct
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
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

    fun fetchTastedState(productId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val tasted = document.get("tastedItems") as? List<String> ?: emptyList()
                    val isTasted = productId in tasted
                    _tastedStates.value = _tastedStates.value + (productId to isTasted)
                }
        }
    }

    fun loadUserNames(userIds: Set<String>) {
        val currentNames = _userNames.value.toMutableMap()

        userIds.forEach { userId ->
            if (!currentNames.containsKey(userId)) {
                firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        val name = document.getString("name") ?: userId
                        _userNames.value = _userNames.value + (userId to name)
                    }
            }
        }
    }

    fun toggleTasted(productId: String) {
        val userId = auth.currentUser?.uid ?: return
        val currentTasted = _tastedStates.value
        val isCurrentlyTasted = currentTasted[productId] ?: false

        val userDoc = firestore.collection("users").document(userId)
        if (isCurrentlyTasted) {
            userDoc.update("tastedItems", com.google.firebase.firestore.FieldValue.arrayRemove(productId))
        } else {
            userDoc.update("tastedItems", com.google.firebase.firestore.FieldValue.arrayUnion(productId))
        }

        _tastedStates.value = _tastedStates.value + (productId to !isCurrentlyTasted)
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
