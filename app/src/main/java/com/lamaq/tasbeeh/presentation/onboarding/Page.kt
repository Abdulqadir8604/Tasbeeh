package com.lamaq.tasbeeh.presentation.onboarding

import androidx.annotation.DrawableRes
import com.lamaq.tasbeeh.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "Welcome to the Tasbeeh App",
        description = "One Place for all your Tasbeeh needs",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Menu Options",
        description = "Swipe right \uD83D\uDC49 to see the menu options",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Edit Total Count",
        description = "LONG PRESS on the Total Count to edit it",
        image = R.drawable.onboarding7
    ),
    Page(
        title = "Tasbeeh Counter",
        description = "Tap on the screen to count your Tasbeeh",
        image = R.drawable.onboarding3
    ),
    Page(
        title = "Settings",
        description = "Tap on the Settings button to change the feel of the app",
        image = R.drawable.onboarding4
    ),
    Page(
        title = "Edit Count",
        description = "Tap on the Pencil button to edit the Counter",
        image = R.drawable.onboarding5
    ),
    Page(
        title = "Reset Count",
        description = "Tap on the Reset button to reset the Counter",
        image = R.drawable.onboarding3
    ),
    Page(
        title = "Edit Limit",
        description = "Tap on the 'limit' button to edit the Limit",
        image = R.drawable.onboarding6
    ),
    Page(
        title = "Reset Limit",
        description = "LONG PRESS on the Reset button to reset the Limit",
        image = R.drawable.onboarding8
    ),
    Page(
        title = "That's it!",
        description = "Start using the app now \uD83D\uDE0A",
        image = R.drawable.onboarding9
    ),
)
