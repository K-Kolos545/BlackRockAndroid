package hu.bme.aut.android.hf.viewmodel.product
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.ProductModel
import kotlin.collections.List
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun LoadRandomProduct(randomProducts: MutableState<List<ProductModel>>) {
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .get()
            .addOnSuccessListener { result ->
                val allProducts = result.documents.mapNotNull {
                    it.toObject(ProductModel::class.java)
                }
                randomProducts.value = allProducts.shuffled().take(5)
            }
    }
}