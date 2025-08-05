package ru.normno.mymeshgradientscompose

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val CUSTOM_SHADER = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;
    uniform float2 point;

    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord / resolution;

        float dist = distance(uv, point);
        
        float t = clamp(dist, 0.0, 1.0);
        t = t * t * (3.0 - 2.0 * t); // bi-cubic smoothing (Hermite)

        return mix(color, color2, t);
    }

""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.bilinearMashGradient(
    firstColor: Color,
    secondColor: Color,
    point: Offset,
) = drawWithCache {
    val shader = RuntimeShader(CUSTOM_SHADER)
    val shaderBrush = ShaderBrush(shader)

    shader.setFloatUniform("resolution", size.width, size.height)

    onDrawBehind {
        shader.setColorUniform(
            "color",
            android.graphics.Color.valueOf(
                firstColor.red,
                firstColor.green,
                firstColor.blue,
                firstColor.alpha
            )
        )
        shader.setColorUniform(
            "color2",
            android.graphics.Color.valueOf(
                secondColor.red,
                secondColor.green,
                secondColor.blue,
                secondColor.alpha
            )
        )
        shader.setFloatUniform("point", point.x, point.y)

        drawRect(brush = shaderBrush)
    }
}
