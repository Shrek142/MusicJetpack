package com.example.musicappmvvmjetpack.Activities

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicappmvvmjetpack.Activities.theme.ColorButton
import com.example.musicappmvvmjetpack.R
import com.example.musicappmvvmjetpack.ViewModel.AuthViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LogInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                LoginScreen(navController = navController)
            }
        }
    }
}
@Composable
fun LoginScreen(
    navController: NavController
) {
    val viewModel: AuthViewModel = viewModel()

    val loginFailMessage = stringResource(id = R.string.login_fail)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authResult by viewModel.authResult.collectAsState()
    val callbackManager = remember { CallbackManager.Factory.create() }

    val context = LocalContext.current

    var isShowed by remember {
        mutableStateOf(false)
    }
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let {
                viewModel.loginWithGoogle(it)
            }
        } catch (e: Exception) {
            Log.e("GOOGLE_LOGIN", "Google sign-in failed", e)
        }
    }
    DisposableEffect(Unit) {
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.setClientToken("YOUR_CLIENT_TOKEN_HERE")
            FacebookSdk.sdkInitialize(context.applicationContext)
            AppEventsLogger.activateApp(context.applicationContext as Application)
        }
        onDispose { }
    }


    // Setup Google SignIn Client
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    LaunchedEffect(authResult) {
        authResult?.let {
            if (it) {
                viewModel.loadCurrentUser()
                navController.navigate(Screen.HOMESCREEN.route)
            } else {
                Toast.makeText(context, loginFailMessage, Toast.LENGTH_SHORT).show()
            }
            viewModel.resetAuthResult()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(26.dp, 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = stringResource(id = R.string.login), fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.login_1), color = Color.Gray, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painterResource(id = R.drawable.ic_mic),
            contentDescription = "",
            colorFilter = ColorFilter.tint(ColorButton),
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))
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
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(id = R.string.pass), fontSize = 15.sp, color = ColorButton) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { isShowed = !isShowed }) {
                    Image(
                        painterResource(id = if (isShowed){
                            R.drawable.ic_hide} else R.drawable.ic_show),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = Color.Gray))
                }
            },
            visualTransformation = if (isShowed) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(text = stringResource(id = R.string.login_2), fontSize = 15.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Sign Up",
                fontSize = 15.sp,
                color = ColorButton,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.SIGNUP.route)
                })
        }
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                    viewModel.login(email, password)
            },
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            ),
            modifier = Modifier.padding(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorButton,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(id = R.string.login), modifier = Modifier.padding(6.dp))
        }
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(text = stringResource(id = R.string.or), modifier = Modifier
                .padding(vertical = 20.dp)
                .background(Color.White))
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painterResource(id = R.drawable.fb),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(50.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .clickable {
                        context
                            .findActivity()
                            ?.let {
                                LoginManager
                                    .getInstance()
                                    .logInWithReadPermissions(
                                        it,
                                        listOf("email", "public_profile")
                                    )
                            }
                    },
                contentScale = ContentScale.Fit)
            Spacer(modifier = Modifier.width(30.dp))
            Image(
                painterResource(id = R.drawable.gg),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(50.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .clickable {
                        val signInIntent = googleSignInClient.signInIntent
                        googleLauncher.launch(signInIntent)
                    },
                contentScale = ContentScale.Fit)
            Spacer(modifier = Modifier.width(30.dp))
            Image(
                painterResource(id = R.drawable.git),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(50.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .clickable {
                        val activity = context.findActivity()  // Extension function
                        activity?.let { viewModel.loginWithGitHub(it) }
                    },
                contentScale = ContentScale.Fit)
        }
    }
    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    viewModel.loginWithFacebook(result.accessToken)
                }

                override fun onCancel() {}
                override fun onError(error: FacebookException) {
                    Toast.makeText(context, "Lỗi Facebook: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
        onDispose {}
    }
}

