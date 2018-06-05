package com.hanadkubat.kuvalefitness.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hanadkubat.kuvalefitness.Database.Database;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;
import com.hanadkubat.kuvalefitness.MainActivity;
import com.hanadkubat.kuvalefitness.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by vale on 6/5/2018.
 */

public class WorkoutLoggerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private ListView lstWorkouts;
    private FloatingActionButton fabAdd;
    private ArrayAdapter<Workout> listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_workout_log, null);

        lstWorkouts = view.findViewById(R.id.listView);
        fabAdd = view.findViewById(R.id.fabAdd);

        listAdapter = createAdapter();
        lstWorkouts.setAdapter(listAdapter);
        lstWorkouts.setOnItemClickListener(this);
        lstWorkouts.setOnItemLongClickListener(this);

        fabAdd.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle(R.string.title_workout_logger_fragment);
        reloadData();
    }

    private void reloadData()
    {
        List<Workout> workoutList = Database.getDatabase(getActivity()).workoutDao().getAll();

        listAdapter.clear();
        listAdapter.addAll(workoutList);
        listAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<Workout> createAdapter()
    {
        return new ArrayAdapter<Workout>(getActivity(), 0)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                if(convertView == null)
                    convertView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_2, null);

                TextView txtTitle = convertView.findViewById(android.R.id.text1);
                TextView txtSubtitle = convertView.findViewById(android.R.id.text2);

                Workout workout = getItem(position);

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd.MM.yyyy");
                txtTitle.setText(dateFormat.format(workout.getDate()));
                if(workout.getName() == null || workout.getName().trim().isEmpty())
                    txtSubtitle.setText(null);
                else
                    txtSubtitle.setText(workout.getName());

                return convertView;
            }
        };
    }

    public void createNewWorkout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_add_workout);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edittext, null);
        final EditText txtWorkoutName = dialogView.findViewById(R.id.editText);
        txtWorkoutName.setInputType(InputType.TYPE_CLASS_TEXT);
        txtWorkoutName.setHint(R.string.hint_workout_name);
        builder.setView(dialogView);

        builder.setNeutralButton(R.string.button_cancel, null);
        builder.setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Workout newWorkout = new Workout(txtWorkoutName.getText().toString().trim(), System.currentTimeMillis());
                Database.getDatabase(getActivity()).workoutDao().insert(newWorkout);
                reloadData();
            }
        });

        builder.show();
    }

    private void renameWorkout(final Workout workout)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_rename_workout);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edittext, null);
        final EditText txtWorkoutName = view.findViewById(R.id.editText);
        txtWorkoutName.setText(workout.getName());
        txtWorkoutName.setInputType(InputType.TYPE_CLASS_TEXT);
        txtWorkoutName.setHint(R.string.hint_workout_name);
        builder.setView(txtWorkoutName);

        builder.setNeutralButton(R.string.button_cancel, null);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                workout.setName(txtWorkoutName.getText().toString().trim());
                Database.getDatabase(getActivity()).workoutDao().update(workout);
                listAdapter.notifyDataSetChanged();
            }
        });

        builder.show();
    }

    private void deleteWorkout(final Workout workout)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_delete_workout);
        builder.setMessage(R.string.message_delete_workout);

        builder.setNeutralButton(R.string.button_cancel, null);
        builder.setNegativeButton(R.string.button_delete, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Database.getDatabase(getActivity()).workoutDao().delete(workout);
                listAdapter.remove(workout);
                listAdapter.notifyDataSetChanged();
            }
        });

        builder.show();
    }

    public void showOptions(final Workout workout)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = new String[] {
                getString(R.string.title_rename_workout),
                getString(R.string.title_delete_workout)
        };
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(i == 0)
                    renameWorkout(workout);
                else if(i == 1)
                    deleteWorkout(workout);
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
            createNewWorkout();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Activity activity = getActivity();
        if(activity instanceof MainActivity)
        {
            Workout workout = listAdapter.getItem(i);
            ((MainActivity) activity).showEditWorkoutFragment(workout);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Workout workout = listAdapter.getItem(i);
        showOptions(workout);

        return true;
    }
}
