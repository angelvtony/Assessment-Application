package com.company.modelviewer.presentation.workspace.component

import androidx.compose.foundation.background
import com.company.modelviewer.presentation.workspace.intent.WorkspaceIntent
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.company.modelviewer.domain.model.ModelContainer
import com.google.android.filament.Engine
import io.github.sceneview.loaders.ModelLoader

@Composable
fun WorkspaceCanvas(
    models: List<ModelContainer>,
    onIntent: (WorkspaceIntent) -> Unit,
    engine: Engine,
    modelLoader: ModelLoader,
    modifier: Modifier = Modifier
) {
    val dotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                val dotRadius = 1.dp.toPx()
                val spacing = 24.dp.toPx()
                for (x in 0..size.width.toInt() step spacing.toInt()) {
                    for (y in 0..size.height.toInt() step spacing.toInt()) {
                        drawCircle(
                            color = dotColor,
                            radius = dotRadius,
                            center = Offset(x.toFloat(), y.toFloat())
                        )
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onIntent(WorkspaceIntent.SelectModel(null)) },
                    onDrag = { _, _ -> }
                )
            }
    ) {
        models.forEach { container ->
            ModelCard(
                container = container,
                onIntent = onIntent,
                engine = engine,
                modelLoader = modelLoader
            )
        }
    }
}
