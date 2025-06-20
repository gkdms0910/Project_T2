package com.example.project_t2.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Weathers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Database(
    entities = [DiaryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao

    companion object {
        @Volatile
        private var DBInstance: DiaryDatabase? = null

        fun getDBInstance(context: Context): DiaryDatabase {
            return DBInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diarydb"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getDBInstance(context).getDiaryDao().insertAll(getInitialDiaries())
                            }
                        }
                    })
                    .build()
                DBInstance = instance
                instance
            }
        }

        private fun getInitialDiaries(): List<DiaryEntity> {
            return listOf(
                DiaryEntity("오늘의 일기", Weathers.SUNNY, Emotion.HAPPY, "오늘은 날씨가 정말 좋아서 기분이 상쾌했다.", LocalDateTime.now().minusDays(1)),
                DiaryEntity("비 오는 날", Weathers.RAINY, Emotion.SAD, "창밖에 비가 내렸다. 집에서 조용히 책을 읽으며 하루를 보냈다.", LocalDateTime.now().minusDays(3)),
                DiaryEntity("프로젝트 회의", Weathers.CLOUDY, Emotion.TENDER, "팀원들과 프로젝트에 대해 깊은 이야기를 나누었다.", LocalDateTime.now().minusDays(5)),
                DiaryEntity("화나는 일", Weathers.STORMY, Emotion.ANGRY, "예상치 못한 문제 때문에 화가 많이 났다. 마음을 다스려야겠다.", LocalDateTime.now().minusDays(10)),
                DiaryEntity("두려운 발표", Weathers.WINDY, Emotion.FEAR, "내일 있을 발표 때문에 조금 두렵고 떨린다.", LocalDateTime.now().minusDays(15)),
                DiaryEntity("눈 오는 날의 산책", Weathers.SNOWY, Emotion.HAPPY, "하얀 눈이 펑펑 내렸다. 오랜만에 눈사람도 만들고 즐거운 하루를 보냈다.", LocalDateTime.now().minusDays(20)),
                DiaryEntity("안개 낀 아침", Weathers.FOGGY, Emotion.TENDER, "자욱한 안개가 낀 아침, 모든 것이 흐릿하게 보였다.", LocalDateTime.now().minusDays(22)),
                DiaryEntity("행복한 저녁 식사", Weathers.SUNNY, Emotion.HAPPY, "가족들과 함께 맛있는 저녁을 먹었다.", LocalDateTime.now().minusDays(25)),
                DiaryEntity("실수 그리고 배움", Weathers.CLOUDY, Emotion.SAD, "오늘 큰 실수를 저질렀다. 속상했지만, 이번 일을 계기로 더 성장할 수 있을 거라 믿는다.", LocalDateTime.now().minusDays(30)),
                DiaryEntity("번개가 치던 밤", Weathers.STORMY, Emotion.FEAR, "천둥 번개가 무섭게 내리쳤다.", LocalDateTime.now().minusDays(35)),
                DiaryEntity("오랜 친구와의 만남", Weathers.WINDY, Emotion.HAPPY, "바람이 많이 부는 날, 오랜 친구를 만났다.", LocalDateTime.now().minusDays(40)),
                DiaryEntity("새로운 시작", Weathers.SUNNY, Emotion.TENDER, "새로운 목표를 세웠다. 앞으로의 날들이 기대된다.", LocalDateTime.now().minusDays(45)),
                DiaryEntity("구름 한 점 없는 하늘", Weathers.SUNNY, Emotion.SAD, "날씨는 이렇게 좋은데, 내 마음은 왜 이리 무거운지 모르겠다.", LocalDateTime.now().minusDays(2)),
                DiaryEntity("억수같이 쏟아지는 비", Weathers.RAINY, Emotion.ANGRY, "모든 게 다 엉망진창이다. 비까지 와서 더 화가 난다.", LocalDateTime.now().minusDays(4)),
                DiaryEntity("바람과 함께 사라지다", Weathers.WINDY, Emotion.FEAR, "강한 바람에 모든 걱정이 날아갔으면 좋겠다.", LocalDateTime.now().minusDays(6)),
                DiaryEntity("조용한 도서관", Weathers.CLOUDY, Emotion.TENDER, "흐린 날엔 역시 도서관이지. 마음이 차분해진다.", LocalDateTime.now().minusDays(8)),
                DiaryEntity("폭풍 전야", Weathers.STORMY, Emotion.FEAR, "무슨 일이 일어나기 직전처럼 불안하다.", LocalDateTime.now().minusDays(12)),
                DiaryEntity("첫눈의 설렘", Weathers.SNOWY, Emotion.HAPPY, "올해 첫눈을 맞았다! 너무 예쁘고 설레는 순간이었다.", LocalDateTime.now().minusDays(18)),
                DiaryEntity("안개 속을 걷다", Weathers.FOGGY, Emotion.SAD, "한 치 앞도 보이지 않는 안개처럼 내 미래도 불투명한 것 같다.", LocalDateTime.now().minusDays(28)),
                DiaryEntity("따스한 햇살 아래", Weathers.SUNNY, Emotion.HAPPY, "지난 달 오늘, 햇살이 참 좋았다. 공원에서 책을 읽으며 여유를 즐겼다.", LocalDateTime.now().minusMonths(1).minusDays(1)),
                DiaryEntity("소나기", Weathers.RAINY, Emotion.TENDER, "갑자기 내린 소나기에 흠뻑 젖었지만, 왠지 모르게 시원하고 상쾌했다.", LocalDateTime.now().minusMonths(1).minusDays(7)),
                DiaryEntity("우울한 하루", Weathers.CLOUDY, Emotion.SAD, "하루 종일 하늘이 회색빛이었다. 내 기분도 딱 그랬다.", LocalDateTime.now().minusMonths(1).minusDays(14)),
                DiaryEntity("뜻밖의 행운", Weathers.SUNNY, Emotion.HAPPY, "길을 가다 우연히 좋은 소식을 들었다. 맑은 날씨처럼 내 기분도 맑음!", LocalDateTime.now().minusMonths(1).minusDays(21)),
                DiaryEntity("복잡한 마음", Weathers.WINDY, Emotion.FEAR, "바람이 세차게 부는 것처럼 마음속도 복잡하고 시끄럽다.", LocalDateTime.now().minusMonths(1).minusDays(28))
            )
        }
    }
}