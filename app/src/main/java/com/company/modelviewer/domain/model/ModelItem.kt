package com.company.modelviewer.domain.model

data class ModelItem(
    val id: String,
    val name: String,
    val fileSize: String,
    val imageResId: Int,
    val modelUrl: String
)
