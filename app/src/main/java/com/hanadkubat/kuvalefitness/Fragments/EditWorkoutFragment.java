package com.hanadkubat.kuvalefitness.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hanadkubat.kuvalefitness.Database.Database;
import com.hanadkubat.kuvalefitness.Database.Entities.Exercise;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;
import com.hanadkubat.kuvalefitness.Database.Entities.WorkoutExercise;
import com.hanadkubat.kuvalefitness.Database.KuValeDatabase;
import com.hanadkubat.kuvalefitness.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vale on 6/5/2018.
 */

public class EditWorkoutFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener
{
    private ListView lstWorkouts;
    private FloatingActionButton fabAdd;
    private ArrayAdapter<WorkoutExercise> listAdapter;

    private Workout workout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_workout_log, null);

        lstWorkouts = view.findViewById(R.id.listView);
        fabAdd = view.findViewById(R.id.fabAdd);

        listAdapter = createAdapter();
        lstWorkouts.setAdapter(listAdapter);
        lstWorkouts.setOnItemLongClickListener(this);

        fabAdd.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle(R.string.title_edit_workout_log);
        reloadData();
    }

    private void reloadData()
    {
        KuValeDatabase database = Database.getDatabase(getActivity());
        List<WorkoutExercise> workoutExercises = database.workoutExerciseDao().getByWorkoutId(workout.getId());

        listAdapter.clear();
        listAdapter.addAll(workoutExercises);
        listAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<WorkoutExercise> createAdapter()
    {
        return new ArrayAdapter<WorkoutExercise>(getActivity(), 0)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                if(convertView == null)
                    convertView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_2, null);

                TextView txtTitle = convertView.findViewById(android.R.id.text1);
                TextView txtSubtitle = convertView.findViewById(android.R.id.text2);

                WorkoutExercise workoutExercise = getItem(position);

                KuValeDatabase database = Database.getDatabase(getContext());
                Exercise exercise = database.exerciseDao().getById(workoutExercise.getExerciseId());
                txtTitle.setText(exercise.getName());

                String subtitle = getString(R.string.label_exercise_details);
                subtitle = String.format(subtitle, workoutExercise.getSets(), workoutExercise.getReps(), workoutExercise.getWeight());
                txtSubtitle.setText(subtitle);

                return convertView;
            }
        };
    }

    private void addExercise()
    {
        KuValeDatabase database = Database.getDatabase(getActivity());
        final List<Exercise> exercises = database.exerciseDao().getAll();

        List<String> exerciseNames = new ArrayList<>(exercises.size());
        for(Exercise exercise : exercises)
            exerciseNames.add(exercise.getName());


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_select_exercise);
        builder.setItems(exerciseNames.toArray(new String[] { }), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Exercise exercise = exercises.get(i);
                addExercise(exercise);
            }
        });
        builder.setNeutralButton(R.string.button_cancel, null);
        builder.show();
    }

    private void addExercise(final Exercise exercise)
    {
        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_exercise, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle(R.string.title_exercise_details);
        builder.setNeutralButton(R.string.button_cancel, null);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                EditText etSets = dialogView.findViewById(R.id.etSets);
                EditText etReps = dialogView.findViewById(R.id.etReps);
                EditText etWeight = dialogView.findViewById(R.id.etWeight);

                int sets;
                int reps;
                double weight;

                try
                {
                    sets = Integer.parseInt(etSets.getText().toString());
                }
                catch (Exception e)
                {
                    sets = 0;
                }

                try
                {
                    reps = Integer.parseInt(etReps.getText().toString());
                }
                catch (Exception e)
                {
                    reps = 0;
                }

                try
                {
                    weight = Double.parseDouble(etWeight.getText().toString());
                }
                catch (Exception e)
                {
                    weight = 0.0;
                }

                KuValeDatabase database = Database.getDatabase(getContext());
                WorkoutExercise workoutExercise = new WorkoutExercise(exercise.getId(), workout.getId(), sets, reps, weight);
                database.workoutExerciseDao().insert(workoutExercise);

                reloadData();
            }
        });
        builder.show();
    }

    private void editExercise(final WorkoutExercise workoutExercise)
    {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_exercise, null);
        final EditText etSets = dialogView.findViewById(R.id.etSets);
        final EditText etReps = dialogView.findViewById(R.id.etReps);
        final EditText etWeight = dialogView.findViewById(R.id.etWeight);

        etSets.setText(String.valueOf(workoutExercise.getSets()));
        etReps.setText(String.valueOf(workoutExercise.getReps()));
        etWeight.setText(String.valueOf(workoutExercise.getWeight()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle(R.string.title_edit_exercise_details);
        builder.setNeutralButton(R.string.button_cancel, null);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                int sets;
                int reps;
                double weight;

                try
                {
                    sets = Integer.parseInt(etSets.getText().toString());
                }
                catch (Exception e)
                {
                    sets = 0;
                }

                try
                {
                    reps = Integer.parseInt(etReps.getText().toString());
                }
                catch (Exception e)
                {
                    reps = 0;
                }

                try
                {
                    weight = Double.parseDouble(etWeight.getText().toString());
                }
                catch (Exception e)
                {
                    weight = 0.0;
                }

                KuValeDatabase database = Database.getDatabase(getContext());
                workoutExercise.setReps(reps);
                workoutExercise.setSets(sets);
                workoutExercise.setWeight(weight);
                database.workoutExerciseDao().update(workoutExercise);

                listAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void deleteExercise(final WorkoutExercise workoutExercise)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_delete_exercise);
        builder.setMessage(R.string.message_delete_exercise);

        builder.setNeutralButton(R.string.button_cancel, null);
        builder.setNegativeButton(R.string.button_delete, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Database.getDatabase(getActivity()).workoutExerciseDao().delete(workoutExercise);
                listAdapter.remove(workoutExercise);
                listAdapter.notifyDataSetChanged();
            }
        });

        builder.show();
    }

    public void setWorkout(Workout workout)
    {
        this.workout = workout;
    }

    private void showOptions(final WorkoutExercise workoutExercise)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = new String[] {
                getString(R.string.title_edit_exercise),
                getString(R.string.title_delete_exercise)
        };
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(i == 0)
                    editExercise(workoutExercise);
                else if(i == 1)
                    deleteExercise(workoutExercise);
            }
        });
        builder.setNeutralButton(R.string.button_cancel, null);
        builder.show();
    }

    @Override
    public void onClick(View view)
    {
        if(view == fabAdd)
        {
            addExercise();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        WorkoutExercise workoutExercise = listAdapter.getItem(i);
        showOptions(workoutExercise);
        return true;
    }
}
