package hu.bme.aut.android.hf.components
import android.R.attr.data
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.AppUtil
import hu.bme.aut.android.hf.GlobalNavigation
import hu.bme.aut.android.hf.model.ProductModel
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.auth
import hu.bme.aut.android.hf.ui.theme.BrownIcon
import hu.bme.aut.android.hf.ui.theme.CardColor
import hu.bme.aut.android.hf.viewmodel.favorites.LoadFavoriteStatus
import hu.bme.aut.android.hf.viewmodel.product.LoadFavorite

//import hu.bme.aut.android.hf.ui.theme.Brown

@Composable
fun ProductItemView(modifier: Modifier = Modifier, product: ProductModel) {

    var context = LocalContext.current
    val isFavorite = remember { mutableStateOf(false) }


    LoadFavorite(productId = product.id, isFavorite = isFavorite)


    Card (
        modifier = modifier
            .padding(8.dp)
            .clickable{
                GlobalNavigation.navController.navigate("product-details/"+product.id)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (

        ){
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.title,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
            )


            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier.padding(10.dp))

                IconButton(
                    onClick = {
                        if (isFavorite.value) {
                            AppUtil.removeFromFavorite(context, product.id)
                        } else {
                            AppUtil.addItemToFavorite(context, product.id)
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
}