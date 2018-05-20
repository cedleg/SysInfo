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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ExportFragment extends Fragment {

    AppCompatCheckBox sys, sensor, nfc, screen, network, phone, cpu, environment;
    AppCompatTextView infoview, text;
    AppCompatButton boutonSave;

    String finalstr = "Android System Informations\nExport:";
    private List<String> liststr;

    StringChangeListener mlistener;

    public interface StringChangeListener {
        void onFinalStringChange(String str);
    }

    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_export, container, false);

        sys = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_sys);
        sensor = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_sensor);
        nfc = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_nfc);
        screen = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_screen);
        network = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_network);
        phone = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_phone);
        cpu = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_cpumem);
        environment = (AppCompatCheckBox) rootView.findViewById(R.id.checkbox_environment);

        infoview = (AppCompatTextView) rootView.findViewById(R.id.infosaveview);
        infoview.setText(Html.fromHtml(getString(R.string.infosaveview)));
        text = (AppCompatTextView) rootView.findViewById(R.id.directoryview);
        text.setText(Html.fromHtml(getString(R.string.dir)));
        text.setText(MainActivity.getFilePatch().toString());

        boutonSave = (AppCompatButton) rootView.findViewById(R.id.buttonSave);

        boutonSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                int storagePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (storagePermission != PackageManager.PERMISSION_GRANTED || !Environment.getExternalStorageDirectory().canWrite()) {
                    Snackbar.make(getActivity().findViewById(R.id.viewpager), "Permission storage not granted!", Snackbar.LENGTH_LONG).show();
                    RestartAppAlert();

                }else {
                    finalstr += CheckElementsForExport();
                    if (null != mlistener && finalstr != null) {
                        mlistener.onFinalStringChange(finalstr);
                    }
                }
            }
        });

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private String CheckElementsForExport() {
        String strForExport = MainActivity.FILEPATCH+"\n\n";

        if(sys.isChecked()){
            strForExport += "System Device:"+"\n"+liststr.get(0)+"\n\n";
        }
        if(sensor.isChecked()){
            strForExport += "Sensors:"+"\n"+liststr.get(1)+"\n\n";
        }
        if(nfc.isChecked()){
            strForExport += "NFC support:"+"\n"+liststr.get(2)+"\n\n";
        }
        if(screen.isChecked()){
            strForExport += "Display:"+"\n"+liststr.get(3)+"\n\n";
        }
        if(network.isChecked()){
            strForExport += "Network:"+"\n"+liststr.get(4)+"\n\n";
        }
        if(phone.isChecked()){
            strForExport += "Telephony:"+"\n"+liststr.get(5)+"\n\n";
        }
        if(cpu.isChecked()){
            strForExport += "Cpu and Memory:"+"\n"+liststr.get(6)+"\n\n";
        }
        if(environment.isChecked()){
            strForExport += "Environment:"+"\n"+liststr.get(7)+"\n\n";
        }

        return strForExport;
    }

    private void RestartAppAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setTitle("Information");
        alertDialogBuilder.setMessage("App need firstly restart to get writing on external storage\n Auto-restart app on OK button");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, close current activity
                PendingIntent pi = PendingIntent.getActivities(getActivity().getApplicationContext(), 0, new Intent[]{getActivity().getIntent()}, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC, System.currentTimeMillis() + 0, pi);
                //Stop now
                System.exit(0);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void updateStringList(List<String> list) {
        liststr = list;
    }

    public void setStringChangeListener(StringChangeListener listener) {
        mlistener = listener;
    }
}
