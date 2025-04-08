package com.example.musicappmvvmjetpack.Activities

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.musicappmvvmjetpack.Model.Music
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteScreen(navController: NavController, musicViewModel: MusicViewModel, padding: Modifier){
    Scaffold(
        topBar = {TopFavBar(navController)}
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ) {
            Spacer(modifier = Modifier.height(90.dp))
            FavoriteList(musicViewModel = musicViewModel, onMusicClick = {
                navController.navigate("${Screen.PLAYMUSICSCREEN.route}/${it}")
            })
        }
    }
}
@Composable
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
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .clickable {
                onMusicClick(music.id.toString())
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(text = music.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(text = music.singer, fontSize = 15.sp)
            }
            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "",
                modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(10.dp))
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
        Text(text = "My Music",
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
