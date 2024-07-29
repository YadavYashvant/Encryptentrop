package com.example.encrytentrop.animations.Neon

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

class NeonIndication(private val shape: Shape, private val borderWidth: Dp) : Indication {

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        // key the remember against interactionSource, so if it changes we create a new instance
        val instance = remember(interactionSource) {
            NeonIndicationInstance(
                shape,
                // Double the border size for a stronger press effect
                borderWidth * 2
            )
        }

        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> instance.animateToPressed(interaction.pressPosition, this)
                    is PressInteraction.Release -> instance.animateToResting(this)
                    is PressInteraction.Cancel -> instance.animateToResting(this)
                }
            }
        }

        return instance
    }

    private class NeonIndicationInstance(
        private val shape: Shape,
        private val borderWidth: Dp
    ) : IndicationInstance {
        var currentPressPosition: Offset = Offset.Zero
        val animatedProgress = Animatable(0f)
        val animatedPressAlpha = Animatable(1f)

        var pressedAnimation: Job? = null
        var restingAnimation: Job? = null

        fun animateToPressed(pressPosition: Offset, scope: CoroutineScope) {
            val currentPressedAnimation = pressedAnimation
            pressedAnimation = scope.launch {
                // Finish any existing animations, in case of a new press while we are still showing
                // an animation for a previous one
                restingAnimation?.cancelAndJoin()
                currentPressedAnimation?.cancelAndJoin()
                currentPressPosition = pressPosition
                animatedPressAlpha.snapTo(1f)
                animatedProgress.snapTo(0f)
                animatedProgress.animateTo(1f, tween(450))
            }
        }

        fun animateToResting(scope: CoroutineScope) {
            restingAnimation = scope.launch {
                // Wait for the existing press animation to finish if it is still ongoing
                pressedAnimation?.join()
                animatedPressAlpha.animateTo(0f, tween(250))
                animatedProgress.snapTo(0f)
            }
        }


        override fun ContentDrawScope.drawIndication() {
            val (startPosition, endPosition) = calculateGradientStartAndEndFromPressPosition(
                currentPressPosition, size
            )
            val brush = animateBrush(
                startPosition = startPosition,
                endPosition = endPosition,
                progress = animatedProgress.value
            )
            val alpha = animatedPressAlpha.value

            drawContent()

            val outline = shape.createOutline(size, layoutDirection, this)
            // Draw overlay on top of content
            drawOutline(
                outline = outline,
                brush = brush,
                alpha = alpha * 0.1f
            )
            // Draw border on top of overlay
            drawOutline(
                outline = outline,
                brush = brush,
                alpha = alpha,
                style = Stroke(width = borderWidth.toPx())
            )

        }

        private fun calculateGradientStartAndEndFromPressPosition(
            pressPosition: Offset,
            size: Size
        ): Pair<Offset, Offset> {
            // Calculate the start and end positions for the gradient based on the press position
            val startPosition = pressPosition
            val endPosition = Offset(size.width - pressPosition.x, size.height - pressPosition.y)
            return Pair(startPosition, endPosition)
        }

        private fun animateBrush(
            startPosition: Offset,
            endPosition: Offset,
            progress: Float
        ): Brush {
            // Create a linear gradient brush that animates based on the progress
            return Brush.linearGradient(
                colors = listOf(
                    androidx.compose.ui.graphics.Color.Yellow,
                    androidx.compose.ui.graphics.Color.Magenta
                ),
                start = startPosition,
                end = Offset(
                    startPosition.x + (endPosition.x - startPosition.x) * progress,
                    startPosition.y + (endPosition.y - startPosition.y) * progress
                )
            )
        }
    }
}