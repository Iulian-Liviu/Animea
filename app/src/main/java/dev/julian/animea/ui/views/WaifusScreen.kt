package dev.julian.animea.ui.views

import android.content.Context
import android.text.BoringLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import dev.julian.animea.R
import dev.julian.animea.data.WaifuImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WaifusScreen(modifier: Modifier = Modifier, viewModel: WaifusScreenViewModel) {
    val listGridState = rememberLazyStaggeredGridState()
    var localScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        localScope.launch {
            viewModel.loadImages()
        }
    }

    val isLoading: Boolean by viewModel.isLoading.observeAsState(false)
    val errorMessage: String by viewModel.ErrorMessage.observeAsState("")
    val images: List<WaifuImageResponse.Image> by viewModel.Images.observeAsState(emptyList())

    var itemTapped: Boolean by remember { mutableStateOf(false) }
    var selectedItem: WaifuImageResponse.Image? by remember {
        mutableStateOf( WaifuImageResponse.Image())
    }


    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingContainer(modifier = modifier)
        }
    } else {



        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ImagesListView(
                modifier = modifier,
                images = images,
                errorMessage = errorMessage,
                viewModel,
                listGridState,
                onTapped = {item -> itemTapped = true; selectedItem = item}
            )
            if (itemTapped && selectedItem != null){
                Box(modifier = modifier.padding(16.dp), contentAlignment = Alignment.Center){
                    Card(modifier= Modifier
                        .padding(14.dp).fillMaxWidth().aspectRatio(selectedItem!!.width.toFloat() / selectedItem!!.height.toFloat())) {
                        ImageCard(modifier = modifier, imageItem = selectedItem!!, width = selectedItem!!.width, height = selectedItem!!.height, onTapped = {itemTapped = false; selectedItem = null}, contentScale = ContentScale.Fit )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(80.dp)
                    .alpha(0.8f)
                    .align(Alignment.BottomCenter),
                elevation = CardDefaults.cardElevation(),
                colors = CardDefaults.cardColors()
            ) {
                Row(modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Button(onClick = { /*TODO*/ },modifier = modifier) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                    Box(modifier = modifier)
                    Button(onClick = { /*TODO*/ },modifier = modifier) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                }

            }
        }
    }
}

@Composable
fun BottomMenu(modifier: Modifier) {
    Card(modifier = modifier) {

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagesListView(
    modifier: Modifier,
    images: List<WaifuImageResponse.Image>,
    errorMessage: String,
    viewModel: WaifusScreenViewModel,
    state: LazyStaggeredGridState,
    onTapped: (WaifuImageResponse.Image) -> Unit
) {
    if (images.isEmpty() && errorMessage.isNotEmpty()) {
        Box(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Rounded.Warning, null)
                Text(text = errorMessage, modifier = modifier)
            }
        }

    } else {
        LazyVerticalStaggeredGrid(
            modifier = modifier,
            state = state,
            columns = StaggeredGridCells.Fixed(3),
        ) {
            items(images) { image ->
                ImageCard(
                    imageItem = image,
                    width = image.width,
                    height = image.height,
                    modifier = modifier,
                    onTapped = onTapped
                )
            }
        }
        InfiniteStaggeredGridHandler(listState = state) {
            viewModel.loadImages()
        }
    }
}

@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    imageItem: WaifuImageResponse.Image,
    contentScale: ContentScale = ContentScale.Inside,
    width: Int,
    height: Int,
    onTapped: (WaifuImageResponse.Image) -> Unit,
    context: Context = LocalContext.current
) {
    val imageRequest = remember {
        ImageRequest.Builder(context)
            .decoderDispatcher(Dispatchers.Default)
            .data(imageItem.url)
            .error(R.drawable.image_loading_placeholder)
            .crossfade(500)
            .build()
    }
    Card(modifier = modifier
        .padding(2.dp)
        .clip(RoundedCornerShape(4.dp))
    ) {
        SubcomposeAsyncImage(
            modifier = modifier
                .aspectRatio(width.toFloat() / height.toFloat())
                .clickable { onTapped(imageItem) },
            model = imageRequest,
            contentDescription = null,
            contentScale = contentScale,
            alignment = Alignment.Center
        ) {
            if (painter.state is AsyncImagePainter.State.Loading) {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = modifier
                            .width(20.dp)
                            .height(20.dp), color = Color(imageItem.dominantColor.toColorInt())
                    )
                }
            } else {
                SubcomposeAsyncImageContent(modifier = modifier
                    .clickable { onTapped(imageItem) })
            }
        }
    }
}


@Composable
fun LoadingContainer(modifier: Modifier) {
    Card(
        modifier = modifier
            .height(120.dp)
            .width(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
            )
            Text(
                text = "Loading...",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfiniteStaggeredGridHandler(
    listState: LazyStaggeredGridState,
    buffer: Int = 6,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                onLoadMore()
            }
    }
}
