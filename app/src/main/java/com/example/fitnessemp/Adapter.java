package com.example.fitnessemp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private HashMap<String, AddExerciseFragment.Workout> data;
    private Context ctx;

    Adapter(Context context, HashMap<String, AddExerciseFragment.Workout> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StringBuilder s = new StringBuilder();
        System.out.println("TEST123: " + position);
        int stevec = 0;
        for (Map.Entry<String, AddExerciseFragment.Workout> entry : this.data.entrySet()) {
            String e = entry.getKey();
            AddExerciseFragment.Workout w = entry.getValue();
            if (stevec == position) {
                holder.setVaj.setText("Exercise group: " + e);
                holder.open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //CardsFragment c = new CardsFragment();
                        System.out.println("Izbana vaja: " + e);
                        Fragment fragment = new EighthFragment(e);
                        FragmentManager fragmentManager = ((AppCompatActivity) ctx).getSupportFragmentManager();
                        ;
                        System.out.println("Fragment manager " + fragmentManager);
                        System.out.println("Fragment " + fragment);
                        if (fragmentManager != null) {
                            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
                        }
                    }
                });

                holder.finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishExercise(e);
                    }
                });
                w.izpisVaj();
                for (String vaje : w.vaje) {
                    s.append("Exercises: " + vaje + "\n");
                    holder.vaja.setText("Exercises: " + vaje + "\n");
                }
            }
            stevec++;
        }
        holder.vaja.setText(s);
        s = new StringBuilder();

        Picasso.get().load("https://api.time.com/wp-content/uploads/2019/09/getting-back-to-exercise-routine.jpg?quality=85&w=1012&h=569&crop=1").into(holder.slika);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView setVaj, vaja;
        ImageView slika;
        Button open, finish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slika = itemView.findViewById(R.id.imageView);
            setVaj = itemView.findViewById(R.id.setVaj);
            vaja = itemView.findViewById(R.id.vaja);
            open = itemView.findViewById(R.id.open);
            finish = itemView.findViewById(R.id.finish);
        }
    }

    public static void lol() {
        DatabaseReference Referenca = MainActivity.mDatabase.getDatabase().getReference(MainActivity.android_id);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.child(MainActivity.TodayDate).child("vaje").getChildren()) {
                    if (ds.getKey().equals("Unfinished")) {
                        for (DataSnapshot ds1 : dataSnapshot.child(MainActivity.TodayDate).child("vaje").child("Unfinished").getChildren()) {
                            String key = ds1.getKey();
                        }
                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        Referenca.addListenerForSingleValueEvent(eventListener);
    }


    public static void finishExercise(String key) {
        Log.d("Info", "Prsu v finishExercise");
        MainActivity.workouts.remove(key);
        System.out.println(key);
        MainActivity.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot save = null;

                if (dataSnapshot.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").exists()) {

                    Iterator<DataSnapshot> it = dataSnapshot.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").getChildren().iterator();
                    while (it.hasNext()) {
                        System.out.println(key);
                        DataSnapshot snap = it.next();
                        if (key != null) {
                            if (snap.hasChild(key))
                                save = snap;
                            System.out.println(snap.toString());
                            MainActivity.mDatabase.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Unfinished").child(key).removeValue();
                            MainActivity.mDatabase.child(MainActivity.android_id).child(MainActivity.TodayDate).child("vaje").child("Finished").child(key).setValue(snap.getValue());
                            MainActivity.FinishedWorkouts++;
                        }
                    }
                }
                Log.d("DataChange", "Value is: " + MainActivity.workouts);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });

    }

    public static void izpisiHashMapSetRep(HashMap<String,Integer> a) {
        for (Map.Entry<String, Integer> entry : a.entrySet()) {
            String set = entry.getKey();
            Integer w = entry.getValue();
            System.out.println(set + " " + w);
        }

    }
}
