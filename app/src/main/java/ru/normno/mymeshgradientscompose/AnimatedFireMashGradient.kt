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
uniform float time;

half4 palette(float t) {
    half3 a = half3(1.0, 0.5, 0.0); // оранжевый
    half3 b = half3(1.0, 1.0, 0.0); // жёлтый
    half3 c = half3(1.0, 0.0, 0.0); // красный
    return half4(mix(c, mix(a, b, t), t), 1.0);
}

float noise(float2 p) {
    return fract(sin(dot(p, float2(12.9898, 78.233))) * 43758.5453);
}

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / resolution;
    uv.y = 1.0 - uv.y;

    float n = noise(float2(uv.x * 10.0, uv.y * 10.0 + time * 2.0));

    float flameShape = smoothstep(0.2, 0.8, uv.y + n * 0.3 - uv.x * uv.x * 1.5);

    return palette(flameShape) * flameShape;
}

""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.animatedFireMashGradient(
    firstColor: Color,
    secondColor: Color,
    time: Float,
) = drawWithCache {
    val shader = RuntimeShader(FLAME_SHADER)
    val shaderBrush = ShaderBrush(shader)

    onDrawBehind {
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("time", time)

        drawRect(brush = shaderBrush)
    }
}