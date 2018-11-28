package com.educa62.backgroundtask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyBoundService extends Service {
    public MyBoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
