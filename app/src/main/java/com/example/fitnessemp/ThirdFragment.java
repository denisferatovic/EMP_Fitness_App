package com.example.fitnessemp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ThirdFragment extends Fragment {

    View view;
    Context ctx;
    public static String CalendarDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            if ((ViewGroup) view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_third, container, false);

        List<EventDay> eUnfinished = new ArrayList<>();
        HashMap<EventDay, String> vUnfinished = new HashMap<EventDay, String>();
        List<EventDay> eFinished = new ArrayList<>();
        HashMap<EventDay, String> vFinished = new HashMap<EventDay, String>();
        CalendarDate = "";

        ctx = this.getContext();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        com.applandeo.materialcalendarview.CalendarView calendarView = (com.applandeo.materialcalendarview.CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setDate(calendar.getTime());

        DatabaseReference Referenca = MainActivity.mDatabase.getDatabase().getReference(MainActivity.android_id);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dateF = "";
                String dateUF = "";
                String vaje = "";

                for (int j = 1; j < 12; j++) {
                    for (int i = 1; i < 31; i++) {
                        String date = String.valueOf(MainActivity.Year) + "/";
                        date += j + "/" + i;
                        for (DataSnapshot ds : dataSnapshot.child(date).child("vaje").getChildren()) {
                            if (ds.getKey().equals("Unfinished")) {
                                vaje = "";
                                calendar.set(MainActivity.Year, j - 1, i);
                                eUnfinished.add(new EventDay(calendar, R.drawable.ic_uncheck, Color.parseColor("#FF0000")));
                                calendarView.setEvents(eUnfinished);
                                for (DataSnapshot ds1 : dataSnapshot.child(date).child("vaje").child("Unfinished").getChildren()) {
                                    vaje += ds1.getKey() + ", ";
                                }
                                if (!vaje.equals("")) {
                                    vaje = vaje.substring(0, vaje.length() - 2);
                                    vUnfinished.put(new EventDay(calendar), vaje);
                                }
                            }
                            if (ds.getKey().equals("Finished")) {
                                vaje = "";
                                calendar.set(MainActivity.Year, j - 1, i);
                                eFinished.add(new EventDay(calendar, R.drawable.ic_check, Color.parseColor("#008000")));
                                calendarView.setEvents(eFinished);

                                for (DataSnapshot ds1 : dataSnapshot.child(date).child("vaje").child("Finished").getChildren()) {
                                    vaje += ds1.getKey() + ", ";
                                }
                                if (!vaje.equals("")) {
                                    vaje = vaje.substring(0, vaje.length() - 2);
                                    vFinished.put(new EventDay(calendar), vaje);
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        Referenca.addListenerForSingleValueEvent(eventListener);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar dateClick = eventDay.getCalendar();
                Calendar date = Calendar.getInstance(Locale.getDefault());
                Date today = date.getTime();

                int Day = dateClick.get(Calendar.DAY_OF_MONTH);
                int Year = dateClick.get(Calendar.YEAR);
                int Month = dateClick.get(Calendar.MONTH) + 1;

                String date1 = String.valueOf(Year) + "/" + String.valueOf(Month) + "/" + String.valueOf(Day);
                Boolean ou = false;
                String out = "";
                PopupWindow popupWindow = null;
                LayoutInflater inflater;
                View popupView = null;

                if (eUnfinished.contains(eventDay)) {
                    ou = true;
                    // inflate the layout of the popup window
                    inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.popup, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    popupWindow = new PopupWindow(popupView, width, height, focusable);
                    out += "Unfinished: ";
                    out += vUnfinished.get(eventDay);
                    out += "\n";

                }
                if (eFinished.contains(eventDay)) {
                    ou = true;
                    // inflate the layout of the popup window
                    inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.popup, null);
                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    popupWindow = new PopupWindow(popupView, width, height, focusable);
                    out += "Finished: ";
                    System.out.println("joj");
                    out += vFinished.get(eventDay);

                }
                if (popupWindow != null && ou) {
                    TextView valueTV = popupView.findViewById(R.id.mojPrint);
                    valueTV.setText(out);
                    Button add = popupView.findViewById(R.id.gumb);
                    Button close = popupView.findViewById(R.id.close);
                    if (popupView != null) {
                        PopupWindow finalPopupWindow = popupWindow;
                        //show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 190);
                        // dismiss the popup window when touched

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CalendarDate = String.valueOf(eventDay.getCalendar().YEAR) + "/" + String.valueOf(eventDay.getCalendar().MONTH + 1) + "/" + String.valueOf(eventDay.getCalendar().DAY_OF_MONTH);
                                finalPopupWindow.dismiss();
                                goToAddExercise();

                            }
                        });
                        popupView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                finalPopupWindow.dismiss();
                                return true;
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalPopupWindow.dismiss();
                            }
                        });
                    }
                }if(eventDay.getCalendar().getTime().after(today)){
                    inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.popup, null);
                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    popupWindow = new PopupWindow(popupView, width, height, focusable);
                    Button add = popupView.findViewById(R.id.gumb);
                    Button close = popupView.findViewById(R.id.close);

                    if (popupView != null) {
                        PopupWindow finalPopupWindow = popupWindow;
                        //show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 190);
                        // dismiss the popup window when touched
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CalendarDate = String.valueOf(eventDay.getCalendar().YEAR) + "/" + String.valueOf(eventDay.getCalendar().MONTH + 1) + "/" + String.valueOf(eventDay.getCalendar().DAY_OF_MONTH);
                                finalPopupWindow.dismiss();
                                goToAddExercise();

                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalPopupWindow.dismiss();
                            }
                        });
                    }
                }

            }
        });


        return view;
    }

    // called when add exercise button is clicked
    public void goToAddExercise(){
        Log.d("Info", "Prsu v goToAddExercise");
        Fragment fragment = new AddExerciseFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
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

