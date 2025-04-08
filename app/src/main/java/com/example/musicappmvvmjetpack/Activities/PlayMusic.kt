package com.example.musicappmvvmjetpack.Activities

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.musicappmvvmjetpack.Activities.theme.ColorButton
import com.example.musicappmvvmjetpack.Model.Music
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModel

@Composable
fun PlayMusicScreen(navController: NavController, musicViewModel: MusicViewModel, id: String?) {
    val music = id?.let { musicViewModel.getMusicById(it) }

    music?.let {
        // Chỉ gọi playMusic khi có bài hát mới được chọn
        if (musicViewModel.currentMusicId != it.id) {
            musicViewModel.playMusic(it.id)
            musicViewModel.currentMusicId = it.id  // Cập nhật ID bài hát hiện tại
        }
        PlayMusicForm(musicViewModel, navController)
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayMusicForm(musicViewModel: MusicViewModel, navController: NavController){
    val music by musicViewModel.currentMusic.observeAsState()
    val currentTime by musicViewModel.currentTime.observeAsState(0L)
    val totalDuration by musicViewModel.totalDuration.observeAsState(0L)

    music?.let {
        Scaffold(
            topBar = {TopPlayBar(navController, musicViewModel, it)}
        ) {

        }
        Column(
            modifier = Modifier.padding(horizontal = 60.dp).clip(RoundedCornerShape(16.dp)) // Bo tròn góc
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            AsyncImage(
                model = it.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                   // .size(300.dp, 400.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = it.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = it.singer, textAlign = TextAlign.Center, fontSize = 15.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(35.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = if (totalDuration > 0) (currentTime.toFloat() / totalDuration) else 0f,
                    modifier = Modifier.weight(3f),
                    color = ColorButton
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = formatTime(currentTime), color = Color.DarkGray)  //hiển thị thời gian đếm ngược
            }
            Spacer(modifier = Modifier.height(30.dp))
            PlayMusicControls( musicViewModel)
            //BottomPlayBar()
        }
    }
}
@Composable
fun PlayMusicControls(musicViewModel: MusicViewModel){
    val icon = if (musicViewModel.isPlay) {
        R.drawable.ic_pause // Nếu đang phát, hiển thị icon tạm dừng
    } else {
        R.drawable.ic_play // Nếu không phát, hiển thị icon phát
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { musicViewModel.isRandom = !musicViewModel.isRandom}) {
            if (musicViewModel.isRandom){
                Image(
                painter = painterResource(id = R.drawable.ic_random),
                contentDescription = "Random",
                    colorFilter = ColorFilter.tint(ColorButton),
                //modifier = Modifier.size(50.dp)
            )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.ic_random),
                    contentDescription = "Random",
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    //modifier = Modifier.size(50.dp)
            )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(onClick = { musicViewModel.previousMusic() }) {
            Image(painterResource(id = R.drawable.ic_previous), contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(onClick = {
            if (musicViewModel.isPlay){
                musicViewModel.pauseMusic()
            }else{
                musicViewModel.resumeMusic()
            }

        }) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(ColorButton),
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(onClick = {
            if (musicViewModel.isRandom){musicViewModel.playNextRandom()}
            else{musicViewModel.nextMusic()}
        }) {
            Image(painterResource(id = R.drawable.ic_next), contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(onClick = {  }) {
            Image(
                painter = painterResource(id = R.drawable.ic_renew),
                contentDescription = "replay",
                colorFilter = ColorFilter.tint(Color.LightGray),
                //modifier = Modifier.size(50.dp)
            )
        }
    }
}
@Composable
fun TopPlayBar(navController: NavController, musicViewModel: MusicViewModel, music: Music){
    val isFavorite = musicViewModel.favoriteSongs.contains(music)

    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            navController.popBackStack()
            musicViewModel.currentMusicId = music.id},
            modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        Text(text = "Now Playing",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.weight(3f))
        IconButton(onClick = {
            if (isFavorite) {
                musicViewModel.removeFavorite(music)
            } else {
                musicViewModel.addFavorite(music)
            }
        },
            modifier = Modifier.weight(1f)) {
            if (isFavorite){
                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
            }else{
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null)
            }
        }
    }
}

private fun formatTime(millis: Long) : String{
    val seconds = (millis /1000) % 60
    val minutes  = (millis/(1000*60)) %60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun BottomPlayBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = "Music")
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Create, contentDescription = "Sound")
        }
    }
}
