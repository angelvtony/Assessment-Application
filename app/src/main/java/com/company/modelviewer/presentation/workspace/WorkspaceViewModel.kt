package com.company.modelviewer.presentation.workspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.modelviewer.domain.usecase.GetAvailableModelsUseCase
import com.company.modelviewer.presentation.workspace.intent.WorkspaceEffect
import com.company.modelviewer.presentation.workspace.intent.WorkspaceIntent
import com.company.modelviewer.presentation.workspace.reducer.WorkspaceReducer
import com.company.modelviewer.presentation.workspace.state.WorkspaceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkspaceViewModel @Inject constructor(
    private val getAvailableModelsUseCase: GetAvailableModelsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkspaceState())
    val state: StateFlow<WorkspaceState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorkspaceEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleIntent(WorkspaceIntent.LoadModels)
    }

    fun handleIntent(intent: WorkspaceIntent) {
        when (intent) {
            is WorkspaceIntent.LoadModels -> loadAvailableModels()
            is WorkspaceIntent.AddModel -> updateState { WorkspaceReducer.reduceAddModel(it, intent.modelItem.modelUrl, intent.modelItem.imageResId, intent.modelItem.name) }
            is WorkspaceIntent.RemoveModel -> updateState { WorkspaceReducer.reduceRemoveModel(it, intent.containerId) }
            is WorkspaceIntent.MoveModel -> updateState { WorkspaceReducer.reduceMoveModel(it, intent.containerId, intent.dx, intent.dy) }
            is WorkspaceIntent.ResizeModel -> updateState { WorkspaceReducer.reduceResizeModel(it, intent.containerId, intent.scaleFactor) }
            is WorkspaceIntent.RotateModel -> updateState { WorkspaceReducer.reduceRotateModel(it, intent.containerId, intent.deltaX, intent.deltaY) }
            is WorkspaceIntent.ZoomModel -> updateState { WorkspaceReducer.reduceZoomModel(it, intent.containerId, intent.zoomFactor) }
            is WorkspaceIntent.SelectModel -> updateState { WorkspaceReducer.reduceSelectModel(it, intent.containerId) }
            is WorkspaceIntent.ToggleInteractionMode -> updateState { WorkspaceReducer.reduceToggleInteractionMode(it, intent.containerId) }
            is WorkspaceIntent.OpenBottomSheet -> updateState { it.copy(isBottomSheetVisible = true) }
            is WorkspaceIntent.CloseBottomSheet -> updateState { it.copy(isBottomSheetVisible = false) }
        }
    }

    private fun loadAvailableModels() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            getAvailableModelsUseCase().collect { models ->
                updateState { it.copy(availableModels = models, isLoading = false) }
            }
        }
    }

    private fun updateState(reducer: (WorkspaceState) -> WorkspaceState) {
        _state.update(reducer)
    }

    private fun sendEffect(effect: WorkspaceEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
