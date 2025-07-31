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
private val FLAME_SHADER = """
    uniform float2 resolution;
    uniform float time;

    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;
    layout(color) uniform half4 color3;

    float noise(float2 p) {
        return fract(sin(dot(p, float2(12.9898, 78.233))) * 43758.5453);
    }

    half4 palette(float t) {
        return mix(color3, mix(color, color2, t), t);
    }

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / resolution;

        uv = uv * 2.0 - 1.0;

        float r = length(uv);

        float wave = 0.5 + 0.5 * sin(10.0 * r - time * 4.0);

        float fade = smoothstep(1.0, 0.2, r);

        float intensity = wave * fade;

        return palette(intensity) * intensity;
    }

""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.animatedFireMashGradient(
    firstColor: Color,
    secondColor: Color,
    thirdColor: Color,
    time: Float,
) = drawWithCache {
    val shader = RuntimeShader(FLAME_SHADER)
    val shaderBrush = ShaderBrush(shader)

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
        shader.setColorUniform(
            "color3",
            android.graphics.Color.valueOf(
                thirdColor.red,
                thirdColor.green,
                thirdColor.blue,
                thirdColor.alpha
            )
        )

        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("time", time)

        drawRect(brush = shaderBrush)
    }
}