package hu.bme.aut.android.hf

import EditProfilePage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import hu.bme.aut.android.hf.pages.CategoryProductsPage
import hu.bme.aut.android.hf.pages.ProductDetailPage
import hu.bme.aut.android.hf.pages.ProfilePage
import hu.bme.aut.android.hf.srceen.AuthScreen
import hu.bme.aut.android.hf.srceen.HomeScreen
import hu.bme.aut.android.hf.srceen.LoginScreen
import hu.bme.aut.android.hf.srceen.SignupScreen


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if (isLoggedIn) "home" else "auth"

        NavHost(navController = navController, startDestination = firstPage) {
            // Define your composable destinations here
            // Example:
            composable("auth") { AuthScreen(
                modifier,
                navController
            ) }

            composable("login") {
                LoginScreen(modifier = modifier,
                    navController
                )
            }

            composable("signUp") {
                SignupScreen(modifier = modifier,
                    navController
                )
            }

            composable("home") {
                HomeScreen(modifier = modifier,
                    navController
                )
            }

            composable("profile") {
                ProfilePage(modifier = modifier,
                    navController
                )
            }

            composable("category-products/{categoryId}"){
                var categoryId = it.arguments?.getString("categoryId")
                CategoryProductsPage(modifier, categoryId?: "" )
            }

            composable("product-details/{productId}"){
                var productId = it.arguments?.getString("productId")
                ProductDetailPage(modifier, productId?: "" , )
            }
            composable("editProfile") {
                EditProfilePage(modifier = modifier,
                    navController)
            }

        }
    }

}

object GlobalNavigation{
    lateinit var navController: NavController
}