package com.example.musicappmvvmjetpack.Activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.musicappmvvmjetpack.Activities.theme.ColorButton
import com.example.musicappmvvmjetpack.Model.Music
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModel
import com.example.musicappmvvmjetpack.ViewModel.MusicViewModelFactory

enum class Screen(val route: String) {
    HOMESCREEN("home"),
    ALBUMSCREEN("album"),
    PLAYMUSICSCREEN("play music"),
    SEARCHSCREEN("search"),
    FAVORITESCREEN("favorite"),
    SPLSCREEN("splash"),
    LOGIN("login"),
    PROFILE("profile"),
    SIGNUP("signup"),
}
@Composable
fun ScreenNavigation(){

    val navController = rememberNavController()
    val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModelFactory(LocalContext.current))

    NavHost(navController = navController, startDestination = Screen.SPLSCREEN.route){
        composable(Screen.HOMESCREEN.route){
            Scaffold(
                bottomBar = {
                    Column {
                        NowMusicBar(musicViewModel, navController)
                        BottomBar(navController, Screen.HOMESCREEN)
                    }
                }
            ) {innerPadding ->
                HomeScreen(navController, musicViewModel, Modifier.padding(innerPadding))
            }
        }
        composable(Screen.ALBUMSCREEN.route){
            Scaffold(
                bottomBar = {
                    Column {
                        NowMusicBar(musicViewModel, navController)
                        BottomBar(navController, Screen.ALBUMSCREEN)
                    }
                }
            ) {innerPadding ->
                AlbumScreen(navController, musicViewModel, Modifier.padding(innerPadding))
            }
        }
        composable(Screen.FAVORITESCREEN.route) {
            Scaffold(
                bottomBar = {
                    Column {
                        NowMusicBar(musicViewModel, navController)
                        BottomBar(navController, Screen.FAVORITESCREEN)
                    }
                }
            ) {innerPadding ->
                FavoriteScreen(navController, musicViewModel, Modifier.padding(innerPadding))
            }
        }
        composable(Screen.PROFILE.route) {
            Scaffold(
                bottomBar = {
                    Column {
                        NowMusicBar(musicViewModel, navController)
                        BottomBar(navController, Screen.PROFILE)
                    }
                }
            ) {innerPadding ->
                ProfileScreen(navController, Modifier.padding(innerPadding))
            }
        }
        composable(
            "${Screen.PLAYMUSICSCREEN.route}/{id}",
            arguments = listOf(navArgument("id") {type = NavType.StringType}),
        ){backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { id ->
                PlayMusicScreen(navController,musicViewModel, id )
            }
        }
        composable(Screen.SEARCHSCREEN.route) { SearchScreen(musicList = Music.getMusic(), navController) }
        composable(Screen.SPLSCREEN.route) { SplScreen(navController) }
        composable(Screen.LOGIN.route) { LoginScreen( navController) }
        composable(Screen.SIGNUP.route) { SignUpScreen(navController) }
    }
}
@Composable
fun BottomBar(navController: NavController, currentScreen: Screen){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Card(
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.HOMESCREEN.route) {
                        popUpTo(Screen.HOMESCREEN.route) { inclusive = true }
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = if (currentScreen == Screen.HOMESCREEN) ColorButton else Color.Gray
                )
                Text("Home", color = if (currentScreen == Screen.HOMESCREEN) ColorButton else Color.Gray)
            }
        }

        Card(
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.ALBUMSCREEN.route) {
                        popUpTo(Screen.ALBUMSCREEN.route) { inclusive = true }
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = R.drawable.ic_album),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(if (currentScreen == Screen.ALBUMSCREEN) ColorButton else Color.Gray))
                Text("Album", color = if (currentScreen == Screen.ALBUMSCREEN) ColorButton else Color.Gray)
            }
        }

        Card(
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.FAVORITESCREEN.route) {
                        popUpTo(Screen.FAVORITESCREEN.route) { inclusive = true }
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "",
                    tint = if (currentScreen == Screen.FAVORITESCREEN) ColorButton else Color.Gray
                )
                Text("My Favotite", color = if (currentScreen == Screen.FAVORITESCREEN) ColorButton else Color.Gray)
            }
        }

        Card(
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.PROFILE.route) {
                        popUpTo(Screen.PROFILE.route) { inclusive = true }
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = if (currentScreen == Screen.PROFILE) ColorButton else Color.Gray
                )
                Text("Profile", color = if (currentScreen == Screen.PROFILE) ColorButton else Color.Gray)
            }
        }
    }
}
@Composable
fun NowMusicBar(musicViewModel: MusicViewModel, navController: NavController){
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
                .clickable {
                    navController.navigate("${Screen.PLAYMUSICSCREEN.route}/${it.id}")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = it.posterUrl,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f))
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.weight(4f)) {
                Text(text = it.title)
                Text(text = it.singer)
            }
            IconButton(modifier = Modifier.weight(1f),
                onClick = { musicViewModel.previousMusic() }) {
                Image(painterResource(id = R.drawable.ic_previous), contentDescription = "",
                    modifier = Modifier.size(150.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
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
                    modifier = Modifier.size(300.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(modifier = Modifier.weight(1f),
                onClick = {
                    if (musicViewModel.isRandom){musicViewModel.playNextRandom()}
                    else{musicViewModel.nextMusic()}
                }) {
                Image(painterResource(id = R.drawable.ic_next), contentDescription = "",
                    modifier = Modifier.size(150.dp))
            }
        }
    }
}