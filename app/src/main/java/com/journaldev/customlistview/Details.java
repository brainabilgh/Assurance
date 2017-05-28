package com.journaldev.customlistview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class Details extends AppCompatActivity {
    public static StorageReference storage;
    private ImageView mImage;
    private TextView mMatricule;
    private TextView mMarque;
    private TextView mModele;
    private TextView mNom;
    //private TextView mPrenom;
    private TextView mPermis;
    private TextView mNouveauPermis;
    private TextView mLieu;
    private TextView mDate;
    private TextView mInfos;
    private View mProgressView;
    private TextView mMantant;
    private String storagePath;
    
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_main);
        setupActionBar();
        Intent intention = getIntent();

        storage = FirebaseStorage.getInstance().getReference();
        storagePath = "";

        final String indice = intention.getStringExtra(MainActivity.MESSAGE_SUPP);

        // mise à jour de l'état et notif
        if(MainActivity.currentUser.role.equals("admin")){
            if(MainActivity.accidents.get(Integer.parseInt(indice)).getEtat()== -1 ||  MainActivity.accidents.get(Integer.parseInt(indice)).getEtat()!= 0){
                // si état pas encore traité
                MainActivity.accidents.get(Integer.parseInt(indice)).setEtat(1);
            }
        }

        mImage = (ImageView) findViewById(R.id.detail_image);

        if(MainActivity.accidents.get(Integer.parseInt(indice)).getNomImage()==null){
            mImage.setImageResource(R.drawable.default_accident);
            Log.d("storageReferenceImage", "null");
        }
        else{
            final File localFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/images/"+MainActivity.accidents.get(Integer.parseInt(indice)).getNomImage());
            // download and create if not exists :
            if(!localFile.exists()) {
                // Reference to an image file in Firebase Storage
                StorageReference storageReference = storage.child("AllImages").child(MainActivity.accidents.get(Integer.parseInt(indice)).getNomImage());
                Log.d("storageReferenceImage", MainActivity.accidents.get(Integer.parseInt(indice)).getNomImage() + " : " + storageReference.toString());
                // Load the image using Glide
                Glide.with(this /* context */)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(mImage);
                //mImage.setImageURI(MainActivity.defaultImage);
            } else mImage.setImageURI(Uri.parse(localFile.getAbsolutePath()));
        }

        if(MainActivity.accidents.get(Integer.parseInt(indice)).getNomVideo()!=null) { // si on a une video...
                StorageReference storageReference = storage.child("AllVideos").child(MainActivity.accidents.get(Integer.parseInt(indice)).getNomVideo());
                Log.d("storageReferenceVideo", MainActivity.accidents.get(Integer.parseInt(indice)).getNomVideo()+" : "+storageReference.toString());

                final File localFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/videos/"+MainActivity.accidents.get(Integer.parseInt(indice)).getNomVideo());
                // download and create if not exists :
                if(!localFile.exists()) {
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.i("Video", "Download Success");
                            readVideo(localFile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.e("Video", "Download Failed");
                        }
                    });
                } else {
                    Log.i("Video", "Exists");
                    readVideo(localFile);
                }
        }

        mMatricule = (TextView) findViewById(R.id.detail_matricule);
        mMatricule.setText(getResources().getString(R.string.prompt_matricule) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getVehicule1().getMatricule());
        mMarque = (TextView) findViewById(R.id.detail_marque);
        mMarque.setText(getResources().getString(R.string.prompt_marque) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getVehicule1().getMarque());
        mModele = (TextView) findViewById(R.id.detail_modele);
        mModele.setText(getResources().getString(R.string.prompt_modele) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getVehicule1().getModele());
        mNom = (TextView) findViewById(R.id.detail_nom);
        mNom.setText(getResources().getString(R.string.prompt_nom) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getVehicule1().getNomProprietaire());
        //mPrenom = (TextView) findViewById(R.id.detail_prenom);
        //mPrenom.setText(getResources().getString(R.string.prompt_prenom) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getVehicule().getPrenomProprietaire());
        mPermis = (TextView) findViewById(R.id.detail_permis);
        mPermis.setText(getResources().getString(R.string.prompt_permis) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getVehicule1().getPermis());
        mNouveauPermis = (TextView) findViewById(R.id.detail_nouveauPermis);
        mNouveauPermis.setText(getResources().getString(R.string.prompt_anciennete) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getVehicule1().getProprietaireNouveauPermis() + "\n");
        mLieu = (TextView) findViewById(R.id.detail_lieu);
        mLieu.setText(getResources().getString(R.string.prompt_lieu) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getLieu());
        mDate = (TextView) findViewById(R.id.detail_date);
        mDate.setText(getResources().getString(R.string.prompt_date) + ":\n" + MainActivity.accidents.get(Integer.parseInt(indice)).getDate() + "\n");
        mProgressView = findViewById(R.id.detail_progress);
        mInfos = (TextView) findViewById(R.id.detail_infos);
        mInfos.setText(getResources().getString(R.string.prompt_infos) + " :\n" + MainActivity.accidents.get(Integer.parseInt(indice)).getInfos());
        mMantant = (TextView) findViewById(R.id.detail_mantant);

        if (MainActivity.accidents.get(Integer.parseInt(indice)).getMantant() > 0.0f && !(String.valueOf(MainActivity.accidents.get(Integer.parseInt(indice)).getMantant())).equals("null")) {
            mMantant.setText(getResources().getString(R.string.prompt_mantant) + " : " + MainActivity.accidents.get(Integer.parseInt(indice)).getMantant()+ " DAZ");
        } else mMantant.setText("(Mantant non encore initialisé)");

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void readVideo(File localFile){
        VideoView mVideo = (VideoView) findViewById(R.id.detail_video);
        mVideo.setVisibility(View.VISIBLE);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(mVideo);
        mc.setMediaPlayer(mVideo);
        mVideo.setMediaController(mc);
        mVideo.setVideoURI(Uri.parse(localFile.getAbsolutePath().toString()));
        mVideo.start();
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

        Intent notificationIntent = new Intent(this, Details.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
