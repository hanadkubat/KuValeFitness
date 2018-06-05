package com.hanadkubat.kuvalefitness.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.hanadkubat.kuvalefitness.Database.Entities.Exercise;
import com.hanadkubat.kuvalefitness.R;

import java.util.concurrent.Executors;

/**
 * Created by vale on 6/5/2018.
 */

public class Database
{
    private static KuValeDatabase database;

    public static KuValeDatabase getDatabase(Context context)
    {
        if(database == null)
            database = buildDatabase(context);

        return database;
    }

    private static KuValeDatabase buildDatabase(Context context)
    {
        database = Room.databaseBuilder(context.getApplicationContext(), KuValeDatabase.class, "kuvaledb")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        prepopulate(context);

        return database;
    }

    private static void prepopulate(Context context)
    {
        Exercise[] exercises = new Exercise[] {
                new Exercise(context.getString(R.string.exercise_bench)),
                new Exercise(context.getString(R.string.exercise_deadlift)),
                new Exercise(context.getString(R.string.exercise_overhead)),
                new Exercise(context.getString(R.string.exercise_rows)),
                new Exercise(context.getString(R.string.exercise_squat)),
                new Exercise(context.getString(R.string.exercise_tricep_extension))
        };

        for(Exercise exercise : exercises)
        {
            try
            {
                database.exerciseDao().insert(exercise);
            }
            catch (Exception e) { }
        }
    }
}
