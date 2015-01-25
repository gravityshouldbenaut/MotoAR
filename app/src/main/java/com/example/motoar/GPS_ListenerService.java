package com.example.motoar;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GPS_ListenerService extends Service {
	private static final String TAG = "GPS_ListenerService";
	//ddprivate FileWriter write;
	private LocationManager locationManager;
	private LocationListener gpsLocationListener;
	private float lastSpeed;
	private final float metersSec_in_MPH = 2.23694f;



	// speed is reported in meters/second
	// speed needs three digits
	// 145
	public String getSpeed() {
		if (lastSpeed < 1.0f) { return "000"; }
		float mph = lastSpeed * metersSec_in_MPH;
		String lValue = Integer.toString((int) mph);
		if (lValue.length() == 1) { return "  " + lValue; }
		if (lValue.length() == 2) { return " " + lValue; }
		return lValue;
	}

	// setup this service to allow binding for access to public methods above.
	// http://developer.android.com/guide/components/bound-services.html
	private final IBinder mBinder = new GPSBinder();

	public class GPSBinder extends Binder {
		GPS_ListenerService getService() {
			return GPS_ListenerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// the usual 'Service' methods below
	@Override
	public void onCreate() {
		super.onCreate();
		//write = FileWriter.getInstance();
		// instantiate the inner class
		// get the system manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// and demand Speed values
		Criteria criteria = new Criteria();
		criteria.setSpeedRequired(true);
		//criteria.setSpeedAccuracy(Criteria.ACCURACY_FINE); // Not supported in Android 2.1 !!
		// register the listener
		locationManager.requestLocationUpdates(
				locationManager.getBestProvider(criteria, false), 250, 
				5, gpsLocationListener);
		//write.syslog(TAG + " GPS updates requested.");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(gpsLocationListener);
	}


}
