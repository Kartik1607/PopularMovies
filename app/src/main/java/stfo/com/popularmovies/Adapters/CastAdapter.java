package stfo.com.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import stfo.com.popularmovies.Constants.Casts;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 22/10/16.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    public interface OnCastSelectedListener{void onCastSelected(int person_id, int color);}

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Casts> data;

    private int  color;

    public CastAdapter(Context context, ArrayList<Casts> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CastViewHolder(layoutInflater.inflate(R.layout.layout_cast_single,parent,false));
    }

    public void setColor(int color){
        this.color = color;
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        holder.onBind(position);
    }

    public void setData(ArrayList<Casts> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class CastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView_poster;
        private TextView textView_name;
        private TextView textView_character;
        private View view;

        public CastViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView_poster = (ImageView) itemView.findViewById(R.id.imageView_cast_poster);
            textView_name = (TextView) itemView.findViewById(R.id.textView_cast_name);
            textView_character = (TextView) itemView.findViewById(R.id.textView_cast_character);
            view.setOnClickListener(this);
        }

        public void onBind(int position) {
            Casts current = data.get(position);
            if(current.getImagePath()!=null) {
                imageView_poster.setVisibility(View.VISIBLE);

                DrawableRequestBuilder<String> thumbnailRequest = Glide.with(context)
                        .load(Utility.getw92Path(current.getImagePath()))
                        .bitmapTransform(new BlurTransformation(context));

                Glide.with(context)
                        .load(Utility.getw300Path(current.getImagePath()))
                        .thumbnail(thumbnailRequest)
                        .centerCrop().into(imageView_poster);
            }
            else
                imageView_poster.setVisibility(View.GONE);
            textView_name.setText(current.getName());
            textView_character.setText(current.getRole());
        }

        @Override
        public void onClick(View v) {
            if(context instanceof OnCastSelectedListener){
                ((OnCastSelectedListener) context).onCastSelected(data.get(
                        this.getLayoutPosition()).getId(), color);
            }
        }
    }
}
