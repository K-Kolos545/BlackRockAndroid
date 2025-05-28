package hu.bme.aut.android.hf.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import hu.bme.aut.android.hf.AppUtil
import hu.bme.aut.android.hf.model.ProductModel
import com.google.maps.android.compose.*
import hu.bme.aut.android.hf.ui.theme.BrownButton
import hu.bme.aut.android.hf.ui.theme.BrownIcon
import hu.bme.aut.android.hf.ui.theme.CardColor
import hu.bme.aut.android.hf.viewmodel.product.LoadCommentUserNames
import hu.bme.aut.android.hf.viewmodel.product.ObserveFavoriteStatus
import hu.bme.aut.android.hf.viewmodel.product.ObserveProduct
import hu.bme.aut.android.hf.viewmodel.product.ObserveTastedStatus

@Composable
fun ProductDetailPage(
    modifier: Modifier = Modifier,
    productId : String

) {

    var product by remember {
        mutableStateOf(ProductModel())
    }
    var context = LocalContext.current
    val userId = Firebase.auth.currentUser?.uid
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val firestore = Firebase.firestore
    val productRef = firestore.collection("data").document("stock")
        .collection("products")
        .document(productId)


//    val product = remember { mutableStateOf(ProductModel()) }
    val isFavorite = remember { mutableStateOf(false) }
    val isTasted = remember { mutableStateOf(false) }
    val commentNames = remember { mutableStateMapOf<String, String>() }

// Call external logic hooks
    ObserveProduct(productId) { updatedProduct ->
        product = updatedProduct
    }

    ObserveFavoriteStatus(productId) { status ->
        isFavorite.value = status
    }

    ObserveTastedStatus(productId) { status ->
        isTasted.value = status
    }

    LoadCommentUserNames(product.comments.keys, commentNames) { userId, name ->
        commentNames[userId] = name
    }





    Column(
        modifier = modifier
            .fillMaxSize()
            .padding (16. dp)
            .verticalScroll(rememberScrollState())
    ){

        Text (
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = modifier.height(8.dp))

        Column{
            val pagerState = rememberPagerState(0){ product.images.size }

            HorizontalPager(
                state = pagerState,
                pageSpacing = 24.dp,
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(200.dp)
            ) {
//                Text("Images list: ${product.images.joinToString()}")


                AsyncImage(
                    model = product.images.get(it),
                    contentDescription = "Product images",
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
//        Text(text = "Dot count: ${bannerList.size}")

            com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator(
                dotCount = product.images.size,
                type = ShiftIndicatorType(
                    DotGraphic(
                        color = MaterialTheme.colorScheme.secondary,
                        size = 8.dp,
                    )
                ),
                pagerState = pagerState,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){




                Spacer(modifier = Modifier.width(8.dp))

                Button(

                    onClick = {
                        if (isTasted.value) {
                            AppUtil.removeFromTasted(context, productId)
                        } else {
                            AppUtil.addItemToTasted(context, productId)
                        }
                        isTasted.value = !isTasted.value
                    },
                    modifier = Modifier
                        .width(250.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),

                ) {
                    Text(text = "Kostoltam mar")
                    Spacer(modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (isTasted.value) {
                                AppUtil.removeFromTasted(context, product.id)
                            } else {
                                AppUtil.addItemToTasted(context, product.id)
                            }
                            isTasted.value = !isTasted.value
                        },

                        ) {
                        Icon(
                            imageVector = if (isTasted.value) Icons.Default.CheckCircle else Icons.Default.Check, //or u can get it from drawable
                            contentDescription = "toggle tasted",
                            tint = if (isFavorite.value) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier.width(20.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            if(product.otherDetail.isNotEmpty())
                Text(
                    text = "product details:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )

            Spacer(modifier = Modifier.height(8.dp))

            product.otherDetail.forEach { (key, value)->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                ){
                    Text(text = "$key : ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        )
                    Text(text = value , fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Product description:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onBackground,

            )


            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Store Location:", fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                )
            Spacer(modifier = Modifier.height(8.dp))
            ProductLocationMap(product = product)

            Spacer(modifier = Modifier.height(16.dp))

            if(product.otherDetail.isNotEmpty())
                Text(
                    text = "comments: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,

                )

            Spacer(modifier = Modifier.height(8.dp))

            product.comments.forEach { (userId, commentText)->
                val name = commentNames[userId] ?: userId

                Column(
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                ){

                    Text(text = "${name.split(" ").get(0)} : ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        )
                    Text(text = commentText , fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp,),
                        color = MaterialTheme.colorScheme.onBackground,

                    )


                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus() // 🔐 Hides cursor by removing focus
                    }
                    .padding(16.dp)
            ){
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("write your own comment") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
//                singleLine = false,
//                maxLines = 20,

                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    shape = RoundedCornerShape(12.dp),
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    AppUtil.addComment(context, productId, text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),

            ) {
                Text(text = "Add comment",
                    color = MaterialTheme.colorScheme.onBackground,)
            }



        }
    }
}




@Composable
fun ProductLocationMap(product: ProductModel) {
    if (product.latitude == 0.0 && product.longitude == 0.0) {
        Text("No location available",
            color = MaterialTheme.colorScheme.onBackground,)
        return
    }
    val productPosition = LatLng(product.latitude, product.longitude)
//    val productPosition = LatLng(47.4979, 19.0402)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(productPosition, 14f)
    }


    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp)),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isBuildingEnabled = true)
    ) {
        Marker(
            state = MarkerState(position = productPosition),
            title = product.placeToTaste,
//            title = "test"
        )
    }

}