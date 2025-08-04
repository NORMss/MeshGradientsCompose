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

data class Point(
    val color: Color,
    val offset: Offset,
)

@Language("AGSL")
private val CUSTOM_SHADER = """uniform float time;
uniform float2 resolution;

uniform int pointCount;

uniform float2 points_0;
uniform float2 points_1;
uniform float2 points_2;
uniform float2 points_3;
uniform float2 points_4;
uniform float2 points_5;
uniform float2 points_6;
uniform float2 points_7;
uniform float2 points_8;
uniform float2 points_9;

layout(color) uniform half4 colors_0;
layout(color) uniform half4 colors_1;
layout(color) uniform half4 colors_2;
layout(color) uniform half4 colors_3;
layout(color) uniform half4 colors_4;
layout(color) uniform half4 colors_5;
layout(color) uniform half4 colors_6;
layout(color) uniform half4 colors_7;
layout(color) uniform half4 colors_8;
layout(color) uniform half4 colors_9;

half4 main(in float2 fragCoord){
    float2 uv = fragCoord / resolution.xy;
    half4 color = half4(0.0);
    float totalWeight = 0.0;

    if (pointCount > 0) {
        float d = distance(uv, points_0);
        float w = 1.0 / (d + 0.001);
        color += colors_0 * w;
        totalWeight += w;
    }
    if (pointCount > 1) {
        float d = distance(uv, points_1);
        float w = 1.0 / (d + 0.001);
        color += colors_1 * w;
        totalWeight += w;
    }
    if (pointCount > 2) {
        float d = distance(uv, points_2);
        float w = 1.0 / (d + 0.001);
        color += colors_2 * w;
        totalWeight += w;
    }
    if (pointCount > 3) {
        float d = distance(uv, points_3);
        float w = 1.0 / (d + 0.001);
        color += colors_3 * w;
        totalWeight += w;
    }
    if (pointCount > 4) {
        float d = distance(uv, points_4);
        float w = 1.0 / (d + 0.001);
        color += colors_4 * w;
        totalWeight += w;
    }
    if (pointCount > 5) {
        float d = distance(uv, points_5);
        float w = 1.0 / (d + 0.001);
        color += colors_5 * w;
        totalWeight += w;
    }
    if (pointCount > 6) {
        float d = distance(uv, points_6);
        float w = 1.0 / (d + 0.001);
        color += colors_6 * w;
        totalWeight += w;
    }
    if (pointCount > 7) {
        float d = distance(uv, points_7);
        float w = 1.0 / (d + 0.001);
        color += colors_7 * w;
        totalWeight += w;
    }
    if (pointCount > 8) {
        float d = distance(uv, points_8);
        float w = 1.0 / (d + 0.001);
        color += colors_8 * w;
        totalWeight += w;
    }
    if (pointCount > 9) {
        float d = distance(uv, points_9);
        float w = 1.0 / (d + 0.001);
        color += colors_9 * w;
        totalWeight += w;
    }

    return color / totalWeight;
}
""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.animatedMultiplayPointMashGradient(
    points: List<Point>,
    time: Float,
) = drawWithCache {
    val shader = RuntimeShader(CUSTOM_SHADER)
    val shaderBrush = ShaderBrush(shader)

    onDrawBehind {
        shader.setFloatUniform("time", time)
        shader.setFloatUniform("resolution", size.width, size.height)

        val count = points.size.coerceAtMost(10)
        shader.setIntUniform("pointCount", count)

        for (i in 0 until count) {
            val pointName = "points_$i"
            val colorName = "colors_$i"

            val normalizedX = points[i].offset.x / size.width
            val normalizedY = points[i].offset.y / size.height

            shader.setFloatUniform(pointName, normalizedX, normalizedY)
            shader.setColorUniform(
                colorName,
                android.graphics.Color.valueOf(
                    points[i].color.red,
                    points[i].color.green,
                    points[i].color.blue,
                    points[i].color.alpha,
                )
            )
        }

        drawRect(shaderBrush)
    }
}
