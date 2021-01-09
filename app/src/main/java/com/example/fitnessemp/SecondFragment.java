package com.example.fitnessemp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SecondFragment extends Fragment {

    View view;
    Button exercise;
    Button start,finish;
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
        finish = view.findViewById(R.id.finishExercise);
        finish.setOnClickListener(view -> finishExercise());

        Spinner s1 = (Spinner) view.findViewById(R.id.spinner2);
        ArrayList<String> arraySpinner2 = new ArrayList<String>();
        for(HashMap.Entry<String, AddExerciseFragment.Workout> entry : MainActivity.workouts.entrySet()){
            arraySpinner2.add(entry.getValue().ime());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, arraySpinner2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter2);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    public void finishExercise(){
        Log.d("Info", "Prsu v finishExercise");
        MainActivity.workouts.remove(key);
        System.out.println(key);
        MainActivity.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot save = null;

                if(dataSnapshot.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").exists()) {

                    Iterator<DataSnapshot> it = dataSnapshot.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").getChildren().iterator();
                    while(it.hasNext()){
                        DataSnapshot snap = it.next();
                        if(snap.hasChild(key))
                             save = snap;
                             System.out.println(snap.toString());
                             MainActivity.mDatabase.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").child(key).removeValue();
                             MainActivity.mDatabase.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Finished").child(key).setValue(snap.getValue());
                    }
                }
                Log.d("DataChange", "Value is: " + MainActivity.workouts);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });

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
