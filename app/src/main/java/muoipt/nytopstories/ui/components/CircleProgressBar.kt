package muoipt.nytopstories.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleProgressBar(
    modifier: Modifier = Modifier.fillMaxSize(),
    size: Dp = 30.dp,
    color: Color = Color.Blue,
    strokeWidth: Dp = 3.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                color = color,
                strokeWidth = strokeWidth
            )
        }
    }
}