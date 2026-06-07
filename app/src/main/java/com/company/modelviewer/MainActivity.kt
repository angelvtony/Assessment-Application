package com.company.modelviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.company.modelviewer.presentation.workspace.WorkspaceScreen
import com.company.modelviewer.core.designsystem.Workspace3DTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Workspace3DTheme {
                WorkspaceScreen()
            }
        }
    }
}
