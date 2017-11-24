package webpract.com.myapplication;

import android.app.Application;

import webpract.com.myapplication.database.Database;

/**
 * Created by PCS76 on 11/24/2017.
 */

public class AppDelegate extends Application {

    public static Database database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = new Database(getApplicationContext());
        database = database.OpenDatabase();
    }
}
