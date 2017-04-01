package pl.temomuko.pokedexbootcampfinal.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    public static final String BASE_URL = "http://pokeapi.co/api/v2/";
    private static APIManager instance;
    private PokemonService service;

    private APIManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PokemonService.class);
    }

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }
        return instance;
    }

    public PokemonService getService() {
        return service;
    }
}