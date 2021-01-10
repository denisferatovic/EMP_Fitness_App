package com.example.fitnessemp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import static com.example.fitnessemp.MainActivity.mDatabase;

public class AddExerciseFragment extends Fragment {

    View view;
    Button mButton, backSecond;
    EditText mEdit;
    EditText set;
    String TodayDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        backSecond = view.findViewById(R.id.backSecond);

        mEdit   = view.findViewById(R.id.editTextTextMultiLine3);
        set   = view.findViewById(R.id.ime_seta);

        //current date
        TodayDate = MainActivity.TodayDate;

        //listener za button click
        mButton.setOnClickListener(view -> addExercise());
        backSecond.setOnClickListener(view -> goToSecond());

        return view;
    }


    public void addExercise(){
        //get string from inputs when Add exercise is clicked
        Scanner sc= new Scanner(mEdit.getText().toString());
        ArrayList<String> vaje = new ArrayList<>();
        String vaja;
        while(sc.hasNextLine()){
            vaja = sc.nextLine();
            if(!vaja.equals("")&&!vaja.equals(" "))
                vaje.add(vaja);
        }

        String workoutSet = set.getText().toString();
        //Info to console
        Log.d("ime_text",workoutSet);
        Log.d("EditText", vaje.toString());

        Workout workout = new Workout(workoutSet,vaje);

        if(workout.isComplete()) {
            String name = workout.ime();

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child(MainActivity.android_id).child(TodayDate).child("vaje").child("Unfinished").child(name).exists()) {
                        for(String a : workout.vaje) {
                            mDatabase.child(MainActivity.android_id).child(TodayDate).child("vaje").child("Unfinished").child(name+name).child(a).setValue("");
                        }
                    }else{
                        for(String a : workout.vaje) {
                            mDatabase.child(MainActivity.android_id).child(TodayDate).child("vaje").child("Unfinished").child(workout.ime()).child(a).setValue("");
                        }
                    }

                    if(snapshot.child(MainActivity.android_id).child("vaje").child(name).exists()){
                        for(String a : workout.vaje) {
                            mDatabase.child(MainActivity.android_id).child("vaje").child(name+name).child(a).setValue("");
                        }
                    }else {
                        for (String a : workout.vaje) {
                            mDatabase.child(MainActivity.android_id).child("vaje").child(workout.ime()).child(a).setValue("");
                        }
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Toast.makeText(this.getContext(), "Exercise added to database!  ", Toast.LENGTH_LONG).show();


        }
        else {
            Log.d("Incomplete", "Attempted to add incomplete data to firebase");
            Toast.makeText(this.getContext(), "Incomplete data, fill out the whole form", Toast.LENGTH_LONG).show();
        }

        //go to parent fragment
        goToSecond();

        }

    // data class
    public static class Workout {
        public String ime_workout;
        ArrayList<String> vaje = new ArrayList<String>();
        HashMap<String,Integer[]> vaja_sets_reps = new HashMap<>();
        public Workout(){

        }
        public Workout(String ime, ArrayList vaje){
            this.ime_workout = ime;
            this.vaje = vaje;
        }
        public Workout(String ime, ArrayList vaje, HashMap vaje_sets_reps){
            this.ime_workout = ime;
            this.vaje = vaje;
            this.vaja_sets_reps = vaje_sets_reps;
        }
        public Integer [] getSpecificSetsReps(String key){
            if(vaja_sets_reps.get(key) != null) {
                return vaja_sets_reps.get(key);
            }
            return new Integer[] {};
        }
        public HashMap<String,Integer[]> getSetReps(){
            return this.vaja_sets_reps;
        }
        public String ime(){
            return ime_workout;
        }
        public ArrayList<String> vaje(){
            return vaje;
        }
        public boolean isComplete(){
            return !vaje.isEmpty() && !ime_workout.equals("");
        }
        @Override
        public String toString(){
            return ime_workout+" => " + izpisVaj() + " ";
        }
        public String izpisVaj(){
            Iterator<String> it = this.vaje.iterator();
            String text="";
            while(it.hasNext()){
                String next = it.next();
                text = text.concat(next+" ");
                if(vaja_sets_reps.get(next) != null){
                    int a = vaja_sets_reps.get(next)[0];
                    int b = vaja_sets_reps.get(next)[1];
                    text = text.concat("->["+a+" "+b+"] ");
                }
            }
            return text;
        }
        public String izpisWorkout(){
            return ime_workout  + " : "  + izpisVaj() + "\n";
        }


    }

    public void goToSecond(){
        Log.i("Info", "Prsu v goToAddExercise");
        Fragment fragment = new SecondFragment();
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
