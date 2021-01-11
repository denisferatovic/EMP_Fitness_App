package com.example.fitnessemp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FourthFragment extends Fragment {

    View view;
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<BarEntry> dataset = new ArrayList<>();
    BarChart chart;
    int maxDays;
    Button next;
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_fourth, container, false);

        chart = view.findViewById(R.id.chartView);
        next = view.findViewById(R.id.next);


        maxDays = numberOfDaysInMonth(MainActivity.Month,MainActivity.Year);
        for(int i = 1; i<maxDays; i++){
            labels.add(String.valueOf(i));
        }

        DatabaseReference Referenca = MainActivity.mDatabase.getDatabase().getReference(MainActivity.android_id);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dateF = "";
                String dateUF = "";
                String vaje = "";
                dataset = new ArrayList<>();

                if(count == 0) {
                    count++;
                    for (int i = 1; i < maxDays; i++) {
                        String date = String.valueOf(MainActivity.Year) + "/";
                        date += MainActivity.Month + "/" + i;
                        if (dataSnapshot.hasChild(date)) {
                            for (DataSnapshot ds : dataSnapshot.child(date).child("vaje").getChildren()) {
                                if (ds.getKey().equals("Finished")) {
                                    int counter = 0;
                                    for (DataSnapshot ds1 : dataSnapshot.child(date).child("vaje").child("Finished").getChildren()) {
                                        counter++;
                                    }
                                    updateChart(i, counter, "# Monthly finished exercises");
                                } else {
                                    updateChart(i, 0, "# Monthly finished exercises");
                                }
                            }
                        } else {
                            updateChart(i, 0, "# Monthly finished exercises");
                        }

                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        };

        Referenca.addListenerForSingleValueEvent(eventListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count % 2 != 0) {
                    chart.clear();
                    DatabaseReference Referenca = MainActivity.mDatabase.getDatabase().getReference(MainActivity.android_id);

                    ValueEventListener eventListener1 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String dateF = "";
                            String dateUF = "";
                            String vaje = "";
                            dataset = new ArrayList<>();

                            for (int i = 1; i < maxDays; i++) {
                                String date = String.valueOf(MainActivity.Year) + "/";
                                date += MainActivity.Month + "/" + i;
                                if (dataSnapshot.hasChild(date)) {
                                    for (DataSnapshot ds : dataSnapshot.child(date).child("vaje").getChildren()) {
                                        if (ds.getKey().equals("Unfinished")) {
                                            int counter = 0;
                                            for (DataSnapshot ds1 : dataSnapshot.child(date).child("vaje").child("Unfinished").getChildren()) {
                                                counter++;
                                            }
                                            updateChart(i, counter, "# Monthly unfinished exercises");
                                        } else {
                                            updateChart(i, 0, "# Monthly unfinished exercises");
                                        }
                                    }
                                } else {
                                    updateChart(i, 0, "# Monthly unfinished exercises");
                                }

                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };


                    Referenca.addListenerForSingleValueEvent(eventListener1);
                    count++;


                }else if(count % 2 == 0){
                    chart.clear();
                    DatabaseReference Referenca = MainActivity.mDatabase.getDatabase().getReference(MainActivity.android_id);

                    ValueEventListener eventListener2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String dateF = "";
                            String dateUF = "";
                            String vaje = "";
                            dataset = new ArrayList<>();

                            for (int i = 1; i < maxDays; i++) {
                                String date = String.valueOf(MainActivity.Year) + "/";
                                date += MainActivity.Month + "/" + i;
                                if(dataSnapshot.hasChild(date)) {
                                    for (DataSnapshot ds : dataSnapshot.child(date).child("vaje").getChildren()) {
                                        if (ds.getKey().equals("Finished")) {
                                            int counter = 0;
                                            for (DataSnapshot ds1 : dataSnapshot.child(date).child("vaje").child("Finished").getChildren()) {
                                                counter++;
                                            }
                                            updateChart(i, counter,"# Monthly finished exercises");
                                        } else {
                                            updateChart(i, 0, "# Monthly finished exercises");
                                        }
                                    }
                                }else{
                                    updateChart(i, 0, "# Monthly finished exercises");
                                }

                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    Referenca.addListenerForSingleValueEvent(eventListener2);
                    count++;

                }

            }
        });

        return view;
    }

    private void updateChart(int x, int y, String label) {
        dataset.add(new BarEntry(x, y));
        BarDataSet datasetF = new BarDataSet(dataset, label);
        datasetF.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(datasetF);
        BarData data = new BarData(dataSets);
        data.setValueTextColor(Color.DKGRAY);
        data.setDrawValues(false);
        chart.setNoDataText("");
        chart.setTouchEnabled(false);
        XAxis xAxis = chart.getXAxis();

        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true); // Required to enable granularity

        chart.getAxisRight().setGranularity(1.0f);
        chart.getAxisLeft().setGranularityEnabled(true); // Required to enable granularity

        chart.getAxisLeft().setGranularity(1.0f);
        chart.getAxisLeft().setGranularityEnabled(true); // Required to enable granularity

        chart.getAxisRight().setGranularity(1.0f);
        chart.getAxisLeft().setGranularityEnabled(true); // Required to enable granularity

        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.setData(data);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setAxisMinimum((int) 1);
        xAxis.setAxisMaximum((int) maxDays);
        xAxis.setLabelCount(maxDays);
        xAxis.setSpaceMin(0.3f);
        chart.notifyDataSetChanged();
        chart.invalidate();
        Description title = new Description();
        title.setText("");
        chart.setDescription(title);
    }

    public static int numberOfDaysInMonth(int month, int year) {
        Calendar monthStart = new GregorianCalendar(year, month, 1);
        return monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
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

