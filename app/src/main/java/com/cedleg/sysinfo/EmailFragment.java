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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmailFragment extends Fragment {

	private AppCompatEditText objet, deliver;
	private AppCompatTextView infoview, fileview;
	private AppCompatButton send;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_email, container, false);

		  infoview = (AppCompatTextView) rootView.findViewById(R.id.infomailview);
		  infoview.setText(Html.fromHtml(getString(R.string.infomailview)));

		  deliver = (AppCompatEditText) rootView.findViewById(R.id.edit_to);

		  objet = (AppCompatEditText) rootView.findViewById(R.id.edit_objet);
		  
		  fileview = (AppCompatTextView) rootView.findViewById(R.id.dirfilemail);
		  fileview.setText(Html.fromHtml(getString(R.string.dirfilemail)));
		  fileview.setText(MainActivity.FILEPATCH);
		  
		  send = (AppCompatButton) rootView.findViewById(R.id.button_emailGo);
		  send.setOnClickListener(new OnClickListener(){
			   public void onClick(View v) {
				   
				   //INTENT IMPLICITE APPEL D'UN CLIENT MAIL + EXTRA
				   String to = deliver.getText().toString();
				   String subject = objet.getText().toString();
				   String message = fileview.getText().toString(); 
				   
				   Intent email = new Intent(Intent.ACTION_SEND);
				   email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
				   //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                   //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                   email.putExtra(Intent.EXTRA_SUBJECT, subject);
                   email.putExtra(Intent.EXTRA_TEXT, message);
                   email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + MainActivity.FILEPATCH));
		 
                   //need this to prompts email client only
                   email.setType("message/rfc822");
		 
                   startActivity(Intent.createChooser(email, "Choose client email :"));

				   //OPTIONAL INTENT TO GO MAIN PAGE
				   /*
					Intent mainActivity = new Intent();
					mainActivity.putExtra(MainActivity.EMAILSEND_OK, "YES");
					setResult(RESULT_OK, mainActivity);
					//startActivity(mainActivity);
					finish();
					*/
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
}
