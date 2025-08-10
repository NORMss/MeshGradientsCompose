package ru.normno.mymeshgradientscompose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import org.intellij.lang.annotations.Language

@Language("AGSL")
val glowButtonShader = """
  // Shader for a glowing rounded rectangle button

  uniform shader button;              // Input texture or color for the button
  uniform float2 size;                // Button size
  uniform float cornerRadius;         // Corner radius of the button
  uniform float glowRadius;           // Radius of the glow effect
  uniform float glowIntensity;        // Intensity of the glow
  layout(color) uniform half4 glowColor; // Color of the glow

  // Signed Distance Function (SDF) for a rounded rectangle
  float calculateRoundedRectSDF(vec2 position, vec2 rectSize, float radius) {
      vec2 adjustedPosition = abs(position) - rectSize + radius; // Adjust for rounded corners
      return min(max(adjustedPosition.x, adjustedPosition.y), 0.0) 
             + length(max(adjustedPosition, 0.0)) - radius;
  }

  // Function to calculate glow intensity based on distance
  float calculateGlow(float distance, float radius, float intensity) {
      return pow(radius / distance, intensity); // Glow falls off as distance increases
  }

  half4 main(float2 coord) {
      // Normalize coordinates and aspect ratio
      float aspectRatio = size.y / size.x;
      float2 normalizedPosition = coord.xy / size;
      normalizedPosition.y *= aspectRatio;

      // Define normalized rectangle size and center
      float2 normalizedRect = float2(1.0, aspectRatio);
      float2 normalizedRectCenter = normalizedRect / 2.0;
      normalizedPosition -= normalizedRectCenter;

      // Calculate normalized corner radius and distance
      float normalizedRadius = aspectRatio / 2.0;
      float distanceToRect = calculateRoundedRectSDF(normalizedPosition, normalizedRectCenter, normalizedRadius);

      // Get the button's color
      half4 buttonColor = button.eval(coord);

      // Inside the rounded rectangle, return the button's original color
      if (distanceToRect < 0.0) {
        return buttonColor;
      }

      // Outside the rectangle, calculate glow effect
      float glow = calculateGlow(distanceToRect, glowRadius, glowIntensity);
      half4 glowEffect = glow * glowColor;

      // Apply tone mapping to the glow for a natural look
      glowEffect = 1.0 - exp(-glowEffect);

      return glowEffect;
  }
""".trimIndent()


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GlowButton(
    modifier: Modifier = Modifier,
    text: String = "Click me",
    cornerRadius: Float = 30f,
    glowRadius: Float = 40f,
    glowIntensity: Float = 3f,
    glowColor: Color = Color.Cyan,
    onClick: () -> Unit
) {
    val shader = remember { RuntimeShader(glowButtonShader) }

    Box(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            shader.setFloatUniform("size", size.width, size.height)
            shader.setFloatUniform("cornerRadius", cornerRadius)
            shader.setFloatUniform("glowRadius", glowRadius)
            shader.setFloatUniform("glowIntensity", glowIntensity)
            shader.setColorUniform("glowColor", glowColor.toArgb())

            // подаём в шейдер градиент кнопки
            val buttonGradient = android.graphics.LinearGradient(
                0f, 0f, size.width, size.height,
                Color(0xFF1E88E5).toArgb(),
                Color(0xFF42A5F5).toArgb(),
                android.graphics.Shader.TileMode.CLAMP
            )
            shader.setInputShader("button", buttonGradient)

            drawRect(
                brush = ShaderBrush(shader),
                size = size
            )
        }

        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_7_PRO,)
@Composable
private fun GlowButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GlowButton(
            onClick = {

            },
            text = "Click Me",
            modifier = Modifier.size(100.dp, 50.dp),
        )
    }
}
