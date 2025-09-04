package com.example.myapplicationnew.presenter.detail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationnew.domain.model.Anime
import com.example.myapplicationnew.domain.usecase.GetAnimeDetailUseCase
import com.example.myapplicationnew.helper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AnimeDetailUiState(isLoading = true))
    val state: StateFlow<AnimeDetailUiState> = _state

    fun loadDetail(id: Int) {
        viewModelScope.launch {
            getAnimeDetailUseCase(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        _state.value = AnimeDetailUiState(
                            isLoading = false,
                            anime = resource.data,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = AnimeDetailUiState(
                            isLoading = false,
                            anime = _state.value.anime, // show cached data if any
                            error = resource.message
                        )
                    }
                }
            }
        }
    }
}

data class AnimeDetailUiState(
    val isLoading: Boolean = false,
    val anime: Anime? = null,
    val error: String? = null
)


