package hu.bme.aut.android.hf.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.jvm.java
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.hf.components.ProductItemView
import hu.bme.aut.android.hf.model.ProductModel
import hu.bme.aut.android.hf.model.UserModel


@Composable
fun CategoryProductsPage(
    modifier: Modifier = Modifier,
    categoryId: String,
    viewModel: CategoryPagesViewModel = hiltViewModel()
) {


    val productsList = viewModel.productList.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.fetchProductData(categoryId)
    }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "category", style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        LazyColumn {
            items(productsList.value.chunked(2)){ rowItems ->
                Row{
                    rowItems.forEach {
                        ProductItemView(
                            product = it,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                    if(
                        rowItems.size == 1
                    ){
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }


}