package hu.bme.aut.android.hf.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.UserModel

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth

    private val firestore = Firebase.firestore

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    onResult(true, null)
                }else{
                    onResult(false, it.exception?.localizedMessage)
                }
            }
    }

    fun signUp(email: String, password: String, name: String, onResult: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {

                    var userId = it.result?.user?.uid

                    val userModel = UserModel(name, email, userId!!)
                    firestore.collection("users").document(userId)
                        .set(userModel)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, "something went wrong")
                            }
                        }

                }else{
                    onResult(false, it.exception?.localizedMessage)
                }
            }
    }
}