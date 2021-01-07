package com.example.fitnessemp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EighthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EighthFragment extends Fragment {
    private static Context ctx;
    private ArrayAdapter<String> adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String key;
    private Button back;
//    private Button finishBtn;



    public EighthFragment(String key) {
        // Required empty public constructor
        this.key = key;
    }

//    public static EighthFragment newInstance(String param1,String param2) {
//        EighthFragment fragment = new EighthFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eighth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back = view.findViewById(R.id.backBtn);

        HashMap<String,AddExerciseFragment.Workout> workouts = ((MainActivity)getActivity()).workouts;
        LinearLayout myLayout = view.findViewById(R.id.items);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 20, 30, 20);


        for (Map.Entry<String, AddExerciseFragment.Workout> entry : workouts.entrySet()) {
            String workout_name = entry.getKey();
            AddExerciseFragment.Workout w  = entry.getValue();
            int stevec = 0;
            if(workout_name.equals(key)) {
                //adapter.add("Workout: " + workout_name);
                TextView test = new TextView(getActivity());
                test.setTextSize(20);
                test.setText("Workout: " + key);
                myLayout.addView(test,layoutParams);
                for (int i = 0; i < w.vaje.size();i++) {
                    String vaja = w.vaje.get(i);
                    System.out.println("Key: " + workout_name + " value: " + vaja);
                    TextView e = new TextView(getActivity());
                    e.setText("Exercise: " +  vaja);
                    EditText s = new EditText(getActivity());
                    s.setHint("Enter sets");
                    s.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EditText r = new EditText(getActivity());
                    r.setInputType(InputType.TYPE_CLASS_NUMBER);
                    r.setHint("Enter reps");
                    myLayout.addView(e,layoutParams);
                    myLayout.addView(s,layoutParams);
                    myLayout.addView(r,layoutParams);
                }
            }
        }

        Button finishBtn = new Button(getActivity());
        finishBtn.setText("Finish");
        myLayout.addView(finishBtn,layoutParams);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TO DO: TUKAJ SHRANIS V BAZO


                //Toast.makeText(ctx,"Finished workout",Toast.LENGTH_LONG).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment fragment = new SecondFragment();
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();

            }
        });


    }



}