package com.hanadkubat.kuvalefitness.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hanadkubat.kuvalefitness.Database.Entities.Exercise;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;

import java.util.List;

/**
 * Created by vale on 6/5/2018.
 */

@Dao
public interface WorkoutDao
{
    @Query("SELECT * FROM workout ORDER BY time DESC")
    List<Workout> getAll();

    @Update
    void update(Workout workout);

    @Insert
    void insert(Workout workout);

    @Delete
    void delete(Workout workout);

}
