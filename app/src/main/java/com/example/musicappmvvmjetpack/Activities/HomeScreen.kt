package com.example.musicappmvvmjetpack.Activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.musicappmvvmjetpack.Activities.theme.ColorButton
import com.example.musicappmvvmjetpack.Model.Music
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.AuthViewModel
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModel


@Composable
fun HomeScreen(navController: NavController, musicViewModel: MusicViewModel, padding: Modifier){
    val musicState = musicViewModel.musics.observeAsState(initial = emptyList())
    val musics = musicState.value

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        TopHomeBar()
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painterResource(id = R.drawable.img),
            contentDescription = "",
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Most Popular",
                modifier = Modifier.weight(2f),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp)
            Button(onClick = {  },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = ColorButton
                ),
                modifier = Modifier.weight(1f)) {
                Text(text = "View All", fontSize = 15.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyMusicGrid(musics, onMusicClick = {
            navController.navigate("${Screen.PLAYMUSICSCREEN.route}/${it}")
        })
    }

}
@Composable
fun LazyMusicGrid(musics: List<Music>,onMusicClick: (id: String) -> Unit){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ){
        items(musics.size){index ->
            ItemMusic(music = musics[index], onMusicClick = onMusicClick)
        }
    }
}

@Composable
fun ItemMusic(music: Music, onMusicClick: (id: String) -> Unit){
    Card(
        modifier = Modifier
            .size(160.dp, 250.dp)
            .clickable {
                onMusicClick(music.id.toString())
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier) {
            AsyncImage(
                model = music.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .weight(7f)
                    .clip(shape = RoundedCornerShape(10.dp))
            )
            Text(
                text = music.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.weight(1f)
            ){
                Image(painterResource(id = R.drawable.ic_dowload), contentDescription = "",
                    colorFilter = ColorFilter.tint(color = Color.Gray))
                Spacer(modifier = Modifier.width(5.dp))
                Text(text ="${music.download}M Download", color = Color.DarkGray)
            }

        }
    }
}
@Composable
fun TopHomeBar(){
    val viewModel: AuthViewModel = viewModel()
    val user by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        user?.let {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(text = "Hello", fontSize = 15.sp)
                Text(text = it.username ?: "User", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
            }
            //CircleAvatar()
            Image(
                painter = rememberAsyncImagePainter(user?.photoUrl ?: R.drawable.img_bachduong),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .border(1.dp, ColorButton),
                contentScale = ContentScale.Crop
            )
        }
    }
}
