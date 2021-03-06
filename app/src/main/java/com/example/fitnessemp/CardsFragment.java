package com.example.fitnessemp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import java.util.HashMap;

public class CardsFragment extends Fragment {

    RecyclerView recyclerView;
    Adapter adapter;
    HashMap<String,AddExerciseFragment.Workout> w;
    private Button addExercise;

    public CardsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CardsFragment newInstance(String param1, String param2) {
        CardsFragment fragment = new CardsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =  view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //System.out.println("Ola : " + MainActivity.workouts);

        adapter = new Adapter(getActivity(),MainActivity.workouts);
        recyclerView.setAdapter(adapter);

        /* // Reload current fragment
        Fragment fragment = null;
        fragment = getFragmentManager().findFragmentByTag("CardsFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(fragment!= null) {
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        }
         */

        addExercise = (Button)view.findViewById(R.id.addExercise);
        setMargins(addExercise, 10, 10, 10, 200);
        addExercise.bringToFront();
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddExerciseFragment();
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
            }
        });
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof RelativeLayout.MarginLayoutParams) {
            RelativeLayout.MarginLayoutParams p = (RelativeLayout.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}