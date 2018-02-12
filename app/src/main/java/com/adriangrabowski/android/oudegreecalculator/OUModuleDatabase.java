package com.adriangrabowski.android.oudegreecalculator;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Adrian on 06/02/2018.
 */

@Database(entities = {OUModule.class}, version = 1)
public abstract class OUModuleDatabase extends RoomDatabase {

    private static final String DB_NAME = "modulesDatabase.db";
    private static volatile OUModuleDatabase instance;

    static synchronized OUModuleDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }

        return instance;
    }

    private static OUModuleDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                OUModuleDatabase.class,
                DB_NAME).build();

    }

    public abstract OUModuleDAO getOUModuleDAO();

}
