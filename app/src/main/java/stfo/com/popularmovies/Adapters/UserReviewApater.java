package stfo.com.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import stfo.com.popularmovies.Constants.Review;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 26/10/16.
 */
public class UserReviewApater extends RecyclerView.Adapter<UserReviewApater.UserViewHolder>{

    private Context context;
    private ArrayList<Review> data;
    private LayoutInflater layoutInflater;
    private int color;

    public UserReviewApater(Context context, ArrayList<Review> data, int color) {
        this.context = context;
        this.data = data;
        this.color = color;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public UserReviewApater.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserReviewApater.UserViewHolder(layoutInflater.inflate(R.layout.layout_user_review_single, parent , false));
    }

    @Override
    public void onBindViewHolder(UserReviewApater.UserViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if(data == null)
            return 0;
        return data.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView userName, userReview;
        boolean expanded;

        public UserViewHolder(View itemView) {
            super(itemView);
            expanded = false;
            userName = (TextView) itemView.findViewById(R.id.textView_User_Name);
            userReview = (TextView) itemView.findViewById(R.id.textView_User_Reviews_Content);
            userName.setTextColor(color);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!expanded){
                        userReview.setEllipsize(null);
                        userReview.setMaxLines(Integer.MAX_VALUE);
                        expanded = true;
                    }else{
                        userReview.setEllipsize(TextUtils.TruncateAt.END);
                        userReview.setMaxLines(4);
                        expanded = false;
                    }
                }
            });
        }

        public void onBind(int postion){
            Review review = data.get(postion);
            userName.setText(review.getAuthor());
            userReview.setText(review.getReview());
        }
    }

}
