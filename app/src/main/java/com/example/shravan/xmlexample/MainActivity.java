package com.example.shravan.xmlexample;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String QUESTION = "question";
    final String DESCRIPTION = "description";
    final String OPTION = "option";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("questions.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            // Automatically identify the encoding based on xml 1.0 standard.
            xmlPullParser.setInput(inputStream, null);

            int eventType = xmlPullParser.getEventType();
            Question question = null;
            ArrayList<Question> questions = new ArrayList<>();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals(QUESTION)) {
                            // Create a new questions object.
                            question = new Question();
                        }
                        else if(xmlPullParser.getName().equals(DESCRIPTION)) {
                            // Inside a description.
                            // The next text element is the description of question.
                            question.setDescription(xmlPullParser.nextText());
                        }
                        else if(xmlPullParser.getName().equals(OPTION)) {
                            // Inside a description.
                            // Add the option to the current question.
                            question.addOption(new Option(
                                    Integer.parseInt(xmlPullParser.getAttributeValue(0)),
                                    xmlPullParser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(xmlPullParser.getName().equals(QUESTION)) {
                            // Added the question to the list.
                            questions.add(question);
                        }
                        else if(xmlPullParser.getName().equals(DESCRIPTION)) {
                        }
                        else if(xmlPullParser.getName().equals(OPTION)) {
                        }
                        break;
                    case XmlPullParser.TEXT:
                        // The actual text inside the tags.
                        break;
                }
                eventType = xmlPullParser.next();
            }

            for(Question q : questions) {
                Log.d("Question", q.getDescription());
                for(Option o : question.getOptions()) {
                    Log.d("Option", "Name="+o.getText()+" Value="+o.getValue());
                }
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Class which represents a question.
class Question {
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    String description;

    public ArrayList<Option> getOptions() {
        return options;
    }

    ArrayList<Option> options;

    public void addOption(Option op) {
        options.add(op);
    }

    public Question() {
        options = new ArrayList<>();
    }
}

// Class which represents an option.
class Option {
    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    int value;
    String text;

    public Option(int value, String text) {
        this.value = value;
        this.text = text;
    }
}