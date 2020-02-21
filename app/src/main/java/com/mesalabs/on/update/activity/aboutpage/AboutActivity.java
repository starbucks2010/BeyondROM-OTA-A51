package com.mesalabs.on.update.activity.aboutpage;

import com.mesalabs.cerberus.base.BaseAboutActivity;
import com.mesalabs.on.update.R;

/*
 * On Update
 *
 * Coded by BlackMesa @2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

public class AboutActivity extends BaseAboutActivity {
    @Override
    protected String getAppName() {
        return getString(R.string.mesa_onupdate);
    }

    @Override
    protected boolean getIsAppUpdateable() {
        return true;
    }
}
