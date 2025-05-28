package hu.bme.aut.android.hf.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import hu.bme.aut.android.hf.ui.theme.BrownButton
import hu.bme.aut.android.hf.viewmodel.ProfileViewModel



@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val comments by viewModel.comments.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Name: $name", fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,)
        Text(text = "Email: $email", fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Your Comments:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,)
        Spacer(modifier = Modifier.height(8.dp))

        if (comments.isEmpty()) {
            Text(text = "You haven't commented yet.", fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,)
        } else {
            comments.forEach { (productTitle, comment) ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = productTitle, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground,)
                    Text(text = comment, modifier = Modifier.padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground,)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("editProfile")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Text("Edit Profile",
                color = MaterialTheme.colorScheme.onBackground,)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                Firebase.auth.signOut()
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Text("Sign Out")
        }
    }
}
