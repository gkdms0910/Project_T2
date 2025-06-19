package com.example.project_t2.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.project_t2.roomDB.SortType
import androidx.compose.ui.graphics.Color

@Composable
fun SortButtons(current: SortType, onSortChange: (SortType) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { onSortChange(SortType.NEWEST) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (current == SortType.NEWEST) Color.Gray else Color.LightGray
            )
        ) {
            Text("최신순")
        }

        Button(
            onClick = { onSortChange(SortType.OLDEST) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (current == SortType.OLDEST) Color.Gray else Color.LightGray
            )
        ) {
            Text("오래된순")
        }
    }
}
