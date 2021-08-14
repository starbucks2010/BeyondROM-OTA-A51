package com.mesalabs.cerberus.root;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.mesalabs.ten.update.utils.LogUtils;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2020
 * Original code by Willi Ye & AndreiLux
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

public class SU {
    private Process mProcess;
    private BufferedWriter mWriter;
    private BufferedReader mReader;
    private final boolean mRoot;
    private final String mTag;
    public boolean closed;
    public boolean denied;
    private boolean firstTry;

    public SU() {
        this(true, null);
    }

    public SU(boolean root, String tag) {
        mRoot = root;
        mTag = tag;
        try {
            if (mTag != null) {
                LogUtils.i(mTag, String.format("%s initialized", root ? "SU" : "SH"));
            }
            firstTry = true;
            mProcess = Runtime.getRuntime().exec(root ? "su" : "sh");
            mWriter = new BufferedWriter(new OutputStreamWriter(mProcess.getOutputStream()));
            mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
        } catch (IOException e) {
            if (mTag != null) {
                LogUtils.e(mTag, root ? "Failed to run shell as su" : "Failed to run shell as sh");
            }
            denied = true;
            closed = true;
        }
    }

    public synchronized String runCommand(final String command) {
        synchronized (this) {
            try {
                StringBuilder sb = new StringBuilder();
                String callback = "/shellCallback/";
                mWriter.write(command + "\necho " + callback + "\n");
                mWriter.flush();

                int i;
                char[] buffer = new char[256];
                while (true) {
                    sb.append(buffer, 0, mReader.read(buffer));
                    if ((i = sb.indexOf(callback)) > -1) {
                        sb.delete(i, i + callback.length());
                        break;
                    }
                }
                firstTry = false;
                if (mTag != null) {
                    LogUtils.i(mTag, "run: " + command + " output: " + sb.toString().trim());
                }

                return sb.toString().trim();
            } catch (IOException e) {
                closed = true;
                e.printStackTrace();
                if (firstTry) denied = true;
            } catch (ArrayIndexOutOfBoundsException e) {
                denied = true;
            } catch (Exception e) {
                e.printStackTrace();
                denied = true;
            }
            return null;
        }
    }

    public void close() {
        try {
            if (mWriter != null) {
                mWriter.write("exit\n");
                mWriter.flush();

                mWriter.close();
            }
            if (mReader != null) {
                mReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mProcess != null) {
            try {
                mProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mProcess.destroy();
            if (mTag != null) {
                LogUtils.i(mTag, String.format("%s closed: %d", mRoot ? "SU" : "SH", mProcess.exitValue()));
            }
        }
        closed = true;
    }

}
