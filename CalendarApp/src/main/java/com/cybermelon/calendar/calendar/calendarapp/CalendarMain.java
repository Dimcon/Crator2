package com.cybermelon.calendar.calendar.calendarapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class CalendarMain extends Activity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    /**
     */
    private CharSequence mTitle;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);
        mTitle = getTitle();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        // Replace with Screen width * 0.72f \/
        opts.outHeight = 240;
        opts.outWidth = 480;
        opts.inSampleSize = 4;
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.screenshot,opts);
        Drawable db = getResources().getDrawable( R.drawable.default123 );
        Canvas canv = new Canvas(bMap);
        Paint p = new Paint();
        p.setARGB(80,255,255,255);
        canv.drawRect(new Rect(0,0,bMap.getWidth(),bMap.getHeight()),p);
        bMap = blur(getApplicationContext(),bMap,2f);
        RelativeLayout imv2 = (RelativeLayout)findViewById(R.id.profile_bkg);
        imv2.setBackground(new BitmapDrawable(getResources(), bMap));
        ImageView imv = (ImageView)findViewById(R.id.profielview);
        imv.setBackground(db);

        List<RelativeLayout> Buttons = new ArrayList<RelativeLayout>();
        Buttons.add((RelativeLayout) findViewById(R.id.contactslayout));
        Buttons.add((RelativeLayout) findViewById(R.id.calendarlayout));
        Buttons.add((RelativeLayout) findViewById(R.id.memolayout));
        Buttons.add((RelativeLayout) findViewById(R.id.taskslayout));
        for (RelativeLayout r: Buttons) {
            r.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundColor(Color.argb(255,150,150,150));
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(Color.argb(255,100,100,100));
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            v.setBackgroundColor(Color.argb(0,0,0,0));
                            break;
                    }
                    return false;
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Bitmap blur(Context ctx, Bitmap image,float fRadius) {
        //  Ok don't ask me how this works :P
        //  Android renderscript is a beast :D
        float BITMAP_SCALE = 0.01f;
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(fRadius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        inputBitmap = null;
        return outputBitmap;
    }

}


