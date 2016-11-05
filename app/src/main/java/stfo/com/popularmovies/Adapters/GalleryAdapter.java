package stfo.com.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import stfo.com.popularmovies.Constants.Trailer;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 22/10/16.
 */
public
class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Trailer> trailer;
    private ArrayList<String> images;

    private static final int VIEW_TRAILER = 0;
    private static final int VIEW_IMAGE = 1;

    public GalleryAdapter(Context context, ArrayList<Trailer> trailer, ArrayList<String> images){
        this.context = context;
        this.trailer = trailer;
        this.images = images;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setTrailer(ArrayList<Trailer> trailer){
        this.trailer = trailer;
        notifyDataSetChanged();
    }

    public void setImages(ArrayList<String> images){
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(trailer!=null && position<trailer.size())
            return VIEW_TRAILER;
        else
            return VIEW_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TRAILER :
                return new TrailerViewHolder(layoutInflater.inflate(R.layout.layout_trailer_single, parent, false));
            case VIEW_IMAGE:
                return new ImageViewHolder(layoutInflater.inflate(R.layout.layout_gallery_single,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type){
            case VIEW_IMAGE :
                if(trailer == null)
                    ((ImageViewHolder) holder).bind(position);
                else
                    ((ImageViewHolder) holder).bind(position-trailer.size());
                break;
            case VIEW_TRAILER:
                ((TrailerViewHolder) holder).bind(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(trailer!=null)
            count+=trailer.size();
        if(images!=null)
            count+=images.size();
        return count;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_gallery_image);
        }

        public void bind(int position){
            Glide.with(context)
                    .load(Utility.getw300Path(images.get(position)))
                    .centerCrop()
                    .into(imageView);
        }

    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        ImageView imageView;
        View view;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.textView_trailer_name);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_gallery_image);
            view.setOnClickListener(this);
        }

        public void bind(int position){
            textView.setText(trailer.get(position).getName());
            Glide.with(context)
                    .load(trailer.get(position).getThumbnail_url())
                    .centerCrop().into(imageView);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            context.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.get(position).getVideo_url()))
            );

        }
    }

}