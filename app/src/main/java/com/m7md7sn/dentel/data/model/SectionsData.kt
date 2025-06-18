package com.m7md7sn.dentel.data.model

import androidx.compose.ui.graphics.Color
import com.m7md7sn.dentel.R

/**
 * Pre-defined list of all application sections
 */
val sectionsData = listOf(
    Section(
        id = "regular_filling",
        imageRes = R.drawable.ic_regular_filling,
        titleRes = R.string.regular_filling,
        color = Color(0xFFA6A4ED)
    ),
    Section(
        id = "mobile_denture",
        imageRes = R.drawable.ic_mobile_denture,
        titleRes = R.string.mobile_denture,
        color = Color(0xFF6A89E0)
    ),
    Section(
        id = "nerve_filling",
        imageRes = R.drawable.ic_nerve_filling,
        titleRes = R.string.nerve_filling,
        color = Color(0xFF43B4DB)
    ),
    Section(
        id = "fixed_denture",
        imageRes = R.drawable.ic_fixed_denture,
        titleRes = R.string.fixed_denture,
        color = Color(0xFF96CCBC)
    ),
    Section(
        id = "bedo",
        imageRes = R.drawable.ic_bedo_teeth,
        titleRes = R.string.bedo,
        color = Color(0xFFD37C9C)
    ),
    Section(
        id = "diagnosis",
        imageRes = R.drawable.ic_diagnosis,
        titleRes = R.string.diagnosis,
        color = Color(0xFFCAAD95)
    ),
)
