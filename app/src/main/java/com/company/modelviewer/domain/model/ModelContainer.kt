package com.company.modelviewer.domain.model

data class ModelContainer(
    val id: String,
    val name: String,
    val imageResId: Int,
    val modelUrl: String,
    val positionX: Float,
    val positionY: Float,
    val width: Float,
    val height: Float,
    val isInteractionMode: Boolean = false,
    val isSelected: Boolean = false,
    val rotationX: Float = 0f,
    val rotationY: Float = 0f,
    val scale: Float = 1f
)
