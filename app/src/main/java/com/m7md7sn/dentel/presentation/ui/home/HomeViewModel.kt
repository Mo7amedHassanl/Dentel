package com.m7md7sn.dentel.presentation.ui.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        _uiState.value = HomeUiState(
            sections = listOf(
                Section(
                    id = "regular_filling",
                    imageRes = R.drawable.ic_regular_filling, // Replace with your actual drawable resource
                    titleRes = R.string.regular_filling,   // Replace with your actual string resource
                    color = Color(0xFFA6A4ED) // Example color (light blue)
                ),
                Section(
                    id = "mobile_denture",
                    imageRes = R.drawable.ic_mobile_denture, // Replace with your actual drawable resource
                    titleRes = R.string.mobile_denture,     // Replace with your actual string resource
                    color = Color(0xFF6A89E0) // Example color (light green)
                ),
                Section(
                    id = "nerve_filling",
                    imageRes = R.drawable.ic_nerve_filling, // Replace with your actual drawable resource
                    titleRes = R.string.nerve_filling,    // Replace with your actual string resource
                    color = Color(0xFF43B4DB) // Example color (light red)
                ),
                Section(
                    id = "fixed_denture",
                    imageRes = R.drawable.ic_fixed_denture, // Replace with your actual drawable resource
                    titleRes = R.string.fixed_denture,    // Replace with your actual string resource
                    color = Color(0xFF96CCBC) // Example color (light red)
                ),
                Section(
                    id = "bedo",
                    imageRes = R.drawable.ic_bedo_teeth, // Replace with your actual drawable resource
                    titleRes = R.string.bedo,    // Replace with your actual string resource
                    color = Color(0xFFD37C9C) // Example color (light red)
                ),
                Section(
                    id = "diagnosis",
                    imageRes = R.drawable.ic_diagnosis, // Replace with your actual drawable resource
                    titleRes = R.string.diagnosis,    // Replace with your actual string resource
                    color = Color(0xFFCAAD95) // Example color (light red)
                ),
            )
        )
    }
} 