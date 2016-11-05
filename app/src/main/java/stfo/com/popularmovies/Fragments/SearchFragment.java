package stfo.com.popularmovies.Fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import stfo.com.popularmovies.Adapters.SearchAdapter;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.SearchResults;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.Constants.VolleyInstance;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 29/10/16.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    SearchAdapter searchAdapter;
    private static SearchFragment instance;
    String query;

    public static SearchFragment getInstance(String query){
        if(instance == null)
            instance = new SearchFragment();
        instance.query = query;
        return instance;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            query = savedInstanceState.getString(Utility.KEY_APP_STRING_DATA,"");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Utility.KEY_APP_STRING_DATA, query);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        init(v);
        recyclerView.setVisibility(View.VISIBLE);
        return v;
    }

    private void init(View v){
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(query);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
        Toolbar activityToolbar = ((MainActivity)getActivity()).getToolbar();
        if(activityToolbar == null)
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        else
            activityToolbar.setTitle(query);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_main);
        searchAdapter = new SearchAdapter(getContext(), null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }
        doSearch(query);
    }

    private void doSearch(String query){
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setVisibility(View.GONE);

        requestQueue = VolleyInstance.getInstance(getContext()).getRequestQueue();

        Uri getSearchUri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_SEARCH_API)
                .appendPath(Utility.KEY_MULTI_API)
                .appendQueryParameter(Utility.KEY_QUERY_API, query)
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getSearchUri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<SearchResults> resultMovies = new ArrayList<>();
                        ArrayList<SearchResults> resultPersons = new ArrayList<>();

                        try {
                            JSONArray results = response.getJSONArray(Utility.JSON_KEY_API_RESULTS);
                            for (int i = 0; i < results.length(); ++i) {
                                String name, poster_path;
                                int id;
                                JSONObject object = results.getJSONObject(i);
                                String media_type = object.getString(Utility.JSON_KEY_API_MEDIA_TYPE);
                                if (media_type.equals("movie")) {
                                    name = object.getString(Utility.JSON_KEY_API_TITLE);
                                    poster_path = object.getString(Utility.JSON_KEY_API_POSTER_PATH);
                                    id = object.getInt(Utility.JSON_KEY_API_ID);
                                    resultMovies.add(new SearchResults(
                                            id, poster_path, name, SearchResults.TYPE_MOVIE
                                    ));
                                } else if (media_type.equals("person")) {
                                    name = object.getString(Utility.JSON_KEY_API_NAME);
                                    poster_path = object.getString(Utility.JSON_KEY_API_CAST_PROFILE_PATH);
                                    id = object.getInt(Utility.JSON_KEY_API_ID);
                                    resultPersons.add(new SearchResults(
                                            id, poster_path, name, SearchResults.TYPE_PERSON
                                    ));
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("MY_APP", e.toString());
                        } finally {
                            resultMovies.addAll(resultPersons);
                            recyclerView.setVisibility(View.VISIBLE);
                            searchAdapter.setData(resultMovies);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}
