package com.lamaq.tasbeeh.presentation.onboarding

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.lamaq.tasbeeh.components.TasbeehData
import com.lamaq.tasbeeh.presentation.common.OnBoardingButton
import com.lamaq.tasbeeh.presentation.common.OnBoardingTextButton
import com.lamaq.tasbeeh.presentation.onboarding.components.OnBoardingPage
import com.lamaq.tasbeeh.presentation.onboarding.components.PageIndicator
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    tasbeehData: TasbeehData,
) {

    val onboardingPref = LocalContext.current.getSharedPreferences(
        "onboarding", MODE_PRIVATE
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColorScheme.background)
    ) {
        val pagerState = rememberPagerState(initialPage = 0) {
            pages.size
        }

        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", "Next")
                    1 -> listOf("Back", "Next")
                    2 -> listOf("Back", "Next")
                    3 -> listOf("Back", "Next")
                    4 -> listOf("Back", "Next")
                    5 -> listOf("Back", "Next")
                    6 -> listOf("Back", "Next")
                    7 -> listOf("Back", "Next")
                    8 -> listOf("Back", "Next")
                    9 -> listOf("Back", "Start")
                    else -> {
                        listOf("", "")
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState
        ) { page ->
            OnBoardingPage(
                page = pages[page]
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.MediumPadding2)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PageIndicator(
                modifier = Modifier.fillMaxWidth(0.5f),
                pagesSize = pages.size,
                selectedPage = pagerState.currentPage
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                val scope = rememberCoroutineScope()

                if (buttonState.value[0] != "") {
                    OnBoardingTextButton(
                        text = buttonState.value[0],
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    )
                }

                OnBoardingButton(
                    text = buttonState.value[1],
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage == pages.size - 1) {
                                onboardingPref.edit().putBoolean("onboardingDone", true).apply()
                                navController.navigate("home/${tasbeehData.homeTasbeeh.elementAt(0)}")
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}