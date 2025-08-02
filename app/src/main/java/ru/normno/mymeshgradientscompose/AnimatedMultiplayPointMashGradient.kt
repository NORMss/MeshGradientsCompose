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
    
    uniform float2 point;
    
    layout(color) uniform half4 primaryColor; 
    layout(color) uniform half4 secondaryColor;
    
    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        
        float mixValue = distance(uv, point);
        return mix(primaryColor, secondaryColor, mixValue)
    }
""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.animatedMultiplayPointMashGradient(
    primaryColor: Color,
    secondaryColor: Color,
    points: List<Point>,
    time: Float,
) = drawWithCache {
    val shader = RuntimeShader(CUSTOM_SHADER)
    val shaderBrush = ShaderBrush(shader)

    onDrawBehind {
        shader.setColorUniform(
            "primaryColor",
            android.graphics.Color.valueOf(
                primaryColor.red,
                primaryColor.green,
                primaryColor.blue,
                primaryColor.alpha,
            )
        )

        shader.setColorUniform(
            "secondaryColor",
            android.graphics.Color.valueOf(
                secondaryColor.red,
                secondaryColor.green,
                secondaryColor.blue,
                secondaryColor.alpha,
            )
        )

        shader.setFloatUniform(
            "resolution",
            size.width,
            size.height,
        )

        shader.setFloatUniform(
            "point",
            points.first().offset.x,
            points.first().offset.y,
        )

        shader.setFloatUniform(
            "time",
            time,
        )

        drawRect(shaderBrush)
    }
}