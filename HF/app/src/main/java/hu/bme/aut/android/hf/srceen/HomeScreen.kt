package hu.bme.aut.android.hf.srceen

import EditProfilePage
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.hf.pages.WishListPage
import hu.bme.aut.android.hf.pages.FavoritePage
import hu.bme.aut.android.hf.pages.HomePage
import hu.bme.aut.android.hf.pages.ProfilePage
import hu.bme.aut.android.hf.ui.theme.BrownIcon
import hu.bme.aut.android.hf.ui.theme.NavBarColor


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val bottomNavController = rememberNavController()
//    val bottomNavController = navController as NavHostController

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home, HomeNavRoutes.MAIN),
        NavItem("Favorite", Icons.Default.Favorite, HomeNavRoutes.FAVORITE),
        NavItem("Tasted", Icons.Default.CheckCircle, HomeNavRoutes.WISHLIST),
        NavItem("Profile", Icons.Default.Person, HomeNavRoutes.PROFILE),
    )

    Scaffold(
//        contentWindowInsets = WindowInsets(0, 0, 0, 0), // optional
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onSecondary,

            ) {
                val currentRoute = bottomNavController.currentBackStackEntry?.destination?.route
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                bottomNavController.navigate(item.route) {
                                    popUpTo(HomeNavRoutes.MAIN) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = androidx.compose.ui.graphics.Color.White,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = androidx.compose.ui.graphics.Color.White,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary,

                        )

                    )

                }
            }
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = HomeNavRoutes.MAIN,
            modifier = modifier.padding(padding)
        ) {
            composable(HomeNavRoutes.MAIN) { HomePage(modifier, navController) }
            composable(HomeNavRoutes.FAVORITE) { FavoritePage(modifier) }
            composable(HomeNavRoutes.WISHLIST) { WishListPage(modifier) }
            composable(HomeNavRoutes.PROFILE) { ProfilePage(modifier, navController) }
            composable("editProfile") {
                EditProfilePage(modifier, navController)
            }
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)


object HomeNavRoutes {
    const val MAIN = "home/main"
    const val FAVORITE = "home/favorite"
    const val WISHLIST = "home/wishlist"
    const val PROFILE = "home/profile"
}



