package com.muzamil.rickamortywithpaging3.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.muzamil.rickamortywithpaging3.ui.CharacterViewModel

@Composable
fun MainScreen() {
    val moviesViewModel = hiltViewModel<CharacterViewModel>()

    val characters = moviesViewModel.pager.collectAsLazyPagingItems()
    val loadState = characters.loadState.mediator

    LazyColumn {
        items(items  = characters) { character ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (character != null) {
                    var isImageLoading by remember { mutableStateOf(false) }

                    val painter = rememberAsyncImagePainter(
                        model = character?.image,
                    )

                    isImageLoading = when (painter.state) {
                        is AsyncImagePainter.State.Loading -> true
                        else -> false
                    }

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                                .height(100.dp)
                                .width(70.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            painter = painter,
                            contentDescription = "image",
                            contentScale = ContentScale.FillBounds,
                        )

                        if (isImageLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 3.dp),
                                color = MaterialTheme.colors.primary,
                            )
                        }
                    }
                }
                Column {
                    character?.name?.let {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 18.dp, horizontal = 8.dp),
                            text = it
                        )
                    }
                    character?.species?.let {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 18.dp, horizontal = 8.dp),
                            text = it
                        )
                    }
                }

            }
            Divider()

        }
        item {
            handlestate(loadState = loadState)
        }
    }
}

@Composable
fun handlestate(loadState: LoadStates?) {
    if (loadState?.refresh == LoadState.Loading) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Loading.."
            )

            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
    }
    if (loadState?.append == LoadState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
    }

}