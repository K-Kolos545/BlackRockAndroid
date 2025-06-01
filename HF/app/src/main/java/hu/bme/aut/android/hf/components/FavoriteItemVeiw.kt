package hu.bme.aut.android.hf.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import hu.bme.aut.android.hf.GlobalNavigation
import hu.bme.aut.android.hf.viewmodel.favorites.FavoritesComponentsViewModel

@Composable
fun FavoriteItemView(
    modifier: Modifier = Modifier,
    productId: String,
    viewModel: FavoritesComponentsViewModel = hiltViewModel()
) {
    val products = viewModel.favProducts.collectAsState()
    val favoriteStates = viewModel.favoriteStates.collectAsState()

    val product = products.value[productId]
    val isFavorite = favoriteStates.value[productId] ?: false

    LaunchedEffect(productId) {
        viewModel.fetchProductData(productId)
        viewModel.fetchFavoriteState(productId)
    }

    product?.let {
        Card(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    GlobalNavigation.navController.navigate("product-details/${product.id}")
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = product.images.firstOrNull(),
                    contentDescription = product.title,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                IconButton(onClick = {
                    viewModel.toggleFavorite(productId)
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle favorite",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
