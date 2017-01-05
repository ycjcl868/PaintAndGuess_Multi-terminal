package com.self.ylog.letspantandguess.Activity;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.self.ylog.letspantandguess.R;
import com.self.ylog.letspantandguess.View.PaintView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Set_SubActionButton();
    }

    public void Set_SubActionButton(){
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.mipmap.ic_launcher);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView item_clean = new ImageView(this);
        icon.setImageResource(R.mipmap.ic_launcher);
        SubActionButton button_clean = itemBuilder.setContentView(item_clean).build();
        button_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("menu","clean bitmap");
            }
        });

        ImageView item_eraser = new ImageView(this);
        icon.setImageResource(R.drawable.eraser);
        SubActionButton button_eraser = itemBuilder.setContentView(item_eraser).build();
        button_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaintView.IsPaint=false;
                Log.i("menu","choose eraser");
            }
        });

        ImageView item_paint = new ImageView(this);
        icon.setImageResource(R.drawable.pen);
        SubActionButton button_paint = itemBuilder.setContentView(item_paint).build();
        button_paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaintView.IsPaint=true;
                Log.i("menu","choose paint");
            }
        });

        ImageView item_style = new ImageView(this);
        icon.setImageResource(R.mipmap.ic_launcher);
        SubActionButton button_style = itemBuilder.setContentView(item_style).build();
        button_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("menu","set paint style");
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button_style)
                .addSubActionView(button_clean)
                .addSubActionView(button_eraser)
                .addSubActionView(button_paint)
                .attachTo(actionButton)
                .build();
    }

    @Override
    public void onClick(View v) {

    }
}
