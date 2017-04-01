package pl.temomuko.pokedexbootcampfinal.remote;

import pl.temomuko.pokedexbootcampfinal.model.Pokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokemonService {

    @GET("pokemon/{id}")
    Call<Pokemon> getPokemon(@Path("id") String id);

}
