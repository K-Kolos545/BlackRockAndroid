package hu.bme.aut.android.hf.viewmodel.tasted



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.ProductModel
import kotlinx.coroutines.tasks.await

@Composable
fun LoadProductDataTasted(productId: String, product: MutableState<ProductModel>) {
    LaunchedEffect(productId) {
        try {
            val snapshot = Firebase.firestore.collection("data")
                .document("stock")
                .collection("products")
                .document(productId)
                .get()
                .await() // ✅ use await to wait for completion

            val result = snapshot.toObject(ProductModel::class.java)
            if (result != null) {
                result.id = snapshot.id
                product.value = result
            }

        } catch (e: Exception) {
            e.printStackTrace() // Log error if any
        }
    }
}



@Composable
fun LoadTastedStatus(productId: String, isTasted: MutableState<Boolean>) {
    val userId = Firebase.auth.currentUser?.uid
    LaunchedEffect(productId) {
        if (userId != null) {
            Firebase.firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val tasted= document.get("tastedItems") as? List<String> ?: emptyList()
                    isTasted.value = productId in tasted
                }
        }
    }
}

