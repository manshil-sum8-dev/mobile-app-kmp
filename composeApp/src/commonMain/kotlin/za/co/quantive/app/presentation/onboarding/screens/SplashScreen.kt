package za.co.quantive.app.presentation.onboarding.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import quantive.composeapp.generated.resources.Res
import quantive.composeapp.generated.resources.quantive_logo

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animate logo scale and alpha
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800),
        )
        logoAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800),
        )

        // Animate text after logo
        delay(300)
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600),
        )

        // Hold splash for branding effect
        delay(1200)
        onSplashComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Quantive Logo with animation
            Image(
                painter = painterResource(Res.drawable.quantive_logo),
                contentDescription = "Quantive Logo",
                modifier = Modifier
                    .size(160.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value),
            )

            // Quantive Brand Text
            Text(
                text = "Quantive",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(textAlpha.value),
            )

            Text(
                text = "Invoice in Seconds",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(textAlpha.value),
            )
        }
    }
}
