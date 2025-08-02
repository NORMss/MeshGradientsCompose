package ru.normno.mymeshgradientscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.normno.mymeshgradientscompose.ui.theme.MyMeshGradientsComposeTheme
import kotlin.collections.mutableListOf
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMeshGradientsComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        GradientBoxWithAnimationMultiplayPoint()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(
                0.6f,
            )
            .aspectRatio(1f)
            .clip(
                RoundedCornerShape(
                    16.dp,
                )
            )
            .basicMashGradient(
                firstColor = Color.Cyan,
                secondColor = Color.DarkGray,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Mash Gradient",
        )
    }
}

@Preview
@Composable
fun GradientBoxWithAnimation(
    modifier: Modifier = Modifier,
) {
    val animatedX = remember { Animatable(0f) }
    val animatedY = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            animatedX.animateTo(
                targetValue = Random.nextFloat(),
                animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
            )
            animatedY.animateTo(
                targetValue = Random.nextFloat(),
                animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(
                0.6f,
            )
            .aspectRatio(1f)
            .clip(
                RoundedCornerShape(
                    16.dp,
                )
            )
            .animatedMashGradient(
                firstColor = Color.Cyan,
                secondColor = Color.DarkGray,
                point = Offset(animatedX.value, animatedY.value)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Animated Mash Gradient",
        )
    }
}

@Preview
@Composable
fun GradientBoxWithAnimationFire(
    modifier: Modifier = Modifier,
) {
    val time = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time.animateTo(
                targetValue = time.value + 1f,
                animationSpec = tween(durationMillis = 1024, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(
                0.6f,
            )
            .aspectRatio(1f)
            .clip(
                RoundedCornerShape(
                    16.dp,
                )
            )
            .animatedFireMashGradient(
                firstColor = Color.Cyan,
                secondColor = Color.DarkGray,
                thirdColor = Color.Red,
                time = time.value,
            )
            .clickable {

            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Animated Fire Mash Gradient",
        )
    }
}

@Preview
@Composable
fun GradientBoxWithAnimationMultiplayPoint(
    modifier: Modifier = Modifier,
) {
    val points = remember {
        mutableListOf<Point>()
    }

    val animatedX = remember { Animatable(0f) }
    val animatedY = remember { Animatable(1f) }

    val time = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time.animateTo(
                targetValue = time.value + 1f,
                animationSpec = tween(durationMillis = 1024, easing = LinearEasing)
            )
            animatedX.animateTo(
                targetValue = Random.nextFloat(),
                animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
            )
            animatedY.animateTo(
                targetValue = Random.nextFloat(),
                animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(
                0.6f,
            )
            .aspectRatio(1f)
            .clip(
                RoundedCornerShape(
                    16.dp,
                )
            )
            .animatedMultiplayPointMashGradient(
                primaryColor = MaterialTheme.colorScheme.primary,
                secondaryColor = MaterialTheme.colorScheme.secondary,
                points = listOf(
                    Point(
                        color = Color.Yellow,
                        offset = Offset(
                            x = animatedX.value,
                            y = animatedY.value,
                        )
                    )
                ),
                time = time.value,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Animated Gradient",
        )
    }
}