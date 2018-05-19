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

import android.graphics.drawable.Drawable;

public class MyObject {
    private String cardtitle;
    private String text;
    private Drawable draw;

    public MyObject(String cardtitle, String text, Drawable draw) {
        this.cardtitle = cardtitle;
        this.text = text;
        this.draw = draw;
    }

    public String getText(){return this.text;}
    public String getCardtitle(){return this.cardtitle;}
    public Drawable getDraw(){return this.draw;}
}
