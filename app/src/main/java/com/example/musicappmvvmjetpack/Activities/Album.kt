package com.example.musicappmvvmjetpack.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musicappmvvmjetpack.Model.Music
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModel
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModelFactory

class AlbumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val activity = LocalContext.current as FragmentActivity
                val musicViewModel: MusicViewModel = ViewModelProvider(
                    activity,
                    MusicViewModelFactory(activity)
                ).get(MusicViewModel::class.java)

                AlbumScreen(
                    musicViewModel = musicViewModel,
                    modifier = Modifier,
                    onPlayMusic = { id ->
                        val fragment = PlayMusicFragment().apply {
                            arguments = Bundle().apply { putString("id", id) }
                        }
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit()
                    },
                    onBack = {
                        parentFragmentManager.popBackStack()
                    },
                    onSearch = {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, SearchFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlbumScreen(
    musicViewModel: MusicViewModel,
    modifier: Modifier,
    onPlayMusic: (String) -> Unit,
    onBack: () -> Unit,
    onSearch: () -> Unit
) {
    val musicState = musicViewModel.musics.observeAsState(initial = emptyList())
    val musics = musicState.value

    Scaffold(
        topBar = { TopAlbumBar(onBack = onBack, onSearch = onSearch) }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Image(
                painterResource(id = R.drawable.banner2),
                contentDescription = "",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(30.dp))
            LazyColumnMusic(
                musics = musics,
                musicViewModel = musicViewModel,
                onMusicClick = onPlayMusic
            )
        }
    }
}

@Composable
fun LazyColumnMusic(
    musics: List<Music>,
    musicViewModel: MusicViewModel,
    onMusicClick: (id: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(musics.size) { index ->
            ItemAlbum(
                music = musics[index],
                musicViewModel = musicViewModel,
                onMusicClick = onMusicClick
            )
        }
    }
}

@Composable
fun TopAlbumBar(onBack: () -> Unit, onSearch: () -> Unit) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = { onBack() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        Text(
            text = stringResource(id = R.string.album),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.weight(3f)
        )
        IconButton(
            onClick = { onSearch() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }
}

@Composable
fun ItemAlbum(
    music: Music,
    musicViewModel: MusicViewModel,
    onMusicClick: (id: String) -> Unit
) {
    val isFavorite = musicViewModel.favoriteSongs.contains(music)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
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
            Column(
                modifier = Modifier.weight(4f)
            ) {
                Text(text = music.title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Text(text = music.singer, fontSize = 15.sp, color = Color.DarkGray)
            }
            IconButton(
                onClick = {
                    if (isFavorite) {
                        musicViewModel.removeFavorite(music)
                    } else {
                        musicViewModel.addFavorite(music)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                if (isFavorite) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                } else {
                    Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null)
                }
            }
        }
    }
}