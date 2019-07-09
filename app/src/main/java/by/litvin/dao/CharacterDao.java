package by.litvin.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import by.litvin.model.Character;

@Dao
public interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Character character);

    @Delete
    void delete(Character character);

    @Query("DELETE FROM character_table")
    void deleteAll();

    @Query("SELECT * FROM character_table")
    LiveData<List<Character>> getAll();

}
