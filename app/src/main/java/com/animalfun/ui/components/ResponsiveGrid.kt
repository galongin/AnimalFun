package com.animalfun.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Returns the appropriate column count based on [WindowWidthSizeClass]:
 * - Compact = 2
 * - Medium  = 3
 * - Expanded = 4
 */
fun columnsForWidth(windowWidthSizeClass: WindowWidthSizeClass): Int = when (windowWidthSizeClass) {
    WindowWidthSizeClass.Compact -> 2
    WindowWidthSizeClass.Medium -> 3
    else -> 4 // Expanded
}

/**
 * A reusable responsive grid composable that adapts its column count to the
 * current window size class. Wraps [LazyVerticalGrid] with adaptive columns,
 * configurable spacing, and a generic content lambda.
 *
 * @param windowWidthSizeClass The current window width size class.
 * @param modifier Modifier applied to the grid.
 * @param itemSpacing Spacing between grid items (both horizontal and vertical). Default 12.dp.
 * @param contentPadding Padding around the entire grid content. Default 12.dp on all sides.
 * @param columns Optional explicit column count override. When null, uses [columnsForWidth].
 * @param content The [LazyGridScope] content lambda — callers use `items(...)` etc. inside.
 */
@Composable
fun ResponsiveGrid(
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    columns: Int? = null,
    content: LazyGridScope.() -> Unit
) {
    val columnCount = columns ?: columnsForWidth(windowWidthSizeClass)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
        content = content
    )
}
