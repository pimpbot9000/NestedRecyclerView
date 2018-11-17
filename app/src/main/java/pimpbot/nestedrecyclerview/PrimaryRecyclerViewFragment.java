package pimpbot.nestedrecyclerview;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PrimaryRecyclerViewFragment extends Fragment {

    private RecyclerView mPrimaryRecyclerView;
    private String[] mMoviesGenre, mActionMovies, mAdventureMovies, mComedyMovies;
    private int mCurrentRow = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoviesGenre = new String[]{
                "Action", "Adventure", "Comedy"
        };

        mActionMovies = new String[] {"Mission: Impossible – Rogue Nation",
                "Mad Max: Fury Road", "Star Wars: The Force Awakens",
                "Avengers: Age of Ultron", "Ant- Man","Terminator Genisys","Furious 7","Blackhat", "The Man from U.N.C.L.E",
                "Jurassic World"
        };

        mAdventureMovies = new String[] {"Inkkarijonkkari",
                "Inkkarijonkkari2", "Star Wars: The Force Awakens",
                "Avengers: Age of Ultron", "Ant- Man","Terminator Genisys","Furious 7","Blackhat", "The Man from U.N.C.L.E",
                "Jurassic World"
        };

        mComedyMovies = new String[] {"Pieru",
                "Kummeli", "Star Wars: The Force Farts Funny",
                "Avengers: Age of Ultron", "Ant- Man","Terminator Genisys","Furious 7","Blackhat", "The Man from U.N.C.L.E",
                "Jurassic World"
        };

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_recycler_view, container, false);

        // Creating the primary recycler view adapter


        LinearLayoutManager layoutManager = new CustomLinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false
        );

        PrimaryAdapter adapter = new PrimaryAdapter(mMoviesGenre);

        mPrimaryRecyclerView = (RecyclerView) view.findViewById(R.id.primary_recycler_view);
        mPrimaryRecyclerView.setLayoutManager(layoutManager);
        mPrimaryRecyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Custom layout manager to adjust scrolling speed
     */
    private class CustomLinearLayoutManager extends LinearLayoutManager{
        //TODO: remove the hardcoded scrolling speed
        private static final float MILLISECONDS_PER_INCH = 200f;
        public CustomLinearLayoutManager(Context ctx, int orientation, boolean reverseLayout){
            super(ctx, orientation, reverseLayout);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

            final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                @Override
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return super.computeScrollVectorForPosition(targetPosition);
                }

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                }
            };

            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }
    private class PrimaryViewHolder extends RecyclerView.ViewHolder {
        private TextView mPrimaryMovieGenre;
        private RecyclerView mSecondaryRecyclerView;


        public PrimaryViewHolder(View itemView) {
            super(itemView);
            mPrimaryMovieGenre = (TextView) itemView.findViewById(R.id.primary_movie_genre);
            mSecondaryRecyclerView = (RecyclerView) itemView.findViewById(R.id.secondary_recycler_view);
        }

        // This get called in PrimaryAdapter onBindViewHolder method
        public void bindViews(String genre, int position) {

            mPrimaryMovieGenre.setText(genre);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );

            mSecondaryRecyclerView.setLayoutManager(linearLayoutManager);
            mSecondaryRecyclerView.setAdapter(getSecondaryAdapter(position));
        }


    }

    private class PrimaryAdapter extends RecyclerView.Adapter<PrimaryViewHolder> {
        private String[] mMovieGenre;

        public PrimaryAdapter(String[] moviesGenre) {
            mMovieGenre = moviesGenre;
        }

        /**
         * Not necessary to overwrite: Provides a viewType for onCreateHolder method
         * @param position is the row number
         * @return row number
         */
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public PrimaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            //TODO: Inflate different type of views based on the viewType
            //R.layout.primary_recycler_view_item is a recyclerview
            View view = inflater.inflate(R.layout.primary_recycler_view_item, parent, false);
            //returns a basically a view which is a recyclerview
            return new PrimaryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PrimaryViewHolder holder, int position) {
            Log.d("jeejee", "binded!" + position);
            String genre = mMovieGenre[position];
            holder.bindViews(genre, position);
        }

        @Override
        public int getItemCount() {
            return mMovieGenre.length;
        }
    }

    private class SecondaryViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private int mRowNumber = 0;
        public SecondaryViewHolder(final View view, int row) {
            super(view);
            mRowNumber = row;
            View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        //check if row is changed: if so scroll the row in right position
                        if(mCurrentRow != mRowNumber) {
                            mPrimaryRecyclerView.smoothScrollToPosition(mRowNumber);
                            mCurrentRow = mRowNumber;
                        }
                        int position = getAdapterPosition();
                        Log.d("jeejee", "focus change: item " + position + " row: " + mRowNumber);
                    }
                }
            };

            itemView.setOnFocusChangeListener(listener);
            mTextView = (TextView) itemView.findViewById(R.id.secondary_text_view);
        }

        public void bindView(String name) {
            mTextView.setText(name);
        }
    }

    /**
     *
     */
    private class SecondaryAdapter extends RecyclerView.Adapter<SecondaryViewHolder> {
        private String[] mMovies;
        private int mRowNumber = 0;

        public SecondaryAdapter(String[] movies, int position) {
            mMovies = movies;
            mRowNumber = position;
        }

        @Override
        public SecondaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            //TODO: inflate different type of item based on row number
            View view = inflater.inflate(R.layout.secondary_recycler_view_item, parent, false);
            return new SecondaryViewHolder(view, mRowNumber);
        }

        @Override
        public void onBindViewHolder(SecondaryViewHolder holder, int position) {
            String name = mMovies[position];
            holder.bindView(name);
        }

        @Override
        public int getItemCount() {
            return mMovies.length;
        }
    }

    private SecondaryAdapter getSecondaryAdapter(int position) {


        // passes a row number as an argument
        switch (position) {
            case 0:
                return new SecondaryAdapter(mActionMovies, position);
            case 1:
                return new SecondaryAdapter(mAdventureMovies, position);
            case 2:
                return new SecondaryAdapter(mComedyMovies, position);
            default:
                return null;
        }
    }
}