package com.m7md7sn.dentel.presentation.ui.section.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

/**
 * Search bar component for searching section topics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(54.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 16.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = Color.Transparent,
            inputFieldColors = SearchBarDefaults.inputFieldColors(
                focusedTextColor = DentelDarkPurple,
                unfocusedTextColor = DentelDarkPurple,
                cursorColor = DentelDarkPurple,
            )
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {}
}
