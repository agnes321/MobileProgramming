package com.example.a1408876.films;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;


public class Search extends Fragment {

    private SearchView searchField;
    private ListView lv;
    public ArrayList<Movie> movies;
    private Button b;
    private TextView tv;
    private String s;
    DatabaseHelper myDatabaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //sets layout to search.xml
        View rootView = inflater.inflate(R.layout.search, container, false);

            //creates needed areas and gives them ids from search.xml file
            this.searchField = (SearchView) rootView.findViewById(R.id.searchView);
            this.lv = (ListView) rootView.findViewById(R.id.listViewId);
            movies = new ArrayList<>();
            this.searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                //executes tasks for user input(query)
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("Why", "Started query" + query);
                    searchField.clearFocus();
                    DownloadFilesTask task = new DownloadFilesTask();
                    task.execute(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
                }
            });
            return rootView;
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, ArrayList<Movie>> {


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            try {
                //gets data from the movie db
                movies.clear();
                Log.d("Why", "Started task");
                Log.d("Why", params[0]);
                String urlString = "https://api.themoviedb.org/3/search/movie?api_key=c3b71235edcd1d31c7043892fc4dcf4d&language=en-US&query=" + params[0] + "&page=1&include_adult=false";
                Log.d("Why", urlString);
                URL url = new URL(urlString);
                Log.d("Why", "all fine");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                BufferedReader bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String currentLine;
                while ((currentLine = bufReader.readLine()) != null) {
                    response.append(currentLine);
                }
                bufReader.close();
                connection.disconnect();


                //puts data into json object
                JSONObject Jobject = new JSONObject(response.toString());
                Log.d("Why", response.toString());
                //puts data in json objects to json array
                JSONArray Jarray = Jobject.getJSONArray("results");


                for (int i = 0; i < Jarray.length(); i++) {
                    JSONObject object = Jarray.getJSONObject(i);

                    Movie tempMovie = new Movie();

                    tempMovie.setTitle(object.getString("title"));
                    tempMovie.setOverview(object.getString("overview"));
                    tempMovie.setRelease_date(object.getString("release_date"));
                    tempMovie.setOriginal_title(object.getString("original_title"));
                    tempMovie.setOriginal_language(object.getString("original_language"));
                    tempMovie.setPopularity(object.getString("popularity"));
                    tempMovie.setVote_average(object.getString("vote_average"));
                    tempMovie.setVote_count(object.getString("vote_count"));

                    movies.add(tempMovie);


                }
                Log.d("Why", movies.toString());
            } catch (IOException e) {
                //Log.d("Why", e.getMessage());

            } catch (JSONException e) {
               // Log.d("Why", e.getMessage());
            }
            return new ArrayList<>();
        }
        protected void onPostExecute(ArrayList<Movie> result) {
            for (int i = 0; i < movies.size(); i++) {
                //outputs search results from the array list of movie objects
                System.out.println(movies.get(i).toString());
            }
            //needed to put data from array list of movie objects to list view and
            //set the id of that list view
            MyAdapter adapter = new MyAdapter(getActivity().getBaseContext(), movies);
            ((ListView) getActivity().findViewById(R.id.listViewId)).setAdapter(adapter);
        }
    }


    public class MyAdapter extends ArrayAdapter<Movie> {


        public MyAdapter(@NonNull Context context, @NonNull ArrayList<Movie> objects) {
            super(context, R.layout.list_item, objects);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View v = view;
            //inflates layout of list_item.xml
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);

            //sets data that was found into text view
            TextView textView = (TextView) v.findViewById(R.id.textItem);
            textView.setText("Title\n" + movies.get(position).getTitle() + "\nOverview\n" +
            movies.get(position).getOverview() + "\nRelease date\n" + movies.get(position).getRelease_date() +
            "\nOriginal title\n" + movies.get(position).getOriginal_title() +
            "\nOriginal language\n" + movies.get(position).getOriginal_language() +
            "\nPopularity\n" + movies.get(position).getPopularity() +
            "\nVote average\n" + movies.get(position).getVote_average() +
            "\nVote count\n" + movies.get(position).getVote_count());

            //button for saving
            Button btt = (Button) v.findViewById(R.id.saveButton);
            myDatabaseHelper = new DatabaseHelper(Search.this.getActivity());
            boolean checking = myDatabaseHelper.checkIfDataExists(movies.get(position).getOverview());

            btt.setOnClickListener(new MyOnClickListener(movies.get(position)));

            if (checking){
                //checks if that movie was already saved
                //and if it was, won't let it be saved again
                btt.setEnabled(false);
                btt.setText("Saved to your watchlist!");
            } else{
            }

            return v;
        }




    }

    public void addNewData(String title, String overview, String rd, String ot, String ol, String p, String va, String vc, String n){
        //method for saving data found
        myDatabaseHelper = new DatabaseHelper(Search.this.getActivity());
        boolean insertData = myDatabaseHelper.addData(title, overview, rd, ot, ol, p, va, vc, n);
        if(insertData){
            Log.d("Success", "Data inserted");
            System.out.println("Yes");
        }
        else{
            Log.d("Failure", "Data not inserted");
            System.out.println("No");
        }
    }

    public class MyOnClickListener implements View.OnClickListener {
        private Movie movie;

        public MyOnClickListener(Movie movie) {
            this.movie = movie;
        }

        public void onClick(View v) {
            //gets data that will be saved
            String t = movie.getTitle();
            String o = movie.getOverview();
            String rd = movie.getRelease_date();
            String ot = movie.getOriginal_title();
            String ol = movie.getOriginal_language();
            String p = movie.getPopularity();
            String va = movie.getVote_average();
            String vc = movie.getVote_count();

            //disabled save button and changes its text when it is clicked
            ((Button)v.findViewById(R.id.saveButton)).setEnabled(false);
            ((Button)v.findViewById(R.id.saveButton)).setText("Saved to your watchlist!");
            //adds new data to the database
            addNewData(t, o, rd, ot, ol, p, va, vc, "");
        }
    }

}
