package id.ac.umy.unires.mh;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    public static String email;

    AlertDialog exitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerlayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");

        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Home()).commit();
            navigationView.setCheckedItem(R.id.homeMenu);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.homeMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Home()).commit();
                break;
            case R.id.presensiMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Presensi()).commit();
                break;
            case R.id.riwayatMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Riwayat()).commit();
                break;
            case R.id.profileMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Profile()).commit();
                break;
            case R.id.aboutMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new About()).commit();
                break;
            case R.id.exitMenu:
                exit();
                break;
        }

        navigationView.setCheckedItem(menuItem.getItemId());
        mDrawerlayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerlayout.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }

    private void exit() {
        exitDialog = new AlertDialog.Builder(MainActivity.this).create();
        exitDialog.setTitle("Keluar");
        exitDialog.setMessage("Anda ingin Keluar ?");
        exitDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                });
        exitDialog.show();
    }
}
