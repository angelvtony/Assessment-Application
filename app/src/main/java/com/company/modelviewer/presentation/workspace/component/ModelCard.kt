package com.company.modelviewer.presentation.workspace.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import com.company.modelviewer.presentation.workspace.intent.WorkspaceIntent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.LightNode
import com.google.android.filament.LightManager
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import com.company.modelviewer.domain.model.ModelContainer
import com.google.android.filament.Engine
import io.github.sceneview.loaders.ModelLoader
import kotlin.math.roundToInt

@Composable
fun ModelCard(
    container: ModelContainer,
    onIntent: (WorkspaceIntent) -> Unit,
    engine: Engine,
    modelLoader: ModelLoader
) {
    val isSelected = container.isSelected
    val isInteraction = container.isInteractionMode

    val elevation by animateFloatAsState(
        targetValue = if (isSelected || isInteraction) 16f else 4f,
        animationSpec = spring(),
        label = "elevation"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isInteraction -> MaterialTheme.colorScheme.primary
            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        },
        label = "borderColor"
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(container.positionX.roundToInt(), container.positionY.roundToInt()) }
            .size(width = container.width.dp, height = container.height.dp)
            .shadow(
                elevation = elevation.dp, 
                shape = RoundedCornerShape(16.dp), 
                spotColor = Color.Black.copy(alpha = 0.04f),
                ambientColor = Color.Black.copy(alpha = 0.04f)
            )
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .pointerInput(container.id, isInteraction) {
                detectTransformGestures { _, pan, zoom, _ ->
                    if (isInteraction) {
                        onIntent(WorkspaceIntent.RotateModel(container.id, pan.x * 0.5f, pan.y * 0.5f))
                        onIntent(WorkspaceIntent.ZoomModel(container.id, zoom))
                    } else {
                        onIntent(WorkspaceIntent.MoveModel(container.id, pan.x, pan.y))
                        onIntent(WorkspaceIntent.ResizeModel(container.id, zoom))
                    }
                }
            }
            .clickable(indication = null, interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }) {
                onIntent(WorkspaceIntent.SelectModel(container.id))
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = container.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    if (isInteraction) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Interacting",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                Row {
                    IconButton(
                        onClick = { onIntent(WorkspaceIntent.ToggleInteractionMode(container.id)) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.TouchApp,
                            contentDescription = "Interact",
                            tint = if (isInteraction) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { onIntent(WorkspaceIntent.RemoveModel(container.id)) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                
                val modelNode = rememberNode {
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(container.modelUrl),
                        scaleToUnits = 1.0f
                    )
                }

                val keyLight = rememberNode {
                    LightNode(engine = engine, type = LightManager.Type.DIRECTIONAL) {
                        direction(-1.0f, -1.0f, -1.0f)
                        intensity(120_000.0f)
                    }
                }
                val fillLight = rememberNode {
                    LightNode(engine = engine, type = LightManager.Type.DIRECTIONAL) {
                        direction(1.0f, -0.5f, -0.5f)
                        intensity(60_000.0f)
                    }
                }
                val backLight = rememberNode {
                    LightNode(engine = engine, type = LightManager.Type.DIRECTIONAL) {
                        direction(0.0f, -0.5f, 1.0f)
                        intensity(80_000.0f)
                    }
                }
                LaunchedEffect(container.rotationX, container.rotationY, container.scale) {
                    try {
                        modelNode.rotation = io.github.sceneview.math.Rotation(
                            x = container.rotationX,
                            y = container.rotationY,
                            z = 0f
                        )
                        modelNode.scale = io.github.sceneview.math.Scale(container.scale)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                Scene(
                    modifier = Modifier.fillMaxSize(),
                    engine = engine,
                    modelLoader = modelLoader,
                    childNodes = listOf(modelNode, keyLight, fillLight, backLight)
                )
            }
        }
    }
}
