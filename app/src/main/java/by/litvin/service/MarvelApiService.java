package by.litvin.service;

import java.util.List;

import by.litvin.model.ApiResponse;
import by.litvin.model.Character;
import by.litvin.model.Comic;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelApiService {

    String BASE_URL = "http://gateway.marvel.com/v1/public/";
    String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ssZ"; //TODO
    String PUBLIC_KEY = "858961528d9190d6ef185e668599b761"; //TODO move into properties file, see - secure-preferences
    String PRIVATE_KEY = "b9e01a841a358218d6a31809a193111401da9674"; //TODO move into properties file - see secure-preferences


    @GET("characters")
    Observable<ApiResponse<Character>> getAllCharacters(@Query("limit") long limit,
                                                        @Query("ts") String timestamp,
                                                        @Query("apikey") String apikey,
                                                        @Query("hash") String hash);

    @GET("characters/{characterId}")
    Single<ApiResponse<Character>> getCharacterById(@Path("characterId") long characterId,
                                                    @Query("ts") String timestamp,
                                                    @Query("apikey") String apikey,
                                                    @Query("hash") String hash);

    @GET("characters/{characterId}/comics")
    Observable<ApiResponse<Comic>> getComicsWithCharacter(@Path("characterId") int characterId,
                                                          @Query("ts") String timestamp,
                                                          @Query("apikey") String apikey,
                                                          @Query("hash") String hash);

    class Factory {
        public static MarvelApiService create(String baseUrl) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(MarvelApiService.class);
        }
    }

}
