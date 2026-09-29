package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CachedChapterEntity::class,
        QuizAttemptEntity::class,
        ForumPostEntity::class,
        ForumReplyEntity::class,
        ExamDeadlineEntity::class,
        UserProfileEntity::class,
        VideoStatusEntity::class,
        FlashcardEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AlidemyDatabase : RoomDatabase() {
    abstract fun alidemyDao(): AlidemyDao

    companion object {
        @Volatile
        private var INSTANCE: AlidemyDatabase? = null

        fun getDatabase(context: Context): AlidemyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlidemyDatabase::class.java,
                    "alidemy_education.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
