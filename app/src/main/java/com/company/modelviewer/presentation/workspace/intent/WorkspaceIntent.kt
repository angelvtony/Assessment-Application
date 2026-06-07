package com.company.modelviewer.presentation.workspace.intent
import com.company.modelviewer.domain.model.ModelItem

sealed interface WorkspaceIntent {
    data object LoadModels : WorkspaceIntent
    data class AddModel(val modelItem: ModelItem) : WorkspaceIntent
    data class RemoveModel(val containerId: String) : WorkspaceIntent
    data class MoveModel(val containerId: String, val dx: Float, val dy: Float) : WorkspaceIntent
    data class ResizeModel(val containerId: String, val scaleFactor: Float) : WorkspaceIntent
    data class RotateModel(val containerId: String, val deltaX: Float, val deltaY: Float) : WorkspaceIntent
    data class ZoomModel(val containerId: String, val zoomFactor: Float) : WorkspaceIntent
    data class SelectModel(val containerId: String?) : WorkspaceIntent
    data class ToggleInteractionMode(val containerId: String) : WorkspaceIntent
    data object OpenBottomSheet : WorkspaceIntent
    data object CloseBottomSheet : WorkspaceIntent
}
