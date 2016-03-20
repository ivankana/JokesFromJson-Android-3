package ivan.jokes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    //Link to the Chuck Norris jokes (contains 584 jokes)
    private static final String URLPath = "http://api.icndb.com/jokes";


    //This is the second joke and is a global field because JSONTask class needs to reach this variable
    private String joke2Result;
    //TextView
    private TextView joke, joke2;

    /**
     * Was used for for listView
     */
    //ArrayList<String> jokesArray = new ArrayList<String>();
    //customArrayAdapter adapter = new customArrayAdapter(this,jokesArray);
    //ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting objects from xml
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button button = (Button) findViewById(R.id.button);
        joke = (TextView) findViewById(R.id.joke);
        joke2 = (TextView) findViewById(R.id.joke2);
        ImageView img = (ImageView) findViewById(R.id.imageView);

            //listView = (ListView) findViewById(R.id.listView);
            //listView.setAdapter(adapter);

        //Setting the image & toolbar
        img.setImageResource(R.mipmap.chucknorris);
        setSupportActionBar(toolbar);

        //When button is clicked get jokes, passing URL path to JsonTask class
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute(URLPath);
            }

        });

    }


    /**
     * AsyncTask provides easy use of UI thread
     * If you want to change anything on the User Interface, it can only be done on the main thread
     * But Android doesn't allow you to make network calls on the UI thread, and can cause the app to crash due to ANR error if an task takes more than five seconds
     * Thats why we have to use AsyncTask. Asynctask works in the background thread and publishes the result in the UI thread
     * doInBackground() - runs on a background thread
     * onPostExecute() - Runs on main thread
     */
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //had to make them equals to null otherwise will get error "is not initialized"
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            //Have to use try catch blocks to handle possible errors
            try {
                //Establishing a connection to the URL server and connecting
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                //Storing the data from connection to the inputStream
                InputStream stream = connection.getInputStream();

                //Creating a bufferReader to read the data from stream
                reader = new BufferedReader((new InputStreamReader(stream)));


                StringBuffer buffer = new StringBuffer();

                //BufferReader to read everyline and storing it to StringBuffer
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                //Setting bufferText as the stringBuffer
                String bufferText = buffer.toString();

                //Setting the JsonObject
                JSONObject jsonObject = new JSONObject((bufferText));

                //Getting the array "value" from JSON object
                JSONArray jokeArray = jsonObject.getJSONArray("value");

                //Creating a random number of a length of the joke array which will be 584
                Random r = new Random();
                int id1 = r.nextInt(jokeArray.length());
                //Getting the random id and joke, setting it to string variable "joke1" (this is better/faster than looping and picking a random)
                String joke1 = jokeArray.getJSONObject(id1).getString("joke");

                int id2 = r.nextInt(jokeArray.length());
                String joke2 = jokeArray.getJSONObject(id2).getString("joke");

                //Creating the result string to include the joke and id (ex: jokeText (idNumber))
                String joke1Result = joke1 + " (" + id1 +")";
                joke2Result = joke2 + " (" + id2 +")";
                return joke1Result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException je){
                je.printStackTrace();
            }

            finally {
                //Closing the connection and buffered reader if the value is not null
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //jokesArray.add(result);

            //Setting the text to view the jokes
            joke.setText(result);
            //setting the second joke from the global field
            joke2.setText(joke2Result);
        }


    }

}

