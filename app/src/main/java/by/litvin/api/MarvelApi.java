package by.litvin.api;

import by.litvin.model.ApiResponse;
import by.litvin.model.Character;
import by.litvin.model.RelatedItem;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelApi {

    String BASE_URL = "http://gateway.marvel.com/v1/public/";


    @GET("characters")
    Observable<ApiResponse<Character>> getAllCharacters(@Query("limit") long limit,
                                                        @Query("offset") int offset,
                                                        @Query("ts") String timestamp,
                                                        @Query("apikey") String apikey,
                                                        @Query("hash") String hash);

    @GET("characters/{characterId}")
    Single<ApiResponse<Character>> getCharacterById(@Path("characterId") long characterId,
                                                    @Query("ts") String timestamp,
                                                    @Query("apikey") String apikey,
                                                    @Query("hash") String hash);

    @GET("characters/{characterId}/comics")
    Observable<ApiResponse<RelatedItem>> getComicsWithCharacter(@Path("characterId") int characterId,
                                                                @Query("limit") long limit,
                                                                @Query("ts") String timestamp,
                                                                @Query("apikey") String apikey,
                                                                @Query("hash") String hash);

    @GET("characters/{characterId}/series")
    Observable<ApiResponse<RelatedItem>> getSeriesWithCharacter(@Path("characterId") int characterId,
                                                                @Query("limit") long limit,
                                                                @Query("ts") String timestamp,
                                                                @Query("apikey") String apikey,
                                                                @Query("hash") String hash);

    @GET("characters/{characterId}/events")
    Observable<ApiResponse<RelatedItem>> getEventsWithCharacter(@Path("characterId") int characterId,
                                                                @Query("limit") long limit,
                                                                @Query("ts") String timestamp,
                                                                @Query("apikey") String apikey,
                                                                @Query("hash") String hash);

    class Factory {
        public static MarvelApi create(String baseUrl) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(MarvelApi.class);
        }
    }

}
