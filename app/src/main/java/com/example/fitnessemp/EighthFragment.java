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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EighthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EighthFragment extends Fragment {
    private static Button calculateBtn;
    private static EditText w;
    private static EditText r;
    private static TextView result;
    private static Context ctx;

    public EighthFragment() {
        // Required empty public constructor
    }

    public static EighthFragment newInstance(String param1, String param2) {
        EighthFragment fragment = new EighthFragment();


        return fragment;
    }

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
        calculateBtn = (Button) view.findViewById(R.id.setG);
        w = (EditText)view.findViewById(R.id.steps);
        r = (EditText)view.findViewById(R.id.wrkouts);
        result = (TextView) view.findViewById(R.id.output);
        ctx = this.getContext();

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(w.getText().toString().length() > 0 && r.getText().toString().length() > 0) {
                    double weight = Double.parseDouble(w.getText().toString());
                    double reps = Double.parseDouble(r.getText().toString());
                    double r = Math.round(weight * Math.pow(reps,0.10));
                    result.setText(String.valueOf(r) + " kg");
                }else{
                    Toast.makeText(ctx,"Insert weight and reps!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}