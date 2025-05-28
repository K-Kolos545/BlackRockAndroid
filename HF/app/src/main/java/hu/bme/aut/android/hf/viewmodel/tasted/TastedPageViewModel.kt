package hu.bme.aut.android.hf.viewmodel.tasted

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.hf.model.UserModel

@Composable
fun ObserveUserTasted(userModel: MutableState<UserModel>) {
    DisposableEffect(key1 = Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        var listener: ListenerRegistration? = null

        if (userId != null) {
            listener = Firebase.firestore.collection("users")
                .document(userId)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        val result = snapshot.toObject(UserModel::class.java)
                        if (result != null) {
                            userModel.value = result
                        }
                    }
                }
        }

        onDispose {
            listener?.remove()
        }
    }
}