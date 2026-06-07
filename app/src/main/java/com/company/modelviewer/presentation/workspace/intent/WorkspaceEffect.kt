package com.company.modelviewer.presentation.workspace.intent

sealed interface WorkspaceEffect {
    data class ShowToast(val message: String) : WorkspaceEffect
}
