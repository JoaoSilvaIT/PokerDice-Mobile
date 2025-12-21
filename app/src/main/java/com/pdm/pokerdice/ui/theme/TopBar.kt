package com.pdm.pokerdice.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onTitleClick: (() -> Unit)? = null,
    onBackAction: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                        .then(
                            if (onTitleClick != null) {
                                Modifier
                                    .clickable { onTitleClick() }
                                    .padding(end = 200.dp)
                            } else {
                                Modifier
                            },
                        ),
            )
        },
        navigationIcon = {
            if (onBackAction != null) {
                IconButton(onClick = onBackAction) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.Go_back),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        },
        actions = { actions() },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        scrollBehavior = scrollBehavior,
        modifier = modifier.statusBarsPadding(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewGenericTopAppBar() {
    PokerDiceTheme {
        Surface {
            Column {
                GenericTopAppBar(
                    title = "Home",
                    onBackAction = { },
                    actions = {
                        TextButton(onClick = { }) {
                            Text(
                                text = stringResource(R.string.login),
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                        TextButton(onClick = { }) {
                            Text(
                                text = stringResource(R.string.sign_Up),
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    },
                )
            }
        }
    }
}
