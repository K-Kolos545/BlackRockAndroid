package hu.bme.aut.android.hf.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkBrownButton, //used for buttons, active states, etc.
    secondary = DarkBrownIcon, //Used for icons or less prominent buttons.
    background = DarkBackGroundColor, //background color for the app
    surface = DarkCardColor, //color for cards, sheets, etc.
    onPrimary = DarkBrownText, //Text/icon color on primary button (white on brown).
    onSecondary = DarkNavBarColor, //Text/icon color on secondary button (white on brown).
    onBackground = DarkBrownText, //Text/icons shown on the dark background.
    onSurface = DarkBrownText //Text/icons shown on the dark surface (cards, sheets, etc.)
)

private val LightColorScheme = lightColorScheme(
    primary = BrownButton,               // used for buttons, FABs, etc.
    secondary = BrownIcon,              // secondary color (can be used for icons)
    background = BackGroundColor,       // global background color
    surface = CardColor,            // card/surface color
    onPrimary = BrownText,            // text/icon on BrownButton
    onSecondary = NavBarColor,
    onBackground = BrownText,           // text on background
    onSurface = BrownText


)

@Composable
fun HFTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
