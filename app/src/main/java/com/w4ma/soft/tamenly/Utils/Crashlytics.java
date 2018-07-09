package com.w4ma.soft.tamenly.Utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class Crashlytics extends Application  {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new com.crashlytics.android.Crashlytics());

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new com.crashlytics.android.Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("data.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }



}
