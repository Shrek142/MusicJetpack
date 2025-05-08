package com.example.musicappmvvmjetpack.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import coil.compose.AsyncImage
import com.example.musicappmvvmjetpack.Activities.theme.ColorBackgr
import com.example.musicappmvvmjetpack.Model.Music
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModel

class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                val musicViewModel: MusicViewModel = viewModel()
                // Truyền danh sách nhạc từ ViewModel hoặc một nguồn khác
                FavoriteScreen(navController = navController, musicViewModel = musicViewModel, padding = Modifier)
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteScreen(navController: NavController, musicViewModel: MusicViewModel, padding: Modifier) {
    LaunchedEffect(Unit) {
        musicViewModel.loadFavoritesFromFirestore()
    }

    Scaffold(topBar = { TopFavBar(navController) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ColorBackgr)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
            ) {
                Spacer(modifier = Modifier.height(70.dp))
                Image(
                    painterResource(id = R.drawable.banner3),
                    contentDescription = "",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(30.dp))

                if (musicViewModel.favoriteSongs.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.favorite_1),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    FavoriteList(
                        musicViewModel = musicViewModel,
                        onMusicClick = {
                            navController.navigate("${Screen.PLAYMUSICSCREEN.route}/$it")
                        }
                    )
                }
            }
        }
    }
}@Composable
fun FavoriteList(musicViewModel: MusicViewModel, onMusicClick: (id: String) -> Unit){
    LazyColumn(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ){
        items(musicViewModel.favoriteSongs.size){index ->
            ItemFavorite(music = musicViewModel.favoriteSongs[index], onMusicClick = onMusicClick
            )
        }
    }
}
@Composable
fun ItemFavorite(music: Music, onMusicClick: (id: String) -> Unit){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .clickable {
                onMusicClick(music.id.toString())
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = music.posterUrl,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .clip(shape = RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.weight(5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = music.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(text = music.singer, fontSize = 15.sp)
            }
            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f))
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
fun TopFavBar(navController: NavController){
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = { navController.popBackStack() },
            modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        Text(text = stringResource(id = R.string.favorite),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.weight(3f))
        IconButton(onClick = { navController.navigate(Screen.SEARCHSCREEN.route) },
            modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }
}
