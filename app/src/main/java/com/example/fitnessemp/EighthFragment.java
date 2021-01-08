package com.example.fitnessemp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static com.example.fitnessemp.MainActivity.android_id;
import static com.example.fitnessemp.MainActivity.mReference;


public class EighthFragment extends Fragment {
    private static Context ctx;
    private ArrayAdapter<String> adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String key;
    private Button back;
//    private Button finishBtn;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Button finishBtn;
    private View view;
    private int id;
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
        this.view =inflater.inflate(R.layout.fragment_eighth, container, false);
        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back = view.findViewById(R.id.backBtn);

        HashMap<String,AddExerciseFragment.Workout> workouts = ((MainActivity)getActivity()).workouts;
        LinearLayout myLayout = view.findViewById(R.id.items);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 20, 30, 20);

        id=1;
        for (Map.Entry<String, AddExerciseFragment.Workout> entry : workouts.entrySet()) {
            String workout_name = entry.getKey();
            AddExerciseFragment.Workout w  = entry.getValue();
            int stevec = 0;
            if(workout_name.equals(key)) {
                //adapter.add("Workout: " + workout_name);
                TextView test = new TextView(getContext());
                test.setTextSize(20);
                test.setText("Workout: " + key);
                myLayout.addView(test,layoutParams);
                for (int i = 0; i < w.vaje.size();i++) {
                    String vaja = w.vaje.get(i);
                    System.out.println("Key: " + workout_name + " value: " + vaja);
                    TextView e = new TextView(getContext());
                    e.setText("Exercise: " +  vaja);
                    e.setId(id++);
                    EditText s = new EditText(getContext());
                    s.setHint("Enter sets");
                    s.setId(id++);
                    s.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EditText r = new EditText(getContext());
                    r.setInputType(InputType.TYPE_CLASS_NUMBER);
                    r.setHint("Enter reps");
                    r.setId(id++);
                    myLayout.addView(e,layoutParams);
                    myLayout.addView(s,layoutParams);
                    myLayout.addView(r,layoutParams);
                }
            }
        }

        finishBtn = new Button(view.getContext());
        finishBtn.setId(id);
        finishBtn.setText("Finish");

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
            }
        });
        myLayout.addView(finishBtn,layoutParams);

        back.setOnClickListener(arg0 -> {
            Fragment fragment = new SecondFragment();
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();

        });


    }
    public void saveToDB(){
            // TO DO: TUKAJ SHRANIS V BAZO
            Log.d("Button","clicked");
            //mDatabase.child(android_id+"/dates").setValue(""); //reset all date data
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("yyyyMMDD");
            date = dateFormat.format(calendar.getTime());
            Log.d("Date",date);
            for(int i=1;i<id-1;i+=3) {
                TextView t = view.findViewById(i);
                TextView e1 = view.findViewById(i+1); //set
                TextView e2 = view.findViewById(i+2); //rep
                String vaja = t.getText().toString().replaceFirst("Exercise: ","");
                Log.d("Vaja",String.valueOf(i));

                if(e1.getText().equals("")||e2.getText().equals(""))
                    continue;
                mReference.child(android_id+"/dates/"+date+"/workout").child(vaja).setValue(e1.getText().toString()+" x "+e2.getText().toString());
            }
            //Toast.makeText(ctx,"Finished workout",Toast.LENGTH_LONG).show();
            //Log.d("Database",mDatabase.child(date).toString());

    }


}