package stfo.com.popularmovies.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import stfo.com.popularmovies.Activities.MainActivity;
import stfo.com.popularmovies.Adapters.MovieCreditsAdapter;
import stfo.com.popularmovies.Constants.Casts;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.MovieCredits;
import stfo.com.popularmovies.Constants.Person;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.Constants.VolleyInstance;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 28/10/16.
 */
public class PersonFragment extends Fragment {
    private ImageView imageView_person, imageView_backdrop;
    private TextView textView_name, textView_bio, textView_bday_dday;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int personid, id,person_position;
    private Casts person;
    private int color;

    private MovieCreditsAdapter movieCreditsAdapter;

    public static PersonFragment newInstance(int person_id, int color){
        PersonFragment personFragment = new PersonFragment();
        personFragment.personid = person_id;
        personFragment.color = color;
        return personFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Utility.KEY_APP_INT_EXTRA_DATA, personid);
        outState.putInt(Utility.KEY_APP_INT_EXTRA_COLOR,color);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        person = new Casts(personid, null, null, null);
        if(savedInstanceState!=null){
            personid = savedInstanceState.getInt(Utility.KEY_APP_INT_EXTRA_DATA);
            color = savedInstanceState.getInt(Utility.KEY_APP_INT_EXTRA_COLOR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container,false);
        init(v);
        return v;
    }
    

    void init(View v) {

        loadData();

        imageView_person = (ImageView) v.findViewById(R.id.imageView_person_poster);
        textView_name = (TextView) v.findViewById(R.id.textView_person_name);
        textView_bday_dday = (TextView) v.findViewById(R.id.textView_bday_dday);
        textView_bio = (TextView) v.findViewById(R.id.textView_bio);
        imageView_backdrop = (ImageView)v.findViewById(R.id.imageView_person_backdrop);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Person);
        movieCreditsAdapter = new MovieCreditsAdapter(getActivity(),null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(movieCreditsAdapter);

        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setContentScrimColor(color);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        Toolbar activityToolbar = ((MainActivity)getActivity()).getToolbar();
        if(activityToolbar == null)
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        loadCredits();
    }

    private String get_bday_dday_String() {
        String bday = person.getBirthday();
        String dday = person.getDeathday();
        try {
            if (bday != null) {
                bday = Utility.formatDate(bday);
            }
            if (dday != null) {
                dday = Utility.formatDate(dday);
                bday = bday + " - " + dday;
            }
        } catch (Exception e) {

        }
        return bday;
    }

    private void loadData(){

        final RequestQueue requestQueue = VolleyInstance.getInstance(getContext()).getRequestQueue();

        Uri uri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_PERSON_API)
                .appendPath(personid + "")
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String bio = response.getString(Utility.JSON_KEY_API_PERSON_BIOGRAPHY);
                            String birthday = response.getString(Utility.JSON_KEY_API_PERSON_BIRTHDAY);
                            String deathday = response.getString(Utility.JSON_KEY_API_PERSON_DEATHDAY);
                            person.setId(personid);
                            person.setName(response.getString(Utility.JSON_KEY_API_CAST_NAME));
                            person.setImagePath(response.getString(Utility.JSON_KEY_API_CAST_PROFILE_PATH));
                            person.setBio(bio);
                            person.setBirthday(birthday);
                            person.setDeathday(deathday);
                        }catch (JSONException e){

                        }finally{
                            Glide.with(getContext()).load(Utility.getw300Path(person.getImagePath()))
                                    .bitmapTransform(new BlurTransformation(getContext()))
                                    .into(imageView_backdrop);

                            textView_name.setText(person.getName());
                            String bday_dday = get_bday_dday_String();
                            if(!bday_dday.equals("null"))
                                textView_bday_dday.setText(bday_dday);
                            if(!person.getBio().equals("null"))
                                textView_bio.setText(person.getBio());
                            Glide.with(getContext())
                                    .load(Utility.getw300Path(person.getImagePath()))
                                    .centerCrop()
                                    .into(imageView_person);

                            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(person.getName());
                            collapsingToolbarLayout.setTitle(person.getName());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        jsonObjectRequest.setTag(Utility.TAG);
        requestQueue.add(jsonObjectRequest);

    }


    private void loadCredits(){
        RequestQueue requestQueue = VolleyInstance.getInstance(getContext()).getRequestQueue();

        Uri uri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_PERSON_API)
                .appendPath(personid + "")
                .appendPath(Utility.KEY_MOVIE_CREDITS_API)
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<MovieCredits> credits = new ArrayList<>();
                        try{
                            JSONArray array = response.getJSONArray(Utility.JSON_KEY_API_CASTS);
                            for(int i = 0 ; i < array.length(); ++i){
                                JSONObject object = array.getJSONObject(i);
                                int movie_id = object.getInt(Utility.JSON_KEY_API_ID);
                                String movie_name = object.getString(Utility.JSON_KEY_API_TITLE);
                                String poster_path = object.getString(Utility.JSON_KEY_API_POSTER_PATH);
                                String character = object.getString(Utility.JSON_KEY_API_CAST_CHARACTER);
                                credits.add(new MovieCredits(movie_id,movie_name,character,poster_path));
                            }
                        }catch (JSONException e){

                        }finally {
                            person.setMovieCredits(credits);
                            movieCreditsAdapter.setMovieCredits(credits);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        jsonObjectRequest.setTag(Utility.TAG);
        requestQueue.add(jsonObjectRequest);
    }
}
