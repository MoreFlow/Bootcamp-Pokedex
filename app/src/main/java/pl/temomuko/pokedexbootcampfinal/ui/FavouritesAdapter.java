package pl.temomuko.pokedexbootcampfinal.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.temomuko.pokedexbootcampfinal.R;
import pl.temomuko.pokedexbootcampfinal.model.Pokemon;
import pl.temomuko.pokedexbootcampfinal.remote.DatabaseHelper;
import pl.temomuko.pokedexbootcampfinal.util.Utils;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private DatabaseHelper databaseHelper = null;
    private List<Pokemon> pokemons;
    private Context context;
    private TextView errorView;

    public FavouritesAdapter(Context context, TextView errorView) {
        this.context = context;
        this.errorView = errorView;
        try {
            Dao<Pokemon, Integer> dao = Utils.getHelper(context, databaseHelper).getDao();
            pokemons = dao.queryForAll();
            setErrorView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Pokemon pokemon = pokemons.get(position);
        setHolderData(holder, pokemon);
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
                try {
                    deletePokemon(helper, pokemon, position);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setErrorView() {
        if (pokemons.size() > 0) {
            errorView.setVisibility(View.GONE);
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void deletePokemon(DatabaseHelper helper, Pokemon pokemon, int position) throws SQLException {
        Dao<Pokemon, Integer> dao = helper.getDao(Pokemon.class);
        DeleteBuilder<Pokemon, Integer> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().eq("id", pokemon.getId());
        dao.delete(deleteBuilder.prepare());
        pokemons.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pokemons.size());
        setErrorView();
    }

    public void setHolderData(ViewHolder holder, Pokemon pokemon) {
        holder.nameTextView.setText(String.format("%s. %s", pokemon.getId(), Utils.capitalizeFirstLetter(pokemon.getName())));
        holder.heightTextView.setText(String.format("Height: %s", pokemon.getHeight()));
        holder.weightTextView.setText(String.format("Weight: %s", pokemon.getWeight()));
        holder.typeTextView.setText(String.format("Types: %s", pokemon.getType()));
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pokemon_card_view)
        CardView cardView;

        @BindView(R.id.favourite_name)
        TextView nameTextView;

        @BindView(R.id.favourite_height)
        TextView heightTextView;

        @BindView(R.id.favourite_weight)
        TextView weightTextView;

        @BindView(R.id.favourite_type)
        TextView typeTextView;

        @BindView(R.id.favourite_delete)
        ImageView deleteView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
