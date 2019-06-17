package by.litvin.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;

import by.litvin.dao.CharacterDao;
import by.litvin.model.Character;
import by.litvin.model.Image;
import by.litvin.model.Link;

@Database(entities = {Character.class}, version = 3)
public abstract class CharacterDatabase extends RoomDatabase {
    private static CharacterDatabase instance;

    public abstract CharacterDao characterDao();

    public static synchronized CharacterDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(context, CharacterDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterDao characterDao;

        public PopulateDbAsyncTask(CharacterDatabase characterDatabase) {
            characterDao = characterDatabase.characterDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Character character = new Character();
            character.setDescription("Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! ");
            character.setId(1017100);
            character.setName("A-Bomb (HAS)");
            Link link1 = new Link("detail", "http://marvel.com/comics/characters/1017100/a-bomb_has?utm_campaign=apiRef&utm_source=858961528d9190d6ef185e668599b761");
            Link link2 = new Link("comiclink", "http://marvel.com/comics/characters/1017100/a-bomb_has?utm_campaign=apiRef&utm_source=858961528d9190d6ef185e668599b761");
            character.setLinks(Arrays.asList(link1, link2));
            character.setThumbnail(new Image("http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));
            characterDao.insert(character);
            return null;
        }
    }


}
