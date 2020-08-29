package com.example.uber_clone;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    public void onClick(View view) {
        if(edtanonymous.getText().toString().equals("Driver")||edtanonymous.getText().toString().equals("Passenger")){
            if(ParseUser.getCurrentUser()==null){
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null){
                            Toast.makeText(MainActivity.this, "We have an anonymous user", Toast.LENGTH_SHORT).show();

                            user.put("as", edtanonymous.getText().toString());
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    transitionToPassengerActivity();
                                    transitionToDriverActivity();

                                }
                            });
                        }
                    }
                });
            }
        }else{
            Toast.makeText(MainActivity.this,"Are You a driver or a passenger?",Toast.LENGTH_SHORT).show();
            return;
        }



    }
    enum State{
        SIGNUP, LOGIN
    }
    private State state;

    private EditText edtusername,edtpassword,edtanonymous;
    private Button btnLogSignup,btnOneTimeLogin;
    private RadioButton radioPassenger,radioDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        if(ParseUser.getCurrentUser()!=null){
            //ParseUser.logOut();
            transitionToPassengerActivity();
            transitionToDriverActivity();
        }
        edtusername=findViewById(R.id.edtUsername);
        edtpassword=findViewById(R.id.edtPassword);
        edtanonymous=findViewById(R.id.edtAnonymous);
        btnLogSignup=findViewById(R.id.btnLoginSignUp);
        btnOneTimeLogin=findViewById(R.id.btnOneTime);
        btnOneTimeLogin.setOnClickListener(this);
        state=State.SIGNUP;

        radioPassenger=findViewById(R.id.rdoPassenger);
        radioDriver=findViewById(R.id.rdoDriver);

    btnLogSignup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(state == State.SIGNUP){
                if(radioDriver.isChecked()==false && radioPassenger.isChecked() ==false){
                    Toast.makeText(MainActivity.this,"Are you a driver or a passenger?",Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser appUser = new ParseUser();
                appUser.setUsername(edtusername.getText().toString());
                appUser.setPassword(edtpassword.getText().toString());
                if(radioDriver.isChecked()){
                    appUser.put("as","Driver");
                }else if(radioPassenger.isChecked()){
                    appUser.put("as","Passenger");
                }
                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(MainActivity.this,"Signed Up!",Toast.LENGTH_SHORT).show();
                            transitionToPassengerActivity();
                            transitionToDriverActivity();
                        }
                    }
                });
            }else if(state==State.LOGIN){
                ParseUser.logInInBackground(edtusername.getText().toString(),
                        edtpassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null && e==null){
                            Toast.makeText(MainActivity.this,"User Logged In",Toast.LENGTH_SHORT).show();
                            transitionToPassengerActivity();
                            transitionToDriverActivity();
                        }
                    }
                });
            }
        }
    });

    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.loginItem:
                if(state == State.SIGNUP){
                    state=State.LOGIN;
                    item.setTitle("SIGN UP");
                    btnLogSignup.setText("LOG IN");

                }else if(state == State.LOGIN){
                    state = State.SIGNUP;
                    item.setTitle("LOG IN");
                    btnLogSignup.setText("SIGN UP");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void transitionToDriverActivity() {

        if (ParseUser.getCurrentUser() != null) {

            if (ParseUser.getCurrentUser().get("as").equals("Driver")) {

                Intent intent = new Intent(MainActivity.this, DriverRequestListActivity.class);
                startActivity(intent);
            }

        }
    }

    private void transitionToPassengerActivity() {
        if (ParseUser.getCurrentUser() != null) {

            if (ParseUser.getCurrentUser().get("as").equals("Passenger")) {

                Intent intent = new Intent(this, PassengerActivity.class);
                startActivity(intent);

            }

        }

    }



}