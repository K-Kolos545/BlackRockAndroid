package hu.bme.aut.android.hf.components


import android.R.attr.category
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.model.CategoryModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import hu.bme.aut.android.hf.GlobalNavigation
import hu.bme.aut.android.hf.ui.theme.CardColor
import hu.bme.aut.android.hf.viewmodel.category.CategoriesComponentsViewModel


@Composable
fun CategoriesView(
    modifier: Modifier = Modifier,
    viewModel: CategoriesComponentsViewModel = hiltViewModel(),
) {

    val categoryList = viewModel.categoryList.collectAsState()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy (20.dp),
    ) {
        items(categoryList.value) { item ->
            CategoryItem(category = item)
        }
    }


}


@Composable
fun CategoryItem(category : CategoryModel, ){

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp)
            .clickable{
                GlobalNavigation.navController.navigate("category-products/"+category.id)
            }
            .padding(start = 8.dp)
            ,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = category.name,
                textAlign = TextAlign.Center ,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier
//                    .padding()
            )
        }
    }

}