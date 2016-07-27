package fr.myapplication.dc.myapplication;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by chris on 03/05/2016.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService{
    /**
     * When token refresh, start service to get new token
     */
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
