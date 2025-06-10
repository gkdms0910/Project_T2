package com.example.project_t2.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Weathers
import java.time.LocalDateTime

@Entity(tableName = "diaryTable")
data class DiaryEntity(
    val title: String,
//    @Embedded val weather: Weather,
    val weather: Weathers,
    val emotion: Emotion,
    val content: String,
    val time: LocalDateTime = LocalDateTime.now(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)