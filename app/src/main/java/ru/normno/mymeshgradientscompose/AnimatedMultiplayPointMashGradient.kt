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
private val CUSTOM_SHADER = """
    uniform float time;
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

    half4 getColor(int i) {
        if (i == 0) return colors_0;
        if (i == 1) return colors_1;
        if (i == 2) return colors_2;
        if (i == 3) return colors_3;
        if (i == 4) return colors_4;
        if (i == 5) return colors_5;
        if (i == 6) return colors_6;
        if (i == 7) return colors_7;
        if (i == 8) return colors_8;
        return colors_9;
    }

    float2 getPoint(int i) {
        if (i == 0) return points_0;
        if (i == 1) return points_1;
        if (i == 2) return points_2;
        if (i == 3) return points_3;
        if (i == 4) return points_4;
        if (i == 5) return points_5;
        if (i == 6) return points_6;
        if (i == 7) return points_7;
        if (i == 8) return points_8;
        return points_9;
    }

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;
        half4 color = half4(0.0);
        float totalWeight = 0.0;

        for (int i = 0; i < pointCount; i++) {
            float2 point = getPoint(i);
            float dist = distance(uv, point);
            float weight = 1.0 / (dist + 0.001);
            color += getColor(i) * weight;
            totalWeight += weight;
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
