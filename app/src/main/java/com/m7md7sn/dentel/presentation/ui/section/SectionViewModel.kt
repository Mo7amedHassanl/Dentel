package com.m7md7sn.dentel.presentation.ui.section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.SectionException
import com.m7md7sn.dentel.data.repository.SectionRepository
import com.m7md7sn.dentel.data.repository.FavoritesRepository
import com.m7md7sn.dentel.data.repository.FavoriteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flatMapLatest

/**
 * ViewModel for the Section screen that handles UI state and user interactions
 */
@HiltViewModel
class SectionViewModel @Inject constructor(
    private val sectionRepository: SectionRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    // UI state exposed to the UI
    private val _uiState = MutableStateFlow<SectionUiState>(SectionUiState.Loading)
    val uiState: StateFlow<SectionUiState> = _uiState

    // Keep all topics in memory to support filtering
    private var allTopics: List<Topic> = emptyList()

    private val _currentType = MutableStateFlow(TopicType.Article)
    val currentType: StateFlow<TopicType> = _currentType

    val favoriteTopicIds: StateFlow<Set<String>> = _currentType
        .flatMapLatest { type ->
            when (type) {
                TopicType.Article -> favoritesRepository.getFavoriteArticles()
                TopicType.Video -> favoritesRepository.getFavoriteVideos()
            }
        }
        .map { items -> items.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    /**
     * Set the current section by its ID and load associated topics
     */
    fun setSectionById(sectionId: String) {
        _uiState.value = SectionUiState.Loading
        viewModelScope.launch {
            try {
                // Get section by ID
                val section = sectionRepository.getSectionById(sectionId)
                if (section != null) {
                    // Load topics for this section
                    loadTopics(sectionId)
                } else {
                    _uiState.value = SectionUiState.Error("Section not found")
                }
            } catch (e: Exception) {
                _uiState.value = SectionUiState.Error("Failed to load section: ${e.message}")
            }
        }
    }

    /**
     * Change the selected topic type (Article or Video)
     */
    fun selectType(type: TopicType) {
        _currentType.value = type
        when (val currentState = _uiState.value) {
            is SectionUiState.Success -> {
                // Update current state to indicate searching
                _uiState.update {
                    (it as SectionUiState.Success).copy(
                        selectedType = type,
                        isSearching = true
                    )
                }

                // Filter topics and determine if we need Success or Empty state
                val filteredTopics = sectionRepository.filterTopics(
                    allTopics,
                    type,
                    currentState.searchQuery
                )

                if (filteredTopics.isEmpty()) {
                    _uiState.value = SectionUiState.Empty(
                        section = currentState.section,
                        selectedType = type,
                        searchQuery = currentState.searchQuery
                    )
                } else {
                    _uiState.value = SectionUiState.Success(
                        section = currentState.section,
                        selectedType = type,
                        topics = filteredTopics,
                        searchQuery = currentState.searchQuery,
                        isSearching = false
                    )
                }
            }

            is SectionUiState.Empty -> {
                // If we're in Empty state, try filtering with the new type
                val filteredTopics = sectionRepository.filterTopics(
                    allTopics,
                    type,
                    currentState.searchQuery
                )

                if (filteredTopics.isEmpty()) {
                    _uiState.update {
                        (it as SectionUiState.Empty).copy(selectedType = type)
                    }
                } else {
                    _uiState.value = SectionUiState.Success(
                        section = currentState.section,
                        selectedType = type,
                        topics = filteredTopics,
                        searchQuery = currentState.searchQuery
                    )
                }
            }

            is SectionUiState.Error -> {
                // Update type even in error state to maintain state consistency
                _uiState.update {
                    (it as SectionUiState.Error).copy(selectedType = type)
                }
            }

            else -> {} // Do nothing for Loading state
        }
    }

    /**
     * Load topics for a section from the repository
     */
    private fun loadTopics(sectionId: String) {
        viewModelScope.launch {
            try {
                // Get section by ID (again, to ensure we have it)
                val section = sectionRepository.getSectionById(sectionId)

                // Fetch all topics for this section
                allTopics = sectionRepository.getTopicsForSection(sectionId)

                // Get state values from current state if possible
                val currentState = _uiState.value
                val selectedType = when (currentState) {
                    is SectionUiState.Success -> currentState.selectedType
                    is SectionUiState.Empty -> currentState.selectedType
                    is SectionUiState.Error -> currentState.selectedType
                    else -> TopicType.Article
                }

                val searchQuery = when (currentState) {
                    is SectionUiState.Success -> currentState.searchQuery
                    is SectionUiState.Empty -> currentState.searchQuery
                    is SectionUiState.Error -> currentState.searchQuery
                    else -> ""
                }

                // Filter topics based on current preferences
                val filteredTopics = sectionRepository.filterTopics(
                    allTopics,
                    selectedType,
                    searchQuery
                )

                // Set state based on whether we have results
                if (filteredTopics.isEmpty()) {
                    _uiState.value = SectionUiState.Empty(
                        section = section,
                        selectedType = selectedType,
                        searchQuery = searchQuery
                    )
                } else {
                    _uiState.value = SectionUiState.Success(
                        section = section,
                        topics = filteredTopics,
                        selectedType = selectedType,
                        searchQuery = searchQuery
                    )
                }
            } catch (e: SectionException) {
                _uiState.value = SectionUiState.Error(e.message ?: "Unknown error")
            } catch (e: Exception) {
                _uiState.value = SectionUiState.Error("Failed to load topics: ${e.message}")
            }
        }
    }

    /**
     * Handle search query changes
     */
    fun onSearchQueryChange(query: String) {
        // Update query in current state
        when (val currentState = _uiState.value) {
            is SectionUiState.Success -> {
                // First update the UI to show we're searching
                _uiState.update {
                    (it as SectionUiState.Success).copy(
                        searchQuery = query,
                        isSearching = true
                    )
                }

                // Filter with new query
                val filteredTopics = sectionRepository.filterTopics(
                    allTopics,
                    currentState.selectedType,
                    query
                )

                // Set state based on results
                if (filteredTopics.isEmpty()) {
                    _uiState.value = SectionUiState.Empty(
                        section = currentState.section,
                        selectedType = currentState.selectedType,
                        searchQuery = query
                    )
                } else {
                    _uiState.value = SectionUiState.Success(
                        section = currentState.section,
                        selectedType = currentState.selectedType,
                        topics = filteredTopics,
                        searchQuery = query,
                        isSearching = false
                    )
                }
            }

            is SectionUiState.Empty -> {
                // Try filtering with new query from Empty state
                val filteredTopics = sectionRepository.filterTopics(
                    allTopics,
                    currentState.selectedType,
                    query
                )

                if (filteredTopics.isEmpty()) {
                    // Still empty, just update query
                    _uiState.update {
                        (it as SectionUiState.Empty).copy(searchQuery = query)
                    }
                } else {
                    // Found results, switch to Success
                    _uiState.value = SectionUiState.Success(
                        section = currentState.section,
                        selectedType = currentState.selectedType,
                        topics = filteredTopics,
                        searchQuery = query
                    )
                }
            }

            is SectionUiState.Error -> {
                // Update query even in error state
                _uiState.update {
                    (it as SectionUiState.Error).copy(searchQuery = query)
                }
            }

            else -> {} // Do nothing for Loading state
        }
    }
}
