package by.litvin.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import by.litvin.dao.CharacterDao;
import by.litvin.database.CharacterDatabase;
import by.litvin.model.Character;

public class CharacterRepository {

    private CharacterDao characterDao;
    private LiveData<List<Character>> characters;

    public CharacterRepository(Application application) {
        CharacterDatabase database = CharacterDatabase.getInstance(application);
        characterDao = database.characterDao();
        characters = characterDao.getAll();
    }

    public LiveData<List<Character>> getCharacters() {
        return characters;
    }

    public void insert(Character character) {
        new InsertAsyncTask(characterDao).execute(character);
    }

    public void delete(Character character) {
        new DeleteAsyncTask(characterDao).execute(character);
    }

    public void deleteAllCharacters() {
        new DeleteAllAsyncTask(characterDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Character, Void, Void> {
        private CharacterDao characterDao;

        public InsertAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Character... characters) {
            characterDao.insert(characters[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Character, Void, Void> {
        private CharacterDao characterDao;

        public DeleteAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Character... characters) {
            characterDao.delete(characters[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterDao characterDao;

        public DeleteAllAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.deleteAll();
            return null;
        }
    }

}
