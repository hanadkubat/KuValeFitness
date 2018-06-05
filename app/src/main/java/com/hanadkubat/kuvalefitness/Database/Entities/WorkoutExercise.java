package com.hanadkubat.kuvalefitness.Database.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by vale on 6/5/2018.
 */

@Entity(tableName = "workout_exercise", foreignKeys = {
        @ForeignKey(
                entity = Workout.class,
                parentColumns = "id",
                childColumns = "workout_id"),
        @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"
        )})
public class WorkoutExercise
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "exercise_id")
    private int exerciseId;

    @ColumnInfo(name = "workout_id")
    private int workoutId;

    private double weight;

    private int reps;

    private int sets;

    public WorkoutExercise(int exerciseId, int workoutId, int sets, int reps, double weight)
    {
        this.exerciseId = exerciseId;
        this.workoutId = workoutId;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getExerciseId()
    {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId)
    {
        this.exerciseId = exerciseId;
    }

    public int getWorkoutId()
    {
        return workoutId;
    }

    public void setWorkoutId(int workoutId)
    {
        this.workoutId = workoutId;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public int getReps()
    {
        return reps;
    }

    public void setReps(int reps)
    {
        this.reps = reps;
    }

    public int getSets()
    {
        return sets;
    }

    public void setSets(int sets)
    {
        this.sets = sets;
    }
}
