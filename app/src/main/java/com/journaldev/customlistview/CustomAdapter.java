package com.journaldev.customlistview;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapter extends ArrayAdapter<Accident> implements View.OnClickListener{

    private ArrayList<Accident> accidents;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        ImageView info;
    }



    public CustomAdapter(ArrayList<Accident> data, Context context) {
        super(context, R.layout.row_item, data);
        this.accidents = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Accident accidentModele=(Accident)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Infos :\n" +accidentModele.getInfos(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Accident accidentModele = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(accidentModele.getVehicule1().getNomProprietaire());
        viewHolder.txtType.setText(String.valueOf(accidentModele.getMantant()));
        viewHolder.txtVersion.setText(accidentModele.getLieu());

        // charger l'image à droite de l'item
        if(accidentModele.getNomImage()==null) viewHolder.info.setImageResource(R.drawable.default_accident);
        else{
            // Reference to an image file in Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("AllImages").child(accidentModele.getNomImage());
            // Load the image using Glide
            Glide.with(getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(viewHolder.info);
            //mImage.setImageURI(MainActivity.defaultImage);
        }

        /*viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.firebaseAccident.deleteSouvenir(articles.get(position));
                MainActivity.adapter = new CustomAdapter(MainActivity.db.getAllSouvenirs(), getContext());
                MainActivity.listView.setAdapter(MainActivity.adapter);
            }
        });*/

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
