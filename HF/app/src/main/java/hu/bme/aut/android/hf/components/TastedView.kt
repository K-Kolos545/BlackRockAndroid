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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.AppUtil
import hu.bme.aut.android.hf.GlobalNavigation
import hu.bme.aut.android.hf.model.ProductModel
import hu.bme.aut.android.hf.ui.theme.BrownIcon
import hu.bme.aut.android.hf.ui.theme.CardColor
import hu.bme.aut.android.hf.viewmodel.favorites.FavoritesComponentsViewModel
import hu.bme.aut.android.hf.viewmodel.tasted.TastedViewModel


@Composable
fun WishListView(
    modifier: Modifier = Modifier,
    productId: String,
    viewModel: TastedViewModel = hiltViewModel()

) {



    val products = viewModel.tastedProducts.collectAsState()
    val tastedStates = viewModel.tastedStates.collectAsState()

    val product = products.value[productId]
    val isTasted = tastedStates.value[productId] ?: false


    LaunchedEffect(productId) {
        viewModel.fetchProductData(productId)
        viewModel.fetchTastedState(productId)
    }



product?.let{

    Card (
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable{
                if (product.id.isNotEmpty()) {
                    GlobalNavigation.navController.navigate("product-details/${product.id}")
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
                model = product.images.firstOrNull(),
                contentDescription = product.title,
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
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,

                    )
            }

            IconButton(
                onClick = {
                    viewModel.toggleTasted(productId)
                },

                ) {
                Icon(
                    imageVector = if (isTasted) Icons.Default.CheckCircle else Icons.Default.Check, //or u can get it from drawable
                    contentDescription = "toggle tasted",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}
}
