package com.example.musicappmvvmjetpack.Activities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicappmvvmjetpack.Model.Music

@Composable
fun SearchScreen(musicList: List<Music>, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredMusic = musicList.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.singer.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(text = "Search singer, song...")},
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "")},
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (filteredMusic.isNotEmpty()) {
            LazyColumn {
                items(filteredMusic) { music ->
                    MusicItem(music, onMusicClick = {navController.navigate("${Screen.PLAYMUSICSCREEN.route}/${it}")})
                }
            }
        } else {
            Text(text = "Không tìm thấy bài hát nào", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun MusicItem(music: Music, onMusicClick: (id: String) -> Unit) {
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { onMusicClick(music.id.toString()) }) {
        Text(text = music.title, style = MaterialTheme.typography.titleMedium)
        Text(text = music.singer, style = MaterialTheme.typography.bodyMedium)
    }
}