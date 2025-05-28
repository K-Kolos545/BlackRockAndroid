package hu.bme.aut.android.hf.pages

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.hf.model.ProductModel
import javax.inject.Inject // ✅ Use this!
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class CategoryPagesViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _productList = MutableStateFlow<List<ProductModel>>(emptyList())
    val productList: StateFlow<List<ProductModel>> get() = _productList

    init {
        fetchProductData(categoryId = "default_category_id") // Replace with actual default category ID
    }

    fun fetchProductData(categoryId: String) {
        firestore.collection("data")
            .document("stock")
            .collection("products")
            .whereEqualTo("category", categoryId)
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.mapNotNull { it.toObject(ProductModel::class.java) }
                _productList.value = products.shuffled().take(5)
            }
    }
}
