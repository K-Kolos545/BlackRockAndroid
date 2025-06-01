package hu.bme.aut.android.hf.viewmodel.tasted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.hf.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TastedPageViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> get() = _user

    private var listenerRegistration: ListenerRegistration? = null

    private val userId: String? = FirebaseAuth.getInstance().currentUser?.uid

    init {
        subscribeToUserFavorites()
    }

    private fun subscribeToUserFavorites() {
        userId?.let { uid ->
            listenerRegistration = firestore.collection("users")
                .document(uid)
                .addSnapshotListener { snapshot, _ ->
                    snapshot?.toObject(UserModel::class.java)?.let { user ->
                        viewModelScope.launch {
                            _user.emit(user)
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
