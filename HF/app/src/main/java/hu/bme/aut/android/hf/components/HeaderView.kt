package hu.bme.aut.android.hf.components

import android.R.attr.name
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.hf.viewmodel.HeaderViewModel
import hu.bme.aut.android.hf.viewmodel.product.RandomProductViewModel

@Composable
fun HeaderView(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    viewModel: HeaderViewModel = hiltViewModel()
){


    val nameState by viewModel.header.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserName()
    }

    val displayName = if (nameState.isNotBlank()) "Welcome back, $nameState" else "Welcome!"


    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

    ) {
        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.Start
        )
        {
            Text(text = displayName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold

                ),
                color = MaterialTheme.colorScheme.onBackground,
            )

        }
        IconButton(
            onClick = {onSearchClick()}
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }

}