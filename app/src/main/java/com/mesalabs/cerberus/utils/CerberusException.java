package com.mesalabs.cerberus.utils;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * ULTRA-MEGA-PRIVATE SOURCE CODE. SHARING TO DEVKINGS TEAM
 * EXTERNALS IS PROHIBITED AND WILL BE PUNISHED WITH ANAL ABUSE.
 */

public class CerberusException extends RuntimeException {
    public CerberusException() {
        super();
    }

    public CerberusException(String message) {
        super(message);
    }

    public CerberusException(String message, Throwable tr) {
        super(message, tr);
    }

    public CerberusException(Throwable tr) {
        super(tr);
    }
}
