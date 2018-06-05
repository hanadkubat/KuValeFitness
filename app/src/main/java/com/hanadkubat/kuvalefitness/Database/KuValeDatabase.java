package com.hanadkubat.kuvalefitness.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.hanadkubat.kuvalefitness.Database.Daos.ExerciseDao;
import com.hanadkubat.kuvalefitness.Database.Daos.WorkoutDao;
import com.hanadkubat.kuvalefitness.Database.Daos.WorkoutExerciseDao;
import com.hanadkubat.kuvalefitness.Database.Entities.Exercise;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;
import com.hanadkubat.kuvalefitness.Database.Entities.WorkoutExercise;

/**
 * Created by vale on 6/5/2018.
 */

@Database(entities = {Workout.class, Exercise.class, WorkoutExercise.class}, version = 5)
public abstract class KuValeDatabase extends RoomDatabase
{
    public abstract WorkoutDao workoutDao();
    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutExerciseDao workoutExerciseDao();
}
