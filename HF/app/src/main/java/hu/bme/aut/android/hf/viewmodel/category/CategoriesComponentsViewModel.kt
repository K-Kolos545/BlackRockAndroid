package hu.bme.aut.android.hf.viewmodel.category

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.hf.model.CategoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesComponentsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _categoryList = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categoryList: StateFlow<List<CategoryModel>> get() = _categoryList

    init {
        fetchCategoryData()
    }

    private fun fetchCategoryData() {
        firestore.collection("data")
            .document("stock")
            .collection("categories")
            .get()
            .addOnSuccessListener { documents ->
                val categories = documents.mapNotNull { it.toObject(CategoryModel::class.java) }
                _categoryList.value = categories
            }
    }

}