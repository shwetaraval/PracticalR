package com.example.myapplication.ui.schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.example.myapplication.data.model.GameStatus
import com.example.myapplication.ui.SCHEDULE_LIST
import com.example.myapplication.ui.schedule.data.GameDisplayData
import com.example.myapplication.ui.theme.BgDarkGrey
import com.example.myapplication.ui.theme.BodyText
import com.example.myapplication.ui.theme.CardBgGrey
import com.example.myapplication.ui.theme.HeaderLight
import com.example.myapplication.ui.theme.HeaderText
import com.example.myapplication.ui.theme.OffWhiteGrey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(viewModel: ScheduleViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val headerIndexToMonth = remember(uiState) {
        uiState.mapIndexed { index, pair -> index to pair.first }
    }
    val coroutineScope = rememberCoroutineScope()
    val headerIndexMap = remember(uiState) {
        buildList {
            var currentIndex = 0
            uiState.forEach { (yearMonth, games) ->
                add(yearMonth to currentIndex) // sticky header index
                currentIndex += 1 + games.size // header + items
            }
        }.toMap() // Map<YearMonth, Int>
    }

    fun onMonthNavigate(targetMonth: YearMonth) {
        val targetIndex = headerIndexMap[targetMonth]
        if (targetIndex != null) {
            coroutineScope.launch {
                listState.animateScrollToItem(index = targetIndex, scrollOffset = 48)
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .mapNotNull { visibleIndex ->
                // Find the latest header before or at this visible index
                headerIndexToMonth.lastOrNull { it.first <= visibleIndex }?.second
            }
            .distinctUntilChanged()
            .collect { visibleMonth ->
                currentMonth = visibleMonth
            }
    }

    ScheduleUI(
        uiState = uiState,
        onMonthNavigate = ::onMonthNavigate,
        listState = listState,
        imageLoader = viewModel.imageLoader
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleUI(
    uiState: List<Pair<YearMonth, List<GameDisplayData>>>,
    listState: LazyListState,
    onMonthNavigate: (YearMonth) -> Unit, // isNext = true/false
    imageLoader: ImageLoader
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .testTag(SCHEDULE_LIST),
        state = listState
    ) {
        uiState.forEach { (yearMonth, games) ->
            stickyHeader {
                StickyMonthHeader(
                    month = yearMonth,
                    onPreviousClick = { onMonthNavigate(yearMonth.minusMonths(1)) },
                    onNextClick = { onMonthNavigate(yearMonth.plusMonths(1)) }
                )
            }
            items(items = games, key = { it.hashCode() }) { game ->
                GameItem(game = game, imageLoader = imageLoader)
            }
        }
    }
}

@Composable
fun GameItem(game: GameDisplayData, imageLoader: ImageLoader) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgGrey),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScheduleGameHeader(game)

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TeamUI(
                    modifier = Modifier.weight(1f),
                    isFutureGame = game.gameStatus == GameStatus.FUTURE_GAME,
                    isFirstTeam = true,
                    teamLogo = game.homeTeam.logo,
                    teamName = game.homeTeam.ta,
                    teamScore = game.homeTeam.score,
                    imageLoader = imageLoader
                )
                Text(
                    text = if (game.isHome) "vs" else "@",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                TeamUI(
                    modifier = Modifier.weight(1f),
                    isFutureGame = game.gameStatus == GameStatus.FUTURE_GAME,
                    isFirstTeam = false,
                    teamLogo = game.visitorTeam.logo,
                    teamName = game.visitorTeam.ta,
                    teamScore = game.homeTeam.score,
                    imageLoader = imageLoader
                )
            }

            if (game.gameStatus == GameStatus.FUTURE_GAME) {
                BuyTicketUI(game.gameTicketUrl)
            }
        }
    }
}

@Composable
fun ScheduleGameHeader(game: GameDisplayData) {
    // TODO: Live game status is currently not handled due to insufficient data for proper implementation.
    val displayList = if (game.gameStatus == GameStatus.PAST_GAME) {
        listOf(
            if (game.isHome) stringResource(id = R.string.home) else stringResource(id = R.string.away),
            game.displayDate,
            game.startTimeOrStatus.toUpperCase(Locale.current)
        )
    } else {
        listOf(
            if (game.isHome) stringResource(id = R.string.home) else stringResource(id = R.string.away),
            game.displayDate,
            game.displayTime
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        displayList.forEachIndexed { index, item ->
            Text(
                text = item,
                style = BodyText
            )
            if (index < displayList.lastIndex) {
                VerticalDivider(
                    modifier = Modifier
                        .height(10.dp),
                    thickness = 1.dp,
                    color = OffWhiteGrey
                )
            }
        }
    }
}

@Composable
fun TeamUI(
    modifier: Modifier = Modifier,
    isFutureGame: Boolean = false,
    isFirstTeam: Boolean = false,
    teamLogo: String,
    teamName: String,
    teamScore: String,
    imageLoader: ImageLoader
) {
    if (isFirstTeam) {
        Row(
            modifier = modifier
                .width(130.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(60.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(width = 40.dp, height = 40.dp),
                    painter = rememberAsyncImagePainter(teamLogo, imageLoader),
                    contentDescription = null
                )
                if (!isFutureGame)
                    Text(
                        text = teamName,
                        style = HeaderText
                    )
            }
            Text(
                text = if (isFutureGame) teamName else teamScore,
                style = if (isFutureGame) HeaderText else HeaderLight
            )
        }
    } else {
        Row(
            modifier = modifier
                .width(130.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isFutureGame) teamName else teamScore,
                style = if (isFutureGame) HeaderText else HeaderLight
            )

            Column(
                modifier = Modifier
                    .width(60.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(width = 40.dp, height = 40.dp),
                    painter = rememberAsyncImagePainter(teamLogo, imageLoader),
                    contentDescription = null
                )
                if (!isFutureGame)
                    Text(
                        text = teamName,
                        style = HeaderText
                    )
            }
        }
    }
}

/**
 * This function currently accepts null data but it should not
 * as when we will not have URL data we are not suppose to display this View
 */
@Composable
fun BuyTicketUI(ticketUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = OffWhiteGrey, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            buildAnnotatedString {
                append(stringResource(id = R.string.buy_ticket_on))
                withStyle(
                    style = BodyText.copy(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.W700
                    ).toSpanStyle()
                ) {
                    append(stringResource(id = R.string.ticketmaster))
                }
            },
            style = BodyText,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun StickyMonthHeader(
    month: YearMonth,
    modifier: Modifier = Modifier,
    onPreviousClick: (() -> Unit)? = null,
    onNextClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BgDarkGrey)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onPreviousClick?.invoke() }) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.previous_month)
            )
        }

        Text(
            text = month.format(
                DateTimeFormatter.ofPattern(
                    "MMMM yyyy",
                    java.util.Locale.getDefault()
                )
            ),
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(onClick = { onNextClick?.invoke() }) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.next_month)
            )
        }
    }
}

