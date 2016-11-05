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

import stfo.com.popularmovies.Constants.DataHolder;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.MovieCredits;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 28/10/16.
 */
public
class MovieCreditsAdapter extends RecyclerView.Adapter<MovieCreditsAdapter.MovieViewHolder>{

    private Context context;
    private ArrayList<MovieCredits> movieCredits;
    private LayoutInflater layoutInflater;

    public MovieCreditsAdapter(Context context, ArrayList<MovieCredits> movieCredits) {
        this.context = context;
        this.movieCredits = movieCredits;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setMovieCredits(ArrayList<MovieCredits> credits){
        this.movieCredits = credits;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(layoutInflater.inflate(R.layout.layout_cast_single,parent,false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if(movieCredits==null)
            return 0;
        return movieCredits.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView_movie, textView_character;
        ImageView imageView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            textView_movie = (TextView) itemView.findViewById(R.id.textView_cast_name);
            textView_character = (TextView) itemView.findViewById(R.id.textView_cast_character);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_cast_poster);
            itemView.setOnClickListener(this);
        }

        public void onBind(int position){
            Glide.with(context)
                    .load(Utility.getw300Path(movieCredits.get(position).getImage_path()))
                    .centerCrop()
                    .into(imageView);
            textView_movie.setText(movieCredits.get(position).getMovie_name());
            textView_character.setText(movieCredits.get(position).getCharacter());
        }

        @Override
        public void onClick(View v) {
            DataHolder holder = DataInstance.getTempInstance();
            holder.setTitle(movieCredits.get(this.getAdapterPosition()).getMovie_name());
            holder.setPoster_path(movieCredits.get(this.getAdapterPosition()).getImage_path());
            holder.setId(movieCredits.get(this.getAdapterPosition()).getMovie_id());
            holder.setRating(0);
            holder.setOverview(null);
            holder.setVote_count(0);
            holder.setBackdrop_path(null);
            if(context instanceof MainDataAdapter.OnMovieSelectedListener){
                ((MainDataAdapter.OnMovieSelectedListener) context).onMovieSelected(
                        movieCredits.get(this.getAdapterPosition()).getMovie_id());
            }

        }
    }

}

