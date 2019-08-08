/**
 * NAME: ANJOLAOLUWA ARIYIKE ADETIMEHIN
 * STUDENT NO.: S1719003
 * **/

package org.me.gcu.weatherapp.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.me.gcu.weatherapp.R;
import org.me.gcu.weatherapp.models.Item;
import org.me.gcu.weatherapp.utils.ViewPagerAdapter;
import org.me.gcu.weatherapp.utils.WeatherXMLParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String weatherCond;
    String weatherDesc;
    TextView viewDetails;
    ViewPager viewPager;
    TextView homeCondition;
    TextView location;
    TextView homeTodayDate;
    TextView homeTodayTemp;
    ImageView homeWeatherPng;

    final Context context = this;
    String hour;
    String minute;
    private static final String TAG ="MainActivity";
    PendingIntent myPendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver myBroadcastReceiver;
    Calendar firingCal;

    public String active;
    List <Item> passThis;

    List<Item> bangResult;
    List<Item> glasResult;
    List<Item> lagosResult;
    List<Item> londonResult;
    List<Item> maurResult;
    List<Item> newResult;
    List<Item> omanResult;


    Date todayDate = new Date();
    @SuppressLint("SimpleDateFormat") //formatting
    DateFormat dateFormat = new SimpleDateFormat("EEE, dd, MMMM yyyy");

    String dateString = dateFormat.format(todayDate);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        INITIALISE THE VIEWS FOR THIS ACTIVITY
        homeCondition = findViewById(R.id.homeCondition);
        viewPager = findViewById(R.id.locSelect);
        location = findViewById(R.id.homeLocation);
        homeTodayDate = findViewById(R.id.homeTodayDate);
        homeTodayTemp = findViewById(R.id.homeTodayTemp);
        homeWeatherPng = findViewById(R.id.homeWeatherPng);
        viewDetails = findViewById(R.id.viewDetails);

        //SET UP THE VIEWPAGER ADAPTER FOR THE LOCATION MENU
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        //Adding today's date
        homeTodayDate.setText(dateString);


        //--------------------------------------
        //LOAD THE DATA FROM THE RSS FEED INTO THE APPROPRIATE LISTS
        //--------------------------------------
        try {
            loadInitData();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //--------------------------------------
        //--------------------------------------

        onPostExec(bangResult);
        active = getString(R.string.bangladeshImg);

        //SET UP THE CHANGE LISTENER FOR THE LOCATION MENU TO CHANGE THE LOCATION TEXT ACCORDINGLY
        //THIS ACTS AS THE PRIMARY MENU FOR CONTROLLING THE LOADING OF THE APPROPRIATE LOCATION DATA
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            //this sets what location's data is visible on the main screen.
            public void onPageSelected(int position) {
                if (position == 0) {
                    location.setText(getString(R.string.bangladeshLoc));
                    active = getString(R.string.bangladeshImg);

                    onPostExec(bangResult);
                }
                else if (position == 1) {
                    location.setText(getString(R.string.glasgowLoc));
                    active = getString(R.string.glasgowImg);

                    onPostExec(glasResult);
                }
                else if (position == 2) {
                    location.setText(getString(R.string.lagosLoc));
                    active = getString(R.string.lagosImg);

                    onPostExec(lagosResult);
                }
                else if (position == 3) {
                    location.setText(getString(R.string.londonLoc));
                    active = getString(R.string.londonImg);

                    onPostExec(londonResult);
                }
                else if (position == 4) {
                    location.setText(getString(R.string.mauritiusLoc));
                    active = getString(R.string.mauritiusImg);

                    onPostExec(maurResult);
                }
                else if (position == 5) {
                    location.setText(getString(R.string.newYorkLoc));
                    active = getString(R.string.newYorkImg);

                    onPostExec(newResult);
                }
                else if (position == 6) {
                    location.setText(getString(R.string.omanLoc));
                    active = getString(R.string.omanImg);

                    onPostExec(omanResult);
                }
            }
        });

        //END OF LOCATION MENU SETUP

        viewDetails.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                Intent intent = new Intent(getApplicationContext(), LocationWeather.class);


                if (active.equalsIgnoreCase("bangladesh")){
                    passThis = bangResult;
                }
                else if (active.equalsIgnoreCase("glasgow")){
                    passThis = glasResult;
                }
                else if (active.equalsIgnoreCase("lagos")){
                    passThis = lagosResult;
                }
                else if (active.equalsIgnoreCase("london")){
                    passThis = londonResult;
                }
                else if (active.equalsIgnoreCase("mauritius")){
                    passThis = maurResult;
                }
                else if (active.equalsIgnoreCase("new_york")){
                    passThis = newResult;
                }
                else if (active.equalsIgnoreCase("oman")){
                    passThis = omanResult;
                }

                intent.putExtra("itemFeed", (Serializable) passThis);

                intent.putExtra("currentLocation", location.getText());
                startActivity(intent);

            }
        });
    }
    //END OF ON CREATE METHOD

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get prompts.xml view
        //THE DIALOG CREATION WAS WRITTEN WITH ASSISTANCE FROM: https://www.mkyong.com/android/android-prompt-user-input-dialog-example/
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompt.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userHour = promptsView.findViewById(R.id.editHour);
        final EditText userMinute = promptsView.findViewById(R.id.editMinute);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result

                                hour = String.valueOf(userHour.getText());
                                minute = String.valueOf(userMinute.getText());

                                int hourInt = Integer.parseInt(hour);
                                int minuteInt = Integer.parseInt(minute);

                                Toast.makeText(getApplicationContext(),"Refresh time set for "+hour+":"+minute,Toast.LENGTH_SHORT).show();

                                firingCal= Calendar.getInstance();
                                firingCal.set(Calendar.HOUR, hourInt); // At the hour you want to fire the alarm
                                firingCal.set(Calendar.MINUTE, minuteInt); // alarm minute
                                firingCal.set(Calendar.SECOND, 0); // and alarm second
                                long intendedTime = firingCal.getTimeInMillis();


                                registerMyAlarmBroadcast();
                                alarmManager.set( AlarmManager.RTC_WAKEUP, intendedTime, myPendingIntent );

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        return true;
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

    }

    //THIS WILL RUN AFTER THE ASYNC TASK IS DONE. IT WORKS WITH THE LOCATION MENU
    public void onPostExec(List<Item> result){

        weatherDesc = result.get(0).desc;
        getTemperature(weatherDesc); //self-written method to get the current temperature


        weatherCond = result.get(0).title;
        setCondition(weatherCond); //self-written method to get the current weather condition
    }

    //----------------------------------------------
    // USES THE ASYNC TASK 'DOWNLOADXMLTASK' TO GET DATA FROM BBC LINKS
    public void loadInitData () throws ExecutionException, InterruptedException{
            DownloadXmlTask taskBang = new DownloadXmlTask();
        DownloadXmlTask taskGlas = new DownloadXmlTask();
        DownloadXmlTask taskLagos = new DownloadXmlTask();
        DownloadXmlTask taskLondon = new DownloadXmlTask();
        DownloadXmlTask taskMaur = new DownloadXmlTask();
        DownloadXmlTask taskNew = new DownloadXmlTask();
        DownloadXmlTask taskOman = new DownloadXmlTask();

        taskBang.execute("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241");
        taskGlas.execute("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579");
        taskLagos.execute("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2332459");
        taskLondon.execute("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743");
        taskMaur.execute("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154");
        taskNew.execute("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581");
        taskOman.execute("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286");

        bangResult = taskBang.get();
        glasResult = taskGlas.get();
        lagosResult = taskLagos.get();
        londonResult = taskLondon.get();
        maurResult = taskMaur.get();
        newResult = taskNew.get();
        omanResult = taskOman.get();

    }
    //----------------------------------------------
    //----------------------------------------------

    //----------------------------------------------
    // Implementation of AsyncTask used to download XML feed from BBC
    private class DownloadXmlTask extends AsyncTask<String, Void, List<Item>> {
        @Override
        protected List<Item> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null;
            } catch (XmlPullParserException e) {
                return null;
            }
        }

    }
    //----------------------------------------------
    //----------------------------------------------

    //----------------------------------------------
    // BEGINNING OF THE LOAD XML FROM NETWORK METHOD WHICH THE ASYNC TASK USES
    private List<Item> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        WeatherXMLParser weatherXMLParser = new WeatherXMLParser();
        List<Item> items = null;

        try {
            stream = downloadUrl(urlString);
            items = weatherXMLParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return items;
    }
    //END OF LOAD XML FROM NETWORK METHOD
    //----------------------------------------------

    //----------------------------------------------
    //GIVEN A STRING URL, THIS SETS UP A CONNECTION AND GETS AN INPUT STREAM
    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
    //END OF DOWNLOAD URL METHOD
    //----------------------------------------------


    //----------------------------------------------
    //GETS THE TEMPERATURE FOR THE SPECIFIED LOCATION AND DISPLAYS IT
    public void getTemperature (String tempDesc){

        String[] weatherDetails = tempDesc.split(",");

        String[] weatherTemperature = weatherDetails[0].split(":");
        String[] tempBoth = weatherTemperature[1].split("\\(");

        String temp = tempBoth[0];

        homeTodayTemp.setText(temp);
    }
    //END OF METHOD
    //----------------------------------------------

    //----------------------------------------------
    //SET CONDITION METHOD TO PROCESS AND DISPLAY NECESSARY WEATHER DETAILS
    public void setCondition (String weatherCond){
        String [] splitWeatherCond = weatherCond.split(",");
        String [] splitPng = splitWeatherCond[0].split(":");

        String weatherPng = splitPng[1].toLowerCase();
        System.out.println(weatherPng);

        if (weatherPng.contains("cloud")){
            homeWeatherPng.setImageResource(R.drawable.cloudy);
        }
        else if (weatherPng.contains("sun")){
            homeWeatherPng.setImageResource(R.drawable.sunny);
        }
        else if (weatherPng.contains("thunder")){
            homeWeatherPng.setImageResource(R.drawable.thunder);
        }
        else if (weatherPng.contains("rain")){
            homeWeatherPng.setImageResource(R.drawable.rain);
        }
        else if (weatherPng.contains("snow")){
            homeWeatherPng.setImageResource(R.drawable.snowflake);
        }
        else if (weatherPng.contains("clear")){
            homeWeatherPng.setImageResource(R.drawable.clear);
        }
        else if (weatherPng.contains("fog") || weatherPng.contains("mist") || weatherPng.contains("haz")){
            homeWeatherPng.setImageResource(R.drawable.fog);
        }
        else if (weatherPng.contains("hail")){
            homeWeatherPng.setImageResource(R.drawable.hail);
        }
        else if (weatherPng.contains("sleet")){
            homeWeatherPng.setImageResource(R.drawable.sleet);
        }
        else if (weatherPng.contains("drizzle")){
            homeWeatherPng.setImageResource(R.drawable.drizzle);
        }
        else {
            homeWeatherPng.setImageResource(R.drawable.sunny);
        }

        homeCondition.setText(weatherPng);
    }
    //END OF SET CONDITION METHOD
    //----------------------------------------------


    //FOLLOWING CODE WRITTEN WITH HELP FROM https://stackoverflow.com/questions/35703898/schedule-a-timertask-at-specific-time-once-per-day-in-a-service-in-android
    private void registerMyAlarmBroadcast()
    {
        //This is the call back function(BroadcastReceiver) which will be called when your
        //alarm time will reached.
        myBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.i(TAG,"BroadcastReceiver::OnReceive()");
                Toast.makeText(context, "Data from BBC refreshing...", Toast.LENGTH_LONG).show();
                try {
                    loadInitData();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        registerReceiver(myBroadcastReceiver, new IntentFilter("android.intent.action.MAIN") );
        myPendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("android.intent.action.MAIN"),0 );
        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }
}


