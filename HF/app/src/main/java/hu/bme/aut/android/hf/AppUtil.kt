package hu.bme.aut.android.hf

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.ProductModel
import kotlinx.coroutines.tasks.await

object AppUtil {

    fun showToast(context : Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

//    fun addItemToCart( context: Context, productId: String,) {
//        val userDoc = Firebase.firestore.collection("users")
//            .document(Firebase.auth.currentUser?.uid!!)
//
//        userDoc.get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val currentCart = it.result.get("cartItems") as? Map<String, Long>
//                    ?: emptyMap() //nagyjabol ez kell az add favorite-hez
//
//                val currentQuantity = currentCart[productId] ?: 0
//                val updatedQuantity = currentQuantity + 1
//
//
//                val updatedCart =
//                    mapOf("cartItems.$productId" to updatedQuantity) //es azetan ez kell ujra a favorithoz
//                userDoc.update(updatedCart)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            showToast(context, "Item added to cart")
//                        } else {
//                            showToast(context, "Failed to add item to cart")
//                        }
//
//                    }
//
//
//            }
//        }
//
//    }
//
//    fun removeFromCart(context: Context, productId: String, removeAll: Boolean = false) {
//        val userDoc = Firebase.firestore.collection("users")
//            .document(Firebase.auth.currentUser?.uid!!)
//
//        userDoc.get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val currentCart = it.result.get("cartItems") as? Map<String, Long>
//                    ?: emptyMap() //nagyjabol ez kell az add favorite-hez
//
//                val currentQuantity = currentCart[productId] ?: 0
//                val updatedQuantity = currentQuantity - 1
//
//
//
//                val updatedCart =
//                    if(updatedQuantity<=0 || removeAll){
//                        mapOf("cartItems.$productId" to FieldValue.delete()) //ezzel lehet torolni a favoritot
//                    }else{
//                        mapOf("cartItems.$productId" to updatedQuantity) //es azetan ez kell ujra a favorithoz
//                    }
//                mapOf("cartItems.$productId" to updatedQuantity) //es azetan ez kell ujra a favorithoz
//                userDoc.update(updatedCart)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            showToast(context, "Item remove to cart")
//                        } else {
//                            showToast(context, "Failed to remove item to cart")
//                        }
//                    }
//            }
//        }
//    }




    fun addItemToFavorite(context: Context, productId: String) {

        val userDoc = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentFavorites = it.result.get("favoriteItems") as? List<String> ?: emptyList()

                if (productId in currentFavorites) {
                    showToast(context, "Item already in favorites")
                    return@addOnCompleteListener
                }

                val updatedFavorites = currentFavorites + productId

                userDoc.update("favoriteItems",updatedFavorites)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Item added to favorites")
                        } else {
                            showToast(context, "Failed to add item to favorites")
                        }
                    }

            }
        }
    }

    fun removeFromFavorite(context: Context, productId: String) {
        val userDoc = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentFavorites = it.result.get("favoriteItems") as? List<String> ?: emptyList()

                if (productId !in currentFavorites) {
                    showToast(context, "Item not in favorites")
                    return@addOnCompleteListener
                }

                val updatedFavorites = currentFavorites - productId

                userDoc.update("favoriteItems", updatedFavorites)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Item removed from favorites")
                        } else {
                            showToast(context, "Failed to remove item from favorites")
                        }
                    }
            }
        }
    }


    fun addItemToTasted(context: Context, productId: String) {

        val userDoc = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentTasted = it.result.get("tastedItems") as? List<String> ?: emptyList()

                if (productId in currentTasted) {
                    showToast(context, "Item already in tasted")
                    return@addOnCompleteListener
                }

                val updatedTasted = currentTasted + productId

                userDoc.update("tastedItems",updatedTasted)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Item added to tasted")
                        } else {
                            showToast(context, "Failed to add item to tested")
                        }
                    }

            }
        }
    }

    fun removeFromTasted(context: Context, productId: String) {
        val userDoc = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentTasted = it.result.get("tastedItems") as? List<String> ?: emptyList()

                if (productId !in currentTasted) {
                    showToast(context, "Item not in tasted")
                    return@addOnCompleteListener
                }

                val updatedTasted = currentTasted - productId

                userDoc.update("tastedItems", updatedTasted)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Item removed from tasted")
                        } else {
                            showToast(context, "Failed to remove item from tasted")
                        }
                    }
            }
        }
    }

    fun addComment(context: Context, productId: String, commentText: String) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            showToast(context, "User not authenticated")
            return
        }

        val userId = user.uid // Or user.displayName if you prefer names

        val productDoc = Firebase.firestore
            .collection("data").document("stock")
            .collection("products")
            .document(productId)

        // Add the comment under comments.userId = commentText
        val updateData = mapOf(
            "comments.$userId" to commentText
        )

        productDoc.update(updateData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(context, "Comment added")
                } else {
                    showToast(context, "Failed to add comment")
                }
            }
    }



    suspend fun searchProducts(query: String): List<ProductModel> {
        if (query.isBlank()) return emptyList()

        val snapshot = FirebaseFirestore.getInstance()
            .collection("data")
            .document("stock")
            .collection("products")
            .get()
            .await()

        val allProducts = snapshot.documents.mapNotNull {
            it.toObject(ProductModel::class.java)
        }

        val lowerQuery = query.lowercase()

        return allProducts.filter { product ->
            product.title.lowercase().contains(lowerQuery) ||
                    (product.description?.lowercase()?.contains(lowerQuery) == true) ||
                    product.category.lowercase().contains(lowerQuery) ||
                    (product.placeToTaste?.lowercase()?.contains(lowerQuery) == true) ||
                    product.otherDetail.any { (key, value) ->
                        key.lowercase().contains(lowerQuery) || value.lowercase().contains(lowerQuery)
                    }
        }
    }


}