package by.litvin.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import by.litvin.model.Character;
import by.litvin.repository.CharacterRepository;

public class CharacterViewModel extends AndroidViewModel {

    private CharacterRepository characterRepository;
    private LiveData<List<Character>> allCharacters;

    public CharacterViewModel(@NonNull Application application) {
        super(application);
        characterRepository = new CharacterRepository(application);
        allCharacters = characterRepository.getCharacters();
    }

    public void insert(Character character) {
        characterRepository.insert(character);
    }

    public void delete(Character character) {
        characterRepository.delete(character);
    }

    public void deleteAll() {
        characterRepository.deleteAllCharacters();
    }

    public LiveData<List<Character>> getAllCharacters() {
        return allCharacters;
    }
}
