package com.example.sns_project2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sns_project2.fragment.Alarm;
import com.example.sns_project2.fragment.Hot;
import com.example.sns_project2.fragment.Main;
import com.example.sns_project2.fragment.Profile;
import com.example.sns_project2.fragment.Userlist;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements Main.OnFragmentInteractionListener, Hot.OnFragmentInteractionListener, Alarm.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener, Userlist.OnFragmentInteractionListener {

    private final static String TAG="MainActivity";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    static int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user == null) {
            myStartActivity(SignUpActivity.class);
        }


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Main"));
        tabLayout.addTab(tabLayout.newTab().setText("Hot"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Alarm"));
        tabLayout.addTab(tabLayout.newTab().setText("Userlist"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00008B"));
       // tabLayout.setTabTextColors(getResources().getColor(R.color.blue));
        tabLayout.setTabTextColors(Color.parseColor("#00008B"),Color.parseColor("#00008B"));

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        ImageView bb = (ImageView) findViewById(R.id.option);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.getMenuInflater().inflate(R.menu.otion, popup.getMenu());
                popup.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.menu1 :
                                        startSignUpActivity();
                                        startToast("로그아웃에 성공하였습니다.");
                                        FirebaseAuth.getInstance().signOut();
                                        break;

                                    case R.id.menu2 :
                                        myStartActivity(googlemap.class);
                                        break;

                                    case R.id.menu3 :
                                        break;

                                    case R.id.menu4 :
                                        myStartActivity(HelpActivity.class);
                                        break;

                                    case R.id.menu5 :
                                        break;
                                }
                                return true;
                            }
                        }
                );
                popup.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.otion,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    //로그아웃 시 로그인창으로..
    private void startSignUpActivity() {
        Intent Intent = new Intent(this, LoginActivity.class);
        startActivity(Intent);
    }



    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

