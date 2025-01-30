package example.com.fielthyapps.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import example.com.fielthyapps.R
import kotlin.math.roundToInt

@Composable
fun StepCard(
    steps: Long,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {},
        enabled = false,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(R.drawable.ic_running),
                contentDescription = stringResource(R.string.heart_description),
                modifier = Modifier.padding(end = 8.dp).size(24.dp)
            )
            Column {
                val stepsText = steps.toString()
                Text(stepsText)
                Text(
                    text = stringResource(id = R.string.last_measured),
                    style = MaterialTheme.typography.caption3
                )
            }
        }
    }
}