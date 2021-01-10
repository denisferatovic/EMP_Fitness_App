package com.example.fitnessemp;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private HashMap<String,AddExerciseFragment.Workout> data;
    private Context ctx;
    Adapter(Context context,HashMap<String,AddExerciseFragment.Workout> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.ctx = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view,parent,false);
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
            if(stevec == position) {
                holder.setVaj.setText("Skupina vaj: " + e);
                holder.open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //CardsFragment c = new CardsFragment();
                        System.out.println("Izbana vaja: " + e);
                        Fragment fragment = new EighthFragment(e);
                        FragmentManager fragmentManager = ((AppCompatActivity)ctx).getSupportFragmentManager();;
                        System.out.println("Fragment manager " + fragmentManager);
                        System.out.println("Fragment " + fragment);
                        if(fragmentManager != null) {
                            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
                        }
                    }
                });
                for (String vaje : w.vaje) {
                    s.append("Vaja: " + vaje + "\n");
                    holder.vaja.setText("Vaja: " + vaje + "\n");
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView setVaj,vaja;
        ImageView slika;
        Button open,finish;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slika = itemView.findViewById(R.id.imageView);
            setVaj = itemView.findViewById(R.id.setVaj);
            vaja = itemView.findViewById(R.id.vaja);
            open = itemView.findViewById(R.id.open);
            finish = itemView.findViewById(R.id.finish);
        }
    }
}
