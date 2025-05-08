package com.example.musicappmvvmjetpack.Activities

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import coil.compose.rememberAsyncImagePainter
import com.example.musicappmvvmjetpack.Activities.theme.ColorButton
import com.example.musicappmvvmjetpack.LanguageManager
import com.example.musicappmvvmjetpack.Model.User
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.AuthViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                ProfileScreen(navController = navController, padding = Modifier)
            }
        }
    }
}
@Composable
fun ProfileScreen(navController: NavController, padding: Modifier) {
    val viewModel: AuthViewModel = viewModel()
    val user by viewModel.currentUser.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    Scaffold(
        topBar = { TopProBar(navController) }
    ) { innerPadding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                isRefreshing = true
                viewModel.loadCurrentUser()
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            LaunchedEffect(user) {
                isRefreshing = false
            }
            MainContent(innerPadding, navController)
        }
    }
}

@Composable
fun MainContent(innerPadding: PaddingValues, navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val user by viewModel.currentUser.collectAsState()

    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {
        UserDialog(
            onDismissRequest = { openDialog = false },
            onConfirmation = { openDialog = false }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        user?.let {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(it.photoUrl.ifEmpty { R.drawable.img_bachduong }),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(130.dp)
                        .border(1.dp, ColorButton),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(10.dp))
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    val (text, img) = createRefs()
                    Text(
                        text = it.username, fontSize = 40.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.constrainAs(text) {
                            centerHorizontallyTo(parent)
                            centerVerticallyTo(parent)
                        }
                    )
                    IconButton(
                        onClick = { openDialog = true },
                        modifier = Modifier.constrainAs(img){
                            start.linkTo(text.end, margin = 5.dp)
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Create, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Premium User", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.username, fontSize = 20.sp, color = ColorButton, maxLines = 1)
                        Text(text = stringResource(id = R.string.profile_1), fontSize = 15.sp, color = Color.Gray)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.email, fontSize = 20.sp, color = ColorButton, maxLines = 1)
                        Text(text = stringResource(id = R.string.profile_2), fontSize = 15.sp, color = Color.Gray)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.phoneNumber, fontSize = 20.sp, color = ColorButton, maxLines = 1)
                        Text(text = stringResource(id = R.string.profile_3), fontSize = 15.sp, color = Color.Gray)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate(Screen.LOGIN.route) {
                    popUpTo(Screen.HOMESCREEN.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorButton,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(id = R.string.signout))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = stringResource(id = R.string.profile_4), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumnSinger()
    }
}
@Composable
fun LanguageSelectionButtons(
    user: User?,
    viewModel: AuthViewModel,
    context: Context,
    activity: Activity?,
    modifier: Modifier
) {
    val updateResult by viewModel.updateResult.collectAsState()
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var selectedLanguage by rememberSaveable { mutableStateOf(prefs.getString("lang", "vi") ?: "vi") }
    var pendingLanguage by rememberSaveable { mutableStateOf<String?>(null) }
    var shouldRecreate by rememberSaveable { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(vertical = 5.dp)
    ) {
        Box {
            Button(
                onClick = { showDropdown = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row {
                    Image(
                        painter = rememberAsyncImagePainter(
                            if (selectedLanguage == "vi") R.drawable.viet
                                else R.drawable.anh),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(20.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (selectedLanguage == "vi") "VIE" else "ENG",
                        fontSize = 14.sp
                    )
                }
            }

            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Tiếng Việt") },
                    onClick = {
                        if (selectedLanguage != "vi") {
                            selectedLanguage = "vi"
                            pendingLanguage = "vi"
                            val updatedUser = user?.copy(language = "vi")
                            if (updatedUser != null) {
                                viewModel.updateUser(updatedUser)
                                prefs.edit().putString("lang", "vi").commit()
                            }
                        }
                        showDropdown = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("English") },
                    onClick = {
                        if (selectedLanguage != "en") {
                            selectedLanguage = "en"
                            pendingLanguage = "en"
                            val updatedUser = user?.copy(language = "en")
                            if (updatedUser != null) {
                                viewModel.updateUser(updatedUser)
                                prefs.edit().putString("lang", "en").commit()
                            }
                        }
                        showDropdown = false
                    }
                )
            }
        }
    }

    LaunchedEffect(updateResult, shouldRecreate, pendingLanguage) {
        if (updateResult == true && !shouldRecreate && pendingLanguage != null) {
            val languageToApply = pendingLanguage!!
            activity?.let {
                val updatedContext = LanguageManager.setLocale(it, languageToApply)
                it.recreate()
            }
            shouldRecreate = true
            viewModel.resetAuthResult()
        }
    }

    LaunchedEffect(Unit) {
        if (shouldRecreate) {
            shouldRecreate = false
            pendingLanguage = null
            val currentLang = prefs.getString("lang", "vi") ?: "vi"
            if (currentLang != selectedLanguage) {
                selectedLanguage = currentLang
            }
        }
    }
}

@Composable
fun TopProBar(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val user by viewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        Text(
            text = stringResource(id = R.string.profile),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.weight(3f)
        )
        LanguageSelectionButtons(user, viewModel, context, activity, modifier = Modifier.weight(1f))
    }
}

@Composable
fun UserDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val user by viewModel.currentUser.collectAsState()
    val updateResult by viewModel.updateResult.collectAsState()
    val context = LocalContext.current

    var username by remember { mutableStateOf(user?.username ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val updateOkMessage = stringResource(id = R.string.update_ok)
    val updateFailedMessage = stringResource(id = R.string.update_fail)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        photoUri = uri
    }

    LaunchedEffect(updateResult) {
        updateResult?.let {
            val msg = if (it) updateOkMessage else updateFailedMessage
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(300.dp, 500.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(photoUri ?: user?.photoUrl ?: R.drawable.img_bachduong),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(90.dp)
                        .clickable { imagePickerLauncher.launch("image/*") }
                        .border(1.dp, ColorButton),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = stringResource(id = R.string.username), fontSize = 15.sp, color = ColorButton) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = stringResource(id = R.string.email), fontSize = 15.sp, color = ColorButton) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(text = stringResource(id = R.string.phone), fontSize = 15.sp, color = ColorButton) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.dialog_cancel))
                    }
                    TextButton(
                        onClick = {
                            user?.let {
                                val updatedUser = it.copy(
                                    username = username,
                                    email = email,
                                    phoneNumber = phone,
                                    photoUrl = photoUri?.toString() ?: it.photoUrl
                                )
                                viewModel.updateUser(updatedUser)
                                viewModel.updateUserEmailAndFirestore(email)
                            }
                            onConfirmation()
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.dialog_confirm))
                    }
                }
            }
        }
    }
}
@Composable
fun LazyColumnSinger() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(5) {
            ItemSinger(singer = "aa", song = "ssss")
        }
    }
}

@Composable
fun ItemSinger(singer: String, song: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_1),
            contentDescription = "",
            modifier = Modifier
                .weight(1.5f)
                .size(70.dp)
                .padding(horizontal = 5.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier.weight(3f)) {
            Text(text = singer, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text(text = song, color = Color.Gray, fontSize = 15.sp)
        }
        Button(
            onClick = {},
            modifier = Modifier.weight(2f),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = ColorButton
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(id = R.string.profile_btn_follow))
        }
    }
}