package hu.bme.aut.android.hf.viewmodel.product

import androidx.lifecycle.ViewModel
import hu.bme.aut.android.hf.model.ProductModel
import kotlin.collections.List
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class RandomProductViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel(){
    private val _randomProductList = MutableStateFlow<List<ProductModel>>(emptyList())
    val randomProductList:  StateFlow<List<ProductModel>> get() = _randomProductList

    init {
        fetchRandomProducts()
    }

    fun fetchRandomProducts(){
        firestore.collection("data")
            .document("stock")
            .collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.mapNotNull { it.toObject(ProductModel::class.java) }
                _randomProductList.value = products.shuffled().take(5)
            }
    }
}