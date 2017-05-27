package com.journaldev.customlistview;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    boolean isFABOpen = false;
    public static final String MESSAGE_SUPP = "android.journaldev.customlistview.MESSAGE";
    public static final Uri defaultImage = Uri.parse("android.resource://com.journaldev.customlistview/" + R.drawable.cameraicon);

    public static DatabaseReference firebaseAccident;
    public static PostUser currentUser = null;

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

        // recuperer les infos user et créer currentUser
        if(currentUser==null){
            Intent myIntent = getIntent();
            currentUser = new PostUser( myIntent.getStringExtra("username"),
                    myIntent.getStringExtra("password"),
                    myIntent.getStringExtra("role"));
            /*Log.d("Username", currentUser.username);
            Log.d("Password", currentUser.password);
            Log.d("Role", currentUser.role);*/
        }

        final Intent intention_details = new Intent(this, Details.class);
        listView=(ListView)findViewById(R.id.list);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        // check WIFI
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        if (activeNetwork!=null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                // desable sync
                ref.keepSynced(false);
            else ref.keepSynced(true);// enable sync
        }

        firebaseAccident = ref.child("Accident");

        if(currentUser.role.equals("user")) firebaseAccident = ref.child("Accident").child(currentUser.username);


        adapter= new CustomAdapter(accidents, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intention_details.putExtra(MESSAGE_SUPP, String.valueOf(position));
                startActivity(intention_details);
            }
        });

        // Read from Firebase
        firebaseAccident.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accidents.clear();

                GenericTypeIndicator genericTypeIndicator = null;
                ArrayList<Post> postsUser = null;
                Map<String, ArrayList<Post>> postsAdmin = null;
                ArrayList<Integer> aMettreEtatA0 = new ArrayList<Integer>(); //les accidents vont basculer à l'etat envoyé
                try {
                    if (currentUser.role.equals("user")) {
                        genericTypeIndicator = new GenericTypeIndicator<ArrayList<Post>>() {};
                        postsUser = (ArrayList<Post>) dataSnapshot.getValue(genericTypeIndicator);
                        for (int i = 0; i < postsUser.size(); i++) {
                            accidents.add(new Accident(postsUser.get(i).vehicule1, postsUser.get(i).date, postsUser.get(i).lieu, postsUser.get(i).infos, postsUser.get(i).type, postsUser.get(i).vehicule2, postsUser.get(i).mantant, postsUser.get(i).nomImage, postsUser.get(i).nomVideo, postsUser.get(i).etat));
                        }
                    } else { // admin
                        genericTypeIndicator = new GenericTypeIndicator<Map<String, ArrayList<Post>>>() {};
                        postsAdmin = (Map<String, ArrayList<Post>>) dataSnapshot.getValue(genericTypeIndicator);

                        for(Map.Entry<String, ArrayList<Post>> user : postsAdmin.entrySet()) {
                            String username = user.getKey();
                            Log.i("Username", username);
                            ArrayList<Post> userAccidents = user.getValue();
                            for (int i = 0; i < userAccidents.size(); i++) {
                                accidents.add(new Accident(userAccidents.get(i).vehicule1, userAccidents.get(i).date, userAccidents.get(i).lieu, userAccidents.get(i).infos, userAccidents.get(i).type, userAccidents.get(i).vehicule2, userAccidents.get(i).mantant, userAccidents.get(i).nomImage, userAccidents.get(i).nomVideo, postsUser.get(i).etat));
                                if(userAccidents.get(i).etat == -1) aMettreEtatA0.add(i);
                            }
                        }
                    }
                } catch (Exception e) {Log.e("Error onDataChange", "java.util.ArrayList.size() on a null object reference");}

                adapter.notifyDataSetChanged();
                for(int i=0; i<aMettreEtatA0.size();i++){
                    //accidents.get(aMettreEtatA0.get(i)).setEtat(0);// envoyé
                }
                aMettreEtatA0 = null;
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
        if(!isFABOpen){
            showFABMenu();
        }else{
            closeFABMenu();
        }
    }


    private void showFABMenu(){
        isFABOpen=true;
        findViewById(R.id.fab1).animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        findViewById(R.id.fab2).animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        findViewById(R.id.fab3).animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        findViewById(R.id.fab1).animate().translationY(0);
        findViewById(R.id.fab2).animate().translationY(0);
        findViewById(R.id.fab3).animate().translationY(0);
    }
    public void onBackPressed() {
        if(isFABOpen){
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

    private void addNotification(String data, String title, String text) {
        String message = "Votre accident est : " + data;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.default_accident)
                .setContentTitle(title+":")
                .setContentText(text+":")
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
