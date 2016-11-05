package stfo.com.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import stfo.com.popularmovies.Constants.Casts;
import stfo.com.popularmovies.Constants.DataHolder;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.SearchResults;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 29/10/16.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    private Context context;
    private ArrayList<SearchResults> data;
    private LayoutInflater layoutInflater;

    public SearchAdapter(Context context, ArrayList<SearchResults> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<SearchResults> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(layoutInflater.inflate(R.layout.layout_movie_discovery,parent,false));
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.onBindView(position);
    }

    @Override
    public int getItemCount() {
        if(data == null)
            return  0;
        return data.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView;
        private ImageView imageView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_movie_poster);
            textView = (TextView) itemView.findViewById(R.id.textView_movie_name);
            itemView.setOnClickListener(this);
        }

        public void onBindView(int position) {
            SearchResults curData = data.get(position);
            textView.setText(curData.getName());
            Glide.with(context)
                    .load(Utility.getw300Path(curData.getPoster_path()))
                    .centerCrop()
                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
            int position = this.getLayoutPosition();
            SearchResults curData = data.get(position);
            DataHolder holder = DataInstance.getTempInstance();
            holder.setTitle(curData.getName());
            holder.setBackdrop_path(curData.getPoster_path());
            holder.setId(curData.getId());
            if(curData.getType() == SearchResults.TYPE_MOVIE){
                if(context instanceof MainDataAdapter.OnMovieSelectedListener){
                    ((MainDataAdapter.OnMovieSelectedListener) context).onMovieSelected(
                            curData.getId()
                    );
                }
            }else{
                if(context instanceof CastAdapter.OnCastSelectedListener) {
                    ((CastAdapter.OnCastSelectedListener) context).onCastSelected(
                            curData.getId(),R.color.colorPrimaryDark
                    );
                }
            }
        }
    }
}

