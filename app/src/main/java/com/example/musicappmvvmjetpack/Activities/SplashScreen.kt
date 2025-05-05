package com.example.musicappmvvmjetpack.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicappmvvmjetpack.Activities.theme.ColorButton
import com.example.musicappmvvmjetpack.R
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                SplScreen(navController = navController)
            }
        }
    }
}

@Composable
fun SplScreen(navController: NavController){
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Spacer(modifier = Modifier.height(40.dp))
        ColorfulCircles()
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)){
                append("Listen To Your ")
            }
            withStyle(style = SpanStyle(color = ColorButton)){
                append("Heart")
            } },
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.splash), color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(200.dp))
        Button(
            onClick = { navController.navigate(if (isLoggedIn) Screen.HOMESCREEN.route else Screen.LOGIN.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorButton,
                contentColor = Color.White
            )
        ) {
            Text(text = "Get Started",fontSize = 17.sp)
        }

    }
}
@Composable
fun ColorfulCircles() {
    val canvasSize = 350.dp
    Box(modifier = Modifier.size(canvasSize)){
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawConcentricCircles()
        }

        // Thêm biểu tượng ở giữa
        Image(
            painter = painterResource(id = R.drawable.ic_mic),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center)
        )
    }

}

fun DrawScope.drawConcentricCircles() {
    val colors = listOf(
        Color(0xFFFCF3F5),
        Color(0xFFFBE2E5),
        Color(0xFFFAC4CA),
        Color(0xFFFB9EA8),
        Color(0xFFFB7F88),
        Color(0xFFFC485D),
    )

    val centerX = size.width / 2
    val centerY = size.height / 2
    val maxRadius = size.minDimension / 2

    for ((index, color) in colors.withIndex()) {
        drawCircle(
            color = color,
            radius = maxRadius - index * 20.dp.toPx(), // Điều chỉnh khoảng cách giữa các vòng
            center = androidx.compose.ui.geometry.Offset(centerX, centerY),
            style = androidx.compose.ui.graphics.drawscope.Fill
        )
    }
}