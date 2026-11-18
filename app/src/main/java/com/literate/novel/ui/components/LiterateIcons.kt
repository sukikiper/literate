package com.literate.novel.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object LiterateIcons {

    val Bookmark: ImageVector by lazy {
        ImageVector.Builder("Bookmark", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(17f, 3f)
                horizontalLineTo(7f)
                curveTo(5.9f, 3f, 5f, 3.9f, 5f, 5f)
                verticalLineTo(21f)
                lineTo(12f, 18f)
                lineTo(19f, 21f)
                verticalLineTo(5f)
                curveTo(19f, 3.9f, 18.1f, 3f, 17f, 3f)
                close()
            }
        }.build()
    }

    val BookmarkBorder: ImageVector by lazy {
        ImageVector.Builder("BookmarkBorder", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(17f, 3f)
                horizontalLineTo(7f)
                curveTo(5.9f, 3f, 5f, 3.9f, 5f, 5f)
                verticalLineTo(21f)
                lineTo(12f, 18f)
                lineTo(19f, 21f)
                verticalLineTo(5f)
                curveTo(19f, 3.9f, 18.1f, 3f, 17f, 3f)
                close()
                moveTo(17f, 18f)
                lineTo(12f, 15.82f)
                lineTo(7f, 18f)
                verticalLineTo(5f)
                horizontalLineTo(17f)
                verticalLineTo(18f)
                close()
            }
        }.build()
    }

    val ChevronRight: ImageVector by lazy {
        ImageVector.Builder("ChevronRight", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(10f, 6f)
                lineTo(8.59f, 7.41f)
                lineTo(13.17f, 12f)
                lineTo(8.59f, 16.59f)
                lineTo(10f, 18f)
                lineTo(16f, 12f)
                close()
            }
        }.build()
    }

    val ImagePlaceholder: ImageVector by lazy {
        ImageVector.Builder("ImagePlaceholder", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19f, 5f)
                verticalLineTo(19f)
                horizontalLineTo(5f)
                verticalLineTo(5f)
                horizontalLineTo(19f)
                moveTo(19f, 3f)
                horizontalLineTo(5f)
                curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
                verticalLineTo(19f)
                curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
                horizontalLineTo(19f)
                curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
                verticalLineTo(5f)
                curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
                close()
                moveTo(14.14f, 11.86f)
                lineTo(11.14f, 15.73f)
                lineTo(9f, 13.14f)
                lineTo(6f, 17f)
                horizontalLineTo(18f)
                lineTo(14.14f, 11.86f)
                close()
            }
        }.build()
    }

    val MenuBook: ImageVector by lazy {
        ImageVector.Builder("MenuBook", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(21f, 5f)
                curveTo(19.89f, 4.65f, 18.67f, 4.5f, 17.5f, 4.5f)
                curveTo(15.55f, 4.5f, 13.45f, 4.9f, 12f, 6f)
                curveTo(10.55f, 4.9f, 8.45f, 4.5f, 6.5f, 4.5f)
                curveTo(4.55f, 4.5f, 2.45f, 4.9f, 1f, 6f)
                verticalLineTo(20.65f)
                curveTo(1f, 20.9f, 1.25f, 21.15f, 1.5f, 21.15f)
                curveTo(1.6f, 21.15f, 1.65f, 21.1f, 1.75f, 21.1f)
                curveTo(3.1f, 20.45f, 5.05f, 20f, 6.5f, 20f)
                curveTo(8.45f, 20f, 10.55f, 20.4f, 12f, 21.5f)
                curveTo(13.35f, 20.65f, 15.8f, 20f, 17.5f, 20f)
                curveTo(19.15f, 20f, 20.85f, 20.3f, 22.25f, 21.05f)
                curveTo(22.35f, 21.1f, 22.4f, 21.1f, 22.5f, 21.1f)
                curveTo(22.75f, 21.1f, 23f, 20.85f, 23f, 20.6f)
                verticalLineTo(6f)
                curveTo(22.4f, 5.55f, 21.75f, 5.25f, 21f, 5f)
                close()
                moveTo(21f, 18.5f)
                curveTo(19.9f, 18.15f, 18.7f, 18f, 17.5f, 18f)
                curveTo(15.8f, 18f, 13.35f, 18.65f, 12f, 19.5f)
                verticalLineTo(8f)
                curveTo(13.35f, 7.15f, 15.8f, 6.5f, 17.5f, 6.5f)
                curveTo(18.7f, 6.5f, 19.9f, 6.65f, 21f, 7f)
                verticalLineTo(18.5f)
                close()
            }
        }.build()
    }

    val Tune: ImageVector by lazy {
        ImageVector.Builder("Tune", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(3f, 17f)
                verticalLineTo(19f)
                horizontalLineTo(9f)
                verticalLineTo(17f)
                horizontalLineTo(3f)
                close()
                moveTo(3f, 5f)
                verticalLineTo(7f)
                horizontalLineTo(13f)
                verticalLineTo(5f)
                horizontalLineTo(3f)
                close()
                moveTo(13f, 21f)
                verticalLineTo(19f)
                horizontalLineTo(21f)
                verticalLineTo(17f)
                horizontalLineTo(13f)
                verticalLineTo(15f)
                horizontalLineTo(11f)
                verticalLineTo(21f)
                horizontalLineTo(13f)
                close()
                moveTo(7f, 9f)
                verticalLineTo(11f)
                horizontalLineTo(3f)
                verticalLineTo(13f)
                horizontalLineTo(7f)
                verticalLineTo(15f)
                horizontalLineTo(9f)
                verticalLineTo(9f)
                horizontalLineTo(7f)
                close()
                moveTo(21f, 13f)
                verticalLineTo(11f)
                horizontalLineTo(11f)
                verticalLineTo(13f)
                horizontalLineTo(21f)
                close()
                moveTo(15f, 9f)
                horizontalLineTo(17f)
                verticalLineTo(7f)
                horizontalLineTo(21f)
                verticalLineTo(5f)
                horizontalLineTo(17f)
                verticalLineTo(3f)
                horizontalLineTo(15f)
                verticalLineTo(9f)
                close()
            }
        }.build()
    }

    val MoreHoriz: ImageVector by lazy {
        ImageVector.Builder("MoreHoriz", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(6f, 10f)
                curveTo(4.9f, 10f, 4f, 10.9f, 4f, 12f)
                reflectiveCurveTo(4.9f, 14f, 6f, 14f)
                reflectiveCurveTo(8f, 13.1f, 8f, 12f)
                reflectiveCurveTo(7.1f, 10f, 6f, 10f)
                close()
                moveTo(18f, 10f)
                curveTo(16.9f, 10f, 16f, 10.9f, 16f, 12f)
                reflectiveCurveTo(16.9f, 14f, 18f, 14f)
                reflectiveCurveTo(20f, 13.1f, 20f, 12f)
                reflectiveCurveTo(19.1f, 10f, 18f, 10f)
                close()
                moveTo(12f, 10f)
                curveTo(10.9f, 10f, 10f, 10.9f, 10f, 12f)
                reflectiveCurveTo(10.9f, 14f, 12f, 14f)
                reflectiveCurveTo(14f, 13.1f, 14f, 12f)
                reflectiveCurveTo(13.1f, 10f, 12f, 10f)
                close()
            }
        }.build()
    }

    val StarBorder: ImageVector by lazy {
        ImageVector.Builder("StarBorder", 24.dp, 24.dp, 24f, 24f).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(22f, 9.24f)
                lineTo(14.81f, 8.62f)
                lineTo(12f, 2f)
                lineTo(9.19f, 8.63f)
                lineTo(2f, 9.24f)
                lineTo(7.46f, 13.97f)
                lineTo(5.82f, 21f)
                lineTo(12f, 17.27f)
                lineTo(18.18f, 21f)
                lineTo(16.55f, 13.97f)
                lineTo(22f, 9.24f)
                close()
                moveTo(12f, 15.4f)
                lineTo(8.24f, 17.67f)
                lineTo(9.24f, 13.39f)
                lineTo(5.92f, 10.51f)
                lineTo(10.3f, 10.13f)
                lineTo(12f, 6.1f)
                lineTo(13.71f, 10.14f)
                lineTo(18.09f, 10.52f)
                lineTo(14.77f, 13.4f)
                lineTo(15.77f, 17.68f)
                lineTo(12f, 15.4f)
                close()
            }
        }.build()
    }
}

