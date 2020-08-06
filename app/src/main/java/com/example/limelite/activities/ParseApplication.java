package com.example.limelite.activities;

import android.app.Application;

import com.example.limelite.models.Link;
import com.example.limelite.models.Relationships;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONArray;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();



        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // Register Parse models
        ParseObject.registerSubclass(Link.class);
        ParseObject.registerSubclass(Relationships.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("bmfc_limelite") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("https://limeliteparse.herokuapp.com/parse").build());

        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(this);


        // New test creation of object below
        // For testing purposes. If this object is not in the db, Parse is not configured correctly
        // Debugging hint if object does not show up: CHECK to see if server urls used in initializing
        // the server for the dashboard and in the app are "https" not "http"
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        JSONArray array = new JSONArray();
//        Relationships r = new Relationships();
//        r.setStatus(0);
//        r.setRequestor(ParseUser.getCurrentUser());
//        r.setRequestee(ParseUser.getCurrentUser());
//        array.put(r);
//        testObject.put("goo", array);
//        testObject.saveInBackground();
    }
}
