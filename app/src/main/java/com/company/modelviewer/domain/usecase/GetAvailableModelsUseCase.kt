package com.company.modelviewer.domain.usecase

import com.company.modelviewer.domain.model.ModelItem
import com.company.modelviewer.domain.repository.WorkspaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableModelsUseCase @Inject constructor(
    private val repository: WorkspaceRepository
) {
    operator fun invoke(): Flow<List<ModelItem>> {
        return repository.getAvailableModels()
    }
}
