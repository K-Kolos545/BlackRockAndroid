package hu.bme.aut.android.hf.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.UserModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.hf.components.FavoriteItemView
import hu.bme.aut.android.hf.components.WishListView
import hu.bme.aut.android.hf.viewmodel.favorites.FavoritesPageViewModel
import hu.bme.aut.android.hf.viewmodel.tasted.TastedPageViewModel


@Composable
fun WishListPage(
    modifier: Modifier = Modifier,
    viewModel: TastedPageViewModel = hiltViewModel()
) {

    val userState = viewModel.user.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tasted Page", style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
        )

        userState.value?.let { user ->
            LazyColumn {
                items(user.tastedItems.toList(), key = { it }) { productId ->
                    WishListView(productId = productId)
                }
            }
        }
    }
}