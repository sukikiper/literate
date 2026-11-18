package com.literate.novel.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.literate.novel.data.BookshelfType
import com.literate.novel.ui.theme.*

@Composable
fun BookmarkDialog(
    novelTitle: String,
    currentShelf: BookshelfType?,
    onDismiss: () -> Unit,
    onSelectShelf: (BookshelfType?) -> Unit
) {
    var selectedShelf by remember { mutableStateOf(currentShelf) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF111622),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Atur Rak Buku (PRD 4.3)",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = novelTitle,
                    color = MintSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                val shelves = listOf(
                    BookshelfType.CURRENTLY_READING to "Sedang Dibaca (Currently Reading)",
                    BookshelfType.WANT_TO_READ to "Ingin Dibaca (Want to Read)",
                    BookshelfType.READ to "Selesai Dibaca (Read)",
                    BookshelfType.DNF to "Did Not Finish (DNF)"
                )

                shelves.forEach { (shelf, label) ->
                    val isSelected = selectedShelf == shelf
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedShelf = if (isSelected) null else shelf }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) MintSecondary else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MintSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    HorizontalDivider(color = DividerDark, thickness = 0.5.dp)
                }

                Spacer(modifier = Modifier.height(20.dp))

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
                            onSelectShelf(selectedShelf)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Simpan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

