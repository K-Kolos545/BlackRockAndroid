package hu.bme.aut.android.hf.viewmodel.tasted

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.hf.model.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TastedViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _products = MutableStateFlow<Map<String, ProductModel>>(emptyMap())
    val tastedProducts: StateFlow<Map<String, ProductModel>> get() = _products

    private val _tastedStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val tastedStates: StateFlow<Map<String, Boolean>> get() = _tastedStates

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
}
