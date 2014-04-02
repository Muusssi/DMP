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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.text.DateFormatSymbols;
import java.util.Calendar;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    private static final String TAG = "MainActivity";
    private ViewPager pager;
    private TabAdapter tabAdapter;
    private ActionBar actionBar;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(5);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        actionBar = getActionBar();

        pager.setAdapter(tabAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create tabs
        DateFormatSymbols symbols = new DateFormatSymbols(); // Locale can be supplied as a parameter
        String[] weekdays = symbols.getShortWeekdays();
        // Array starts from 1 and we also want to skip Sunday which is the first day of the week.
        // Therefore we start iterating from 2 (i.e. Monday).
        for (int i = 2; i < 7; i++) {
            actionBar.addTab(actionBar.newTab().setText(weekdays[i])
                    .setTabListener(this));
        }

        // Select tab for current weekday
        Log.d(TAG, "Selecting tab");
        if (savedInstanceState != null) {  // If current day is Saturday or Sunday set day to Monday instead
            Integer selectedTab = savedInstanceState.getInt("tab", 0);
            pager.setCurrentItem(selectedTab);
            actionBar.setSelectedNavigationItem(selectedTab);
        } else {
            Calendar c = Calendar.getInstance();
            Integer selectedTab = c.get(Calendar.DAY_OF_WEEK) - 2;    // Again offset
            if (selectedTab > 4) {
                selectedTab = 0;
            }
            pager.setCurrentItem(selectedTab);
            actionBar.setSelectedNavigationItem(selectedTab);
        }

        /*
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                pager.setCurrentItem(0);
                actionBar.setSelectedNavigationItem(0);
                break;
            case Calendar.TUESDAY:
                pager.setCurrentItem(1);
                actionBar.setSelectedNavigationItem(1);
                break;
            case Calendar.WEDNESDAY:
                pager.setCurrentItem(2);
                actionBar.setSelectedNavigationItem(2);
                break;
            case Calendar.THURSDAY:
                pager.setCurrentItem(3);
                actionBar.setSelectedNavigationItem(3);
                break;
            case Calendar.FRIDAY:
                pager.setCurrentItem(4);
                actionBar.setSelectedNavigationItem(4);
            default:
                pager.setCurrentItem(0);
                actionBar.setSelectedNavigationItem(0);
        }
        */

        // This enables tab swiping
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        DataProvider.init(this);
        // DataProvider.refresh(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
        Log.d(TAG, "Saved tab " + getActionBar().getSelectedNavigationIndex());
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Starting");
        DataProvider.refresh();
        tabAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Log.d(TAG, "Resuming");
        // DataProvider.refresh(this);
    }

    // Create action bar overflow menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    // Handle menu selections
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

    // These enable tab selection
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

