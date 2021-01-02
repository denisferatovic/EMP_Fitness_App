package com.example.fitnessemp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddExerciseFragment extends Fragment {

    View view;
    Button mButton;
    EditText mEdit;
    EditText set;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_add_exercise, container, false);
        mButton = view.findViewById(R.id.addExercise);
        mEdit   = view.findViewById(R.id.editTextTextMultiLine3);
        set   = view.findViewById(R.id.ime_seta);

        //listener za button click
        mButton.setOnClickListener(
                view -> addExercise());
        return view;
    }
    public void addExercise(){
        //get string from inputs when Add exercise is clicked
        String [] vaje = mEdit.getText().toString().split("\n");
        String workoutSet = set.getText().toString();

        //Info to console
        Log.i("ime_text",workoutSet);
        Log.i("EditText", vaje.toString());

        Workout workout = new Workout(workoutSet,vaje);

        //send data to DB
        //firebase zrihtat...
        //mDatabase = FirebaseDatabase.getInstance().getReference();

        //mDatabase.child("vaje").child(workout.ime()).setValue(workout);

        //give feedback... "Exercise added" ipd.


        //reset field or go to parent view


    }
    // data class
    public class Workout {
        public String ime_workout;
        public String [] vaje;
        public String [] opis_vaj; // ce se odlocmo dodati se opise k vajam
        public Workout(){

        }
        public Workout(String ime, String [] vaje){
            this.ime_workout = ime;
            this.vaje = vaje;

        }
        public String ime(){
            return ime_workout;
        }
        public String [] vaje(){
            return vaje;
        }

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
