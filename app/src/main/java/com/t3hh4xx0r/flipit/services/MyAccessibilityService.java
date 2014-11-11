package com.t3hh4xx0r.flipit.services;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {
    AccessibilityNodeInfo source;
    int currentOrientation = -1;
    String lastPackageName;

    OrientationEventListener mOrientationEventListener;
    FlipNotifierService.ServiceBinder serviceBinder;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof FlipNotifierService.ServiceBinder) {
                serviceBinder = (FlipNotifierService.ServiceBinder) service;
            }
            // No need to keep the service bound.
            unbindService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Nothing to do here.
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        source = event.getSource();
        if (source == null ||
                source.getPackageName() == null) {
            return;
        }
        if (source.getPackageName().toString() != lastPackageName) {
            lastPackageName = source.getPackageName().toString();
        }
        if (findCameraApps().contains(lastPackageName)) {
            if (mOrientationEventListener.canDetectOrientation()) {
                mOrientationEventListener.enable();
                startService(new Intent(this, FlipNotifierService.class));
                bindService(new Intent(this, FlipNotifierService.class),
                        mConnection, 0);
            }
        } else {
            removeView();
            stopService(new Intent(this, FlipNotifierService.class));
            mOrientationEventListener.disable();
        }
    }

    @Override
    public void onInterrupt() {

    }


    public ArrayList<String> findCameraApps() {
        ArrayList<String> result = new ArrayList<String>();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                cameraIntent, 0);

        for (ResolveInfo resolveInfo : list) {
            result.add(resolveInfo.activityInfo.packageName);
        }

        return result;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        mOrientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int i) {
                currentOrientation = i;
                if (currentOrientation > 60 &&
                        currentOrientation < 120) {
                    Log.d("THE NEW ORIENTATION", i + " : " + lastPackageName + " : LANDSCAPE");
                    removeView();
                } else if (currentOrientation > 240 &&
                        currentOrientation < 300) {
                    Log.d("THE NEW ORIENTATION", i + " : " + lastPackageName + " : LANDSCAPE");
                    removeView();
                } else {
                    Log.d("THE NEW ORIENTATION", i + " : " + lastPackageName + " : PORTRAIT");
                    addView();
                }
            }
        };
    }

    public void removeView() {
        if (serviceBinder != null) {
            serviceBinder.remove();
        }
    }

    public void addView() {
        if (serviceBinder != null) {
            serviceBinder.add();
        }
    }
}