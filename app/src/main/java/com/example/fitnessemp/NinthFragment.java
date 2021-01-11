package com.example.fitnessemp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static com.example.fitnessemp.MyLocationListener.latitude;


public class NinthFragment extends Fragment {
    private static Button setGoals;
    private static EditText steps,workouts;
    private int DailySteps,DailyWorkouts;

    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        Context ctx = this.getContext();


        view = inflater.inflate(R.layout.fragment_ninth, container, false);
        setGoals = view.findViewById(R.id.setG);
        steps = view.findViewById(R.id.steps);
        workouts = view.findViewById(R.id.wrkouts);

        setGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(steps.getText().toString().length() > 0 && workouts.getText().toString().length() > 0) {
                    DailySteps = Integer.parseInt(steps.getText().toString());
                    DailyWorkouts = Integer.parseInt(workouts.getText().toString());
                    Toast.makeText(ctx,"Daily goals set!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ctx,"Insert daily steps and workouts!",Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.DailySteps != 0) {
            steps.setText(String.valueOf(MainActivity.DailySteps));
            this.DailySteps = MainActivity.DailySteps;
        }
        if(MainActivity.DailyWorkouts != 0) {
            workouts.setText(String.valueOf(MainActivity.DailyWorkouts));
            this.DailyWorkouts = MainActivity.DailyWorkouts;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.DailySteps = this.DailySteps;
        MainActivity.DailyWorkouts = this.DailyWorkouts;
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.DailySteps = this.DailySteps;
        MainActivity.DailyWorkouts = this.DailyWorkouts;
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