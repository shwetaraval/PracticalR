package com.example.myapplication.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.myapplication.domain.usecase.GetScheduleUseCase
import com.example.myapplication.ui.schedule.data.GameDisplayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,
    val imageLoader: ImageLoader
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Pair<YearMonth, List<GameDisplayData>>>>(emptyList())
    val uiState: StateFlow<List<Pair<YearMonth, List<GameDisplayData>>>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = getScheduleUseCase()
        }
    }
}
