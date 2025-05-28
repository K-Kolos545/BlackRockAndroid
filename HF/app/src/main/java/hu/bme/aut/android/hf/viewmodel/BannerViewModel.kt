package hu.bme.aut.android.hf.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _bannerList = MutableStateFlow<List<String>>(emptyList())
    val bannerList: StateFlow<List<String>> get() = _bannerList

    init {
        fetchBannerUrls()
    }

    private fun fetchBannerUrls() {
        firestore.collection("data")
            .document("banners")
            .get()
            .addOnSuccessListener { document ->
                val urls = document.get("urls") as? List<String>
                _bannerList.value = urls ?: emptyList()
            }
    }
}