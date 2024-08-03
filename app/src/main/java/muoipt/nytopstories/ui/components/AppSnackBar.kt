package muoipt.nytopstories.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import muoipt.nytopstories.R

@Composable
fun AppSnackBar(
    scaffoldState: ScaffoldState,
    message: String,
    infoIcon: Int,
    backgroundColor: Color,
    action: (() -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        scaffoldState.snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Indefinite
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 16.dp)
    ) {
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomStart),
            hostState = scaffoldState.snackbarHostState
        ) {
            CustomSnackBar(
                message,
                backgroundColor
            ) {
                scaffoldState.snackbarHostState
                    .currentSnackbarData?.dismiss()
                action?.invoke()
            }
        }
    }
}


@Composable
fun CustomSnackBar(
    message: String,
    backgroundColor: Color,
    onCloseClicked: () -> Unit
) {
    Snackbar(
        backgroundColor = backgroundColor
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                LayoutDirection.Ltr
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (icon, messageText, closeIcon) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .constrainAs(icon) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "ic_close",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .constrainAs(closeIcon) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .clickable(onClick = onCloseClicked)
                )

                Text(
                    text = message,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 30.dp)
                        .constrainAs(messageText) {
                            start.linkTo(icon.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(closeIcon.end)
                            width = Dimension.fillToConstraints
                        }
                )
            }
        }
    }
}