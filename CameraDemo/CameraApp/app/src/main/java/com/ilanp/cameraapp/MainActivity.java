package com.ilanp.cameraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

// ===================================================================
// Lesson 7 - Using CAMERA Permission Demo. 03.08.20
// By Ilan Perez (ilanperets@gmail.com)
// ===================================================================
public class MainActivity extends AppCompatActivity
{
        private static final int REQUEST_IMAGE_CAPTURE = 1;
        private FloatingActionButton fab;

        private static int autoPicNum = 0;
        private ArrayList<CamPic> picsList;
        private CamPicsAdapter picsAdapter;
        private ListView listViewCamPics;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            fab = findViewById(R.id.fabID);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    takePicture();
                }
            });


            picsList = new ArrayList<>();

            picsAdapter = new CamPicsAdapter(this, picsList);

            listViewCamPics = findViewById(R.id.listViewCamPicsID);
            listViewCamPics.setAdapter(picsAdapter);
        }

        // get a picture from camera
        public void takePicture()
        {
            // check CAMERA permission
            if(isCameraPermissionGranted())
            {
                // create an intent to call the camera app to take a picture(image)
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // check if camera app exists ?
                //if (intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }

        }

        // Check if CAMERA Permission granted ?
        public boolean isCameraPermissionGranted()
        {
            // check if permission for READ_CONTACTS is granted ?
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                return true;
            else
            {
                // show requestPermissions dialog
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 111);
                return false;
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
            {
                Bitmap picBitmap = (Bitmap) data.getExtras().get("data"); // get picture as thumbnail
                ImageView imgPic = findViewById(R.id.imgPicID);
                imgPic.setImageBitmap(picBitmap);

                Calendar now = Calendar.getInstance();
                autoPicNum++;
                CamPic camPic = new CamPic(picBitmap, "Pic " + autoPicNum, now.getTimeInMillis());
                picsList.add(camPic);
            }
        }
}