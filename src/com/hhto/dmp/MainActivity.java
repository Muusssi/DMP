package com.hhto.dmp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.text.DateFormatSymbols;
import java.util.Locale;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    private ViewPager pager;
    private TabAdapter tabAdapter;
    private ActionBar actionBar;
    private SharedPreferences sharedPrefs;
    // private DataProvider dataProvider;
    private String[] tabs = new String[5]; // = {"Mon", "Tue", "Wed", "Thu", "Fri"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        actionBar = getActionBar();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        pager.setAdapter(tabAdapter);
        // actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);    // _TABS or _LIST
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        DataProvider.init(this);
        DataProvider.refresh(sharedPrefs);

        DateFormatSymbols symbols = new DateFormatSymbols(new Locale("fi")); // Locale can be supplied as a parameter
        String[] weekdays = symbols.getShortWeekdays();
        for (int i = 2; i < 7; i++) {   // Array start from 1 and we also want to skip Sunday
            tabs[i-2] = weekdays[i];
            actionBar.addTab(actionBar.newTab().setText(weekdays[i])
                    .setTabListener(this));
        }

        /**
         * This enables tab swiping.
         */
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }

    /**
     * Create action bar menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * Handle menu selections.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Launch settings activity
            case R.id.abm_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            // Create about dialog
            case R.id.abm_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setIcon(R.drawable.ic_action_about);
                builder.setTitle(R.string.about_title);
                builder.setMessage(R.string.about_text);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * These enable tab selecting.
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }



}

