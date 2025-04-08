package com.example.musicappmvvmjetpack.Activities

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.musicappmvvmjetpack.Model.Music
import com.example.musicappmvvmjetpack.R
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
        Spacer(modifier = Modifier.height(50.dp))
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
                fontSize = 15.sp)
            Button(onClick = {  },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Red
                ),
                modifier = Modifier.weight(1f)) {
                Text(text = "View All")
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
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier) {
            AsyncImage(
                model = music.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .weight(8f)
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
                Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text ="${music.download}M Dowload", color = Color.DarkGray)
            }

        }
    }
}
@Composable
fun TopHomeBar(){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(text = "Hello", fontSize = 15.sp)
            Text(text = "Duc", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
        }
        //CircleAvatar()
        Image(
            painterResource(id = R.drawable.img_bachduong),
            contentDescription = null,
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(50.dp)
                .clip(RoundedCornerShape(30.dp)),
            contentScale = ContentScale.Fit)
    }
}
@Composable
fun SearchField(modifier: Modifier){
    var SearchText by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = SearchText,
        onValueChange = {searchValue->
            SearchText = searchValue },
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(text = "Search")},
        leadingIcon = { Icon((Icons.Default.Search), contentDescription = "")},
    )

}
