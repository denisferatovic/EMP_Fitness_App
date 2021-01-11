package com.example.fitnessemp;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.fitnessemp.MainActivity.TodayDate;
import static com.example.fitnessemp.MainActivity.mDatabase;
import static com.example.fitnessemp.MainActivity.workoutsOld;

public class MainFragment extends Fragment {

    TextView activeWorkoutContainer;
    Button addExercise;
    FragmentManager fragmentManager = MainActivity.fragmentManager;
    FragmentTransaction fragmentTransaction = MainActivity.fragmentTransaction;
    ProgressBar stepProgress,workoutProgress;
    private int DailySteps, DailyWorkouts;
    private int currentSteps,currentWorkouts;
    Context ctx;
    String key;
    int i = 0;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_main, container, false);
        DailySteps = MainActivity.DailySteps;
        DailyWorkouts = MainActivity.DailyWorkouts;
        currentSteps = MainActivity.steps;
        currentWorkouts = MainActivity.FinishedWorkouts;
        ctx = this.getContext();

        //Daily progress circles
        ProgressCircle circleProg1 = (ProgressCircle) view.findViewById(R.id.workoutBar);
        ProgressCircle circleProg2 = (ProgressCircle) view.findViewById(R.id.stepBar);
        /*
        System.out.println(DailySteps);
        System.out.println(DailyWorkouts);
        System.out.println(currentSteps);
        System.out.println(currentWorkouts);
         */

        if(DailyWorkouts != 0) {
            circleProg1.setMax(DailyWorkouts);
        }else{
            circleProg1.setMax(100);
        }
        circleProg1.setProgress((int) circleProg1.getProgress());

        if(DailyWorkouts != 0) {
            circleProg2.setMax(DailySteps);
        }else{
            circleProg1.setMax(100);
        }
        circleProg2.setProgress((int) circleProg2.getProgress());

        if(DailyWorkouts == 0) {
            circleProg1.setProgressWithAnimation(0);
        }else{
            circleProg1.setProgressWithAnimation(currentWorkouts);
        }

        if(DailySteps == 0) {
            circleProg2.setProgressWithAnimation(0);
        }else{
            circleProg2.setProgressWithAnimation(currentSteps);
        }

        //mDatabase.child(MainActivity.android_id).child("vaje").setValue(""); // reset db entries
        MainActivity.mDatabase.child("LOGEntries").child("LoggedIn").child(String.valueOf(MainActivity.Year)).child(String.valueOf(MainActivity.Month)).child(String.valueOf(MainActivity.Day)).setValue("Logged");


        /*
        MainActivity.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int i  = 0;
                if(dataSnapshot.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").exists()) {

                    Iterator<DataSnapshot> it = dataSnapshot.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").getChildren().iterator();
                    while(it.hasNext()){
                        DataSnapshot snap = it.next();

                        Log.d("Snap", snap.getValue().toString());

                        //MainActivity.workouts.put(snap.getKey(),new AddExerciseFragment.Workout(snap.getKey(), (ArrayList) snap.getValue()));
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
*/

        activeWorkoutContainer = view.findViewById(R.id.activeWorkoutsContainer);
        String novtext="";

        if(!MainActivity.workouts.isEmpty()) {

            for(HashMap.Entry<String, AddExerciseFragment.Workout> entry : MainActivity.workouts.entrySet()){
                novtext = novtext.concat(entry.getValue().izpisWorkout());
            }
            Log.d("Concat",novtext);
            activeWorkoutContainer.setText(novtext);
        }

        addExercise = view.findViewById(R.id.addExercise);
        activeWorkoutContainer.setMovementMethod(new ScrollingMovementMethod());
        while (activeWorkoutContainer.canScrollVertically(1)) {
            activeWorkoutContainer.scrollBy(0, 10);
        }
        setRetainInstance(true);

        /*
        // Reload current fragment
        Fragment fragment = null;
        fragment = getFragmentManager().findFragmentByTag("MainFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(fragment!= null) {
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        }
         */

        Spinner s1 = (Spinner) view.findViewById(R.id.spinner3);
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

        addExercise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(key != null) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if (snapshot.child(MainActivity.android_id).child(TodayDate).child("vaje").child("Unfinished").child(key).exists()) {
                                if (workoutsOld.size() > 0) {
                                    if(MainActivity.workoutsOld.get(key) != null) {
                                        for (String a : MainActivity.workoutsOld.get(key).vaje()) {
                                            mDatabase.child(MainActivity.android_id).child(TodayDate).child("vaje").child("Unfinished").child(key + key).child(a).setValue("");
                                        }
                                    }
                                }
                            } else {
                                if (workoutsOld.size() > 0) {
                                    if(MainActivity.workoutsOld.get(key) != null) {
                                        for (String a : MainActivity.workoutsOld.get(key).vaje()) {
                                            mDatabase.child(MainActivity.android_id).child(TodayDate).child("vaje").child("Unfinished").child(key).child(a).setValue("");
                                        }
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        DailyWorkouts = MainActivity.DailyWorkouts;
        DailySteps = MainActivity.DailySteps;
        currentSteps = MainActivity.steps;
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
