package stfo.com.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import stfo.com.popularmovies.BackEnd.DataHelper;
import stfo.com.popularmovies.BackEnd.MovieContracts;
import stfo.com.popularmovies.Constants.DataHolder;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 05/11/16.
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.myViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<DataHolder> data;

    public FavouriteAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        data = new ArrayList<>();
        loadData();
    }

    public void addData(){
        DataHolder dataItem = new DataHolder();
        DataHolder cur = DataInstance.getTempInstance();
        dataItem.setId(cur.getId());
        dataItem.setTitle(cur.getTitle());
        dataItem.setPoster_path(cur.getPoster_path());
        data.add(dataItem);
        notifyItemInserted(getItemCount()-1);
    }

    public void removeData(){
        int position = data.indexOf(DataInstance.getTempInstance());
        data.remove(position);
        notifyItemRemoved(position);
        for(int i = 0 ; i < data.size(); ++i){
            Log.d("MY_APP",data.get(i).getTitle());
        }
    }

    private void loadData(){
        Cursor cursor = DataHelper.getFavourites(context.getContentResolver());
        if(cursor == null)
            return;
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); ++i){
            DataHolder dataItem = new DataHolder();
            dataItem.setId(cursor.getInt(cursor.getColumnIndex(MovieContracts.MovieEntry._ID)));
            dataItem.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_POSTER)));
            dataItem.setTitle(cursor.getString(cursor.getColumnIndex(MovieContracts.MovieEntry.COLUMN_MOVIE_NAME)));
            data.add(dataItem);
            cursor.moveToNext();
        }
        cursor.close();
        notifyDataSetChanged();
    }

    @Override
    public FavouriteAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myViewHolder(inflater.inflate(R.layout.layout_favourite,parent,false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.myViewHolder holder, int position) {
        holder.onBind(position);
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView textView;
        private View v;

        public myViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            textView = (TextView) v.findViewById(R.id.textView_movie_name);
            imageView = (ImageView) v.findViewById(R.id.imageView_movie_poster);
            v.setOnClickListener(this);
        }

        public void onBind(int position) {
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
            if(context instanceof MainDataAdapter.OnMovieSelectedListener){
                int position = this.getAdapterPosition();
                DataHolder current = data.get(position);
                ((MainDataAdapter.OnMovieSelectedListener) context).onMovieSelected(current.getId());

            }
        }
    }
}
