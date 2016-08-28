package com.aorbegozo005.dbizi;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EstazioProvider{

    private DBiziDataProvider dataProvider;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        createNavigationDrawer();
        dataProvider = new DBiziDataProvider();
    }

    private void createNavigationDrawer(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataProvider.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataProvider.onStop();
    }

    @Override
    public void addEstazioListener(DBiziDataListener listener) {
        dataProvider.addEstazioListener(listener);
    }

    @Override
    public void removeEstazioListener(DBiziDataListener listener) {
        dataProvider.removeEstazioListener(listener);
    }

    @Override
    public ArrayList<Estazioa> getEstazioak() {
        return dataProvider.getEstazioak();
    }

    @Override
    public Estazioa getSelected() {
        return dataProvider.getSelected();
    }

    @Override
    public void setSelected(Estazioa estazioa) {
        dataProvider.setSelected(estazioa);
    }

    @Override
    public void onEstazioakLoaded(ArrayList<Estazioa> estazioak) {
        dataProvider.onEstazioakLoaded(estazioak);
    }

    @Override
    public void onEstazioaSelected(Estazioa previous, Estazioa selected) {
        dataProvider.onEstazioaSelected(previous, selected);
    }


}
