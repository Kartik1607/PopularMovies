package stfo.com.popularmovies.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import stfo.com.popularmovies.Constants.DataHolder;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.Constants.VolleyInstance;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 20/10/16.
 */
public class MainDataAdapter extends RecyclerView.Adapter<MainDataAdapter.myViewHolder> {

    public interface OnMovieSelectedListener {
        public void onMovieSelected(int id);
    }

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<DataHolder> data;
    private int page;
    private int total_items, items_remaining;

    public MainDataAdapter(Context context, ArrayList<DataHolder> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
        page = 1;
        if (data == null)
            items_remaining = total_items = 0;
        else {
            items_remaining = total_items = data.size();
        }
    }

    public void setData(ArrayList<DataHolder> data) {
        if (data == null) {
            page = 1;
            items_remaining = total_items = 0;
        } else {
            items_remaining = total_items = data.size();
            page = data.size() / 20;
        }
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myViewHolder(layoutInflater.inflate(R.layout.layout_movie_discovery, parent, false));

    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        items_remaining = total_items - (position + 1);
        if (items_remaining == 5) {
            loadMore();
        }
        holder.onBindView(position);
    }

    private void loadMore() {
        ++page;
        RequestQueue requestQueue = VolleyInstance.getInstance(context).getRequestQueue();

        Uri getMainData = Uri.parse(Utility.basePathApi).buildUpon()
                .appendPath(Utility.KEY_MOVIE_API)
                .appendPath(Utility.KEY_USER_SELECTED_FILER)
                .appendQueryParameter(Utility.KEY_PAGE_API, page + "")
                .appendQueryParameter(Utility.KEY_API, Utility.VALUE_API_KEY)
                .build();

        final JsonObjectRequest jsonMainDataRequest = new JsonObjectRequest(Request.Method.GET,
                getMainData.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<DataHolder> data = DataInstance.getInstance();

                        try {
                            JSONArray results = response.getJSONArray(Utility.JSON_KEY_API_RESULTS);
                            for (int i = 0; i < results.length(); ++i) {
                                int id, vote_count;
                                String poster_path, backdrop_path, title, release_date, overview;
                                double rating;
                                JSONObject current_Obj = results.getJSONObject(i);
                                id = current_Obj.getInt(Utility.JSON_KEY_API_ID);
                                vote_count = current_Obj.getInt(Utility.JSON_KEY_API_VOTE_COUNT);
                                poster_path = current_Obj.getString(Utility.JSON_KEY_API_POSTER_PATH);
                                backdrop_path = current_Obj.getString(Utility.JSON_KEY_API_BACKDROP_PATH);
                                title = current_Obj.getString(Utility.JSON_KEY_API_TITLE);
                                release_date = current_Obj.getString(Utility.JSON_KEY_API_RELEASE_DATE);
                                overview = current_Obj.getString(Utility.JSON_KEY_API_OVERVIEW);
                                rating = current_Obj.getDouble(Utility.JSON_KEY_API_RATING);
                                data.add(new DataHolder()
                                        .setId(id)
                                        .setVote_count(vote_count)
                                        .setPoster_path(poster_path)
                                        .setBackdrop_path(backdrop_path)
                                        .setTitle(title)
                                        .setRelease_date(release_date)
                                        .setOverview(overview)
                                        .setRating(rating));
                            }
                        } catch (JSONException e) {
                            Log.d(Utility.LOG_TAG + getClass().toString(), e.toString());
                        } finally {
                            setData(data);
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
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;
        private View view;

        public myViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.imageView_movie_poster);
            textView = (TextView) itemView.findViewById(R.id.textView_movie_name);
            view.setOnClickListener(this);
        }

        public void onBindView(int position) {
            DataHolder localdata = data.get(position);
            DrawableRequestBuilder<String> thumbnailRequest = Glide.with(context)
                    .load(Utility.getw92Path(localdata.getPoster_path()))
                    .bitmapTransform(new BlurTransformation(context));

            Glide.with(context)
                    .load(Utility.getw300Path(localdata.getPoster_path()))
                    .thumbnail(thumbnailRequest)
                    .centerCrop()
                    .into(imageView);
            textView.setText(localdata.getTitle());
        }

        @Override
        public void onClick(View v) {
            int position = this.getLayoutPosition();
            if (context instanceof OnMovieSelectedListener) {
                ((OnMovieSelectedListener) context).onMovieSelected(data.get(position).getId());
            }
        }
    }
}

