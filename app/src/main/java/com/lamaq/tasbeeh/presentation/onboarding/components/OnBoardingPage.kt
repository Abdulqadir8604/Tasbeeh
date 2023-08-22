package com.lamaq.tasbeeh.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.lamaq.tasbeeh.presentation.onboarding.Dimens.MediumPadding1
import com.lamaq.tasbeeh.presentation.onboarding.Dimens.MediumPadding2
import com.lamaq.tasbeeh.presentation.onboarding.Page
import com.lamaq.tasbeeh.presentation.onboarding.pages
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme

@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    page: Page,
) {
    Column(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f),
            painter = painterResource(id = page.image),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(MediumPadding1))
        Text(
            text = page.title,
            modifier = Modifier.padding(horizontal = MediumPadding2),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = DarkColorScheme.secondary
        )
        Spacer(modifier = Modifier.height(MediumPadding1))
        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = MediumPadding2),
            style = MaterialTheme.typography.headlineSmall,
            color = DarkColorScheme.inversePrimary
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnBoardingPagePreview() {
    MaterialTheme {
        OnBoardingPage(
            page = pages[5]
        )
    }
}