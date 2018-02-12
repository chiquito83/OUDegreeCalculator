package com.adriangrabowski.android.oudegreecalculator;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Adrian on 06/02/2018.
 */
@Dao
public interface OUModuleDAO {

    @Query("SELECT * FROM OUmodule")
    List<OUModule> getAllModules();

    @Query("DELETE FROM Oumodule")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OUModule... modules);

    @Update
    void update(OUModule... modules);

    @Delete
    void delete(OUModule... modules);

}
