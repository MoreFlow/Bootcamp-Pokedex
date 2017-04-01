package pl.temomuko.pokedexbootcampfinal.util;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import pl.temomuko.pokedexbootcampfinal.remote.DatabaseHelper;

public class Utils {

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static DatabaseHelper getHelper(Context context, DatabaseHelper databaseHelper) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
