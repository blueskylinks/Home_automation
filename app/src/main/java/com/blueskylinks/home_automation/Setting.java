package com.blueskylinks.home_automation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import static com.blueskylinks.home_automation.R.drawable.*;
import static com.blueskylinks.home_automation.R.string.*;

public class Setting extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    public static int navItemIndex =0;
    ImageView view1;
    ImageView view2;
    TextView t1;
    TextView fanSpeed;
    TextView bulb1;
    View view;
    LayoutInflater layoutInflater;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView bulb2;
    LinearLayout root;
    ScrollView root1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int count=0;
    ImageView fan_img[]=new ImageView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        // Toolbar toolbar = findViewById(R.id.toolbar);

        layoutInflater = getLayoutInflater();


        root=findViewById(R.id.root_layout);
        root1=findViewById(R.id.scrollView);
        ActionBar actionbar = getSupportActionBar();
        //Enabling Home button
       // getActionBar().setHomeButtonEnabled(true);
      //  getActionBar().setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
       // actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        t1=findViewById(R.id.Room_name);
        fanSpeed=findViewById(R.id.fan_speed);
        bulb1=findViewById(R.id.bulb1_status);
        bulb2=findViewById(R.id.bulb2_status);

        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int index=sharedPreferences.getInt("index",0);
       if(index==1){ t1.setText("Room1");}
        else if(index==2){ t1.setText("Room2");}
        else if(index==3) {t1.setText("Room3");}
        else if(index==4) {t1.setText("Room4");}
        else return;

        fan_img[0]=findViewById(R.id.fan_img1);
        fan_img[1]=findViewById(R.id.fan_img2);
        fan_img[2]=findViewById(R.id.fan_img3);
        fan_img[3]=findViewById(R.id.fan_img4);
        fan_img[4]=findViewById(R.id.fan_img5);



        navigationView = findViewById(R.id.nav_view);
      navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {
                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.nav_camera:
                                navItemIndex = 1;
                                t1.setText("Room1");
                                mDrawerLayout.closeDrawers();
                                editor.putInt("index",1);
                                editor.commit();
                                break;
                            case R.id.nav_gallery:
                                navItemIndex = 2;
                                t1.setText("Room2");
                                editor.putInt("index",2);
                                editor.commit();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_slideshow:
                                // launch new intent instead of loading fragment
                                // startActivity(new Intent(Setting.this, Room1.class));
                                t1.setText("Room3");
                                editor.putInt("index",3);
                                editor.commit();
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_manage:
                                // launch new intent instead of loading fragment
                                //startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                                t1.setText("Room4");
                                editor.putInt("index",4);
                                editor.commit();
                                mDrawerLayout.closeDrawers();
                                return true;
                            default:
                                navItemIndex = 0;
                        }

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) {
                            menuItem.setChecked(false);
                        } else {

                            menuItem.setChecked(true);
                        }
                        menuItem.setChecked(true);

                        return true;
                    }
                });
    }

    //function for fan speed decreament
  public void decrease_func(View view){
        view1=findViewById(R.id.decrease);
      //view1.setBackgroundColor(Color.parseColor("#f1c40f"));
     if(count>=1){
         count --;
          for(int i=0;i<=count;i++){
              if(count == i){
                  Log.i("count value",String.valueOf(count));
                  fan_img[i].setImageDrawable(null);
                  fanSpeed.setText(String.valueOf(count));
                  fan_img[i].setBackgroundResource(R.mipmap.fan_black);
              }
          }
      }
      else return;
  }

  //function for fan speed increament
    public void increase_func(View view){
      view2=findViewById(R.id.increase);
     // view2.setBackgroundColor(Color.parseColor( "#f1c40f"));
           if(count<5){
               for (int i = 0; i <= count; i++) {
                   if (count == i) {
                       Log.i("count value",String.valueOf(count));
                       fan_img[i].setBackgroundResource(R.mipmap.fan_yellow);
                       fanSpeed.setText(String.valueOf(count+1));
                   }
               }
               count++;
           }
           else return;
    }
    public void add_layout(){

        view = layoutInflater.inflate(R.layout.dynamic_layout, root, false);

        // In order to get the view we have to use the new view with text_layout in it
       LinearLayout layout = (LinearLayout) view.findViewById(R.id.extra_layout);
        // Add the text view to the parent layout
        root.addView(layout);

       /* LinearLayout llay1 = new LinearLayout(this);
//        llay1.getLayoutParams().width=200;
  //      llay1.getLayoutParams().height=0;
        llay1.setVisibility(View.VISIBLE);
        llay1.setWeightSum(2f);
        llay1.setBackgroundResource(R.drawable.border_shadow);
        TextView text=new TextView(this);
        text.setText("Test version");
        llay1.addView(text);
        root.addView(llay1);*/
    }

    //function for bulb1 on/off
    public void bulb1_func(View view){
        bulb1.setText("ON");
       add_layout();
    }

    //function for bulb2 on/off
    public void bulb2_func(View view){
    bulb2.setText("ON");
    }

    public void Home_func(View view){
        //starting another activity..
        Intent it4 = new Intent(Setting.this, MainActivity.class);

        startActivity(it4);
    }
}
