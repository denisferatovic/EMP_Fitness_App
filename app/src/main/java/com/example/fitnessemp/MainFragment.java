package com.example.fitnessemp;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.fitnessemp.MainActivity.DailyWorkouts;
import static com.example.fitnessemp.MainActivity.workouts;

public class MainFragment extends Fragment {

    TextView activeWorkoutContainer;
    Button addExercise;
    FragmentManager fragmentManager = MainActivity.fragmentManager;
    FragmentTransaction fragmentTransaction = MainActivity.fragmentTransaction;
    ProgressBar stepProgress,workoutProgress;
    private int DailySteps, DailyWorkouts;
    private int currentSteps,currrentWorkouts;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup)view.getParent() != null)
                ((ViewGroup)view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_main, container, false);
        DailySteps = MainActivity.DailySteps;
        DailyWorkouts = MainActivity.DailyWorkouts;
        currentSteps = MainActivity.steps;
        currentSteps = MainActivity.ActiveWorkouts;
        stepProgress = (ProgressBar) view.findViewById(R.id.stepProgress);
        workoutProgress = (ProgressBar) view.findViewById(R.id.workoutProgress);
        stepProgress.setIndeterminate(false);
        workoutProgress.setIndeterminate(false);
        stepProgress.setMax(DailySteps);
        workoutProgress.setMax(DailyWorkouts);

        new Thread(new Runnable() {
            public void run() {
                while (stepProgress.getProgress() < stepProgress.getMax()) {
                    // Update the progress bar and display the
                    //current value in the text view
                    stepProgress.setProgress(currentSteps);
                    workoutProgress.setProgress(currrentWorkouts);
                }
            }
        }).start();

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
        setRetainInstance(true);
        addExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new SecondFragment());
                fragmentTransaction.commit();

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
