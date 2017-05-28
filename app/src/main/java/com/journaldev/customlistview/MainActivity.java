package com.journaldev.customlistview;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.location.LocationListener;

import java.util.ArrayList;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements LocationListener {
    boolean isFABOpen = false;
    public static final String MESSAGE_SUPP = "android.journaldev.customlistview.MESSAGE";
    public static final Uri defaultImage = Uri.parse("android.resource://com.journaldev.customlistview/" + R.drawable.cameraicon);

    //Defining Latitude & Longitude & Expert's name
    double lat = 36.718120,
            long1 = 3.175237;
    String nom = "GHOUILA Nabil";
    private int radius = 1500;
    public static String PROX_ALERT_INTENT = "com.journaldev.customlistview.MainActivity";
    public static int requestCode = 300;
    private LocationManager lm;
    public static boolean notice = false;
    public static DatabaseReference firebaseAccident;
    public static PostUser currentUser = null;
    private double finalDistance = 0;
    public static boolean edit = false;
    public static ArrayList<Accident> accidents = new ArrayList<Accident>();
    public static ArrayList<Vehicule> vehicules = new ArrayList<Vehicule>();
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start the service of localisation
       // if(!notice) {
            initialiseReceiver();
            addProximityAlert(lat, long1, nom);
       // }


        // recuperer les infos user et créer currentUser
        if (currentUser == null) {
            Intent myIntent = getIntent();
            currentUser = new PostUser(myIntent.getStringExtra("username"),
                    myIntent.getStringExtra("password"),
                    myIntent.getStringExtra("role"));
            /*Log.d("Username", currentUser.username);
            Log.d("Password", currentUser.password);
            Log.d("Role", currentUser.role);*/
        }

        final Intent intention_details = new Intent(this, Details.class);
        final Intent intention_form = new Intent(this, NewAccidentForm.class);
        listView = (ListView) findViewById(R.id.list);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebaseAccident = FirebaseDatabase.getInstance().getReference().child("Accident");
        if (currentUser.role.equals("user"))
            firebaseAccident = FirebaseDatabase.getInstance().getReference().child("Accident").child(currentUser.username);

        /*if(accidents.size()==0) {
            vehicules.add(new Vehicule("102546-101-19", "GHOUILA Nabil", "1548925", "Ancien", "Citroien", "Berlingo"));
            vehicules.add(new Vehicule("509546-00-31", "BELKAID Aissa", "48925", "Nouveau", "Dacia", "Sendiro"));
            vehicules.add(new Vehicule("132346-109-16", "BENFLAN Flan", "944925", "Ancien", "Renault", "Compus"));

            accidents.add(new Accident(vehicules.get(0), "25/4/2017", "Babezzouar", "Des infos\nsur l'accident qui ...", "image1", "video1"));
            accidents.add(new Accident(vehicules.get(1), "26/4/2017", "Dar elbeida", "Des infos sur\nl'accident qui ...", "image2", "video2"));
            accidents.add(new AccidentDouble(vehicules.get(1), "27/4/2017", "Harrach", "Des infos sur la 2 accident\nqui ...", "image1", "video1", vehicules.get(2)));

        } else {
            Intent intention = getIntent();
            if(intention!=null) {
                final String message = intention.getStringExtra(NewAccidentForm.MESSAGE_SUPP_ADD);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }*/

        adapter = new CustomAdapter(accidents, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intention_details.putExtra(MESSAGE_SUPP, String.valueOf(position));
                startActivity(intention_details);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                edit = true;
                intention_form.putExtra(MESSAGE_SUPP, String.valueOf(position));
                startActivity(intention_form);
                return true;
            }
        });
        // Read from Firebase
        firebaseAccident.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // check WIFI
                /*ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
                if (activeNetwork!=null) {
                    if (activeNetwork.getType()==ConnectivityManager.TYPE_WIFI) {*/
                accidents.clear();

                GenericTypeIndicator genericTypeIndicator = null;
                ArrayList<Post> postsUser = null;
                Map<String, ArrayList<Post>> postsAmin = null;

                try {
                    if (currentUser.role.equals("user")) {
                        genericTypeIndicator = new GenericTypeIndicator<ArrayList<Post>>() {
                        };
                        postsUser = (ArrayList<Post>) dataSnapshot.getValue(genericTypeIndicator);
                        for (int i = 0; i < postsUser.size(); i++) {
                            accidents.add(new Accident(postsUser.get(i).vehicule1, postsUser.get(i).date, postsUser.get(i).lieu, postsUser.get(i).infos, postsUser.get(i).type, postsUser.get(i).vehicule2, postsUser.get(i).mantant, postsUser.get(i).nomImage, postsUser.get(i).nomVideo));
                        }
                    } else { // admin
                        genericTypeIndicator = new GenericTypeIndicator<Map<String, ArrayList<Post>>>() {
                        };
                        postsAmin = (Map<String, ArrayList<Post>>) dataSnapshot.getValue(genericTypeIndicator);

                        for (Map.Entry<String, ArrayList<Post>> user : postsAmin.entrySet()) {
                            String username = user.getKey();
                            ArrayList<Post> userAccidents = user.getValue();

                            for (int i = 0; i < userAccidents.size(); i++) {
                                accidents.add(new Accident(userAccidents.get(i).vehicule1, userAccidents.get(i).date, userAccidents.get(i).lieu, userAccidents.get(i).infos, userAccidents.get(i).type, userAccidents.get(i).vehicule2, userAccidents.get(i).mantant, userAccidents.get(i).nomImage, userAccidents.get(i).nomVideo));
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error onDataChange", "java.util.ArrayList.size() on a null object reference");
                }

                adapter.notifyDataSetChanged();
                    /*}
                    else if (activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE) Toast.makeText(getApplicationContext(), "Attente de connexion Wifi", Toast.LENGTH_LONG);
                         else  Toast.makeText(getApplicationContext(), "Aucune connexion n'est détectée", Toast.LENGTH_LONG); // TYPE_NOT_CONNETED
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intention = new Intent(this, NewAccidentForm.class);
        String typeAccident = item.getTitle().toString();
        intention.putExtra(MESSAGE_SUPP, typeAccident);
        startActivity(intention);

        return super.onOptionsItemSelected(item);
    }

    public void AccidentType1(View view) {
        if (!isFABOpen) {
            showFABMenu();
        } else {
            closeFABMenu();
        }
    }


    private void showFABMenu() {
        isFABOpen = true;
        findViewById(R.id.fab1).animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        findViewById(R.id.fab2).animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        findViewById(R.id.fab3).animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        findViewById(R.id.fab1).animate().translationY(0);
        findViewById(R.id.fab2).animate().translationY(0);
        findViewById(R.id.fab3).animate().translationY(0);
    }

    public void onBackPressed() {
        if (isFABOpen) {
            closeFABMenu();
        } else
            super.onBackPressed();
    }

    public void Fab1(View view) {
        Intent intention1 = new Intent(this, NewAccidentForm.class);
        String typeAccident = this.getText(R.string.type_stationnement).toString();
        intention1.putExtra(MESSAGE_SUPP, typeAccident);
        startActivity(intention1);
    }

    public void Fab2(View view) {
        Intent intention2 = new Intent(this, NewAccidentForm.class);
        String typeAccident = this.getText(R.string.type_double).toString();
        intention2.putExtra(MESSAGE_SUPP, typeAccident);
        startActivity(intention2);
    }

    public void Fab3(View view) {
        Intent intention3 = new Intent(this, NewAccidentForm.class);
        String typeAccident = this.getText(R.string.type_seul).toString();
        intention3.putExtra(MESSAGE_SUPP, typeAccident);
        startActivity(intention3);
    }

    //------------------------------------------------------------------- Localisation Service --------------------------------------------------------------------------//
    private void addProximityAlert(double latitude, double longitude, String poiName) {

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //for debugging...
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);

        Bundle extras = new Bundle();
        extras.putString("name", poiName);
        extras.putInt("id", requestCode);
        Intent intent = new Intent(PROX_ALERT_INTENT);
        intent.putExtra(PROX_ALERT_INTENT, extras);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode , intent, PendingIntent.FLAG_CANCEL_CURRENT);
        lm.addProximityAlert(
                latitude, // the latitude of the central point of the alert region
                longitude, // the longitude of the central point of the alert region
                radius, // the radius of the central point of the alert region, in meters
                -1, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );
        requestCode++;
    }

    private void initialiseReceiver()
    {
        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new ProximityIntentReceiver(), filter);
    }
    @Override

    //just For debugging to See the distance between my actual position and the aproximit point
    public void onLocationChanged(Location newLocation) {

        Location old = new Location("OLD");
        old.setLatitude(lat);
        old.setLongitude(long1);

        double distance = newLocation.distanceTo(old);
        if((distance-finalDistance > 10) && notice) {
            notice = false;
        }
        finalDistance = distance;
        //if (distance < radius) initialiseReceiver();
        Log.i("MyTag", "Distance: " + distance);
    }

    @Override
    public void onProviderDisabled(String arg0) {}

    @Override
    public void onProviderEnabled(String arg0) {}

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
}
