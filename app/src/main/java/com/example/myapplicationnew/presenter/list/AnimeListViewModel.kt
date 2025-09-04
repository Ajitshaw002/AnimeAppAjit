package com.example.myapplicationnew.presenter.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationnew.domain.model.Anime
import com.example.myapplicationnew.domain.usecase.GetAnimeListUseCase
import com.example.myapplicationnew.helper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val getAnimeListUseCase: GetAnimeListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AnimeListUiState(isLoading = true))
    val state: StateFlow<AnimeListUiState> = _state

    init {
        loadTopAnime()
    }

    fun loadTopAnime() {
        viewModelScope.launch {
            getAnimeListUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        val list = resource.data
                        _state.value = AnimeListUiState(
                            isLoading = false,
                            animes = list,
                            error = null,
                            isEmpty = list.isEmpty()
                        )
                    }

                    is Resource.Error -> {
                        val cached = _state.value.animes
                        _state.value = AnimeListUiState(
                            isLoading = false,
                            animes = cached,
                            error = if (cached.isEmpty()) resource.message else null,
                            isEmpty = cached.isEmpty()
                        )
                    }
                }
            }
        }
    }
}


data class AnimeListUiState(
    val isLoading: Boolean = false,
    val animes: List<Anime> = emptyList(),
    val error: String? = null,
    val isEmpty: Boolean = false
)

