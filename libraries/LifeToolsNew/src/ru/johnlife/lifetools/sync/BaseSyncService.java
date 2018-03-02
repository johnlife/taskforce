package ru.johnlife.lifetools.sync;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.johnlife.lifetools.ClassConstantsProvider;
import ru.johnlife.lifetools.service.BaseBackgroundService;

/**
 * USAGE: 
 * 	/AndroidManifest.xml	
 
    <uses-permission android:name="android.permission.READ_SYNC_SETTINGS" />
	<uses-permission android:name="android.permission.WRITE_SYNC_SETTINGS" />
	<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS" />

 	    <service android:name="ru.johnlife.lifetools.sync.FakeAuthenticatorService" >
	        <intent-filter>
	            <action android:name="android.accounts.AccountAuthenticator" />
	        </intent-filter>
	
	        <meta-data
	            android:name="android.accounts.AccountAuthenticator"
	            android:resource="@xml/authenticator" />
	    </service>
		<service
		    android:name="<T extends ru.johnlife.lifetools.sync.BaseSyncService>"
		    android:exported="true"
		    android:process=":sync" >
		    <intent-filter>
		        <action android:name="android.content.SyncAdapter" />
		    </intent-filter>
		
		    <meta-data
		        android:name="android.content.SyncAdapter"
		        android:resource="@xml/syncadapter" />
		</service>
   		<provider
            android:name="ru.johnlife.lifetools.sync.FakeProvider"
            android:authorities="<package>.sync.provider"
            android:exported="false"
            android:syncable="true"
            android:label="Whatever" />


 * /odesk/res/xml/authenticator.xml
		<account-authenticator
	        xmlns:android="http://schemas.android.com/apk/res/android"
	        android:accountType="<account.type>"
	        android:icon="@drawable/ic_launcher"
	        android:smallIcon="@drawable/ic_launcher"
	        android:label="@string/app_name"/>

 * /odesk/res/xml/syncadapter.xml
		<sync-adapter
	        xmlns:android="http://schemas.android.com/apk/res/android"
	        android:contentAuthority="<package>.sync.provider"
	        android:accountType="<account.type>"
	        android:userVisible="true"
	        android:supportsUploading="false"
	        android:allowParallelSyncs="false"
	        android:isAlwaysSyncable="true"/>
        
*/
public abstract class BaseSyncService extends Service {
	
	protected static abstract class BaseSyncAdapter extends AbstractThreadedSyncAdapter {

		public BaseSyncAdapter(Context context, boolean autoInitialize) {
			this(context, autoInitialize, false);
		}

		public BaseSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
			super(context, autoInitialize, allowParallelSyncs);
		}

		@Override
		public abstract void onPerformSync(
			Account account, 
			Bundle extras, 
			String authority, 
			ContentProviderClient provider, 
			SyncResult syncResult
		);

	}

    private static BaseSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();
	private final List<BaseBackgroundService.Requester<? extends BaseBackgroundService>> listeners = new ArrayList<BaseBackgroundService.Requester<?>>();
	private Intent serviceIntent;
	private boolean unbound = true;

	private BaseBackgroundService service;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder binder) {
			Log.d(BaseSyncService.this.getClass().getSimpleName(), "Service connected");
			service = ((BaseBackgroundService.ServiceBinder) binder).getService();
			BaseSyncService.this.onServiceConnected(service);
			unbound = false;
		}

		public void onServiceDisconnected(ComponentName name) {
			Log.d(BaseSyncService.this.getClass().getSimpleName(), "Service disconnected");
			unbound = true;
		}
	};

	protected abstract ClassConstantsProvider getClassConstants();

	@Override
    public void onCreate() {
		serviceIntent = getClassConstants().getBackgroundServiceIntent(this);
		requestServiceConnection();
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = createSyncAdapter(getApplicationContext());
            }
        }
    }

	@Override
	public void onDestroy() {
		if (!unbound) {
			unbindService(serviceConnection);
		}
		super.onDestroy();
	}

	public boolean isServiceConnected() {
		return !(unbound || service == null);
	}

	protected abstract BaseSyncAdapter createSyncAdapter(Context context);

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

	private void requestServiceConnection() {
		if (unbound) {
			bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void onServiceConnected(BaseBackgroundService service) {
		Log.d(getClass().getSimpleName(), "Requesting listeners");
		synchronized (listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				BaseBackgroundService.Requester listener = listeners.get(i);
				Log.d(getClass().getSimpleName(), "Requesting " + listener);
				listener.requestService(service);
			}
			listeners.clear();
			Log.d(getClass().getSimpleName(), "Done requesting listeners");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void requestService(BaseBackgroundService.Requester requester) {
		if (isServiceConnected()) {
			Log.d(getClass().getSimpleName(), "Service already connected. Requesting "+requester);
			requester.requestService(service);
		} else {
			synchronized (listeners) {
				Log.d(getClass().getSimpleName(), "Service not connected. Queueing "+requester);
				listeners.add(requester);
			}
		}
	}

}
