package ru.normno.mymeshgradientscompose

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val lightBallShader = """
    uniform float2 size;       // The size of the canvas in pixels (width, height)
    uniform float time;        // Animation time
    uniform shader composable; // Composable content
    
    half4 main(float2 fragCoord) {
        float4 o = float4(0.0);
        
        float2 u = fragCoord - size * 0.5;
        float2 s = u / size.y;

        for (float i = 0.0; i < 180.0; i++) {
            float a = i / 90.0 - 1.0;
            float sqrtTerm = sqrt(1.0 - a * a);
            float2 p = cos(i * 2.4 + time + float2(0.0, 11.0)) * sqrtTerm;
            
            float2 c = s + float2(p.x, a) / (p.y + 2.0);
            float denom = dot(c, c);
            
            float4 cosTerm = cos(i + float4(0.0, 2.0, 4.0, 0.0)) + 1.0;
            o += cosTerm / denom * (1.0 - p.y) / 300.0; // Ярче
        }

        // Overlay composable content
        float4 baseColor = composable.eval(fragCoord);
        return half4(o.rgb + baseColor.rgb, 1.0);
    }
""".trimIndent()

fun Modifier.lightBall(): Modifier = composed {
    val shader = remember { RuntimeShader(lightBallShader) }
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.016f
            delay(16)
        }
    }

    graphicsLayer {
        shader.setFloatUniform("size", size.width, size.height)
        shader.setFloatUniform("time", time)

        renderEffect = RenderEffect
            .createRuntimeShaderEffect(shader, "composable")
            .asComposeRenderEffect()
    }
}