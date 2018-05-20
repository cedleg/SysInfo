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

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder{

    private AppCompatTextView cardTitleView, textViewView;
    private AppCompatImageView imageView;

    //Item representation
    public MyViewHolder(View itemView) {
        super(itemView);
        cardTitleView = (AppCompatTextView) itemView.findViewById(R.id.cardtitle);
        textViewView = (AppCompatTextView) itemView.findViewById(R.id.text);
        imageView = (AppCompatImageView) itemView.findViewById(R.id.image);
    }

    //bind data from MyObject
    public void bind(MyObject myObject){
        cardTitleView.setText(myObject.getCardtitle());
        textViewView.setText(myObject.getText());

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            imageView.setBackgroundDrawable(myObject.getDraw());
        } else {
            imageView.setBackground(myObject.getDraw());
        }

    }
}