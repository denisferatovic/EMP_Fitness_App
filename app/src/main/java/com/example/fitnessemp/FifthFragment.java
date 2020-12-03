package com.example.fitnessemp;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class FifthFragment extends Fragment {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;
    Button start,stop,reset,lap;
    String lapText = "";
    String currentTime = "";
    int lapNum = 0;
    TextView lapContainer, timeView;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_fifth, container, false);

        lapContainer = view.findViewById(R.id.textView8);
        lapContainer.setMovementMethod(new ScrollingMovementMethod());
        timeView = view.findViewById(R.id.textView7);
        start = view.findViewById(R.id.addExercise3);
        stop = view.findViewById(R.id.addExercise4);
        reset = view.findViewById(R.id.addExercise5);
        lap = view.findViewById(R.id.addExercise6);
        setRetainInstance(true);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickStart();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickStop();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickReset();
            }
        });
        lap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickLap();
            }
        });

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        runTimer();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            savedInstanceState.putInt("seconds", seconds);
            savedInstanceState.putBoolean("running", running);
            savedInstanceState.putBoolean("wasRunning", wasRunning);        }
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    @Override
    public void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    public void onClickStart() {
        running = true;
    }

    public void onClickStop() {
        running = false;
    }
    public void onClickReset(){
        running = false;
        seconds = 0;
        lapContainer.setText("");
        lapNum = 0;
    }
    public void onClickLap(){
        lapNum++;
        String append = lapText + "Lap " + lapNum +": "+ currentTime +"\n";
        lapText += "Lap " + lapNum +": "+ currentTime +"\n";
        lapContainer.setText(append);
    }

    private void runTimer(){

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run(){
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                currentTime = time;

                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
}


