package com.company.modelviewer.data.datasource

import com.company.modelviewer.R
import com.company.modelviewer.domain.model.ModelItem

object MockModelDataSource {
    val models = listOf(
        ModelItem("2", "Avocado", "3.0 MB", R.drawable.img_avocado, "models/Avocado.glb"),
        ModelItem("3", "BoomBox", "5.1 MB", R.drawable.img_boombox, "models/BoomBox.glb"),
        ModelItem("4", "Lantern", "1.5 MB", R.drawable.img_lantern, "models/Lantern.glb"),
        ModelItem("5", "Damaged Helmet", "8.2 MB", R.drawable.img_helmet, "models/DamagedHelmet.glb")
    )
}
