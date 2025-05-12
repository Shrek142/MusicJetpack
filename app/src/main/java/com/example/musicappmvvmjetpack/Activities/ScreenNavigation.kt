package com.example.musicappmvvmjetpack.Activities

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.musicappmvvmjetpack.Activities.theme.ColorButton
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModel
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModelFactory

enum class Screen(val route: String) {
    HOMESCREEN("home"),
    ALBUMSCREEN("album"),
    PLAYMUSICSCREEN("play_music"),
    SEARCHSCREEN("search"),
    FAVORITESCREEN("favorite"),
    SPLSCREEN("splash"),
    LOGIN("login"),
    PROFILE("profile"),
    SIGNUP("signup"),
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModelFactory(context))
    val fragmentManager = (context as FragmentActivity).supportFragmentManager

    var currentScreen by rememberSaveable { mutableStateOf(Screen.SPLSCREEN) }

    LaunchedEffect(Unit) {
        musicViewModel.loadFavoritesFromFirestore()
    }

    // Hàm cập nhật currentScreen chính xác ngay lập tức
    fun updateCurrentScreen() {
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
        currentScreen = when (currentFragment) {
            is HomeFragment -> Screen.HOMESCREEN
            is AlbumFragment -> Screen.ALBUMSCREEN
            is FavoriteFragment -> Screen.FAVORITESCREEN
            is ProfileFragment -> Screen.PROFILE
            is PlayMusicFragment -> Screen.PLAYMUSICSCREEN
            is SearchFragment -> Screen.SEARCHSCREEN
            is SplashFragment -> Screen.SPLSCREEN
            is LogInFragment -> Screen.LOGIN
            is SignUpFragment -> Screen.SIGNUP
            else -> Screen.HOMESCREEN
        }
    }

    // Lắng nghe backstack (để cover popBackStack)
    DisposableEffect(fragmentManager) {
        val callback = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                currentScreen = when (f) {
                    is HomeFragment -> Screen.HOMESCREEN
                    is AlbumFragment -> Screen.ALBUMSCREEN
                    is FavoriteFragment -> Screen.FAVORITESCREEN
                    is ProfileFragment -> Screen.PROFILE
                    is PlayMusicFragment -> Screen.PLAYMUSICSCREEN
                    is SearchFragment -> Screen.SEARCHSCREEN
                    is SplashFragment -> Screen.SPLSCREEN
                    is LogInFragment -> Screen.LOGIN
                    is SignUpFragment -> Screen.SIGNUP
                    else -> Screen.HOMESCREEN
                }
            }
        }
        fragmentManager.registerFragmentLifecycleCallbacks(callback, true)
        onDispose {
            fragmentManager.unregisterFragmentLifecycleCallbacks(callback)
        }
    }

    // Load splash lần đầu
    LaunchedEffect(Unit) {
        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            fragmentManager.commit {
                replace(R.id.fragment_container, SplashFragment())
            }
            updateCurrentScreen() // Cập nhật ngay
        }
    }

    // LOG realtime currentScreen
    LaunchedEffect(currentScreen) {
        println("Current screen = $currentScreen")
    }

    fun navigateTo(screen: Screen) {
        val fragment = when (screen) {
            Screen.HOMESCREEN -> HomeFragment()
            Screen.ALBUMSCREEN -> AlbumFragment()
            Screen.FAVORITESCREEN -> FavoriteFragment()
            Screen.PROFILE -> ProfileFragment()
            Screen.SEARCHSCREEN -> SearchFragment()
            Screen.PLAYMUSICSCREEN -> PlayMusicFragment()
            Screen.SPLSCREEN -> SplashFragment()
            Screen.LOGIN -> LogInFragment()
            Screen.SIGNUP -> SignUpFragment()
        }

        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .apply {
                if (screen !in listOf(
                        Screen.HOMESCREEN,
                    )
                ) addToBackStack(null)
            }
            .commit()

        currentScreen = screen // Cập nhật NGAY SAU replace
    }

    Scaffold(
        bottomBar = {
            Column {
                val currentMusic by musicViewModel.currentMusic.observeAsState()
                if (currentMusic != null && currentScreen !in listOf(
                        Screen.SPLSCREEN,
                        Screen.LOGIN,
                        Screen.SIGNUP,
                        Screen.PLAYMUSICSCREEN
                    )
                ) {
                    NowMusicBar(musicViewModel) { id ->
                        val fragment = PlayMusicFragment().apply {
                            arguments = Bundle().apply { putString("id", id) }
                        }
                        fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit()

                        currentScreen = Screen.PLAYMUSICSCREEN
                    }
                }

                // BottomBar hiện 4 màn
                if (currentScreen in listOf(
                        Screen.HOMESCREEN,
                        Screen.ALBUMSCREEN,
                        Screen.FAVORITESCREEN,
                        Screen.PROFILE
                    )
                ) {
                    BottomBar(currentScreen = currentScreen) { screen ->
                        navigateTo(screen)
                    }
                }
            }
        },
        content = { innerPadding ->
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                factory = { context ->
                    FragmentContainerView(context).apply {
                        id = R.id.fragment_container
                    }
                }
            )
        }
    )
}

@Composable
fun BottomBar(
    currentScreen: Screen, // thêm currentScreen
    onNavigate: (Screen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        BottomBarItem(
            screen = Screen.HOMESCREEN,
            labelResId = R.string.home,
            icon = { tintColor ->
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = tintColor
                )
            },
            currentScreen = currentScreen,
            onNavigate = onNavigate
        )

        BottomBarItem(
            screen = Screen.ALBUMSCREEN,
            labelResId = R.string.album,
            icon = { tintColor ->
                Image(
                    painter = painterResource(id = R.drawable.ic_album),
                    contentDescription = "Album",
                    colorFilter = ColorFilter.tint(tintColor),
                    modifier = Modifier.size(24.dp)  // Thêm size cho đẹp
                )
            },
            currentScreen = currentScreen,
            onNavigate = onNavigate
        )

        BottomBarItem(
            screen = Screen.FAVORITESCREEN,
            labelResId = R.string.favorite,
            icon = { tintColor ->
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = tintColor
                )
            },
            currentScreen = currentScreen,
            onNavigate = onNavigate
        )

        BottomBarItem(
            screen = Screen.PROFILE,
            labelResId = R.string.profile,
            icon = { tintColor ->
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = tintColor
                )
            },
            currentScreen = currentScreen,
            onNavigate = onNavigate
        )

    }
}

@Composable
fun BottomBarItem(
    screen: Screen,
    labelResId: Int,
    icon: @Composable (Color) -> Unit,
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val isSelected = screen == currentScreen
    val tintColor = if (isSelected) ColorButton else Color.Gray

    Card(
        modifier = Modifier
            .clickable { onNavigate(screen) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon(tintColor)  // Truyền màu vào icon
            Text(
                text = stringResource(id = labelResId),
                color = tintColor
            )
        }
    }
}

@Composable
fun NowMusicBar(musicViewModel: MusicViewModel, onNavigate: (String) -> Unit) {
    val currentMusic by musicViewModel.currentMusic.observeAsState()
    val icon = if (musicViewModel.isPlay) {
        R.drawable.ic_pause
    } else {
        R.drawable.ic_play
    }

    currentMusic?.let {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp, 70.dp)
                .background(Color.Gray.copy(alpha = 0.8f))
                .padding(5.dp)
                .clickable { onNavigate(it.id.toString()) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = it.posterUrl,
                contentDescription = "Music poster",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.weight(4f)) {
                Text(text = it.title)
                Text(text = it.singer)
            }
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = { musicViewModel.previousMusic() }
            ) {
                Image(
                    painterResource(id = R.drawable.ic_previous),
                    contentDescription = "Previous",
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (musicViewModel.isPlay) {
                        musicViewModel.pauseMusic()
                    } else {
                        musicViewModel.resumeMusic()
                    }
                }
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Play/Pause",
                    colorFilter = ColorFilter.tint(ColorButton),
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (musicViewModel.isRandom) {
                        musicViewModel.playNextRandom()
                    } else {
                        musicViewModel.nextMusic()
                    }
                }
            ) {
                Image(
                    painterResource(id = R.drawable.ic_next),
                    contentDescription = "Next",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}