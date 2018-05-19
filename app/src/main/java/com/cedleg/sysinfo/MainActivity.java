/*
* Copyright [2018] [Cedric Leguay]
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.cedleg.sysinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 52;
    public static final String FILEPATCH = getFilePatch().toString();

    private DataFragment bfrag;
    private ExportFragment efrag;

    private android.support.v7.app.ActionBar actionBar;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //myToolbar.setTitleTextColor(Color.WHITE);
        actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialize the actionMode callback
        //initializeActionModeCallBack();

        //Check and request authorisation
        if (Build.VERSION.SDK_INT >= 23) checkAndRequestPermissions();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //Get the ViewPager and set it's PagerAdapter so that it can display items
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Give the TabLayout the ViewPager
        //tabLayout.setTabTextColors(R.color.white, ContextCompat.getColor(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        //Initiate fragment and listener
        bfrag = new DataFragment();
        efrag = new ExportFragment();
        bfrag.setListChangeListener(new DataFragment.ListChangeListener() {
            @Override
            public void onListChange(List<String> strings) {
                    efrag.updateStringList(strings);
            }
        });

        efrag.setStringChangeListener(new ExportFragment.StringChangeListener() {
            @Override
            public void onFinalStringChange(String str) {
                        WriteFile(str);
            }

        });

        //Give state color tab textview
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent aboutView = new Intent(this, AboutActivity.class);
            startActivity(aboutView);
            return true;
        }

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /***** CLASS PAGE ADAPTER POUR TABLAYOUT *******/
    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[] { "Infos", "Export", "Email" };
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return bfrag;
                case 1:
                    return efrag;
                case 2:
                    return new EmailFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }

    }
        /************* FIN DE CLASSE ************/

    public static File getFilePatch(){

        Calendar calendrier = Calendar.getInstance();
        int jour = calendrier.get(Calendar.DAY_OF_MONTH);
        int mois = calendrier.get(Calendar.MONTH);
        int annee = calendrier.get(Calendar.YEAR);

        int heure = calendrier.get(Calendar.HOUR_OF_DAY );
        int minute = calendrier.get(Calendar.MINUTE);
        int seconde = calendrier.get(Calendar.SECOND);
        String datestr = Integer.toString(jour)+Integer.toString(mois)+Integer.toString(annee);
        String heurestr = Integer.toString(heure)+Integer.toString(minute)+Integer.toString(seconde);

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "Sysinfo"+"/"+ DataFragment.DEVICEMODEL+"-"+heurestr+datestr+".txt");

        return storageDir;

    }

    public void WriteFile(String data) {

        File filedirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Sysinfo");
        File file = new File(FILEPATCH);
        FileWriter filewriter = null;

        try{

            if (!filedirectory.exists()) {
                filedirectory.mkdirs();
            }

            file.createNewFile();
            filewriter = new FileWriter(file,false);
            filewriter.write(data);
            Snackbar.make(findViewById(R.id.viewpager), "Write in file succesfull ", Snackbar.LENGTH_LONG).show();

        }catch(Exception e) {
            Snackbar.make(findViewById(R.id.viewpager), "Write in file failed ", Snackbar.LENGTH_LONG).show();
        } finally {
            if (filewriter != null) {
                try {
                    filewriter.close();
                } catch (IOException e) {
                    Log.e("GREC", e.getMessage(), e);
                }}}

    }



    /*PERMISSIONS REQUEST AT RUN TIME*/
    private boolean checkAndRequestPermissions() {

        int storage = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int phoneState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (phoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(MainActivity.this, "Accept permission Storage for use Export fonctionality." +"\n"+
                        "Go to Setting/Application/SysInfo/Authorisation", Toast.LENGTH_LONG).show();
            }
            else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE)) {
                Toast.makeText(MainActivity.this, "Accept  permission Phone State for read telephony informations." +"\n"+
                        "Go to Setting/Application/SysInfo/Authorisation", Toast.LENGTH_LONG).show();
            }
            else {
                ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            }

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                for(int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                StringBuilder builder = new StringBuilder();
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission storage Granted, success.");
                    }
                    builder.append("Permission storage granted\n");
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission Storage Denied, please accept to use Export functionality");
                    }
                    builder.append("Permission Storage denied, please accept to use Export functionality\n");
                }

                if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission phone state Granted, success");
                    }
                    builder.append("Permission phone state granted\n");
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission phone state Denied, You cannot read telephony informations.");
                    }
                    builder.append("Permission phone state denied, please accept to use telephony functionality\n");
                }
                Snackbar.make(findViewById(R.id.viewpager), builder, Snackbar.LENGTH_LONG).show();

                break;
            }
        }
    }

}
