package com.ilanp.cameraapp;

import android.graphics.Bitmap;

public class CamPic
{
   private Bitmap bitmap;
   private String name;
   private Long time;

   public CamPic(Bitmap bitmap, String name, Long time)
   {
      this.bitmap = bitmap;
      this.name = name;
      this.time = time;
   }

   public Bitmap getBitmap()
   {
      return bitmap;
   }

   public void setBitmap(Bitmap bitmap)
   {
      this.bitmap = bitmap;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Long getTime()
   {
      return time;
   }

   public void setTime(Long time)
   {
      this.time = time;
   }
}
