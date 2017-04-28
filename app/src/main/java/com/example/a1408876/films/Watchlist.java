package com.example.a1408876.films;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by 1408876 on 12/04/2017.
 */

public class Watchlist extends Fragment {

    DatabaseHelper myDatabaseHelper;
    private ListView lv;
    private TextView tv;
    private ArrayList<Note> watchList;
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflates layouts
        View rootView = inflater.inflate(R.layout.watchlist, container, false);

        this.lv = (ListView) rootView.findViewById(R.id.watchlistId);
        myDatabaseHelper = new DatabaseHelper(getContext());
        //populates watchlist
        watchList = populateWatchList();
        if (watchList != null) {
            adapter = new MyAdapter(getContext(),watchList);
            lv.setAdapter(adapter);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        watchList = populateWatchList();
        if (watchList != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Note> populateWatchList(){
        //method for populating watchlist
        Log.d("Watchlist", "Displaying data in watchlist");
        Cursor data = myDatabaseHelper.getData();
            if (data.getCount() != 0) {
                ArrayList<Note> tempwatchlist = new ArrayList<Note>();

                data.moveToFirst();
                do{
                    Note n = new Note();
                    String title = data.getString(data.getColumnIndex("Title"));
                    String overview = data.getString(data.getColumnIndex("Overview"));
                    String releaseDate = data.getString(data.getColumnIndex("ReleaseDate"));
                    String originalTitle = data.getString(data.getColumnIndex("OriginalTitle"));
                    String originalLanguage = data.getString(data.getColumnIndex("OriginalLanguage"));
                    String popularity = data.getString(data.getColumnIndex("Popularity"));
                    String voteAverage = data.getString(data.getColumnIndex("VoteAverage"));
                    String voteCount = data.getString(data.getColumnIndex("VoteCount"));
                    String myNote = data.getString(data.getColumnIndex("MyNote"));
                    n.setTitle(title);
                    n.setOverview(overview);
                    n.setRelease_date(releaseDate);
                    n.setOriginal_title(originalTitle);
                    n.setOriginal_language(originalLanguage);
                    n.setPopularity(popularity);
                    n.setVote_average(voteAverage);
                    n.setVote_count(voteCount);
                    n.setNote(myNote);
                    tempwatchlist.add(n);
                }
                while (data.moveToNext());
                System.out.println(tempwatchlist);
                return tempwatchlist;
            }
            return null;
    }

    private class MyAdapter extends ArrayAdapter<Note> {
        private ArrayList<Note> movies;

        public MyAdapter(@NonNull Context context, @NonNull ArrayList<Note> movies) {
            super(context, R.layout.watchlist_item, movies);

            this.movies = movies;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View v = view;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.watchlist_item, parent, false);
            TextView textView = (TextView) v.findViewById(R.id.watchlistItem);
            textView.setText("Title" + "\n" + movies.get(position).getTitle() + "\nOverview\n" + movies.get(position).getOverview() +
            "\nRelease date\n" + movies.get(position).getRelease_date() + "\nOriginal title\n" + movies.get(position).getOriginal_title() +
            "\nOriginal language\n" + movies.get(position).getOriginal_language() + "\nPopularity\n" + movies.get(position).getPopularity() +
            "\nVote average\n" + movies.get(position).getVote_average() + "\nVote count\n" + movies.get(position).getVote_count() +
            "\nMy note\n" + movies.get(position).getNote());

            //button for deleting movies from database/watchlist
            Button btt = (Button) v.findViewById(R.id.deleteButton);
            btt.setOnClickListener(new MyOnClickListener(movies.get(position)));


            return v;
        }

        public class MyOnClickListener implements View.OnClickListener {
            Note n = new Note();
            public MyOnClickListener(Note n) {
                this.n = n;
            }
            public void onClick(View v) {
                Button delete = (Button) v.findViewById(R.id.deleteButton);
                TextView tv = (TextView) v.findViewById(R.id.watchlistItem);
                //deletes a movie from the database
                myDatabaseHelper.deleteData(n.getTitle(), n.getRelease_date());
                //disables button so user won't try to delete something more than once
                delete.setEnabled(false);
                delete.setText("Deleted!");
            }
        }
    }
}