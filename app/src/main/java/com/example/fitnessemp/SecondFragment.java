package com.example.fitnessemp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondFragment extends Fragment {

    View view;
    Button exercise;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_second, container, false);
        exercise = view.findViewById(R.id.addExercise2);
        exercise.setOnClickListener(
                view -> goToAddExercise());
        return view;
    }
    // called when add exercise button is clicked
    public void goToAddExercise(){
        Log.i("Info", "Prsu v goToAddExercise");
        Fragment fragment = new AddExerciseFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
    }

    public void WriteToFirebase(String data) {
        final Long[] vrednost = {Long.valueOf(0)};
        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()};
        DatabaseReference myRef = database[0].getReference("miska").child("Statistika");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.getKey().equals("Desni klik")) {
                        vrednost[0] = (Long) singleSnapshot.getValue();
                        myRef.child("Desni klik").setValue(vrednost[0] + 1);
                    }


                }
                myRef.removeEventListener(this);

            }


            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
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
