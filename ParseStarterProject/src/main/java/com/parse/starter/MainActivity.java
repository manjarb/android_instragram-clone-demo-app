/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signUpModeActive = true;
  TextView changeSignupModeTextView;

  EditText passwordEditText;

  public void showUserList() {
    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.changeSignupModeTextView) {
      Log.i("APP info", "Change Signup Mode");

      Button signupButton = (Button) findViewById(R.id.signupButton);

      if (signUpModeActive) {
        signUpModeActive = false;
        signupButton.setText("Login");
        changeSignupModeTextView.setText("Or, signup");
      } else {
        signUpModeActive = true;
        signupButton.setText("Signup");
        changeSignupModeTextView.setText("Or, Login");
      }
    } else
    if (view.getId() == R.id.backgroudLayout ||
        view.getId() == R.id.logoImageView){

      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  public void signUp(View view) {
    EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);

    if (usernameEditText.getText().toString().matches("") ||
        passwordEditText.getText().toString().matches("")) {

      Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
    } else {

      if (signUpModeActive) {
        ParseUser user = new ParseUser();

        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Sign up", "Successfull");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      } else {
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user != null) {
              Log.i("Signup", "Login Successfull");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
      signUp(view);
    }

    return false;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instragram");

    changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

    changeSignupModeTextView.setOnClickListener(this);

    ConstraintLayout backgroundLayout = (ConstraintLayout) findViewById(R.id.backgroudLayout);

    ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);

    backgroundLayout.setOnClickListener(this);
    logoImageView.setOnClickListener(this);

    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    passwordEditText.setOnKeyListener(this);

    if (ParseUser.getCurrentUser() != null) {
      showUserList();
    }

    /*
    ParseObject score = new ParseObject("Score");
    score.put("username", "rob");
    score.put("score", 86);

    score.saveInBackground(new SaveCallback () {
      @Override
      public void done(ParseException ex) {
        if (ex == null) {
          Log.i("SaveInBackGround", "Successful!");
        } else {
          Log.i("SaveInBackGround", "Failed. Error: " + ex.toString());
        }
      }
    });

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.getInBackground("zYXGukkIiM", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if (e == null && object != null) {

          object.put("score", 200);
          object.saveInBackground();

          Log.i("ObjectValue", object.getString("username"));
          Log.i("ObjectValue", Integer.toString(object.getInt("score")));
        }
      }
    });
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());


    ParseObject tweet = new ParseObject("Tweet");

    tweet.put("username", "tommy");
    tweet.put("tweet", "Hey There!");

    tweet.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {

        if (e == null) {
          Log.i("Tweet", "Successful");
        } else {
          Log.i("Tweet", "Failed");
        }
      }
    });

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");

    query.getInBackground("Fb2yGnx9RE", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {

        if (e == null && object != null) {
          Log.i("Tweet", "Successful");

          object.put("tweet", "Bye!");
          object.saveInBackground();
        } else {
          Log.i("Tweet", "Failed");
        }
      }
    });

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if (e == null) {
          Log.i("findInBackGround", "Retrieved" + objects.size() + "objects");

          if (objects.size() > 0) {
            for (ParseObject object : objects) {
              Log.i("findInBackGroundResult", object.getString("username"));
            }
          }
        }
      }
    });


    ParseUser user = new ParseUser();
    user.setUsername("Robpercival");
    user.setPassword("myPass");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {

        if (e == null) {
          Log.i("Sign up", "Successfull");
        } else {
          Log.i("Sign up", "Failed");
        }
      }
    });


    ParseUser.logInInBackground("robpercival", "asdf", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (user != null) {
          Log.i("Login", "Successful");
        } else {
          Log.i("Login", "Failed: " + e.toString());
        }
      }
    });

    if (ParseUser.getCurrentUser() != null) {
      Log.i("current User", "User logged in " + ParseUser.getCurrentUser().getUsername());
    } else {
      Log.i("current User", "User not logged in");
    }
    */
  }

}