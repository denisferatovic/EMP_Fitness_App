package com.example.fitnessemp;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MaxCalculator extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Button calculateBtn;
    private static EditText w;
    private static EditText r;
    private static TextView result;
    private static Context ctx;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MaxCalculator() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MaxCalculator newInstance(String param1, String param2) {
        MaxCalculator fragment = new MaxCalculator();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx = this.getContext();
        return inflater.inflate(R.layout.fragment_max_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calculateBtn = (Button) view.findViewById(R.id.setG);
        w = (EditText)view.findViewById(R.id.steps);
        r = (EditText)view.findViewById(R.id.wrkouts);
        result = (TextView) view.findViewById(R.id.output);
        System.out.println("clicked max");
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(w.getText().toString().length() > 0 && r.getText().toString().length() > 0) {
                    double weight = Double.parseDouble(w.getText().toString());
                    double reps = Double.parseDouble(r.getText().toString());
                    double r = Math.round(weight * Math.pow(reps,0.10));
                    result.setText(String.valueOf(r) + " kg");
                }
                else {
                    Toast.makeText(ctx,"Insert reps and weight!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}