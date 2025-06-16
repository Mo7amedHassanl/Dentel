package com.m7md7sn.dentel.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class Section(
    val id: String,
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    val color: Color
) 