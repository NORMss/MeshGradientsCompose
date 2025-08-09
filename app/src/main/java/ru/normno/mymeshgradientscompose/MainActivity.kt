package ru.normno.mymeshgradientscompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.normno.mymeshgradientscompose.ui.theme.MyMeshGradientsComposeTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                        GradientBoxLightBall()
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun GradientBoxWithAnimationMultiplayPoint(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val points = remember(time) {
        List(10) { index ->
            val angle = time + index
            val radius = 0.5f
            val x = 0.5f + radius * cos(angle + index)
            val y = 0.5f + radius * sin(angle + index)
            Point(
                color = Color.hsv((angle * 40 + index * 60) % 360, 1f, 1f),
                offset = Offset(x, y)
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
                points = points,
                time = time,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Animated Gradient",
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun GradientBoxWithBilinearMashGradient(
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
            .bilinearMashGradient(
                firstColor = Color.Cyan,
                secondColor = Color.DarkGray,
                point = Offset(0.2f, 0.2f),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Animated Gradient",
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun GradientBoxAnimatedSpiralMashGradient(
    modifier: Modifier = Modifier,
) {
    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.01f
            if (time > 1000f) time = 0f
            delay(16L)
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
            .animatedSpiralMashGradient(
                firstColor = Color.Cyan,
                secondColor = Color.Blue,
                point = Offset(0.5f, 0.5f),
                time = time,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Animated Gradient",
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun GradientBoxAngularMashGradient(
    modifier: Modifier = Modifier,
) {
    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.01f
            if (time > 1000f) time = 0f
            delay(16L)
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
            .angularMashGradient(
                firstColor = Color.Magenta,
                secondColor = Color.Cyan,
                point = Offset(0.5f, 0.5f)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Test Animated Gradient",
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun GradientBoxLightBall(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .lightBall(),
        contentAlignment = Alignment.Center
    ) { }
}