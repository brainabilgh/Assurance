package com.journaldev.customlistview;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * Created by Nabil on 21/05/2017.
 */

public class UploadService extends IntentService {
    public static StorageReference storage;

    private Handler handler = new Handler();
    private int status = 0;
    private ProgressDialog dialog;
    private Boolean success = false;

    public UploadService() {
        super("UploadService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String imageString = workIntent.getStringExtra("uriImage");
        String videoString = workIntent.getStringExtra("uriVideo");
        Log.d("Service", "Started");

        // Do work here, based on the contents of dataString
        this.storage = FirebaseStorage.getInstance().getReference();

        //dialog = new ProgressDialog(this);


        // Lancer le dialogue
        //dialog.setMessage("Chargement en cours...");
        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //dialog.show();
        // Gerer le pourcentage
        //ShowProgressDialog();

        // Recuperer le path vers les données choisis
        Uri uriImage = null;
        Uri uriVideo = null;
        if(imageString!=null){
            uriImage = Uri.parse(imageString);
            // set path of the last data to firebase storage
            StorageReference fileImagepath = storage.child("AllImages").child(uriImage.getLastPathSegment());
            // A la fin de l'upload set success to true
            fileImagepath.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(MainActivity.this, "Chargement terminé", Toast.LENGTH_SHORT).show();
                    //dialog.dismiss();
                    if(status!=100){
                        success = true;
                        Log.d("onSuccess", success+" "+status);
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("Error", "Vérifiez votre connexion");
                }
            });
        }
        if(videoString!=null){
            uriVideo = Uri.parse(videoString);
            StorageReference fileVideopath = storage.child("AllVideos").child(uriVideo.getLastPathSegment());
            // A la fin de l'upload set success to true
            fileVideopath.putFile(uriVideo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(MainActivity.this, "Chargement terminé", Toast.LENGTH_SHORT).show();
                    //dialog.dismiss();
                    if(status!=100){
                        success = true;
                        Log.d("onSuccess", success+" "+status);
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("Error", "Vérifiez votre connexion");
                }
            });
        }

    }
}
