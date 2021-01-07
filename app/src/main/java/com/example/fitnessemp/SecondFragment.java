package com.example.fitnessemp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.fitnessemp.AddExerciseFragment.Workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SecondFragment extends Fragment {

    View view;
    Button exercise;
    Button start;
    String key;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_second, container, false);
        exercise = view.findViewById(R.id.addExercise2);
        exercise.setOnClickListener(view -> goToAddExercise());

        Spinner s = (Spinner) view.findViewById(R.id.spinner);
        ArrayList<String> arraySpinner = new ArrayList<String>();
        for(HashMap.Entry<String, AddExerciseFragment.Workout> entry : MainActivity.workouts.entrySet()){
            arraySpinner.add(entry.getValue().ime());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Dropdown",parent.getItemAtPosition(position).toString());
                key=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        start = view.findViewById(R.id.StartExercise);
        start.setOnClickListener(view -> goToExercise());

        return view;

    }

    // called when add exercise button is clicked
    public void goToAddExercise(){
        Log.d("Info", "Prsu v goToAddExercise");
        Fragment fragment = new AddExerciseFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
    }

    public void goToExercise(){
        Log.d("Info", "Prsu v goToExercise");

        Fragment fragment = new EighthFragment(key);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
