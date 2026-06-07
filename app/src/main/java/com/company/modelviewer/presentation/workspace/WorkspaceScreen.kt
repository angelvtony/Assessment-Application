package com.company.modelviewer.presentation.workspace

import android.widget.Toast
import com.company.modelviewer.presentation.workspace.intent.WorkspaceEffect
import com.company.modelviewer.presentation.workspace.intent.WorkspaceIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.modelviewer.presentation.workspace.component.*
import com.google.android.filament.Engine
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader

@Composable
fun WorkspaceScreen(
    viewModel: WorkspaceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WorkspaceEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(activeModelCount = state.activeModelCount)
        },
        floatingActionButton = {
            AddModelFab(onClick = { viewModel.handleIntent(WorkspaceIntent.OpenBottomSheet) })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.models.isEmpty()) {
                EmptyState(onBrowseClick = { viewModel.handleIntent(WorkspaceIntent.OpenBottomSheet) })
            } else {
                WorkspaceCanvas(
                    models = state.models,
                    onIntent = { viewModel.handleIntent(it) },
                    engine = engine,
                    modelLoader = modelLoader
                )
            }
            PerformancePanel(
                activeModelCount = state.activeModelCount,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }

    if (state.isBottomSheetVisible) {
        BottomSheet(
            availableModels = state.availableModels,
            onDismiss = { viewModel.handleIntent(WorkspaceIntent.CloseBottomSheet) },
            onModelSelect = { model ->
                viewModel.handleIntent(WorkspaceIntent.AddModel(model))
            }
        )
    }
}
