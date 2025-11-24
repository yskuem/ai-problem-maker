import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PackageType
import ai_problem_maker.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaywallPart(
    packages: List<Package>,
    isSubscribed: Boolean,
    isProcessing: Boolean,
    onPurchase: (Package) -> Unit,
    onRestore: () -> Unit,
) {
    val primaryColor = Color(0xFF0066CC)
    val backgroundColor = Color(0xFFF5F7FA)

    var selectedPackage by remember(packages) {
        mutableStateOf(
            packages.firstOrNull { it.packageType == PackageType.ANNUAL } ?: packages.firstOrNull()
        )
    }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(Res.string.paywall_title),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(Res.string.paywall_description),
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FeatureItem(
                    icon = Icons.Default.Check,
                    title = stringResource(Res.string.feature_ad_free_title),
                    description = stringResource(Res.string.feature_ad_free_desc),
                    primaryColor = primaryColor
                )
                FeatureItem(
                    icon = Icons.Default.Star,
                    title = stringResource(Res.string.feature_unlock_all_title),
                    description = stringResource(Res.string.feature_unlock_all_desc),
                    primaryColor = primaryColor
                )
                FeatureItem(
                    icon = Icons.Default.Lock,
                    title = stringResource(Res.string.feature_priority_support_title),
                    description = stringResource(Res.string.feature_priority_support_desc),
                    primaryColor = primaryColor
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    packages.forEach { rcPackage ->
                        val title = when (rcPackage.packageType) {
                            PackageType.ANNUAL -> stringResource(Res.string.plan_annual)
                            PackageType.MONTHLY -> stringResource(Res.string.plan_monthly)
                            PackageType.LIFETIME -> stringResource(Res.string.plan_lifetime)
                            else -> stringResource(Res.string.plan_other)
                        }

                        val period = when (rcPackage.packageType) {
                            PackageType.ANNUAL -> stringResource(Res.string.price_per_year)
                            PackageType.MONTHLY -> stringResource(Res.string.price_per_month)
                            else -> ""
                        }

                        val discount = if (rcPackage.packageType == PackageType.ANNUAL) stringResource(Res.string.discount_label) else null

                        PlanCard(
                            title = title,
                            price = rcPackage.storeProduct.price.formatted,
                            period = period,
                            discount = discount,
                            isSelected = selectedPackage == rcPackage,
                            onClick = { selectedPackage = rcPackage },
                            primaryColor = primaryColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    selectedPackage?.let { onPurchase(it) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                enabled = selectedPackage != null && !isSubscribed
            ) {
                Text(
                    text = if (isSubscribed) stringResource(Res.string.subscribed_label) else stringResource(Res.string.start_now_label),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onRestore,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubscribed
            ) {
                Text(
                    text = stringResource(Res.string.restore_purchase_label),
                    color = if (isSubscribed) Color.Gray else primaryColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (isProcessing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String,
    primaryColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(primaryColor.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = primaryColor
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color(0xFF777777)
            )
        }
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    period: String,
    discount: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    primaryColor: Color
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor else Color(0xFFE0E0E0),
        label = "planBorderColor"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor.copy(alpha = 0.06f) else Color.White,
        label = "planBackgroundColor"
    )

    val shape = RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() }
            ),
        color = backgroundColor,
        tonalElevation = if (isSelected) 1.dp else 0.dp,
        shadowElevation = if (isSelected) 1.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = price,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Text(
                        text = period,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                discount?.let {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = primaryColor
                    ) {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = primaryColor,
                        unselectedColor = Color(0xFFB0B0B0)
                    )
                )
            }
        }
    }
}