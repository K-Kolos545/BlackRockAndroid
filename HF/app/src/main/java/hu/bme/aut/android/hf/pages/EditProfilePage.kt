import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.util.CoilUtils.result
import hu.bme.aut.android.hf.AppUtil.showToast
import hu.bme.aut.android.hf.ui.theme.BrownButton
import hu.bme.aut.android.hf.viewmodel.EditProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfilePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val name by viewModel.name.collectAsState()
//    val email by viewModel.email.collectAsState()

    var editedName by remember { mutableStateOf(name) }
    LaunchedEffect(name) {
        editedName = name
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Edit Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = editedName,
            onValueChange = { editedName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),

        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateName(editedName)
                viewModel.saveNameToAuthAndFirestore(
                    onSuccess = {
                        showToast(context, "Name updated")
                        navController.popBackStack()

//
                     },
                    onFailure = { showToast(context, "Failed: ${it.message}") }
                )

//                viewModel.updateEmail(editedEmail)
//                showPasswordDialog = true
            },
            modifier = Modifier.fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
        ) {
            Text("Save Changes")
        }


//        if (showPasswordDialog) {
//            AlertDialog(
//                onDismissRequest = { showPasswordDialog = false },
//                confirmButton = {
//                    Button(onClick = {
//                        showPasswordDialog = false
//                        coroutineScope.launch {
//                            viewModel.saveEmailToAuthAndFirestore2(
//                                password = password,
//                                onSuccess = {
//                                    showToast(context, "Profile updated successfully")
//                                    navController.popBackStack()
//                                },
//                                onFailure = {
//                                    showToast(context, "Update failed: ${it.message ?: "Unknown error"}")
//                                }
//                            )
//                        }
//                    }) {
//                        Text("Confirm")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { showPasswordDialog = false }) {
//                        Text("Cancel")
//                    }
//                },
//                title = { Text("Confirm Password") },
//                text = {
//                    TextField(
//                        value = password,
//                        onValueChange = { password = it },
//                        label = { Text("Enter your password") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            )
//        }
    }
}
