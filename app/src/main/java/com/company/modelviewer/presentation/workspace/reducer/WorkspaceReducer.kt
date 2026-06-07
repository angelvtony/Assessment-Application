package com.company.modelviewer.presentation.workspace.reducer

import com.company.modelviewer.domain.model.ModelContainer
import com.company.modelviewer.presentation.workspace.state.WorkspaceState
import java.util.UUID

object WorkspaceReducer {

    fun reduceAddModel(currentState: WorkspaceState, modelUrl: String, imageResId: Int, name: String): WorkspaceState {
        val newContainer = ModelContainer(
            id = UUID.randomUUID().toString(),
            name = name,
            imageResId = imageResId,
            modelUrl = modelUrl,
            positionX = 100f,
            positionY = 100f,
            width = 300f,
            height = 300f
        )
        val updatedModels = currentState.models + newContainer
        return currentState.copy(
            models = updatedModels,
            activeModelCount = updatedModels.size,
            selectedModelId = newContainer.id,
            isBottomSheetVisible = false
        )
    }

    fun reduceRemoveModel(currentState: WorkspaceState, containerId: String): WorkspaceState {
        val updatedModels = currentState.models.filter { it.id != containerId }
        return currentState.copy(
            models = updatedModels,
            activeModelCount = updatedModels.size,
            selectedModelId = if (currentState.selectedModelId == containerId) null else currentState.selectedModelId
        )
    }

    fun reduceMoveModel(currentState: WorkspaceState, containerId: String, dx: Float, dy: Float): WorkspaceState {
        val updatedModels = currentState.models.map { container ->
            if (container.id == containerId && !container.isInteractionMode) {
                container.copy(
                    positionX = container.positionX + dx,
                    positionY = container.positionY + dy
                )
            } else container
        }
        return currentState.copy(models = updatedModels)
    }

    fun reduceResizeModel(currentState: WorkspaceState, containerId: String, scaleFactor: Float): WorkspaceState {
        val updatedModels = currentState.models.map { container ->
            if (container.id == containerId && !container.isInteractionMode) {
                container.copy(
                    width = (container.width * scaleFactor).coerceIn(200f, 800f),
                    height = (container.height * scaleFactor).coerceIn(200f, 800f)
                )
            } else container
        }
        return currentState.copy(models = updatedModels)
    }

    fun reduceRotateModel(currentState: WorkspaceState, containerId: String, deltaX: Float, deltaY: Float): WorkspaceState {
        val updatedModels = currentState.models.map { container ->
            if (container.id == containerId && container.isInteractionMode) {
                container.copy(
                    rotationX = container.rotationX + deltaY,
                    rotationY = container.rotationY + deltaX
                )
            } else container
        }
        return currentState.copy(models = updatedModels)
    }

    fun reduceZoomModel(currentState: WorkspaceState, containerId: String, zoomFactor: Float): WorkspaceState {
        val updatedModels = currentState.models.map { container ->
            if (container.id == containerId && container.isInteractionMode) {
                container.copy(scale = (container.scale * zoomFactor).coerceIn(0.5f, 3f))
            } else container
        }
        return currentState.copy(models = updatedModels)
    }

    fun reduceSelectModel(currentState: WorkspaceState, containerId: String?): WorkspaceState {
        val updatedModels = currentState.models.map { container ->
            container.copy(isSelected = container.id == containerId)
        }
        return currentState.copy(models = updatedModels, selectedModelId = containerId)
    }

    fun reduceToggleInteractionMode(currentState: WorkspaceState, containerId: String): WorkspaceState {
        val updatedModels = currentState.models.map { container ->
            if (container.id == containerId) {
                container.copy(isInteractionMode = !container.isInteractionMode)
            } else {
                container.copy(isInteractionMode = false)
            }
        }
        return currentState.copy(models = updatedModels, selectedModelId = containerId)
    }
}
