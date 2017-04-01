package pl.temomuko.pokedexbootcampfinal.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.temomuko.pokedexbootcampfinal.R;

public class FavouritesFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.error_view)
    TextView errorView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new FavouritesAdapter(getActivity(), errorView));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
