package com.example.musicappmvvmjetpack.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                SignUpScreen(navController = navController)
            }
        }
    }
}
@Composable
fun SignUpScreen(
    navController: NavController
) {
    val viewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    var isShowed1 by remember { mutableStateOf(false) }
    var isShowed2 by remember { mutableStateOf(false) }

    val result by viewModel.authResult.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    LaunchedEffect(result) {
        result?.let {
            if (it) {
                viewModel.loadCurrentUser()
                Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.LOGIN.route) {
                    popUpTo(Screen.SIGNUP.route) { inclusive = true }
                }
            } else {
                Toast.makeText(context, errorMessage ?: "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
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
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Sign Up", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Create your account", color = Color.Gray, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painterResource(id = R.drawable.ic_mic),
            contentDescription = "",
            colorFilter = ColorFilter.tint(ColorButton),
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email", fontSize = 15.sp, color = ColorButton) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password", fontSize = 15.sp, color = ColorButton) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { isShowed1 = !isShowed1 }) {
                    Image(
                        painterResource(id = if (isShowed1){
                            R.drawable.ic_hide} else R.drawable.ic_show),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = Color.Gray))
                }
            },
            visualTransformation = if (isShowed1) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text(text = "Confirm Password", fontSize = 15.sp, color = ColorButton) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { isShowed2 = !isShowed2 }) {
                    Image(
                        painterResource(id = if (isShowed2){
                            R.drawable.ic_hide} else R.drawable.ic_show),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = Color.Gray))
                }
            },
            visualTransformation = if (isShowed2) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username", fontSize = 15.sp, color = ColorButton) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(text = "Phone", fontSize = 15.sp, color = ColorButton) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(text = "Already have an account?", fontSize = 15.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Log In",
                fontSize = 15.sp,
                color = ColorButton,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.LOGIN.route)
                })
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                if (password == confirm){
                    viewModel.register(
                        email = email,
                        password = password,
                        username = username,
                        phoneNumber = phoneNumber
                    )
                }else{
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
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
            Text(
                text = "Sign Up",
                modifier = Modifier.padding(6.dp)
            )
        }

    }
}