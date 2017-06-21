package redteam.usuevents.view.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import redteam.usuevents.R;
import redteam.usuevents.view.login.LoginActivity;
import redteam.usuevents.view.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private Toolbar mToolbar;
    private ImageView mFilterButton;
    private ImageView mMapButton;
    private CircleImageView mProfileImage;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BottomNavigationView mBottomNavigationView;

    private List<Fragment> mFragmentList = new ArrayList<>(3);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifySignedInStatus();
        bindViews();
        setEventListeners();
        loadProfileImage();
        buildFragmentList();

        switchFragment(0);
    }

    private void verifySignedInStatus(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // not signed in
            startActivity(LoginActivity.newIntent(MainActivity.this));
            finish();
        }
    }

    private void bindViews() {
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        mFilterButton = (ImageView)findViewById(R.id.toolbar_filter);
        mMapButton = (ImageView)findViewById(R.id.toolbar_map);
        mProfileImage = (CircleImageView)findViewById(R.id.toolbar_profile_image);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.main_swipe_refresh);
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);
    }

    private void setEventListeners(){

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent, ActivityOptionsCompat
                        .makeScaleUpAnimation(findViewById(R.id.bottom_navigation_view), 0, 0,MainActivity.this.getResources().getDisplayMetrics().widthPixels,
                                MainActivity.this.getResources().getDisplayMetrics().heightPixels).toBundle());
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //if an item is already checked, don't reload the fragment
                if(item.isChecked()){
                    return true;
                }

                switch (item.getItemId()){
                    case R.id.bottom_nav_home:
                        switchFragment(0);
                        break;
                    case R.id.bottom_nav_trending:
                        switchFragment(1);
                        break;
                    case R.id.bottom_nav_subscriptions:
                        switchFragment(2);
                        break;
                }

                return true;
            }
        });


    }

    private void loadProfileImage(){
        //profile image code - look at migrating most to viewmodel and create helper for loading images
        Glide.with(this).load(mFirebaseUser.getPhotoUrl()).apply(RequestOptions.fitCenterTransform()).into(mProfileImage);
    }

    private void buildFragmentList() {
        MainHomeFragment mainHomeFragment = MainHomeFragment.getInstance();
        MainTrendingFragment mainTrendingFragment = MainTrendingFragment.getInstance();
        MainSubscriptionsFragment mainSubscriptionsFragment = MainSubscriptionsFragment.getInstance();

        mFragmentList.add(mainHomeFragment);
        mFragmentList.add(mainTrendingFragment);
        mFragmentList.add(mainSubscriptionsFragment);
    }

    private void switchFragment(int pos) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mFragmentList.get(pos))
                .commit();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
