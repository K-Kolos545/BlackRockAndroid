package hu.bme.aut.android.hf.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.AppUtil
import hu.bme.aut.android.hf.GlobalNavigation
import hu.bme.aut.android.hf.model.ProductModel
import hu.bme.aut.android.hf.ui.theme.BrownIcon
import hu.bme.aut.android.hf.ui.theme.CardColor
import hu.bme.aut.android.hf.viewmodel.favorites.LoadFavoriteStatus
import hu.bme.aut.android.hf.viewmodel.favorites.LoadProductData
import hu.bme.aut.android.hf.viewmodel.tasted.LoadProductDataTasted
import hu.bme.aut.android.hf.viewmodel.tasted.LoadTastedStatus
import hu.bme.aut.android.hf.viewmodel.tasted.LoadTastedStatus

@Composable
fun WishListView(
    modifier: Modifier = Modifier,
    productId: String,

) {



    var product = remember {
        mutableStateOf(ProductModel())
    }
    var context = LocalContext.current
    var isTasted = remember { mutableStateOf(false) }


    LoadProductDataTasted(productId, product)
    LoadTastedStatus(productId, isTasted)




    Card (
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable{
                if (product.value.id.isNotEmpty()) {
                    GlobalNavigation.navController.navigate("product-details/${product.value.id}")
                }
                // If the product ID is empty, we can show a message or handle it accordingly


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
                    if (isTasted.value) {
                        AppUtil.removeFromTasted(context, product.value.id)
                    } else {
                        AppUtil.addItemToTasted(context, product.value.id)
                    }
                    isTasted.value = !isTasted.value
                },

                ) {
                Icon(
                    imageVector = if (isTasted.value) Icons.Default.CheckCircle else Icons.Default.Check, //or u can get it from drawable
                    contentDescription = "toggle tasted",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}