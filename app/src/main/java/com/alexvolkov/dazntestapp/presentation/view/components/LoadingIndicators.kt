package com.alexvolkov.dazntestapp.presentation.view.components

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alexvolkov.dazntestapp.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ShaderLoadingIndicator(
    textColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    val shader = remember { RuntimeShader(shader) }
    var time by remember { mutableFloatStateOf(Random.nextFloat() * 100) }
    var alpha by remember { mutableFloatStateOf(0f) }

    val alphaAnim = animateFloatAsState(
        targetValue = alpha, animationSpec = tween(
            durationMillis = 1500, delayMillis = 300
        )
    )

    shader.setFloatUniform("alpha", alphaAnim.value)

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        ShaderBox(
            modifier = Modifier.size(200.dp),
            shader = shader,
            time = time
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            )
        }
        Text(
            text = "Loading",
            color = textColor.copy(alpha = alphaAnim.value),
            fontWeight = FontWeight.Bold
        )
    }

    LaunchedEffect(Unit) {
        alpha = 1f
        while (true) {
            time += 2f
            delay(32)
        }
    }
}

@Composable
fun DaznLogoLoadingIndicator(
    modifier: Modifier,
) {
    val shader = remember { RuntimeShader(logoShader) }
    var time by remember { mutableFloatStateOf(Random.nextFloat() * 100) }
    var intensity by remember { mutableFloatStateOf(0f) }

    val intensityAnim = animateFloatAsState(
        targetValue = intensity, animationSpec = tween(
            durationMillis = 3000, delayMillis = 50
        )
    )

    shader.setFloatUniform("intensity", intensityAnim.value)

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        ShaderBox(
            modifier = Modifier.size(200.dp),
            shader = shader,
            time = time
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )
        }
    }

    LaunchedEffect(Unit) {
        intensity = 1f
        while (true) {
            time += 0.05f
            delay(32)
        }
    }
}

@Composable
private fun ShaderBox(
    modifier: Modifier,
    shader: RuntimeShader,
    time: Float,
    content: @Composable () -> Unit = {}
) {
    shader.setFloatUniform("time", time)

    Box(modifier = modifier
        .onSizeChanged { size ->
            shader.setFloatUniform(
                "resolution", size.width.toFloat(), size.height.toFloat()
            )
        }
        .graphicsLayer {
            this.renderEffect = RenderEffect
                .createRuntimeShaderEffect(
                    shader, "image"
                )
                .asComposeRenderEffect()
        }) {
        content()
    }
}

val shader = """
    //necessary for the shader to work:
    uniform float2 resolution;
    uniform shader image;
    uniform float time;

    //custom uniforms:
    uniform float alpha;
    //end custom uniforms


    //helper functions:
    vec2 normalizeUv(float2 fragCoord) {
        vec2 uv = fragCoord/resolution.xy - .5;
        return vec2(uv.x * resolution.x/resolution.y, uv.y);
    }

    half4 mergeImageAndShader(float2 fragCoord, vec4 col, vec4 background) {
        return half4(mix(background.rgb, col.rgb, col.a),col.a);
    }
    //end of helper functions

    vec3 hash33(vec3 p3) {
        p3 = fract(p3 * vec3(.1031,.11369,.13787));
        p3 += dot(p3, p3.yxz+19.19);
        return -1.0 + 2.0 * fract(vec3((p3.x + p3.y)*p3.z, (p3.x+p3.z)*p3.y, (p3.y+p3.z)*p3.x));
    }

    float simplex_noise(vec3 p)
    {
        const float K1 = 0.333333333;
        const float K2 = 0.166666667;

        vec3 i = floor(p + (p.x + p.y + p.z) * K1);
        vec3 d0 = p - (i - (i.x + i.y + i.z) * K2);

        vec3 e = step(vec3(0.0), d0 - d0.yzx);
        vec3 i1 = e * (1.0 - e.zxy);
        vec3 i2 = 1.0 - e.zxy * (1.0 - e);

        vec3 d1 = d0 - (i1 - 1.0 * K2);
        vec3 d2 = d0 - (i2 - 2.0 * K2);
        vec3 d3 = d0 - (1.0 - 3.0 * K2);

        vec4 h = max(0.6 - vec4(dot(d0, d0), dot(d1, d1), dot(d2, d2), dot(d3, d3)), 0.0);
        vec4 n = h * h * h * h * vec4(dot(d0, hash33(i)), dot(d1, hash33(i + i1)), dot(d2, hash33(i + i2)), dot(d3, hash33(i + 1.0)));

        return dot(vec4(31.316), n);
    }

    half4 main(float2 fragCoord) {
        vec2 uv = normalizeUv(fragCoord); // get normalized uv coordinates

        float a = cos(atan(uv.y, uv.x));
        float am = abs(a-.5)/4.;
        float l = length(uv);

        float iTime = time*0.01;

        float m1 = clamp(.1/smoothstep(.0, 1.75, l), 0., 1.);
        float m2 = clamp(.1/smoothstep(.42, 0., l), 0., 1.);
        float s1 = (simplex_noise(vec3(uv*2., 1. + iTime*.525))*(max(1.0 - l*1.75, 0.)) + .9);
        float s2 = (simplex_noise(vec3(uv*1., 15. + iTime*.525))*(max(.0 + l*1., .025)) + 1.25);
        float s3 = (simplex_noise(vec3(vec2(am, am*100. + iTime*3.)*.15, 30. + iTime*.525))*(max(.0 + l*1., .25)) + 1.5);
        s3 *= smoothstep(0.0, .3345, l);

        float sh = smoothstep(0.15, .35, l);

        float m = m1*m1*m2 * ((s1*s2*s3) * (1.-l)) * sh;
        vec4 col = vec4((0.5 + 0.5*cos(iTime+uv.xyx*3.+vec3(0,1,3))),m)*(alpha);

        return mergeImageAndShader(fragCoord, col, vec4(image.eval(fragCoord)));
    }
""".trimIndent()

val logoShader = """
    //necessary for the shader to work:
    uniform float2 resolution;
    uniform shader image;
    uniform float time;
    uniform float intensity;


    //helper functions:
    vec2 normalizeUv(float2 fragCoord) {
        vec2 uv = fragCoord/resolution.xy - .5;
        return vec2(uv.x * resolution.x/resolution.y, uv.y);
    }


    half4 main(float2 fragCoord) {
        vec2 uv = normalizeUv(fragCoord); // get normalized uv coordinates
        vec4 img = image.eval(fragCoord);
        vec3 gradient = 0.5 + 0.5 * sin(time + uv.xyx * 3.0 + vec3(1, 3, 5));  
        if(uv.x < -.5 || uv.x > .5 || uv.y < -.5|| uv.y > 0.5) {
           img.a=0.0;
        }
              
       return vec4(vec3(gradient)*img.a*intensity+vec3(gradient)*intensity*0.05, img.a*intensity+0.05*intensity);
    }
""".trimIndent()