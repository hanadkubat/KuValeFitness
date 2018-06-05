package com.hanadkubat.kuvalefitness.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hanadkubat.kuvalefitness.Database.Entities.Exercise;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;

import java.util.List;

/**
 * Created by vale on 6/5/2018.
 */

@Dao
public interface ExerciseDao
{
    @Query("SELECT * FROM exercise ORDER BY name")
    List<Exercise> getAll();

    @Query("SELECT * FROM exercise WHERE id = :id LIMIT 1")
    Exercise getById(int id);

    @Insert
    void insert(Exercise exercise);

    @Insert
    void insertAll(Exercise... exercises);

    @Delete
    void delete(Exercise exercise);
}
