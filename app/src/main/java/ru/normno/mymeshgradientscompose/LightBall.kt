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
    uniform float time;        // The elapsed time for animating the light effect
    uniform shader composable; // Shader for the composable content
    
    half4 main(float2 fragCoord) {
        // Initialize output color
        float4 o = float4(0.0);
        
        // Normalize coordinates relative to the canvas center
        float2 u = fragCoord.xy * 2.0 - size.xy;
        float2 s = u / size.y;

        // Loop to calculate the light ball effect
        for (float i = 0.0; i < 180.0; i++) {
            float a = i / 90.0 - 1.0;                       // Calculate a normalized angle
            float sqrtTerm = sqrt(1.0 - a * a);            // Circular boundary constraint
            float2 p = cos(i * 2.4 + time + float2(0.0, 11.0)) * sqrtTerm; // Oscillation term
            
            // Compute position and adjust with distortion
            float2 c = s + float2(p.x, a) / (p.y + 2.0);
            
            // Calculate the distance factor (denominator)
            float denom = dot(c, c);
            
            // Add light intensity with color variation
            float4 cosTerm = cos(i + float4(0.0, 2.0, 4.0, 0.0)) + 1.0;
            o += cosTerm / denom * (1.0 - p.y) / 30000.0;
        }

        // Return final color with an alpha of 1.0
        return half4(o.rgb, 1.0);
    }
""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.lightBall(

) = composed {
    val shader = remember { RuntimeShader(lightBallShader) }
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.016f
            delay(16)
        }
    }

    this.graphicsLayer {
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("time", time)

        renderEffect = RenderEffect
            .createRuntimeShaderEffect(shader, "composable")
            .asComposeRenderEffect()
    }
}