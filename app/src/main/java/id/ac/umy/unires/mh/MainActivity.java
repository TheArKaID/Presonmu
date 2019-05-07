package id.ac.umy.unires.mh;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new home()).commit();
            navigationView.setCheckedItem(R.id.homeMenu);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.homeMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new home()).commit();
                break;
            case R.id.presensiMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new presensi()).commit();
                break;
            case R.id.riwayatMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new riwayat()).commit();
                break;
            case  R.id.profileMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new profile()).commit();
                break;
            case R.id.aboutMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new about()).commit();
                break;
            case  R.id.exitMenu:
                finishAndRemoveTask();
                break;
        }

        navigationView.setCheckedItem(menuItem.getItemId());
        mDrawerlayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerlayout.isDrawerOpen(GravityCompat.START)){
            mDrawerlayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}
