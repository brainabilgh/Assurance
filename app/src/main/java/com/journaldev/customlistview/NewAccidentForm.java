package com.journaldev.customlistview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * A login screen that offers login via email/password.
 */
public class NewAccidentForm extends AppCompatActivity {

    public static final String MESSAGE_SUPP_ADD = "android.journaldev.customlistview.MESSAGE";

    // UI references.
    private EditText mMatricule;
    private EditText mMarque;
    private EditText mModele;
    private EditText mNom;
    //private EditText mPrenom;
    private EditText mPermis;
    private TextView mNouveauPermis;
    private Switch mNouveauAncien;

    private EditText mMatricule2;
    private EditText mMarque2;
    private EditText mModele2;
    private EditText mNom2;
    //private EditText mPrenom2;
    private EditText mPermis2;
    private TextView mNouveauPermis2;
    private Switch mNouveauAncien2;

    private TextView mSeconVoitureConnu;
    private Switch mConnuInconnu;

    private EditText mLieu;
    private DatePickerDialog mDate;
    private EditText mDateInput;
    private EditText mInfos;

    private static final int  RESULT_LOAD_IMG = 1;
    String imgDecodableString;

            
    private View mProgressView;
    private View mLoginFormView;

    //---------------------------------------------
    private ImageButton buttonCapture;
    private ImageButton recordingButton;

    String imageNom=null;
    String videoNom=null;

    Uri imageStoragePath=null;
    Uri videoStoragePath=null;

    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

    String indiceForm;
    Intent camera_intent;
    Intent video_intent;

    private final int IMAGE_REQUEST_CODE = 100;
    private final int VIDEO_REQUEST_CODE = 200;
    // UI references.

    Intent intention = getIntent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_form);
        setupActionBar();
        Intent intention = getIntent();
        final String type = intention.getStringExtra(MainActivity.MESSAGE_SUPP);

        Toast.makeText(this, "Type d'accident : "+ type, Toast.LENGTH_LONG).show();
        if (MainActivity.edit) indiceForm = intention.getStringExtra(MainActivity.MESSAGE_SUPP);
        mMatricule = (EditText) findViewById(R.id.matricule);
        mMarque = (EditText) findViewById(R.id.marque);
        mModele= (EditText) findViewById(R.id.modele);
        mNom= (EditText) findViewById(R.id.nom);
        //mPrenom= (EditText) findViewById(R.id.prenom);
        mPermis= (EditText) findViewById(R.id.permis);
        mNouveauPermis = (TextView) findViewById(R.id.nouveauPermis);
        mNouveauPermis.setText(R.string.prompt_nouveau_ancien);
        mNouveauAncien = (Switch) findViewById(R.id.nouveauAncien);
        mNouveauAncien.setTextOn("Ancien");
        mNouveauAncien.setTextOff("Nouveau");

        mMatricule2 = (EditText) findViewById(R.id.matricule2);
        mMarque2 = (EditText) findViewById(R.id.marque2);
        mModele2 = (EditText) findViewById(R.id.modele2);
        mNom2 = (EditText) findViewById(R.id.nom2);
        //mPrenom2 = (EditText) findViewById(R.id.prenom2);
        mPermis2 = (EditText) findViewById(R.id.permis2);
        mNouveauPermis2 = (TextView) findViewById(R.id.nouveauPermis2);
        mNouveauPermis2.setText(R.string.prompt_nouveau_ancien);
        mNouveauAncien2 = (Switch) findViewById(R.id.nouveauAncien2);
        mNouveauAncien2.setTextOn("Ancien");
        mNouveauAncien2.setTextOff("Nouveau");
        mSeconVoitureConnu= (TextView) findViewById(R.id.secondVoitureConnu);
        mSeconVoitureConnu.setText(R.string.secondVoitureConnu);
        mConnuInconnu = (Switch) findViewById(R.id.connu_inconnu);
        mConnuInconnu.setTextOn("Connue");
        mConnuInconnu.setTextOff("Inconnue");
        mConnuInconnu.setChecked(true);

        mConnuInconnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ViewGroup) mMatricule2.getParent()).getVisibility()==View.GONE)
                {
                    ((ViewGroup) mMatricule2.getParent()).setVisibility(View.VISIBLE);
                    ((ViewGroup) mMarque2.getParent()).setVisibility(View.VISIBLE);
                    ((ViewGroup) mModele2.getParent()).setVisibility(View.VISIBLE);
                    ((ViewGroup) mNom2.getParent()).setVisibility(View.VISIBLE);
                    //((ViewGroup) mPrenom2.getParent()).setVisibility(View.VISIBLE);
                    ((ViewGroup) mPermis2.getParent()).setVisibility(View.VISIBLE);
                    ((ViewGroup) mNouveauPermis2.getParent()).setVisibility(View.VISIBLE);
                    ((ViewGroup) mNouveauAncien2.getParent()).setVisibility(View.VISIBLE);
                }
                else{
                    ((ViewGroup) mMatricule2.getParent()).setVisibility(View.GONE);
                    ((ViewGroup) mMarque2.getParent()).setVisibility(View.GONE);
                    ((ViewGroup) mModele2.getParent()).setVisibility(View.GONE);
                    ((ViewGroup) mNom2.getParent()).setVisibility(View.GONE);
                    //((ViewGroup) mPrenom2.getParent()).setVisibility(View.GONE);
                    ((ViewGroup) mPermis2.getParent()).setVisibility(View.GONE);
                    ((ViewGroup) mNouveauPermis2.getParent()).setVisibility(View.GONE);
                    ((ViewGroup) mNouveauAncien2.getParent()).setVisibility(View.GONE);
                }
            }
        });

        if(type.equals("Seul") || (MainActivity.edit && MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2() == null)) {
            ((ViewGroup) mMatricule2.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mMarque2.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mModele2.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mNom2.getParent()).setVisibility(View.GONE);
            //((ViewGroup) mPrenom2.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mPermis2.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mNouveauPermis2.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mNouveauAncien2.getParent()).setVisibility(View.GONE);
        }

         if(!type.equals("Stationnement") || (MainActivity.edit && MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2() == null)) {
            ((ViewGroup) mSeconVoitureConnu.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mConnuInconnu.getParent()).setVisibility(View.GONE);
         }


        mLieu= (EditText) findViewById(R.id.lieu);
        mInfos= (EditText) findViewById(R.id.infos);
        //mDate= (DatePickerDialog) findViewById(date);

        mDateInput= (EditText) findViewById(R.id.date_input);
        mDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                mDate = new DatePickerDialog(NewAccidentForm.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                mDateInput.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                mDate.show();
            }
        });

        Button mValidationButton = (Button) findViewById(R.id.validation_button);
        mValidationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Accident accident = attemptValidation(type);
                // Envoi du mail
                if(accident!=null)sendEmail(accident, type);
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        // Record
        hasSDCard();
        camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        buttonCapture = (ImageButton)findViewById(R.id.buttonLoadPicture);
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image_file = getFilePath();
                imageStoragePath = Uri.fromFile(image_file);
                Log.d("Path = ",imageStoragePath.toString());
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, imageStoragePath);
                camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(camera_intent, IMAGE_REQUEST_CODE);
            }
        });

        recordingButton = (ImageButton)findViewById(R.id.buttonLoadVideo);
        recordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File video_file = getFileVideo();
                videoStoragePath = Uri.fromFile(video_file);
                Log.d("Path = ",videoStoragePath.toString());
                video_intent.putExtra(MediaStore.EXTRA_OUTPUT, videoStoragePath);
                video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(video_intent, VIDEO_REQUEST_CODE);
            }
        });
        if (MainActivity.edit) {
                mMatricule.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule1().getMatricule());
                mMarque.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule1().getMarque());
                mModele.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule1().getModele());
                mNom.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule1().getNomProprietaire());
                mPermis.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule1().getPermis());
                mNouveauPermis.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule1().getProprietaireNouveauPermis());
                mLieu.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getLieu());
                mDateInput.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getDate());
                mInfos.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getInfos());
                System.out.println(type + " Aissa");
                //Integer.parseInt(type) == 1 || ((Integer.parseInt(type) == 2) && (mConnuInconnu.isChecked()))
                if (MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2() != null) {
                    mMatricule2.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2().getMatricule());
                    mMarque2.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2().getMarque());
                    mModele2.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2().getModele());
                    mNom2.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2().getNomProprietaire());
                    mPermis2.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2().getPermis());
                    mNouveauPermis2.setText(MainActivity.accidents.get(Integer.parseInt(indiceForm)).getVehicule2().getProprietaireNouveauPermis());
                }
            if (MainActivity.currentUser.role.equals("admin")) {
                mMatricule.setFocusable(false);
                mMarque.setFocusable(false);
                mModele.setFocusable(false);
                mNom.setFocusable(false);
                mPermis.setFocusable(false);
                mNouveauPermis.setFocusable(false);
                mLieu.setFocusable(false);
                mDateInput.setFocusable(false);
                mInfos.setFocusable(false);
            }
        }
    }

    //------------------------------------------ Upload image ------------------------------------------------------------
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==IMAGE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                imageNom = imageStoragePath.getLastPathSegment();
                Toast.makeText(getApplicationContext(),"Image capturée avec succès : "+imageNom, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Image Capture Failed", Toast.LENGTH_LONG).show();
                imageNom = null;
                imageStoragePath = null;
            }
        }
        if(requestCode==VIDEO_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                videoNom = videoStoragePath.getLastPathSegment();
                Toast.makeText(getApplicationContext(),"Vidéo capturé avec succès : "+videoNom, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Vidéo Capture Failed", Toast.LENGTH_LONG).show();
                videoNom = null;
                videoStoragePath = null;
            }
        }
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                otherData = data.getData(); 
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                /*ImageView imgView = (ImageView) findViewById(R.id.imgView);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
*/
            /*} else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }*/
    //------------------------------------------ End Upload image ------------------------------------------------------------

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private Accident attemptValidation(String type) {

        // Reset errors.
        mMatricule.setError(null);
        mMarque.setError(null);
        mModele.setError(null);
        mNom.setError(null);
        //mPrenom.setError(null);
        mPermis.setError(null);
        mLieu.setError(null);

        // Store values at the time of the Validation attempt.
        String matricule = mMatricule.getText().toString();
        String marque = mMarque.getText().toString();
        String modele = mModele.getText().toString();
        String nom = mNom.getText().toString();
        //String prenom = mPrenom.getText().toString();
        String permis = mPermis.getText().toString();
        String nouveauAncien = mNouveauAncien.getTextOn().toString();

        String matricule2 = "";
        String marque2 = "";
        String modele2 = "";
        String nom2 = "";
        //String prenom2 = "";
        String permis2 = "";
        String nouveauAncien2 = "";

        if(!type.equals("Seul")) {
            matricule2 = mMatricule2.getText().toString();
            marque2 = mMarque2.getText().toString();
            modele2 = mModele2.getText().toString();
            nom2 = mNom2.getText().toString();
            //prenom2 = mPrenom2.getText().toString();
            permis2 = mPermis2.getText().toString();
            nouveauAncien2 = mNouveauAncien.getTextOn().toString();
        }
        
        String lieu = mLieu.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /*if (TextUtils.isEmpty(mDateInput.getText())) {
            mDateInput.setError(getString(R.string.error_field_required));
            focusView = mDateInput;
            cancel = true;
        }*/

        if (TextUtils.isEmpty(lieu)) {
            mLieu.setError(getString(R.string.error_field_required));
            focusView = mLieu;
            cancel = true;
        }
        
        // -------------------------------------------------  2 ---------------------------------------------
        if(!type.equals("Seul")) {
            if (TextUtils.isEmpty(permis2)) {
                mPermis2.setError(getString(R.string.error_field_required));
                focusView = mPermis2;
                cancel = true;
            }

            /*if (TextUtils.isEmpty(prenom2)) {
                mPrenom2.setError(getString(R.string.error_field_required));
                focusView = mPrenom2;
                cancel = true;
            }*/

            if (TextUtils.isEmpty(nom2)) {
                mNom2.setError(getString(R.string.error_field_required));
                focusView = mNom2;
                cancel = true;
            }

            if (TextUtils.isEmpty(modele2)) {
                mModele2.setError(getString(R.string.error_field_required));
                focusView = mModele2;
                cancel = true;
            }

            if (TextUtils.isEmpty(marque2)) {
                mMarque2.setError(getString(R.string.error_field_required));
                focusView = mMarque2;
                cancel = true;
            }
        }
        // ------------------------------------------  2 ------------------------------------------------------------
        if (TextUtils.isEmpty(permis)) {
            mPermis.setError(getString(R.string.error_field_required));
            focusView = mPermis;
            cancel = true;
        }

        /*if (TextUtils.isEmpty(prenom)) {
            mPrenom.setError(getString(R.string.error_field_required));
            focusView = mPrenom;
            cancel = true;
        }*/

        if (TextUtils.isEmpty(nom)) {
            mNom.setError(getString(R.string.error_field_required));
            focusView = mNom;
            cancel = true;
        }

        if (TextUtils.isEmpty(modele)) {
            mModele.setError(getString(R.string.error_field_required));
            focusView = mModele;
            cancel = true;
        }

        if (TextUtils.isEmpty(marque)) {
            mMarque.setError(getString(R.string.error_field_required));
            focusView = mMarque;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(matricule)) {
            mMatricule.setError(getString(R.string.error_field_required));
            focusView = mMatricule;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return null;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Calendar cal = Calendar.getInstance();
            String[] day_month_year;
            if(mDateInput.getText().toString().contains("/")) {
                day_month_year = mDateInput.getText().toString().split("/");
                cal.set(Integer.valueOf(day_month_year[2]),
                        Integer.valueOf(day_month_year[1]),
                        Integer.valueOf(day_month_year[0]));
            }
            
            MainActivity.vehicules.add(new Vehicule(matricule, nom, permis, nouveauAncien, marque, modele));
            Accident accident = new Accident(MainActivity.vehicules.get(MainActivity.vehicules.size()-1), cal.getTime().toString(), lieu, mInfos.getText().toString(), "En conduite", null, 0.0f, imageNom, videoNom,0);
            if (type.equals("Double")) {
                MainActivity.vehicules.add(new Vehicule(matricule2, nom2, permis2, nouveauAncien2, marque2, modele2));
                accident = new Accident(MainActivity.vehicules.get(MainActivity.vehicules.size() - 2), cal.getTime().toString(), lieu, mInfos.getText().toString(), "En conduite", MainActivity.vehicules.get(MainActivity.vehicules.size() - 1), 0.0f, imageNom, videoNom,0);
            } else if (type.equals("Stationnement")){
                if(mConnuInconnu.isChecked()) {
                    MainActivity.vehicules.add(new Vehicule(matricule2, nom2, permis, nouveauAncien2, marque2, modele2));
                    accident = new Accident(MainActivity.vehicules.get(MainActivity.vehicules.size() - 2), cal.getTime().toString(), lieu, mInfos.getText().toString(), "Stationnement", MainActivity.vehicules.get(MainActivity.vehicules.size() - 1), 0.0f, imageNom, videoNom,0);
                }else {
                    accident = new Accident(MainActivity.vehicules.get(MainActivity.vehicules.size() - 1), cal.getTime().toString(), lieu, mInfos.getText().toString(), "Stationnement", null, 0.0f, imageNom, videoNom,0);
                }
            }

            Intent intent = new Intent(NewAccidentForm.this, UploadService.class);
            if(imageStoragePath!=null)intent.putExtra("uriImage", imageStoragePath.toString());
            if(videoStoragePath!=null)intent.putExtra("uriVideo", videoStoragePath.toString());
            startService(intent);

            //MainActivity.accidents.add(accident);
            sendToFirebase(accident);

            Intent intention = new Intent(this, MainActivity.class);
            intention.putExtra(MESSAGE_SUPP_ADD, getResources().getString(R.string.add_success));
            startActivity(intention);


            return accident;
        }
    }

    protected void sendEmail(Accident accident, String type) {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        String corp = "Bonjour "+accident.getVehicule1().getNomProprietaire()
                    +",\n\nVous avez déclaré une accident de type "+type+" au prés de notre societé :"
                    +"\nLa date : "+accident.getDate()
                    +"\nLe lieu : "+accident.getLieu()
                    +"\n\nInformations détaillées : \n"+accident.getInfos()
                    +"\n\nMerci.";

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Déclaration de l'accident");
        emailIntent.putExtra(Intent.EXTRA_TEXT, corp);
        //emailIntent.putExtra(Intent.ACTION_ATTACH_DATA, accident.getNomImage());
        //emailIntent.putExtra(Intent.ACTION_ATTACH_DATA, accident.getNomVideo());

        try {
            startActivity(Intent.createChooser(emailIntent, "Envoi de mail..."));
            finish();
            Log.i("Envoi de mail terminé", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(NewAccidentForm.this, "Il n'y a aucun client mail installé.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void sendToFirebase(Accident accident){
        final int id = MainActivity.accidents.size();
        MainActivity.firebaseAccident.child(String.valueOf(id)).setValue(accident);
    }

//------------------------------------------ Take Image & Record Video ------------------------------------------------------------



    public File getFilePath(){
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/images");
        if(!folder.exists()) folder.mkdir();
        File image_file;
        if(mLieu.getText().toString().length()!=0) image_file = new File(folder, mLieu.getText().toString() + ".jpg");
        else image_file = new File(folder,generateVoiceFilename(6) + ".jpg");
        return image_file;
    }

    public File getFileVideo(){
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/videos");
        if(!folder.exists()) folder.mkdir();
        File video_file;
        if(mLieu.getText().toString().length()!=0) video_file = new File(folder, mLieu.getText().toString() + ".mp4");
        else video_file = new File(folder,generateVoiceFilename(6) + ".mp4");
        return video_file;
    }

    private String generateVoiceFilename( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private void hasSDCard(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(isSDPresent)        {
            System.out.println("Il y a la Carte SD");
        }
        else{
            System.out.println("Il n'y a pas la Carte SD");
        }
    }

//------------------------------------------ End Record Video ------------------------------------------------------------

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

}