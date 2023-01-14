package com.muzamil.rickamortywithpaging3.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
fun Home() {
    val moviesViewModel = hiltViewModel<CharacterViewModel>()

    val character = moviesViewModel.pager.collectAsLazyPagingItems()

    LazyColumn {
        items(
            items = character
        ) { movie ->
            movie?.let {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (movie.image != null) {
                        var isImageLoading by remember { mutableStateOf(false) }

                        val painter = rememberAsyncImagePainter(
                            model = movie.image,
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
                                    .height(115.dp)
                                    .width(77.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                painter = painter,
                                contentDescription = " ",
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
                        it.name?.let { it1 ->
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 18.dp, horizontal = 8.dp),
                                text = it1
                            )
                        }
                        it.gender?.let { it1 ->
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 18.dp, horizontal = 8.dp),
                                text = it1
                            )
                        }
                    }
                }
                Divider()
            }
        }

        val loadState = character.loadState.mediator
        item {
            handlestate(loadState)
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