package hu.bme.aut.android.hf.components

import android.R.attr.category
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.AppUtil
import hu.bme.aut.android.hf.GlobalNavigation
import hu.bme.aut.android.hf.model.ProductModel
import hu.bme.aut.android.hf.ui.theme.CardColor
import hu.bme.aut.android.hf.viewmodel.favorites.LoadFavoriteStatus
import hu.bme.aut.android.hf.viewmodel.favorites.LoadProductData

@Composable
fun FavoriteItemView(
    modifier: Modifier = Modifier,
    productId: String,

    ) {




    val context = LocalContext.current
    val product = remember { mutableStateOf(ProductModel()) }
    val isFavorite = remember { mutableStateOf(false) }

    // Call the logic functions
    LoadProductData(productId, product)
    LoadFavoriteStatus(productId, isFavorite)





    Card (
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable{
                GlobalNavigation.navController.navigate("product-details/"+product.value.id)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically //itt tette kozepre a cimet amig csak az volt
        ){
            AsyncImage(
                model = product.value.images.firstOrNull(),
                contentDescription = product.value.title,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )

            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ){
                Text(
                    text = product.value.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    )
            }

            IconButton(
                onClick = {
                    if (isFavorite.value) {
                        AppUtil.removeFromFavorite(context, productId)
                    } else {
                        AppUtil.addItemToFavorite(context, productId)
                    }
                    isFavorite.value = !isFavorite.value
                },

                ) {
                Icon(
                    imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle favorite",
                    tint = if (isFavorite.value) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}