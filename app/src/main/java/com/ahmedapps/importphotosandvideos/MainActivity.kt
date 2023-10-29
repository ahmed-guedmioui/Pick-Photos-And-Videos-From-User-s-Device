package com.ahmedapps.importphotosandvideos

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ahmedapps.importphotosandvideos.ui.theme.ImportPhotosAndVideosTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ViewModel by viewModels()

    private val pickMedia =
        registerForActivityResult<PickVisualMediaRequest, Uri>(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                println(uri.toString())
                if (viewModel.isVideo) {
                    viewModel.videoUri = uri
                    viewModel.isNewVideoPicked.value = true
                } else {
                    viewModel.imageUrl.value = uri.toString()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImportPhotosAndVideosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val vanController = rememberNavController()

                    NavHost(navController = vanController, startDestination = "HomeScreen") {
                        composable("HomeScreen") { HomeScreen() }
                        composable("VideoScreen") { VideoScreen(viewModel) }
                    }

                    if (viewModel.isNewVideoPicked.value) {
                        viewModel.isNewVideoPicked.value = false
                        vanController.navigate("VideoScreen")
                    }

                }
            }
        }
    }


    @Composable
    private fun HomeScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            DisplayImage()

            Spacer(modifier = Modifier.height(16.dp))

            PickingButtons()

        }
    }

    @Composable
    private fun DisplayImage() {

        val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(viewModel.imageUrl.value)
                .size(Size.ORIGINAL)
                .build()
        ).state

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        {
            if (imageState is AsyncImagePainter.State.Success) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    bitmap = imageState.result.drawable.toBitmap().asImageBitmap()
                )
            }
        }

    }

    @Composable
    private fun PickingButtons() {
        Column(
            modifier = Modifier.wrapContentWidth()
        ) {
            Button(
                modifier = Modifier.padding(horizontal = 30.dp),
                onClick = {

                    viewModel.isVideo = false
                    pickMedia.launch(
                        PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            .build()
                    )

                }
            ) {
                Text(text = "Pick Image")
            }

            Button(
                modifier = Modifier.padding(horizontal = 30.dp),
                onClick = {

                    viewModel.isVideo = true
                    pickMedia.launch(
                        PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            .build()
                    )
                }
            ) {
                Text(text = "Pick Video")
            }
        }
    }
}









