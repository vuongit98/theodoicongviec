package com.theodoilamviec.theodoilamviec.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.theodoilamviec.theodoilamviec.models.Category;
import com.theodoilamviec.theodoilamviec.models.Note;
import com.theodoilamviec.theodoilamviec.models.Notification;
import com.theodoilamviec.theodoilamviec.models.TrashNote;

@Database(
        entities = {
                Note.class,
                Category.class,
                Notification.class,
                TrashNote.class,

        },
        version = 1,
        exportSchema = false)
public abstract class APP_DATABASE extends RoomDatabase {

    public abstract DAO dao();

    private static APP_DATABASE appDatabase;

    public static synchronized APP_DATABASE requestDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, APP_DATABASE.class, "ghichu")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }

    public static void destroyDatabase() {
        appDatabase = null;
    }
}
