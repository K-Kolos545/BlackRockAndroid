package hu.bme.aut.android.hf.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.GlobalNavigation
import hu.bme.aut.android.hf.model.CategoryModel
import hu.bme.aut.android.hf.model.ProductModel
import hu.bme.aut.android.hf.ui.theme.CardColor
import hu.bme.aut.android.hf.viewmodel.product.RandomProductViewModel

@Composable
fun RandomProductView(
    modifier: Modifier = Modifier,
    viewModel: RandomProductViewModel = hiltViewModel()
    ) {

//    val randomProducts = remember { mutableStateOf<List<ProductModel>>(emptyList()) }
//
//    LoadRandomProduct(randomProducts)

    val randomProducts = viewModel.randomProductList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRandomProducts()
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy (20.dp),
    ) {
        items(randomProducts.value) { item ->
            RandomItem(product = item)
        }
    }

}


@Composable
fun RandomItem(product: ProductModel) {

    Card(
        modifier = Modifier
            .size(150.dp)
            .clickable {
                GlobalNavigation.navController.navigate("product-details/" + product.id)
            }
        .padding(start = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = product.images.firstOrNull() ?: "",
                contentDescription = product.title,
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = product.title,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
