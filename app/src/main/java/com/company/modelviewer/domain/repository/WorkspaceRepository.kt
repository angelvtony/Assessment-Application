package com.company.modelviewer.domain.repository

import com.company.modelviewer.domain.model.ModelItem
import kotlinx.coroutines.flow.Flow

interface WorkspaceRepository {
    fun getAvailableModels(): Flow<List<ModelItem>>
}
