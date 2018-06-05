package com.hanadkubat.kuvalefitness.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hanadkubat.kuvalefitness.Database.Entities.Exercise;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;
import com.hanadkubat.kuvalefitness.Database.Entities.WorkoutExercise;

import java.util.List;

/**
 * Created by vale on 6/5/2018.
 */

@Dao
public interface WorkoutExerciseDao
{
    @Query("SELECT * FROM workout_exercise")
    List<WorkoutExercise> getAll();

    @Query("SELECT * FROM workout_exercise WHERE workout_id = :workoutId")
    List<WorkoutExercise> getByWorkoutId(int workoutId);

    @Insert
    void insert(WorkoutExercise workoutExercise);

    @Delete
    void delete(WorkoutExercise workoutExercise);

    @Update
    void update(WorkoutExercise workoutExercise);
}
