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
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataFragment extends Fragment {

    private List<MyObject> cards = new ArrayList<>();
    private NfcAdapter mNfcAdapter = null;

    private String SYSTEMSTR, SENSORSTR, NFCSTR, NETWORKSTR, SCREENSTR, PHONESTR, DEVSTR, CPUSTR;
    private List<String> list;

    //Identifier name of file backup
    public static String DEVICEMODEL = getDeviceModel();

    ListChangeListener mlistener;

    public interface ListChangeListener {
        void onListChange(List<String> strings);
    }

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize
        newcardlist();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyAdapter(cards));

        if (null != mlistener) {
            mlistener.onListChange(list);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void newcardlist() {

        SYSTEMSTR = getSystemInfo();
        SENSORSTR = getSensorsInfo();
        NFCSTR = getNfcInfo();
        SCREENSTR = getDisplayInfo();
        NETWORKSTR = getNetworkInfo();
        PHONESTR = getTelephonieInfo();
        DEVSTR = getDevInfo();
        CPUSTR = getCpuInfo();

        list = new ArrayList<>();
        list.add(SYSTEMSTR);
        list.add(list.size(), SENSORSTR);
        list.add(list.size(), NFCSTR);
        list.add(list.size(), SCREENSTR);
        list.add(list.size(), NETWORKSTR);
        list.add(list.size(), PHONESTR);
        list.add(list.size(), CPUSTR);
        list.add(list.size(), DEVSTR);

        Drawable draw0 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_android_black_24dp);
        Drawable draw1 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_toys_black_24dp);
        Drawable draw2 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_nfc_black_24dp);
        Drawable draw3 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_aspect_ratio_black_24dp);
        Drawable draw4 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_settings_input_antenna_black_24dp);
        Drawable draw5 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_settings_phone_black_24dp);
        Drawable draw6 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_memory);
        Drawable draw7 = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_developement);

        cards.add(new MyObject("System Device", SYSTEMSTR, draw0));
        cards.add(new MyObject("Sensor", SENSORSTR, draw1));
        cards.add(new MyObject("NFC support", NFCSTR, draw2));
        cards.add(new MyObject("Display screen", SCREENSTR, draw3));
        cards.add(new MyObject("Network", NETWORKSTR, draw4));
        cards.add(new MyObject("Telephony", PHONESTR, draw5));
        cards.add(new MyObject("CPU & Memory", CPUSTR, draw6));
        cards.add(new MyObject("Environment", DEVSTR, draw7));

    }

    public String getDevInfo() {

        String str = "";
        try {

            str += "\n Kernel name: " + System.getProperty("os.name");
            str += "\n Kernel version: " + System.getProperty("os.version");
            str += "\n OS arch: " + System.getProperty("os.arch");
            str += "\n Base paths: " + System.getProperty("user.dir");
            str += "\n VM vendor: " + System.getProperty("java.vm.vendor");
            str += "\n VM name: " + System.getProperty("java.vm.name");
            str += "\n VM version: " + System.getProperty("java.vm.version");
            str += "\n VM path: " + System.getProperty("java.home");
            str += "\n VM lib name: " + System.getProperty("java.specification.name");
            str += "\n VM lib version: " + System.getProperty("java.specification.version");
            str += "\n Lib paths: " + System.getProperty("java.library.path");

        } catch (Exception e) {
            str += "No properties found";
        }
        return str;
    }

    public String getSystemInfo() {

        String str = "";
        try {

            str += "\n BOARD: " + android.os.Build.BOARD;
            str += "\n Bootloader: " + android.os.Build.BOOTLOADER;
            str += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
            str += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
            str += "\n Device: " + android.os.Build.DEVICE;
            str += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
            str += "\n ID: " + android.os.Build.ID;
            str += "\n RELEASE: " + android.os.Build.VERSION.RELEASE;
            str += "\n BRAND: " + android.os.Build.BRAND;
            str += "\n RADIO: " + android.os.Build.getRadioVersion();
            str += "\n DISPLAY: " + android.os.Build.DISPLAY;
            str += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
            str += "\n CPU_ABI2: " + android.os.Build.CPU_ABI2;
            str += "\n UNKNOWN: " + android.os.Build.UNKNOWN;
            str += "\n HARDWARE: " + android.os.Build.HARDWARE;
            str += "\n Build ID: " + android.os.Build.ID;
            str += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
            str += "\n SERIAL: " + android.os.Build.SERIAL;
            str += "\n USER: " + android.os.Build.USER;
            str += "\n HOST: " + android.os.Build.HOST;
            str += "\n TYPE: " + android.os.Build.TYPE;
            str += "\n TAGS: " + android.os.Build.TAGS;
        } catch (Exception e) {
            str += "No properties found";
        }
        return str;
    }

    public String getSensorsInfo() {

        String str = "";
        try {
            SensorManager mgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> sensors = mgr.getSensorList(Sensor.TYPE_ALL);

            for (Sensor sensor : sensors) {
                str += "\n" + sensor.getName() + " " + sensor.getVendor();
            }
        } catch (Exception e) {
            str += "Not found sensors";
        }
        return str;
    }

    public String getNetworkInfo() {

        ConnectivityManager cm;
        NetworkInfo ni;
        String str = "";

        try {
            cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            ni = cm.getActiveNetworkInfo();

            //str += "\nType: "+ni.getType();
            str += "\nType: " + ni.getTypeName();
            str += "\nsubType: " + ni.getSubtypeName();
            str += "\nExtraInfo: " + ni.getExtraInfo();
            str += "\nReason: " + ni.getReason();


        } catch (Exception e) {
            str += "\nNo properties found";
        }
        return str;
    }

    public String getTelephonieInfo() {

        String str = "";
        try {
            TelephonyManager telmgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(getActivity().findViewById(R.id.viewpager), "Permission read phone state not granted!", Snackbar.LENGTH_LONG).show();
            }else {
                str += "\nID/IMEI: " + telmgr.getDeviceId() + "\nSVnumber: " + telmgr.getDeviceSoftwareVersion() + "\nISO country: " + telmgr.getNetworkCountryIso()
                        + "\nOperator numeric name: " + telmgr.getNetworkOperator() + "\nOperator: " + telmgr.getNetworkOperatorName() + "\nSIM Operator: " + telmgr.getSimOperator()
                        + "\nSIM operateur name: " + telmgr.getSimOperatorName() + "\nSIM serial number: " + telmgr.getSimSerialNumber() + "\nLigne1 number: " + telmgr.getLine1Number() + "\nSubscriber: " + telmgr.getSubscriberId();
            }
        }catch (Exception e) {
            str += "No properties found";
        }
        return str;
    }

    public String getDisplayInfo(){

        String str = "";
        try{
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            str += "\n"+"Screen pixel size: "+String.valueOf(width)+"x"+String.valueOf(height);

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels/dm.xdpi,2);
            double y = Math.pow(dm.heightPixels/dm.ydpi,2);
            double screenInches = Math.sqrt(x+y);
            str += "\n"+"Screen metrics size: "+String.valueOf(screenInches);
            str += "\n"+"Density level: "+getDensityName(getActivity().getBaseContext());

        }catch (Exception e) {
            str += "No properties found";
        }
        return str;
    }

    private static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public String getNfcInfo(){

        String str = "";
        try{
            mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

            if (mNfcAdapter == null) {
                // Stop here, we definitely need NFC
                str += "\nNot support NFC";
            }

            if (!mNfcAdapter.isEnabled()) {
                str += "\nSupported";
                str += "\n"+"NFC state disable";
            } else {
                str += "\nSupported";
                str += "\n"+"NFC state enable";
            }

        }catch (Exception e) {
            str += "\nNo properties found";
        }

        return str;
    }

    private String getCpuInfo() {
        StringBuffer sb = new StringBuffer();
        //sb.append("abi: ").append(Build.CPU_ABI).append("\n");
        BufferedReader br= null;
        if (new File("/proc/cpuinfo").exists()) {
            try {
                br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String aLine;
                while ((aLine = br.readLine()) != null) {
                    sb.append(aLine + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            sb.append("Cpu info not available\n");
        }

        if (new File("/proc/meminfo").exists()) {
            try {
                sb.append("\n");
                br = new BufferedReader(new FileReader(new File("/proc/meminfo")));
                String aLine;
                while ((aLine = br.readLine()) != null) {
                    sb.append(aLine + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            sb.append("Memory info not available\n");
        }

        return sb.toString();
    }

    public static String getDeviceModel(){
        DEVICEMODEL = android.os.Build.MODEL;
        return DEVICEMODEL;
    }

    public List<String> getMegaString(){
        return list;
    }

    public void setListChangeListener(ListChangeListener listener) {

        mlistener = listener;
    }


}
