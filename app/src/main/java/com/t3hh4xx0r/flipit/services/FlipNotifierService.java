package com.t3hh4xx0r.flipit.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.WindowManager;

import com.t3hh4xx0r.flipit.widgets.FlipNotifierView;

public class FlipNotifierService extends Service {
    FlipNotifierView root;

	@Override
	public void onCreate() {
		super.onCreate();
		root = new FlipNotifierView(this);
	}

	public void addViews() {
		try {
			((WindowManager) getSystemService(Context.WINDOW_SERVICE)).addView(
					root, getLayoutParams());
            root.startRotate();
		} catch (Exception e) {

		}
	}

	private WindowManager.LayoutParams getLayoutParams() {
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0,
				PixelFormat.TRANSLUCENT);

		return layoutParams;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public void removeViews() {
		try {
			((WindowManager) getSystemService(Context.WINDOW_SERVICE))
					.removeView(root);
		} catch (Exception e) {
		}
	}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private ServiceBinder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder {
        public void remove() {
            removeViews();
        }

        public void add() {
            addViews();
        }
    }

}