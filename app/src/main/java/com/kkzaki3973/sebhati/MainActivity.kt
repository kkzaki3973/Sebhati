package com.kkzaki3973.sebhati

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.kkzaki3973.sebhati.ui.theme.SebhatiTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SebhatiTheme {
                SebhatiApp(this)
            }
        }
    }
}


@Composable
fun SebhatiApp(context: Context) {

    val preferences = remember {
        context.getSharedPreferences(
            "sebhati_data",
            Context.MODE_PRIVATE
        )
    }

    var darkMode by remember {
        mutableStateOf(
            preferences.getBoolean(
                "dark_mode",
                false
            )
        )
    }

    val backgroundColor =
        if (darkMode) Color(0xFF101613)
        else Color(0xFFF5F7F2)

    val primaryColor =
        if (darkMode) Color(0xFF6FA88B)
        else Color(0xFF315C4A)

    val secondaryColor =
        if (darkMode) Color(0xFF315C4A)
        else Color(0xFF3F735C)

    val textColor =
        if (darkMode) Color.White
        else Color(0xFF20352C)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {

        SebhatiScreen(
            context = context,
            preferences = preferences,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            textColor = textColor,
            darkMode = darkMode,
            onToggleDarkMode = {

                darkMode = !darkMode

                preferences.edit()
                    .putBoolean(
                        "dark_mode",
                        darkMode
                    )
                    .apply()
            }
        )
    }
}


@Composable
fun SebhatiScreen(
    context: Context,
    preferences: android.content.SharedPreferences,
    primaryColor: Color,
    secondaryColor: Color,
    textColor: Color,
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {

    var count by remember {
        mutableStateOf(
            preferences.getInt(
                "count",
                0
            )
        )
    }

    var selectedDhikr by remember {
        mutableStateOf(
            preferences.getString(
                "dhikr",
                "سبحان الله"
            ) ?: "سبحان الله"
        )
    }

    var target by remember {
        mutableStateOf(
            preferences.getInt(
                "target",
                33
            )
        )
    }

    var showResetDialog by remember {
        mutableStateOf(false)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    val dhikrList = listOf(
        "سبحان الله",
        "الحمد لله",
        "الله أكبر",
        "أستغفر الله"
    )


    fun vibrate() {

        val vibrator: Vibrator

        if (Build.VERSION.SDK_INT >= 31) {

            val manager =
                context.getSystemService(
                    Context.VIBRATOR_MANAGER_SERVICE
                ) as VibratorManager

            vibrator = manager.defaultVibrator

        } else {

            @Suppress("DEPRECATION")
            vibrator =
                context.getSystemService(
                    Context.VIBRATOR_SERVICE
                ) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= 26) {

            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    30,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )

        } else {

            @Suppress("DEPRECATION")
            vibrator.vibrate(30)
        }
    }


    fun saveCount(value: Int) {

        count = value

        preferences.edit()
            .putInt(
                "count",
                value
            )
            .apply()
    }


    val progress = if (target > 0) {

        (count.toFloat() / target.toFloat())
            .coerceIn(0f, 1f)

    } else {

        0f
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
            Arrangement.SpaceBetween,
            verticalAlignment =
            Alignment.CenterVertically
        ) {

            Text(
                text = "سبحتي",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Row {

                IconButton(
                    onClick = onToggleDarkMode
                ) {

                    Icon(
                        imageVector =
                        Icons.Default.DarkMode,
                        contentDescription =
                        "الوضع الليلي",
                        tint = primaryColor
                    )
                }

                IconButton(
                    onClick = {
                        showSettings = true
                    }
                ) {

                    Icon(
                        imageVector =
                        Icons.Default.Settings,
                        contentDescription =
                        "الإعدادات",
                        tint = primaryColor
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = "سبّح واذكر الله",
            fontSize = 15.sp,
            color = textColor.copy(
                alpha = 0.65f
            )
        )


        Spacer(
            modifier = Modifier.height(25.dp)
        )


        Text(
            text = selectedDhikr,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            textAlign = TextAlign.Center
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Box(
            modifier = Modifier.size(270.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.size(270.dp)
            ) {

                drawArc(
                    color = primaryColor.copy(
                        alpha = 0.15f
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(
                        width = 18.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(
                        width = 18.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }


            Button(
                onClick = {

                    saveCount(
                        count + 1
                    )

                    vibrate()
                },
                modifier = Modifier.size(220.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                    secondaryColor
                )
            ) {

                Column(
                    horizontalAlignment =
                    Alignment.CenterHorizontally,
                    verticalArrangement =
                    Arrangement.Center
                ) {

                    Text(
                        text = count.toString(),
                        fontSize = 58.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (target > 0) {

                        Text(
                            text = "من $target",
                            fontSize = 14.sp,
                            color = Color.White.copy(
                                alpha = 0.8f
                            )
                        )
                    }
                }
            }
        }


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        Text(
            text =
            if (target > 0)
                "${(progress * 100).toInt()}% من الهدف"
            else
                "تسبيح مفتوح",
            fontSize = 14.sp,
            color = textColor.copy(
                alpha = 0.65f
            )
        )


        Spacer(
            modifier = Modifier.height(18.dp)
        )


        OutlinedButton(
            onClick = {
                showResetDialog = true
            },
            shape = RoundedCornerShape(14.dp)
        ) {

            Icon(
                imageVector =
                Icons.Default.Refresh,
                contentDescription =
                "تصفير"
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = "تصفير"
            )
        }


        Spacer(
            modifier = Modifier.height(18.dp)
        )


        Text(
            text = "اختر الذكر",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Row(
            horizontalArrangement =
            Arrangement.Center
        ) {

            dhikrList.take(2).forEach { dhikr ->

                DhikrButton(
                    text = dhikr,
                    selected =
                    selectedDhikr == dhikr,
                    primaryColor =
                    primaryColor,
                    onClick = {

                        selectedDhikr = dhikr

                        count = 0

                        preferences.edit()
                            .putString(
                                "dhikr",
                                dhikr
                            )
                            .putInt(
                                "count",
                                0
                            )
                            .apply()
                    }
                )
            }
        }


        Spacer(
            modifier = Modifier.height(6.dp)
        )


        Row(
            horizontalArrangement =
            Arrangement.Center
        ) {

            dhikrList.drop(2).forEach { dhikr ->

                DhikrButton(
                    text = dhikr,
                    selected =
                    selectedDhikr == dhikr,
                    primaryColor =
                    primaryColor,
                    onClick = {

                        selectedDhikr = dhikr

                        count = 0

                        preferences.edit()
                            .putString(
                                "dhikr",
                                dhikr
                            )
                            .putInt(
                                "count",
                                0
                            )
                            .apply()
                    }
                )
            }
        }
    }


    if (showResetDialog) {

        AlertDialog(
            onDismissRequest = {
                showResetDialog = false
            },

            title = {
                Text("تصفير العداد")
            },

            text = {
                Text(
                    "هل تريد تصفير عدد التسبيحات؟"
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        saveCount(0)

                        showResetDialog = false
                    }
                ) {

                    Text("نعم")
                }
            },

            dismissButton = {

                OutlinedButton(
                    onClick = {
                        showResetDialog = false
                    }
                ) {

                    Text("إلغاء")
                }
            }
        )
    }


    if (showSettings) {

        AlertDialog(
            onDismissRequest = {
                showSettings = false
            },

            title = {
                Text("هدف التسبيح")
            },

            text = {

                Column {

                    TargetButton(
                        text = "33 تسبيحة",
                        selected = target == 33,
                        onClick = {

                            target = 33

                            preferences.edit()
                                .putInt(
                                    "target",
                                    33
                                )
                                .apply()

                            showSettings = false
                        }
                    )


                    TargetButton(
                        text = "99 تسبيحة",
                        selected = target == 99,
                        onClick = {

                            target = 99

                            preferences.edit()
                                .putInt(
                                    "target",
                                    99
                                )
                                .apply()

                            showSettings = false
                        }
                    )


                    TargetButton(
                        text = "بدون هدف",
                        selected = target == 0,
                        onClick = {

                            target = 0

                            preferences.edit()
                                .putInt(
                                    "target",
                                    0
                                )
                                .apply()

                            showSettings = false
                        }
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {
                        showSettings = false
                    }
                ) {

                    Text("إغلاق")
                }
            }
        )
    }
}


@Composable
fun DhikrButton(
    text: String,
    selected: Boolean,
    primaryColor: Color,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = Modifier.padding(
            horizontal = 3.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
            if (selected)
                primaryColor
            else
                primaryColor.copy(
                    alpha = 0.35f
                )
        )
    ) {

        Text(
            text = text,
            fontSize = 11.sp
        )
    }
}


@Composable
fun TargetButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor =
            if (selected)
                Color(0xFF315C4A)
            else
                Color(0xFFB7C9BE)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Text(text = text)
    }
}