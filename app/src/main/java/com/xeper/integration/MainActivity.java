package com.xeper.integration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    TextView loginstatus;
    CallbackManager callbackManager;
    TextView first_name,last_name,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        first_name=findViewById(R.id.first_name);
        last_name=findViewById(R.id.last_name);
        email=findViewById(R.id.email);

        loginButton=findViewById(R.id.login_button);
        loginstatus=findViewById(R.id.loginstatus);

        loginButton.setReadPermissions("email","public_profile");


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                loginstatus.setText("Login Success \n" + loginResult.getAccessToken().getToken());

                String userid=loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        displayUserInformation(object);

                    }
                });

                Bundle parameters= new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();


            }



            @Override
            public void onCancel() {
                loginstatus.setText("Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {

                loginstatus.setText("Login error");

            }
        });
    }

    public void displayUserInformation(JSONObject object) {

        String first_name1="", last_name1="", email1="", id1="";

        try {
            first_name1=object.getString("first_name");
            last_name1=object.getString("last_name");
            email1=object.getString("email");
            id1=object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        first_name.setText(first_name1);
        last_name.setText(last_name1);
        email.setText(email1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
