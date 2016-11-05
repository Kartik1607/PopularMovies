package stfo.com.popularmovies.Fragments;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
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
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import stfo.com.popularmovies.Activities.MainActivity;
import stfo.com.popularmovies.Adapters.CastAdapter;
import stfo.com.popularmovies.Adapters.GalleryAdapter;
import stfo.com.popularmovies.BackEnd.DataHelper;
import stfo.com.popularmovies.BackEnd.MovieContracts;
import stfo.com.popularmovies.Constants.Casts;
import stfo.com.popularmovies.Constants.DataHolder;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.Review;
import stfo.com.popularmovies.Constants.Trailer;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.Constants.VolleyInstance;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 22/10/16.
 */
public class DetailFragment extends Fragment {

    private ImageView imageView_backdrop;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DataHolder data;
    private TextView textView_movie_name, textView_movie_tagline, textView_movie_overview,
            textView_release_date, textView_runtime, textView_rating, textView_votes,
            textView_user_review, textView_user_readMore, textView_user_review_content,
            textView_movie_cast;
    private View layout_rating, layout_reviews, layout_cast;
    private int colorVibrant, id;

    private RecyclerView recyclerView_movie_cast, recyclerView_gallery;
    private CastAdapter castAdapter;
    private GalleryAdapter galleryAdapter;

    private RequestQueue requestQueue;
    private Context context;
    private ContentResolver contentResolver;
    private ShareActionProvider mShareActionProvider;

    private FloatingActionButton fab;

    private boolean isFavourite = false;
    private boolean isDetailLoaded = false;
    private boolean isCastLoaded = false;

    public interface OnReviewSelectedListener {
        void onReviewSelected(int colorVibrant);
    }

    public interface OnFavouriteChangedListener {
        void onFavouriteAdded();
        void onFavouriteRemoved();
    }

    public static DetailFragment newInstance( int id, boolean isFavourite, int colorVibrant) {
        DetailFragment fragment = new DetailFragment();
        fragment.isFavourite = isFavourite;
        fragment.id = id;
        fragment.colorVibrant = colorVibrant;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            id = savedInstanceState.getInt(Utility.KEY_APP_INT_EXTRA_DATA_SECONDARY);
            colorVibrant = savedInstanceState.getInt(Utility.KEY_APP_INT_EXTRA_COLOR);
            isFavourite = savedInstanceState.getBoolean(Utility.KEY_APP_BOOL_FAV);
        }
        setHasOptionsMenu(true);
    }

    private void setVisiblility(View myView, int visibility){
        if(visibility == View.GONE)
            myView.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= 21) {
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            myView.setVisibility(View.VISIBLE);
            anim.start();
        }else {
            myView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Utility.KEY_APP_INT_EXTRA_DATA_SECONDARY, id);
        outState.putInt(Utility.KEY_APP_INT_EXTRA_COLOR, colorVibrant);
        outState.putBoolean(Utility.KEY_APP_BOOL_FAV, isFavourite);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        init(v);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_single_movie, menu);
        MenuItem item = menu.findItem(R.id.share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    }


    private void createShareIntent() {
        if (mShareActionProvider == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (data.getTrailers().size() == 0)
            intent.putExtra(Intent.EXTRA_TEXT, "Check out " +  data.getTitle() + "Movie");
        else
            intent.putExtra(Intent.EXTRA_TEXT, data.getTitle() + "Watch : " + data.getTrailers().get(0).getName()
                    + "-" + data.getTrailers().get(0).getVideo_url()
            );
        mShareActionProvider.setShareIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadBackDrop();
        loadDetails();
        loadReviews();
        loadCast();
        loadImages();
        loadTrailers();
    }

    private void init(View v) {
        context = getActivity();
        requestQueue = VolleyInstance.getInstance(getContext()).getRequestQueue();
        data = DataInstance.getTempInstance();
        data.setId(id);

        //TOOLBAR
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        //IMAGE_BACKGROUND
        imageView_backdrop = (ImageView) v.findViewById(R.id.imageView_movie_backdrop);

        //MOVIE_DETAIL
        textView_movie_name = (TextView) v.findViewById(R.id.textView_movie_name);
        textView_movie_tagline = (TextView) v.findViewById(R.id.textView_movie_tagline);
        textView_movie_overview = (TextView) v.findViewById(R.id.textView_movie_overview);
        textView_release_date = (TextView) v.findViewById(R.id.textView_movie_release);
        textView_runtime = (TextView) v.findViewById(R.id.textView_movie_runtime);

        //USER_REVIEW
        textView_user_review = (TextView) v.findViewById(R.id.textView_User_Reviews);
        textView_user_review_content = (TextView) v.findViewById(R.id.textView_User_Reviews_Content);
        textView_user_readMore = (TextView) v.findViewById(R.id.textView_User_Reviews_Readmore);
        textView_rating = (TextView) v.findViewById(R.id.textView_rating);
        textView_votes = (TextView) v.findViewById(R.id.textView_votes);
        layout_rating = v.findViewById(R.id.layout_rating);
        layout_reviews = v.findViewById(R.id.layout_review);
        layout_reviews.setVisibility(View.GONE);

        //CAST
        layout_cast = v.findViewById(R.id.layout_cast);
        textView_movie_cast = (TextView) v.findViewById(R.id.textView_Movie_Cast);
        recyclerView_movie_cast = (RecyclerView) v.findViewById(R.id.recyclerView_Cast);
        recyclerView_movie_cast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        castAdapter = new CastAdapter(getContext(), null);
        recyclerView_movie_cast.setAdapter(castAdapter);
        layout_cast.setVisibility(View.GONE);

        //MEDIA AND GALLERY
        recyclerView_gallery = (RecyclerView) v.findViewById(R.id.recyclerView_gallery);
        recyclerView_gallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        galleryAdapter = new GalleryAdapter(getContext(), null, null);
        recyclerView_gallery.setAdapter(galleryAdapter);

        contentResolver = getContext().getContentResolver();
        Toolbar activityToolbar = ((MainActivity) getActivity()).getToolbar();
        if (activityToolbar == null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        else {
            toolbar = activityToolbar;
        }


        //FAVOURITE
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite) {
                    removeFavourite();
                    fab.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_favorite_border_white_48dp));
                } else {
                    if (isDetailLoaded && isCastLoaded) {
                        addFavourite();
                        fab.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_favorite_white_48dp));
                    } else
                        Toast.makeText(context, "Wait for data to load", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (isFavourite)
            fab.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_favorite_white_48dp));
    }

    private void addFavourite() {
        isFavourite = true;
        addMovie();
        addCast();
        if(context instanceof OnFavouriteChangedListener){
            ((OnFavouriteChangedListener) context).onFavouriteAdded();
        }
        Toast.makeText(context, data.getTitle() + " added to Favourite", Toast.LENGTH_LONG).show();
    }

    private void removeFavourite() {
        isFavourite = false;
        removeMovie();
        removeCast();
        if(context instanceof OnFavouriteChangedListener){
            ((OnFavouriteChangedListener) context).onFavouriteRemoved();
        }
        Toast.makeText(context, data.getTitle() + " removed from Favourite", Toast.LENGTH_LONG).show();
    }

    private void addCast() {
        for (int i = 0; i < data.getCasts().size(); ++i)
            DataHelper.insertCastforMovie(contentResolver, id, data.getCasts().get(i));
    }

    private void addMovie() {
        DataHelper.insertMovie(contentResolver, data);
    }

    private void removeMovie() {
        DataHelper.deleteMovie(contentResolver, id);
    }

    private void removeCast() {
        DataHelper.deleteCast(contentResolver, id);
    }

    private void loadBackDrop() {

        if (data.getBackdrop_path() == null)
            return;

        collapsingToolbarLayout.setTitle(data.getTitle());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(data.getTitle());
        textView_movie_name.setText(data.getTitle());
        textView_rating.setText(data.getRating() + "");
        textView_votes.setText(String.format(
                getResources().getString(R.string.votes), data.getVote_count()
        ));

        Glide.with(context)
                .load(Utility.getw780Path(data.getBackdrop_path()))
                .asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {

                        colorVibrant = palette.getVibrantColor(
                                ContextCompat.getColor(context, R.color.colorPrimary)
                        );
                        collapsingToolbarLayout.setContentScrimColor(colorVibrant);
                        castAdapter.setColor(colorVibrant);
                        if(getActivity()!=null)
                            if (toolbar == ((MainActivity) getActivity()).getToolbar()) {
                                toolbar.setBackgroundColor(colorVibrant);
                            }

                        if (Build.VERSION.SDK_INT >= 21) {
                            if (getActivity() != null)
                                getActivity().getWindow().setStatusBarColor(colorVibrant);
                        }
                        int currentColor = ContextCompat.getColor(context, R.color.colorPrimary);
                        int nextColor = colorVibrant;
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, nextColor);
                        colorAnimation.setDuration(1500);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                int value = (int) animator.getAnimatedValue();
                                layout_rating.setBackgroundColor(value);
                                textView_user_review.setTextColor(value);
                                textView_user_readMore.setTextColor(value);
                                textView_movie_cast.setTextColor(value);
                            }

                        });
                        colorAnimation.start();
                    }
                });
                return false;
            }
        }).into(imageView_backdrop);
    }

    private void loadTrailers() {
        Uri uri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_MOVIE_API)
                .appendPath(id + "")
                .appendPath(Utility.KEY_VIDEOS_API)
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (getActivity() == null)
                            return;
                        ArrayList<Trailer> trailers = new ArrayList<>();
                        try {
                            JSONArray results = response.getJSONArray(Utility.JSON_KEY_API_RESULTS);
                            for (int i = 0; i < results.length(); ++i) {
                                JSONObject object = results.getJSONObject(i);
                                String key = object.getString(Utility.JSON_KEY_API_YOUTUBE_KEY);
                                String name = object.getString(Utility.JSON_KEY_API_NAME);
                                trailers.add(new Trailer(key, name));
                            }
                        } catch (JSONException e) {

                        } finally {
                            setVisiblility(recyclerView_gallery,View.VISIBLE);
                            data.setTrailer(trailers);
                            galleryAdapter.setTrailer(trailers);
                            createShareIntent();
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

    private void loadImages() {
        Uri uri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_MOVIE_API)
                .appendPath(id + "")
                .appendPath(Utility.KEY_IMAGES_API)
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (getActivity() == null)
                            return;
                        ArrayList<String> images = new ArrayList<>();
                        try {
                            JSONArray backdrops = response.getJSONArray(Utility.JSON_KEY_API_BACKDROPS);
                            for (int i = 0; i < backdrops.length(); ++i) {
                                JSONObject object = backdrops.getJSONObject(i);
                                String path = object.getString(Utility.JSON_KEY_API_FILE_PATH);
                                images.add(path);
                            }
                        } catch (JSONException e) {

                        } finally {
                            setVisiblility(recyclerView_gallery,View.VISIBLE);
                            data.setGallery(images);
                            galleryAdapter.setImages(images);
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

    private void loadDetails() {
        Cursor cursor = DataHelper.getMovie(contentResolver, id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            DataHolder dataItem = DataInstance.getTempInstance();
            dataItem.setId(cursor.getInt(cursor.getColumnIndex(MovieContracts.MovieEntry._ID)));
            dataItem.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_POSTER)));
            dataItem.setTagline(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_TAGLINE)));
            dataItem.setRuntime(cursor.getInt(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_RUNTIME)));
            dataItem.setTitle(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_NAME)));
            dataItem.setVote_count(cursor.getInt(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_VOTES)));
            dataItem.setRating(cursor.getDouble(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_AVERAGE)));
            dataItem.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
            dataItem.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_BACKDROP)));
            dataItem.setOverview(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
            textView_movie_tagline.setText(data.getTagline());
            textView_runtime.setText(String.format(getString(R.string.runtime), data.getRuntime()));
            textView_movie_overview.setText(data.getOverview());
            try {
                textView_release_date.setText(Utility.formatDate(data.getRelease_date()));
            } catch (ParseException e) {
                textView_release_date.setText(data.getRelease_date());
            }
            cursor.close();
            isDetailLoaded = true;
            if (!isFavourite) {
                removeFavourite();
            }
            loadBackDrop();
            return;
        }

        Uri uri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_MOVIE_API)
                .appendPath(id + "")
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (getActivity() == null)
                            return;
                        try {
                            data.setTagline(response.getString(Utility.JSON_KEY_API_TAGLINE));
                            data.setRuntime(response.getInt(Utility.JSON_KEY_API_RUNTIME));
                            data.setTitle(response.getString(Utility.JSON_KEY_API_TITLE));
                            data.setVote_count(response.getInt(Utility.JSON_KEY_API_VOTE_COUNT));
                            data.setRating(response.getDouble(Utility.JSON_KEY_API_RATING));
                            data.setPoster_path(response.getString(Utility.JSON_KEY_API_POSTER_PATH));
                            data.setRelease_date(response.getString(Utility.JSON_KEY_API_RELEASE_DATE));
                            data.setBackdrop_path(response.getString(Utility.JSON_KEY_API_BACKDROP_PATH));
                            data.setOverview(response.getString(Utility.JSON_KEY_API_OVERVIEW));

                        } catch (JSONException e) {
                            Log.d(Utility.LOG_TAG + getClass().toString(), e.toString());
                        } finally {
                            isDetailLoaded = true;
                            textView_movie_tagline.setText(data.getTagline());
                            textView_runtime.setText(String.format(getString(R.string.runtime), data.getRuntime()));
                            textView_movie_overview.setText(data.getOverview());
                            loadBackDrop();
                            try {
                                textView_release_date.setText(Utility.formatDate(data.getRelease_date()));
                            } catch (Exception e) {
                                textView_release_date.setText(data.getRelease_date());
                            }
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

    private void loadReviews() {

        Uri uri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_MOVIE_API)
                .appendPath(id + "")
                .appendPath(Utility.KEY_REVIEWS_API)
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (getActivity() == null)
                            return;
                        try {
                            ArrayList<Review> arrayList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray(Utility.JSON_KEY_API_RESULTS);
                            for (int i = 0; i < jsonArray.length(); ++i) {
                                String author, content;
                                JSONObject current = jsonArray.getJSONObject(i);
                                author = current.getString(Utility.JSON_KEY_API_AUTHOR);
                                content = current.getString(Utility.JSON_KEY_API_CONTENT);
                                Review review = new Review(author, content);
                                arrayList.add(review);
                            }
                            data.setReviews(arrayList);
                            setVisiblility(layout_reviews,View.VISIBLE);
                            if (arrayList.size() != 0) {
                                textView_user_review_content.setText(
                                        arrayList.get(0).getReview()
                                );
                                layout_reviews.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (getActivity() instanceof OnReviewSelectedListener) {
                                            ((OnReviewSelectedListener) getActivity()).onReviewSelected(colorVibrant);
                                        }
                                    }
                                });
                            } else {
                                textView_user_review_content.setText(
                                        getString(R.string.no_reviews)
                                );
                                textView_user_readMore.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            Log.d(Utility.LOG_TAG + getClass().toString(), e.toString());
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

    private void loadCast() {
        Cursor castCursor = DataHelper.getCastforMovie(contentResolver, id);
        if (castCursor.getCount() != 0) {
            ArrayList<Casts> castsArrayList = new ArrayList<>();
            ArrayList<Casts> castsArrayListNoImage = new ArrayList<>();
            while (castCursor.moveToNext()) {
                String name, character, imagePath;
                character = castCursor.getString(castCursor.getColumnIndex(MovieContracts.CastEntry.COLUMN_CHARACTER));
                name = castCursor.getString(castCursor.getColumnIndex(MovieContracts.CastEntry.COLUMN_NAME));
                imagePath = castCursor.getString(castCursor.getColumnIndex(MovieContracts.CastEntry.COLUMN_IMAGEPATH));
                if (imagePath != null) {
                    castsArrayList.add(new Casts(id, name, character, imagePath));
                } else {
                    castsArrayListNoImage.add(new Casts(id, name, character, null));
                }
            }
            castCursor.close();
            castsArrayList.addAll(castsArrayListNoImage);
            setVisiblility(layout_cast,View.VISIBLE);
            data.setCasts(castsArrayList);
            castAdapter.setData(castsArrayList);
            isCastLoaded = true;
            if (!isFavourite) {
                removeFavourite();
            }
            return;
        }

        Uri uri = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_MOVIE_API)
                .appendPath(id + "")
                .appendPath(Utility.KEY_CREDITS_API)
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (getActivity() == null)
                            return;
                        ArrayList<Casts> castsArrayList = new ArrayList<>();
                        ArrayList<Casts> castsArrayListNoImage = new ArrayList<>();

                        try {
                            JSONArray castArray = response.getJSONArray(Utility.JSON_KEY_API_CASTS);
                            int id;
                            String name, character, image;
                            for (int i = 0; i < castArray.length(); ++i) {
                                JSONObject obj = castArray.getJSONObject(i);
                                id = obj.getInt(Utility.JSON_KEY_API_ID);
                                name = obj.getString(Utility.JSON_KEY_API_CAST_NAME);
                                character = obj.getString(Utility.JSON_KEY_API_CAST_CHARACTER);
                                image = obj.getString(Utility.JSON_KEY_API_CAST_PROFILE_PATH);
                                if (!image.equals("null")) {
                                    Casts cast = new Casts(id, name, character, image);
                                    castsArrayList.add(cast);
                                } else {
                                    image = null;
                                    Casts cast = new Casts(id, name, character, image);
                                    castsArrayListNoImage.add(cast);
                                }
                            }
                        } catch (JSONException e) {
                            Log.d(Utility.LOG_TAG + getClass().toString(), e.toString());
                        } finally {
                            castsArrayList.addAll(castsArrayListNoImage);
                            setVisiblility(layout_cast,View.VISIBLE);
                            data.setCasts(castsArrayList);
                            castAdapter.setData(castsArrayList);
                            isCastLoaded = true;
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
