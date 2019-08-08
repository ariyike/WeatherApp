/**
 * NAME: ANJOLAOLUWA ARIYIKE ADETIMEHIN
 * STUDENT NO.: S1719003
 * **/

package org.me.gcu.weatherapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.me.gcu.weatherapp.R;
import org.me.gcu.weatherapp.models.Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocationWeather extends AppCompatActivity {

    TextView currentDate;
    TextView currentTemp;
    ImageView todayWeatherImg;
    ImageView tomorrowWeatherImg;
    ImageView nextWeatherImg;
    TextView today;
    TextView tomorrow;
    TextView dayAfter;
    TextView pressureCond;
    TextView humidityCond;
    TextView uvCond;
    TextView visibilityCond;
    TextView directionCond;
    TextView speedCond;

    TextView locPgTwo;
    ImageView locPgTwoIcon;

    TextView todayWeatherCond;
    TextView tomorrowWeatherCond;
    TextView nextWeatherCond;

    Item todayData;
    Item tomorrowData;
    Item dayAfterData;

    String Title1;
    String Title2;
    String Title3;

    String Description1;
    String Description2;
    String Description3;


    Date todayDate = new Date();
    @SuppressLint("SimpleDateFormat") //formatting
            DateFormat dateFormat = new SimpleDateFormat("EEE, dd, MMMM yyyy");

    String todayDateString = dateFormat.format(todayDate);

    Date tomorrowDate = addDays(todayDate, 1);
    String tomorrowDateString = dateFormat.format(tomorrowDate);

    Date nextDate = addDays(todayDate, 2);
    String nextDateString = dateFormat.format(nextDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_weather);


        Intent intent = getIntent();
        String test = intent.getStringExtra("currentLocation");
        List<Item> myList = (List<Item>) getIntent().getSerializableExtra("itemFeed");

        todayData = myList.get(0);
        tomorrowData = myList.get(1);
        dayAfterData = myList.get(2);

        currentDate = findViewById(R.id.currentDate);
        currentTemp = findViewById(R.id.currentTemp);

        todayWeatherImg = findViewById(R.id.todayWeatherImg);
        tomorrowWeatherImg = findViewById(R.id.tomorrowWeatherImg);
        nextWeatherImg = findViewById(R.id.nextWeatherImg);

        today = findViewById(R.id.today); //day of week
        tomorrow = findViewById(R.id.tomorrow); //day of week
        dayAfter = findViewById(R.id.dayAfter); //day of week

        pressureCond = findViewById(R.id.pressureCond);
        humidityCond = findViewById(R.id.humidityCond);
        uvCond = findViewById(R.id.uvCond);
        visibilityCond = findViewById(R.id.visibilityCond);
        directionCond = findViewById(R.id.directionCond);
        speedCond = findViewById(R.id.speedCond);

        locPgTwoIcon = findViewById(R.id.locPgTwoIcon);
        locPgTwo = findViewById(R.id.locPgTwo); //current location at the top of the screen
        locPgTwo.setText(test);
        setLocIcon(test);

        todayWeatherCond = findViewById(R.id.todayWeatherCond);
        tomorrowWeatherCond = findViewById(R.id.tomorrowWeatherCond);
        nextWeatherCond = findViewById(R.id.nextWeatherCond);


        currentDate.setText(todayDateString);

        Title1 = todayData.title;
        Title2 = tomorrowData.title;
        Title3 = dayAfterData.title;

        Description1 = todayData.desc;
        Description2 = tomorrowData.desc;
        Description3 = dayAfterData.desc;

        setInitTemperature(Description1);

        setDays(Title1, Title2, Title3);
        setInitWeatherImg(Title1, Title2, Title3);

        todayWeatherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadToday();
            }
        });

        tomorrowWeatherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTomorrow();
            }
        });

        nextWeatherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDayAfter();
            }
        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    public void setDays(String Title1, String Title2, String Title3){
        String[] TitleSentences = {Title1, Title2, Title3 };
        String[] days = new String[3];

        for (int i = 0; i<3; i++){

            String [] splitWeatherCond = TitleSentences[i].split(",");
            String [] splitPng = splitWeatherCond[0].split(":");

            String weekDay = splitPng[0];

            days[i]=weekDay;

        }

        today.setText(days[0]);
        tomorrow.setText(days[1]);
        dayAfter.setText(days[2]);

    }

    public void setInitWeatherImg(String Title1, String Title2, String Title3){

        setConditionToday(Title1);
        setConditionTomorrow(Title2);
        setConditionDayAfter(Title3);

    }

    public void setInitTemperature(String todayDet){

        String[] weatherDetails = todayDet.split(",");

        String[] weatherTemperature = weatherDetails[0].split(":");
        String[] tempBoth = weatherTemperature[1].split("\\(");

        String temp = tempBoth[0];

        currentTemp.setText(temp);

        //ALSO SET THE OTHER WEATHER DETAILS FOR TODAY
        String [] generalDesc = todayDet.split(",");

        String [] directionArray = generalDesc[2].split(":");
        String [] speedArray = generalDesc[3].split(":");
        String [] visibilityArray = generalDesc[4].split(":");
        String [] pressureArray = generalDesc[5].split(":");
        String [] HumidityArray = generalDesc[6].split(":");
        String [] uvArray = generalDesc[7].split(":");

        String direction = directionArray[1].trim();
        String speed = speedArray[1].trim();
        String visibility = visibilityArray[1].trim();
        String pressure = pressureArray[1].trim();
        String humidity = HumidityArray[1].trim();
        String uv = uvArray[1].replace("]", "").trim();

        pressureCond.setText(pressure);
        humidityCond.setText(humidity);
        uvCond.setText(uv);
        visibilityCond.setText(visibility);
        directionCond.setText(direction);
        speedCond.setText(speed);
    }

    public void getTemperature (String tempDesc){

        String[] weatherDetails = tempDesc.split(",");

        String[] weatherTemperature = weatherDetails[0].split(":");
        String[] tempBoth = weatherTemperature[1].split("\\(");

        String temp = tempBoth[0];

        currentTemp.setText(temp);
    }

    public void setConditionToday (String weatherCond){
        String [] splitWeatherCond = weatherCond.split(",");
        String [] splitPng = splitWeatherCond[0].split(":");

        String weatherPng = splitPng[1].toLowerCase();

        if (weatherPng.contains("cloud")){
            todayWeatherImg.setImageResource(R.drawable.cloudy);
        }
        else if (weatherPng.contains("sun")){
            todayWeatherImg.setImageResource(R.drawable.sunny);
        }
        else if (weatherPng.contains("thunder")){
            todayWeatherImg.setImageResource(R.drawable.thunder);
        }
        else if (weatherPng.contains("rain")){
            todayWeatherImg.setImageResource(R.drawable.rain);
        }
        else if (weatherPng.contains("snow")){
            todayWeatherImg.setImageResource(R.drawable.snowflake);
        }
        else if (weatherPng.contains("clear")){
            todayWeatherImg.setImageResource(R.drawable.clear);
        }
        else if (weatherPng.contains("fog") || weatherPng.contains("mist") || weatherPng.contains("haz")){
            todayWeatherImg.setImageResource(R.drawable.fog);
        }
        else if (weatherPng.contains("hail")){
            todayWeatherImg.setImageResource(R.drawable.hail);
        }
        else if (weatherPng.contains("sleet")){
            todayWeatherImg.setImageResource(R.drawable.sleet);
        }
        else if (weatherPng.contains("drizzle")){
            todayWeatherImg.setImageResource(R.drawable.drizzle);
        }
        else {
            todayWeatherImg.setImageResource(R.drawable.sunny);
        }

        todayWeatherCond.setText(weatherPng);

    }

    public void setConditionTomorrow (String weatherCond){
        String [] splitWeatherCond = weatherCond.split(",");
        String [] splitPng = splitWeatherCond[0].split(":");

        String weatherPng = splitPng[1].toLowerCase();

        if (weatherPng.contains("cloud")){
            tomorrowWeatherImg.setImageResource(R.drawable.cloudy);
        }
        else if (weatherPng.contains("sun")){
            tomorrowWeatherImg.setImageResource(R.drawable.sunny);
        }
        else if (weatherPng.contains("thunder")){
            tomorrowWeatherImg.setImageResource(R.drawable.thunder);
        }
        else if (weatherPng.contains("rain")){
            tomorrowWeatherImg.setImageResource(R.drawable.rain);
        }
        else if (weatherPng.contains("snow")){
            tomorrowWeatherImg.setImageResource(R.drawable.snowflake);
        }
        else if (weatherPng.contains("clear")){
            tomorrowWeatherImg.setImageResource(R.drawable.clear);
        }
        else if (weatherPng.contains("fog") || weatherPng.contains("mist") || weatherPng.contains("haz")){
            tomorrowWeatherImg.setImageResource(R.drawable.fog);
        }
        else if (weatherPng.contains("hail")){
            tomorrowWeatherImg.setImageResource(R.drawable.hail);
        }
        else if (weatherPng.contains("sleet")){
            tomorrowWeatherImg.setImageResource(R.drawable.sleet);
        }
        else if (weatherPng.contains("drizzle")){
            tomorrowWeatherImg.setImageResource(R.drawable.drizzle);
        }
        else {
            tomorrowWeatherImg.setImageResource(R.drawable.sunny);
        }

        tomorrowWeatherCond.setText(weatherPng);

    }

    public void setConditionDayAfter (String weatherCond){
        String [] splitWeatherCond = weatherCond.split(",");
        String [] splitPng = splitWeatherCond[0].split(":");

        String weatherPng = splitPng[1].toLowerCase();

        if (weatherPng.contains("cloud")){
            nextWeatherImg.setImageResource(R.drawable.cloudy);
        }
        else if (weatherPng.contains("sun")){
            nextWeatherImg.setImageResource(R.drawable.sunny);
        }
        else if (weatherPng.contains("thunder")){
            nextWeatherImg.setImageResource(R.drawable.thunder);
        }
        else if (weatherPng.contains("rain")){
            nextWeatherImg.setImageResource(R.drawable.rain);
        }
        else if (weatherPng.contains("snow")){
            nextWeatherImg.setImageResource(R.drawable.snowflake);
        }
        else if (weatherPng.contains("clear")){
            nextWeatherImg.setImageResource(R.drawable.clear);
        }
        else if (weatherPng.contains("fog") || weatherPng.contains("mist") || weatherPng.contains("haz")){
            nextWeatherImg.setImageResource(R.drawable.fog);
        }
        else if (weatherPng.contains("hail")){
            nextWeatherImg.setImageResource(R.drawable.hail);
        }
        else if (weatherPng.contains("sleet")){
            nextWeatherImg.setImageResource(R.drawable.sleet);
        }
        else if (weatherPng.contains("drizzle")){
            nextWeatherImg.setImageResource(R.drawable.drizzle);
        }
        else {
            nextWeatherImg.setImageResource(R.drawable.sunny);
        }
        nextWeatherCond.setText(weatherPng);

    }

    public void setLocIcon(String loc){

        if (loc.equals("Bangladesh")) {
            locPgTwoIcon.setImageResource(R.drawable.bangladesh);
        } else if (loc.equals("Glasgow")) {
            locPgTwoIcon.setImageResource(R.drawable.glasgow);
        } else if (loc.equals("Lagos")) {
            locPgTwoIcon.setImageResource(R.drawable.lagos);
        } else if (loc.equals("London")) {
            locPgTwoIcon.setImageResource(R.drawable.london);
        } else if (loc.equals("Mauritius")) {
            locPgTwoIcon.setImageResource(R.drawable.mauritius);
        } else if (loc.equals("New York")) {
            locPgTwoIcon.setImageResource(R.drawable.new_york);
        } else if (loc.equals("Oman")) {
            locPgTwoIcon.setImageResource(R.drawable.oman);
        }
    }

    public void loadToday(){
        getTemperature(Description1);

        currentDate.setText(todayDateString);

        String [] generalDesc = Description1.split(",");

        String [] directionArray = generalDesc[2].split(":");
        String [] speedArray = generalDesc[3].split(":");
        String [] visibilityArray = generalDesc[4].split(":");
        String [] pressureArray = generalDesc[5].split(":");
        String [] HumidityArray = generalDesc[6].split(":");
        String [] uvArray = generalDesc[7].split(":");

        String direction = directionArray[1].trim();
        String speed = speedArray[1].trim();
        String visibility = visibilityArray[1].trim();
        String pressure = pressureArray[1].trim();
        String humidity = HumidityArray[1].trim();
        String uv = uvArray[1].replace("]", "").trim();

        pressureCond.setText(pressure);
        humidityCond.setText(humidity);
        uvCond.setText(uv);
        visibilityCond.setText(visibility);
        directionCond.setText(direction);
        speedCond.setText(speed);
    }

    public void loadTomorrow(){
        getTemperature(Description2);

        currentDate.setText(tomorrowDateString);

        String [] generalDesc = Description2.split(",");

        String [] directionArray = generalDesc[2].split(":");
        String [] speedArray = generalDesc[3].split(":");
        String [] visibilityArray = generalDesc[4].split(":");
        String [] pressureArray = generalDesc[5].split(":");
        String [] HumidityArray = generalDesc[6].split(":");
        String [] uvArray = generalDesc[7].split(":");

        String direction = directionArray[1].trim();
        String speed = speedArray[1].trim();
        String visibility = visibilityArray[1].trim();
        String pressure = pressureArray[1].trim();
        String humidity = HumidityArray[1].trim();
        String uv = uvArray[1].replace("]", "").trim();

        pressureCond.setText(pressure);
        humidityCond.setText(humidity);
        uvCond.setText(uv);
        visibilityCond.setText(visibility);
        directionCond.setText(direction);
        speedCond.setText(speed);
    }

    public void loadDayAfter(){
        getTemperature(Description3);

        currentDate.setText(nextDateString);

        String [] generalDesc = Description3.split(",");

        String [] directionArray = generalDesc[2].split(":");
        String [] speedArray = generalDesc[3].split(":");
        String [] visibilityArray = generalDesc[4].split(":");
        String [] pressureArray = generalDesc[5].split(":");
        String [] HumidityArray = generalDesc[6].split(":");
        String [] uvArray = generalDesc[7].split(":");

        String direction = directionArray[1].trim();
        String speed = speedArray[1].trim();
        String visibility = visibilityArray[1].trim();
        String pressure = pressureArray[1].trim();
        String humidity = HumidityArray[1].trim();
        String uv = uvArray[1].replace("]", "").trim();

        pressureCond.setText(pressure);
        humidityCond.setText(humidity);
        uvCond.setText(uv);
        visibilityCond.setText(visibility);
        directionCond.setText(direction);
        speedCond.setText(speed);
    }

    //METHOD USED WITH REFERENCE FROM A STACK OVERFLOW SOLUTION: https://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
    public Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
