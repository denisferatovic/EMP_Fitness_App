package com.example.fitnessemp;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

public class MainFragment extends Fragment {

    TextView activeWorkoutContainer;
    Button addExercise;
    FragmentManager fragmentManager = MainActivity.fragmentManager;
    FragmentTransaction fragmentTransaction = MainActivity.fragmentTransaction;

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


        activeWorkoutContainer = view.findViewById(R.id.activeWorkoutsContainer);
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
