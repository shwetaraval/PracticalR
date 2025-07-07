package com.example.myapplication

import androidx.test.core.app.ApplicationProvider
import coil.ImageLoader
import com.example.myapplication.domain.usecase.GetScheduleUseCase
import com.example.myapplication.ui.schedule.ScheduleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ScheduleViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val fakeRepo = FakeScheduleRepository()
        val useCase = GetScheduleUseCase(fakeRepo)
        val fakeImageLoader =
            ImageLoader.Builder(ApplicationProvider.getApplicationContext()).build()
        viewModel = ScheduleViewModel(useCase, fakeImageLoader)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiStateIsUpdatedWithGames() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state).isNotEmpty()
        val (month, games) = state.first()
        assertThat(games.first().homeTeam.ta).isEqualTo("MIA")
        assertThat(games.first().visitorTeam.ta).isEqualTo("LAL")
    }
}
