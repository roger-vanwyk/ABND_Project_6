package com.example.android.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Articles>> {

    private static final String ARTICLES_API_BASE_QUERY =
            "http://content.guardianapis.com/search";

    private static final int ARTICLES_LOADER = 1;
    private static final String SAVED_LAYOUT_MANAGER = "1";
    private static final String SEARCH_RESULTS = "articlesSearchResults";


    private FloatingSearchView searchBarView;
    private String mLastQuery = "";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter RecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private View loadingIndicator;
    private Parcelable layoutManagerSavedState;

    //Reurns the activity's context
    public static Context getContext() {
        return getContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    searchBarView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        searchBarView.setOnHomeActionClickListener(
                new FloatingSearchView.OnHomeActionClickListener() {
        @Override
        public void onHomeClicked() {
            finish ( );
        }
});

    layoutManager = new LinearLayoutManager(getApplicationContext());
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
            DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(RecyclerViewAdapter);

        if (savedInstanceState != null) {
        Articles[] articles = (Articles[]) savedInstanceState.getParcelableArray(SEARCH_RESULTS);
        ArrayList<Articles> list = new ArrayList<Articles>(articles.length);
        Collections.addAll(list, articles);
        RecyclerViewAdapter.updateAdapterData(list);
        setSearchLabelVisibility(false);
    }

    setupSearchBar();

}

    @Override
    public Loader<ArrayList<Articles>> onCreateLoader(int id, Bundle args) {
        setLoadingIndicatorVisibilty(true);
        setSearchLabelVisibility(false);
        return new ArticlesLoader(MainActivity.this, args.getString("request_url"));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Articles>> loader, ArrayList<Articles> data) {
        setLoadingIndicatorVisibilty(false);
        if (data == null) {
            Toast.makeText(this, this.getResources().getString(R.string.bad_server), Toast.LENGTH_SHORT).show();
            setSearchLabelVisibility(true);
            return;
        }
        setSearchLabelVisibility(data.size() == 0);
        if (data.size() == 0) {
            Toast.makeText(this, this.getResources().getString(R.string.no_articles_found), Toast.LENGTH_SHORT).show();

        } else {
            RecyclerViewAdapter.updateAdapterData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Articles>> loader) {
        RecyclerViewAdapter.updateAdapterData(new ArrayList<Articles>());
        setSearchLabelVisibility(true);
        setLoadingIndicatorVisibilty(false);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Setup search bar listeners
    public void setupSearchBar() {


        searchBarView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                RecyclerViewAdapter.getAdapterData().clear();

                Uri baseUri = Uri.parse(ARTICLES_API_BASE_QUERY);
                Uri.Builder uriBuilder = baseUri.buildUpon();
                uriBuilder.appendQueryParameter("api-key", "38418974-ec08-4240-af40-0b0740ddf870");
                if (query != null)
                    uriBuilder.appendQueryParameter("q", query);

                Bundle args = new Bundle();
                args.putString("request_url", uriBuilder.toString());
                // Check if internet available
                if (!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.network_issue), Toast.LENGTH_SHORT).show();
                    return;
                }
                getSupportLoaderManager().restartLoader(ARTICLES_LOADER, args, MainActivity.this).forceLoad();
            }
        });
    }

    //Update the search results in the recycler view
    public void updateResults(ArrayList<Articles> searchResult) {
        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
        adapter.updateAdapterData(searchResult);
        adapter.notifyDataSetChanged();

        searchBarView.clearSuggestions();
        searchBarView.clearQuery();
        searchBarView.clearSearchFocus();
    }

    //Make search label visible
    public void setSearchLabelVisibility(boolean flag) {
        RelativeLayout searchLabel = (RelativeLayout) findViewById(R.id.search_label);
        int id = flag ? View.VISIBLE : View.INVISIBLE;
        searchLabel.setVisibility(id);
    }

    //Enable the loading indicator
    public void setLoadingIndicatorVisibilty(boolean flag) {
        int id = flag ? View.VISIBLE : View.INVISIBLE;
        loadingIndicator.setVisibility(id);
    }

    @Override
    public void onBackPressed() {
        if (searchBarView.isSearchBarFocused()) {
            searchBarView.clearSuggestions();
            searchBarView.clearQuery();
            searchBarView.clearSearchFocus();
            return;
        }
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Articles> searchList = RecyclerViewAdapter.getAdapterData();
        Articles[] books = new Articles[searchList.size()];
        for (int i = 0; i < books.length; i++) {
            books[i] = searchList.get(i);
        }
        outState.putParcelableArray(SEARCH_RESULTS, (Parcelable[]) books);
    }
}
