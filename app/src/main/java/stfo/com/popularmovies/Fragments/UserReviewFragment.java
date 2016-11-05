package stfo.com.popularmovies.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stfo.com.popularmovies.Activities.MainActivity;
import stfo.com.popularmovies.Adapters.UserReviewApater;
import stfo.com.popularmovies.Constants.DataInstance;
import stfo.com.popularmovies.Constants.Utility;
import stfo.com.popularmovies.R;

/**
 * Created by Kartik Sharma on 26/10/16.
 */
public class UserReviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private int  color;

    public static UserReviewFragment newInstance(int color){
        UserReviewFragment userReviewFragment = new UserReviewFragment();
        userReviewFragment.color = color;
        return userReviewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_reviews,container,false);
        init(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            color = savedInstanceState.getInt(Utility.KEY_APP_INT_EXTRA_COLOR);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Utility.KEY_APP_INT_EXTRA_COLOR,color);
        super.onSaveInstanceState(outState);
    }

    private void init(View v){
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.reviews);
        toolbar.setBackgroundColor(color);
        Toolbar activityToolbar = ((MainActivity)getActivity()).getToolbar();
        if(activityToolbar == null)
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.reviews));


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new UserReviewApater(getContext(), DataInstance.getTempInstance().getReviews(),color));

    }

}
