package stfo.com.popularmovies.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;

import stfo.com.popularmovies.Adapters.CastAdapter;
import stfo.com.popularmovies.Adapters.MainDataAdapter;
import stfo.com.popularmovies.BackEnd.DataHelper;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.Constants.VolleyInstance;
import stfo.com.popularmovies.Fragments.DetailFragment;
import stfo.com.popularmovies.Fragments.MainFragment;
import stfo.com.popularmovies.Fragments.PersonFragment;
import stfo.com.popularmovies.Fragments.SearchFragment;
import stfo.com.popularmovies.Fragments.UserReviewFragment;
import stfo.com.popularmovies.R;

public class MainActivity extends AppCompatActivity implements MainDataAdapter.OnMovieSelectedListener,
        DetailFragment.OnReviewSelectedListener, CastAdapter.OnCastSelectedListener,
        MainFragment.OnSearchListener, DetailFragment.OnFavouriteChangedListener {

    MainFragment mainFragment;
    DetailFragment detailFragment;
    FrameLayout container, detail_container;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    SearchFragment searchFragment;
    private static final String TAG_MAIN_FRAGMENT = "mainFragment";
    private static final String TAG_DETAIL_FRAGMENT = "detailFragment";
    private static final String TAG_REVIEW_FRAGMENT = "reviewFragment";
    private static final String TAG_PERSON_FRAGMENT = "personFragment";
    private static final String TAG_SEARCH_FRAGMENT = "searchFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            onSearch(query);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleyInstance.getInstance(this).getRequestQueue().cancelAll(Utility.TAG);
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    private void init(Bundle savedInstanceState) {
        container = (FrameLayout) findViewById(R.id.container);
        detail_container = (FrameLayout) findViewById(R.id.detail_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
            setSupportActionBar(toolbar);
        }

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment, TAG_MAIN_FRAGMENT)
                    .commit();
        } else {
            mainFragment = (MainFragment) fragmentManager.findFragmentByTag(TAG_MAIN_FRAGMENT);
            detailFragment = (DetailFragment) fragmentManager.findFragmentByTag(TAG_DETAIL_FRAGMENT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onMovieSelected( int id) {
        boolean isFav = false;
        if (DataHelper.getMovie(this.getContentResolver(), id).getCount() != 0)
            isFav = true;
        detailFragment = DetailFragment.newInstance(id, isFav, 0);
        FragmentTransaction transaction;
        DataInstance.getTempInstance().clear();
        if (detail_container == null) {
            transaction = fragmentManager.beginTransaction()
                    .replace(R.id.container, detailFragment, TAG_DETAIL_FRAGMENT);
        } else {
            transaction = fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, detailFragment, TAG_DETAIL_FRAGMENT);
        }
        transaction.addToBackStack(Utility.TAG).commit();
    }

    @Override
    public void onReviewSelected(int colorVibrant) {
        UserReviewFragment userReviewFragment = UserReviewFragment.newInstance(colorVibrant);
        if (detail_container == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, userReviewFragment, TAG_REVIEW_FRAGMENT)
                    .addToBackStack(Utility.TAG)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, userReviewFragment, TAG_REVIEW_FRAGMENT)
                    .addToBackStack(Utility.TAG)
                    .commit();
        }
    }

    @Override
    public void onCastSelected(int personId, int color) {
        PersonFragment personFragment = PersonFragment.newInstance(personId, color);
        FragmentTransaction transaction;
        if (detail_container == null) {
            transaction = fragmentManager.beginTransaction()
                    .replace(R.id.container, personFragment, TAG_PERSON_FRAGMENT);
        } else {
            transaction = fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, personFragment, TAG_PERSON_FRAGMENT);
        }
        transaction.addToBackStack(Utility.TAG).commit();
    }


    @Override
    public void onSearch(String query) {
        searchFragment = SearchFragment.getInstance(query);
        fragmentManager.beginTransaction()
                .replace(R.id.container, searchFragment, TAG_SEARCH_FRAGMENT)
                .addToBackStack(null).commit();
    }

    @Override
    public void onFavouriteAdded() {
        if(mainFragment != null)
            mainFragment.onFavouriteAdded();
    }

    @Override
    public void onFavouriteRemoved() {
        if(mainFragment != null)
            mainFragment.onFavouriteRemoved();
    }
}