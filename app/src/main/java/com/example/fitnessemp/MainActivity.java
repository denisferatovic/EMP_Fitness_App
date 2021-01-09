package com.example.fitnessemp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import static com.example.fitnessemp.R.drawable.ic_hamburger;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    public static int seconds;
    public static int steps,ActiveWorkouts;
    public static String android_id;
    private LocationManager locationManager;
    public static Location onlyOneLocation;
    private final int REQUEST_FINE_LOCATION = 1234;
    public static DatabaseReference mDatabase;
    public static int DailySteps,DailyWorkouts;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    public static String TodayDate;
    public static int Day,Year,Month;
    //test id
    //public static String android_id= "13"; //Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    public static HashMap<String,AddExerciseFragment.Workout>  workouts = new HashMap<String,AddExerciseFragment.Workout>();
    public static HashMap<String,WorkoutDays> workoutDays = new HashMap<>();
    private String apikey = "AIzaSyBdOvTWvRNqdJAZzHRx8MyA69l9BK3mSJo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //place search
        if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);
        }
        PlacesClient placesClient = Places.createClient(this);

        Calendar currentDate = Calendar.getInstance();
        Day = currentDate.get(Calendar.DAY_OF_MONTH);
        Year = currentDate.get(Calendar.YEAR);
        Month = currentDate.get(Calendar.MONTH) + 1;
        TodayDate = String.valueOf(Year)+"/"+String.valueOf(Month)+"/"+String.valueOf(Day);


        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //System.out.println(android_id); -> ga dobi!

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(ic_hamburger);
        //load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
        // Read from the database

        //mDatabase.child(MainActivity.android_id).child("vaje").setValue(""); // reset db entries
        //mDatabase.child(android_id).child(MainActivity.TodayDate).child("vaje").setValue(""); //reset db entries
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if(dataSnapshot.child(android_id).child(TodayDate).child("vaje").child("Unfinished").exists()) {
                    int i = 0;
                    Iterator<DataSnapshot> it = dataSnapshot.child(android_id).child(TodayDate).child("vaje").child("Unfinished").getChildren().iterator();
                    while(it.hasNext()){
                        DataSnapshot snap = it.next();

                        Log.d("WorkoutsKey",snap.getChildren().iterator().next().getChildren().toString());
                        workouts.put(snap.getKey(),new AddExerciseFragment.Workout(snap.getKey(), (ArrayList) snap.child(String.valueOf(i)+":").getValue()));
                    }
                }
                Log.d("DataChange", "Value is: " + workouts);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });
        mDatabase.child(android_id+"/dates").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String datum;
                String vaja;
                int set;
                int rep;
                if(dataSnapshot.exists()) {
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while(it.hasNext()){
                        DataSnapshot snap = it.next();
                        datum = snap.getKey();
                        Iterator<DataSnapshot> it2 = snap.child("workout").getChildren().iterator();
                        workoutDays.put(datum,new WorkoutDays());
                        while(it2.hasNext()) {
                            DataSnapshot snap2 = it2.next();
                            vaja =snap2.getKey();
                            set=Integer.parseInt(snap2.getValue().toString().split(" x ")[0]);
                            rep=Integer.parseInt(snap2.getValue().toString().split(" x ")[1]);
                            Integer [] info = new Integer[2];
                            info[0]=set;
                            info[1]=rep;
                            workoutDays.get(datum).add(vaja,info);
                            //Log.d("DataChangeDates", "Value is: " +vaja);
                        }
                    }
                }


                Log.d("DataChangeDates", "Value is: " + workoutDays);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });

    }
    @Override
    public void onLocationChanged(Location location) {
        onlyOneLocation = location;
        locationManager.removeUpdates(this);
    }
    @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override public void onProviderEnabled(String provider) { }
    @Override public void onProviderDisabled(String provider) { }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("gps", "Location permission granted");
                    try {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates("gps", 0, 0, this);
                    } catch (SecurityException ex) {
                        Log.d("gps", "Location permission did not work!");
                    }
                }
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();

        }else if(menuItem.getItemId() == R.id.vaje){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new SecondFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();

        }else if(menuItem.getItemId() == R.id.koledar){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new ThirdFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();

        }else if(menuItem.getItemId() == R.id.grafi){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FourthFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();

        }else if(menuItem.getItemId() == R.id.stoparica){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FifthFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();

        }else if(menuItem.getItemId() == R.id.zemljevid){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new SixthFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();

        }else if(menuItem.getItemId() == R.id.pomoc){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new SeventhFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }
        else if(menuItem.getItemId() == R.id.stepCounter){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new StepCounter());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }
        else if(menuItem.getItemId() == R.id.goals){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new NinthFragment());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
        }
        else if(menuItem.getItemId() == R.id.maxCalulator){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MaxCalculator());
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();


        }
        return true;
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
    class WorkoutDays{
        HashMap<String,Integer[]> vaje = new HashMap<>();
        void add(String vaja,Integer [] reps_sets){
            this.vaje.put(vaja,reps_sets);
        }
        @Override
        public String toString(){
            String text= "";
            for(Map.Entry<String,Integer []> entry :vaje.entrySet()){
                text = text.concat("( "+entry.getKey() +" => "+ entry.getValue()[0]+ " x "+entry.getValue()[1]+" ) ");
            }
            return text;
        }

    }
}