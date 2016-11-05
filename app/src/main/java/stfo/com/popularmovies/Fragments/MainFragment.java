package stfo.com.popularmovies.Fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import stfo.com.popularmovies.Activities.MainActivity;
import stfo.com.popularmovies.Adapters.FavouriteAdapter;
import stfo.com.popularmovies.Adapters.MainDataAdapter;
import stfo.com.popularmovies.Constants.DataHolder;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.Constants.VolleyInstance;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 20/10/16.
 */
public class MainFragment extends Fragment {

    public void onFavouriteAdded() {
        if(favouriteAdapter!=null){
            if(favouriteAdapter.getItemCount() == 0)
                favourite.setVisibility(View.VISIBLE);
            favouriteAdapter.addData();
        }
    }

    public void onFavouriteRemoved() {
        if(favouriteAdapter!=null){
            if(favouriteAdapter.getItemCount() == 1)
                favourite.setVisibility(View.GONE);
            favouriteAdapter.removeData();
        }
    }

    public interface OnSearchListener {
        void onSearch(String query);
    }

    private MainDataAdapter recyclerViewAdapter;
    private RecyclerView recyclerView, recyclerView_favourites;
    private RequestQueue requestQueue;
    private SearchView searchView;
    private String query;
    private CardView favourite;
    private FavouriteAdapter favouriteAdapter;
    //  private ImageView imageView_loading;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(Utility.KEY_APP_STRING_DATA, "");
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        init(v);
        if (DataInstance.getInstance().size() == 0)
            doNetworkStuff();
        else {
            recyclerViewAdapter.setData(DataInstance.getInstance());
            //      imageView_loading.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (searchView != null)
            outState.putString(Utility.KEY_APP_STRING_DATA, searchView.getQuery().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_discovery, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        if (!TextUtils.isEmpty(query)) {
            searchItem.expandActionView();
            searchView.setQuery(query, false);
            searchView.requestFocus();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (getActivity() instanceof OnSearchListener) {
                    ((OnSearchListener) getActivity()).onSearch(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_item_popular:
                Utility.KEY_USER_SELECTED_FILER = Utility.KEY_POPULAR_API;
                doNetworkStuff();
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_item_top_rated:
                Utility.KEY_USER_SELECTED_FILER = Utility.KEY_TOP_RATED_API;
                doNetworkStuff();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(View v) {
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        Toolbar activityToolbar = ((MainActivity)getActivity()).getToolbar();
        if(activityToolbar == null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_main);
        recyclerViewAdapter = new MainDataAdapter(getContext(), null);
        recyclerView.setAdapter(recyclerViewAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }

        favouriteAdapter = new FavouriteAdapter(getContext());
        favourite = (CardView) v.findViewById(R.id.layout_favourite);
        recyclerView_favourites = (RecyclerView) v.findViewById(R.id.recyclerView_favourites);
        recyclerView_favourites.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView_favourites.setAdapter(favouriteAdapter);
        if(favouriteAdapter.getItemCount() > 0)
            favourite.setVisibility(View.VISIBLE);
    }

    public void doNetworkStuff() {
        DataInstance.getInstance().clear();
        recyclerViewAdapter.setData(null);
        requestQueue = VolleyInstance.getInstance(getContext()).getRequestQueue();

        Uri getMainData = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_MOVIE_API)
                .appendPath(Utility.KEY_USER_SELECTED_FILER)
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        final JsonObjectRequest jsonMainDataRequest = new JsonObjectRequest(Request.Method.GET,
                getMainData.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (getActivity() == null)
                            return;
                        ArrayList<DataHolder> data = DataInstance.getInstance();

                        try {
                            JSONArray results = response.getJSONArray(Utility.JSON_KEY_API_RESULTS);
                            for (int i = 0; i < results.length(); ++i) {
                                int id, vote_count;
                                String poster_path, backdrop_path, title, release_date, overview;
                                double rating;
                                JSONObject current_Obj = results.getJSONObject(i);
                                id = current_Obj.getInt(Utility.JSON_KEY_API_ID);
                                title = current_Obj.getString(Utility.JSON_KEY_API_TITLE);
                                poster_path = current_Obj.getString(Utility.JSON_KEY_API_POSTER_PATH);
                                DataHolder currentData = new DataHolder()
                                        .setId(id)
                                        .setPoster_path(poster_path)
                                        .setTitle(title);
                                data.add(currentData);
                            }
                        } catch (JSONException e) {
                            Log.d(Utility.LOG_TAG + getClass().toString(), e.toString());
                        } finally {
                            recyclerViewAdapter.setData(data);
                            //      imageView_loading.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        jsonMainDataRequest.setTag(Utility.TAG);
        requestQueue.add(jsonMainDataRequest);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }
    }
}
