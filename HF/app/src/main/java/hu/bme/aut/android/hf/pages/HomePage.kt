package hu.bme.aut.android.hf.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.android.hf.AppUtil.searchProducts
import hu.bme.aut.android.hf.GlobalNavigation.navController
import hu.bme.aut.android.hf.components.*
import hu.bme.aut.android.hf.model.ProductModel
import hu.bme.aut.android.hf.ui.theme.BackGroundColor
import hu.bme.aut.android.hf.ui.theme.CardColor
import kotlinx.coroutines.tasks.await

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val searchActive = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))

            .padding(horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(16.dp)
        ) {
            HeaderView(
                modifier,
                onSearchClick = {
                searchActive.value = true
                }
            )

            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    BannerView(modifier = Modifier.height(230.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Categories",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CategoriesView(modifier)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Products",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RandomProductView(modifier)
                }
            }

        }
            if (searchActive.value) {
                SearchOverlay(
                    navController = navController,
                    onDismiss = { searchActive.value = false }
                )
            }

    }
}


@Composable
fun SearchOverlay(
    navController: NavController,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(emptyList<ProductModel>()) }

    LaunchedEffect(query) {
        results = searchProducts(query)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Dismiss when tapping outside content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss)
                .clip(RoundedCornerShape(12.dp)) // Clip content
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Just this content, not the whole screen

        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn {
                items(results) { product ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("product-details/${product.id}")
                            },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
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
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = product.title,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

