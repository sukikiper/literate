package com.literate.novel.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.literate.novel.ui.theme.*

@Composable
fun RatingDialog(
    novelTitle: String,
    onDismiss: () -> Unit,
    onSubmit: (stars: Float, content: String, isSpoiler: Boolean, progressTag: String?) -> Unit
) {
    var selectedRating by remember { mutableStateOf(5.0f) }
    var reviewText by remember { mutableStateOf("") }
    var isSpoiler by remember { mutableStateOf(false) }
    var progressTag by remember { mutableStateOf("") }

    // Optional aspect ratings according to PRD 4.2
    var pacingRating by remember { mutableStateOf(4.5f) }
    var characterRating by remember { mutableStateOf(5.0f) }
    var worldBuildingRating by remember { mutableStateOf(5.0f) }
    var plotTwistRating by remember { mutableStateOf(4.5f) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF111622),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Beri Rating & Ulasan",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = novelTitle,
                    color = MintSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Star Rating Picker (0.5 - 5.0) as per PRD 4.2
                Text(
                    text = "Skor Rating: ${String.format("%.1f", selectedRating)} / 5.0",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        IconButton(onClick = { selectedRating = i.toFloat() }) {
                            Icon(
                                imageVector = if (selectedRating >= i) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = "$i Bintang",
                                tint = if (selectedRating >= i) GoldStar else TextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Aspect ratings from PRD 4.2
                Text(
                    text = "Aspek Penilaian (PRD 4.2)",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                AspectRatingRow(label = "Pacing", rating = pacingRating, onRatingChange = { pacingRating = it })
                AspectRatingRow(label = "Character Dev", rating = characterRating, onRatingChange = { characterRating = it })
                AspectRatingRow(label = "World Building", rating = worldBuildingRating, onRatingChange = { worldBuildingRating = it })
                AspectRatingRow(label = "Plot Twist", rating = plotTwistRating, onRatingChange = { plotTwistRating = it })

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Tagging as per PRD 4.2
                Text(
                    text = "Progres Bacaan Saat Mengulas",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = progressTag,
                    onValueChange = { progressTag = it },
                    placeholder = { Text("Contoh: Bab 15 atau Halaman 120", color = TextMuted, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = InputBg,
                        unfocusedContainerColor = InputBg,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintSecondary,
                        unfocusedBorderColor = CardBorder
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Written review text editor
                Text(
                    text = "Tulis Ulasan",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    placeholder = { Text("Bagikan pengalaman membaca novel ini...", color = TextMuted, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = InputBg,
                        unfocusedContainerColor = InputBg,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = MintSecondary,
                        unfocusedBorderColor = CardBorder
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Spoiler Protection Tag checkbox as per PRD 4.2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isSpoiler = !isSpoiler },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isSpoiler,
                        onCheckedChange = { isSpoiler = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MintSecondary,
                            checkmarkColor = BgDark,
                            uncheckedColor = TextMuted
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tandai ulasan ini mengandung spoiler",
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Batal", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (reviewText.isNotBlank()) {
                                onSubmit(
                                    selectedRating,
                                    reviewText,
                                    isSpoiler,
                                    if (progressTag.isNotBlank()) progressTag else null
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Kirim Ulasan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AspectRatingRow(
    label: String,
    rating: Float,
    onRatingChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
        Row {
            for (step in 1..5) {
                Text(
                    text = "★",
                    color = if (rating >= step) GoldStar else Color(0xFF374151),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clickable { onRatingChange(step.toFloat()) }
                )
            }
        }
    }
}

