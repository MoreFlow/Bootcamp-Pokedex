package pl.temomuko.pokedexbootcampfinal.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import pl.temomuko.pokedexbootcampfinal.R;

public class MainActivity extends AppCompatActivity {

    FavouritesFragment favouritesFragment;
    SearchManager searchManager;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favouritesFragment = new FavouritesFragment();
        setFragment(favouritesFragment);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                setFragment(SearchResultFragment.newInstance(query));
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search),
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        setFragment(favouritesFragment);
                        return true;
                    }
                });
        return true;
    }

    @Override
    public void onBackPressed() {
        setFragment(favouritesFragment);
        super.onBackPressed();
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}
