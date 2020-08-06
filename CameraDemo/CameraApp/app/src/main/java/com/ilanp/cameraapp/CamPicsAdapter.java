package com.ilanp.cameraapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CamPicsAdapter extends ArrayAdapter<CamPic>
{
    public CamPicsAdapter(Activity context, ArrayList<CamPic> camPicsList)
    {
        super(context, 0, camPicsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cam_pic_item, parent, false);

        CamPic camPic = getItem(position);

        // set image bitmap
        ImageView imgPic = convertView.findViewById(R.id.imgPicID);
        imgPic.setImageBitmap(camPic.getBitmap());

        // set pic name
        TextView txvPicName = convertView.findViewById(R.id.txvPicNameID);
        txvPicName.setText(camPic.getName());

        // set pic time
        TextView txvPicTime = convertView.findViewById(R.id.txvPicTimeID);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(camPic.getTime());
        String picDate = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        String picTime = new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
        txvPicTime.setText(picDate + " " + picTime);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return convertView;
    }

}
