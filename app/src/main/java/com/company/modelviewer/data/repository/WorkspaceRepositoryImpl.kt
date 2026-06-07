package com.company.modelviewer.data.repository

import com.company.modelviewer.data.datasource.MockModelDataSource
import com.company.modelviewer.domain.model.ModelItem
import com.company.modelviewer.domain.repository.WorkspaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkspaceRepositoryImpl @Inject constructor() : WorkspaceRepository {
    override fun getAvailableModels(): Flow<List<ModelItem>> = flow {
        // Simulating a fast local or remote fetch
        emit(MockModelDataSource.models)
    }
}
