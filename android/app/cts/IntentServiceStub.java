/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.app.cts;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.animation.cts.DelayedCheck;

public class IntentServiceStub extends IntentService {
    public IntentServiceStub() {
        super("IntentServiceStub");
    }

    public static String ISS_ADD = "add";
    public static String ISS_VALUE = "value";

    public static int onHandleIntentCalled;
    public static boolean onBindCalled;
    public static boolean onCreateCalled;
    public static boolean onDestroyCalled;
    public static boolean onStartCalled;
    public static int accumulator;

    private static Throwable throwable;

    public static void reset() {
        onHandleIntentCalled = 0;
        onBindCalled = false;
        onCreateCalled = false;
        onDestroyCalled = false;
        onStartCalled = false;
        accumulator = 0;
        throwable = null;
    }

    public static void waitToFinish(long timeout) throws Throwable {
        new DelayedCheck(timeout) {
            @Override
            protected boolean check() {
                return IntentServiceStub.onDestroyCalled;
            }
        }.run();
        if (throwable != null) {
            throw throwable;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        onHandleIntentCalled += 1;
        try {
            String action = intent.getAction();
            if (action != null && action.equals(ISS_ADD)) {
                accumulator += intent.getIntExtra(ISS_VALUE, 0);
            }
        } catch (Throwable t) {
            throwable = t;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        onBindCalled = true;
        return new Binder();
    }

    @Override
    public void onCreate() {
        onCreateCalled = true;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        onDestroyCalled = true;
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        onStartCalled = true;
        super.onStart(intent, startId);
    }

}
