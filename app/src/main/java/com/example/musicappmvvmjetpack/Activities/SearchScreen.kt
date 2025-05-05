package com.example.musicappmvvmjetpack.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicappmvvmjetpack.Model.Music

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                // Truyền danh sách nhạc từ ViewModel hoặc một nguồn khác
                SearchScreen(musicList = Music.getMusic(), navController = navController)
            }
        }
    }
}
@Composable
fun SearchScreen(musicList: List<Music>, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredMusic = musicList.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.singer.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(horizontal = 30.dp)) {
        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(text = "Search singer, song...")},
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "")},
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (filteredMusic.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(4.dp),
            ) {
                items(filteredMusic) { music ->
                    ItemSearch(music, onMusicClick = {navController.navigate("${Screen.PLAYMUSICSCREEN.route}/${it}")})
                }
            }
        } else {
            Text(text = "Không tìm thấy bài hát nào", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ItemSearch(music: Music, onMusicClick: (id: String) -> Unit){
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
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(text = music.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(text = music.singer, fontSize = 15.sp)
            }
            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "",
                modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}