package com.company.modelviewer.presentation.workspace.state
import com.company.modelviewer.domain.model.ModelContainer
import com.company.modelviewer.domain.model.ModelItem

data class WorkspaceState(
    val models: List<ModelContainer> = emptyList(),
    val availableModels: List<ModelItem> = emptyList(),
    val selectedModelId: String? = null,
    val isBottomSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val activeModelCount: Int = 0
)
