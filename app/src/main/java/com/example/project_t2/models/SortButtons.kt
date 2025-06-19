package com.example.project_t2.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.project_t2.roomDB.SortType
import com.example.project_t2.ui.theme.MainFont

@Composable
fun SortButtons(current: SortType, onSortChange: (SortType) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { onSortChange(SortType.NEWEST) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (current == SortType.NEWEST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("최신순", fontFamily = MainFont)
        }

        Button(
            onClick = { onSortChange(SortType.OLDEST) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (current == SortType.OLDEST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("오래된순", fontFamily = MainFont)
        }
    }
}