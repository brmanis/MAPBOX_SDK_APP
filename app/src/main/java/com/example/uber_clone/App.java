package com.example.uber_clone;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.app_id))
                // if defined
                .clientKey(getString(R.string.client_key))
                .server(getString(R.string.server_url))
                .build()
        );
    }
}