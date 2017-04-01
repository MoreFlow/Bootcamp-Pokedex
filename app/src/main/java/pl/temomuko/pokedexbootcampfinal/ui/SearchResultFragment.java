package pl.temomuko.pokedexbootcampfinal.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.temomuko.pokedexbootcampfinal.R;
import pl.temomuko.pokedexbootcampfinal.model.Pokemon;
import pl.temomuko.pokedexbootcampfinal.remote.APIManager;
import pl.temomuko.pokedexbootcampfinal.remote.DatabaseHelper;
import pl.temomuko.pokedexbootcampfinal.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultFragment extends Fragment {

    @BindView(R.id.error_view)
    TextView errorView;

    @BindView(R.id.name)
    TextView nameTextView;

    @BindView(R.id.pokemon_view)
    LinearLayout pokemonView;

    @BindView(R.id.weight)
    TextView weightTextView;

    @BindView(R.id.height)
    TextView heightTextView;

    @BindView(R.id.types)
    TextView typesTextView;

    @BindView(R.id.favourite_add)
    ImageView favouriteButton;
    ProgressDialog progress;
    private Pokemon pokemon;
    private DatabaseHelper databaseHelper = null;

    public static SearchResultFragment newInstance(String name) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String query = getArguments().getString("name");
        if (query != null) {
            query = query.trim();
            loadPokemonData(query);
        }
    }

    public void loadPokemonData(String query) {
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();
        APIManager.getInstance().getService().getPokemon(query).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                progress.dismiss();
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), "API Call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResponse(Response<Pokemon> response) {
        if (response.body() != null) {
            pokemon = response.body();
            pokemon.formatTypes();
            nameTextView.setText(String.format("%s. %s", pokemon.getId(), Utils.capitalizeFirstLetter(pokemon.getName())));
            weightTextView.setText(String.format("Weight: %s", pokemon.getWeight()));
            heightTextView.setText(String.format("Height: %s", pokemon.getHeight()));
            typesTextView.setText(String.format("Types: %s", pokemon.getType()));
            if (pokemonExists(pokemon.getId())) {
                favouriteButton.setImageResource(R.drawable.ic_star_black_48dp);
                favouriteButton.setClickable(false);
            } else {
                favouriteButton.setImageResource(R.drawable.ic_star_border_black_48dp);
                favouriteButton.setClickable(true);
            }
            pokemonView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getActivity(), "Response body is null", Toast.LENGTH_SHORT).show();
            errorView.setVisibility(View.VISIBLE);
            pokemonView.setVisibility(View.GONE);
        }
    }

    public boolean pokemonExists(String id) {
        try {
            Dao dao = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class).getDao(Pokemon.class);
            List list = dao.queryForEq("id", id);
            if (list.size() > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @OnClick(R.id.favourite_add)
    public void addToFavourites() {
        try {
            Dao<Pokemon, Integer> dao = Utils.getHelper(getActivity(), databaseHelper).getDao();
            dao.create(pokemon);
            Toast.makeText(getActivity(), Utils.capitalizeFirstLetter(pokemon.getName()) + " added to favourites!", Toast.LENGTH_SHORT).show();
            favouriteButton.setImageResource(R.drawable.ic_star_black_48dp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
