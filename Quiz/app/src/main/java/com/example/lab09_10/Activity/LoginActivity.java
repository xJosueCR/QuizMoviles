package com.example.lab09_10.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab09_10.Data.AsyncTaskManager;
import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.Model.Usuario;
import com.example.lab09_10.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar loadingProgressBar;
    private ImageButton loginButton;
    private View mLoginFormView;
    private UserLoginTask mAuthTask = null;
    private Usuario usuario;
    //private DBAdapterSQL db;
    private SharedPreferences prefs;
    private String user;
    private String password;
    private Usuario loggeado;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        //mLoginFormView = findViewById(R.id.login_form);

        //login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                attemptLogin();
            }
        });
        prefs = this.getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE);

    }

    private void attemptLogin() {
        usernameEditText.setError(null);
        passwordEditText.setError(null);
        user = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            focusView = passwordEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(user)) {
            usernameEditText.setError("Username is required");
            focusView = usernameEditText;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            String url = "http://192.168.1.8:14715/QuizWeb/servletUsuario?" +
                    "username="+user+"&password="+password;
            AsyncTaskManager net = new AsyncTaskManager(url, new AsyncTaskManager.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) parser.parse(output);

                    //carreraList = new ArrayList<>();
                    loggeado = new Usuario(
                            jsonObject.get("id").getAsInt(),
                            jsonObject.get("username").getAsString(),
                            jsonObject.get("password").getAsString(),
                            jsonObject.get("rol").getAsString()
                    );
                    showProgress(true);
                    mAuthTask = new UserLoginTask(user, password);
                    mAuthTask.execute((Void) null);
                }
            });
            net.execute(AsyncTaskManager.GET);

        }
    }
    protected Usuario getUsuario(String mUser, String mPassword){
        return loggeado;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

            loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            loadingProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mUser = email;
            mPassword = password;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                //db.insertarUsuario(new Usuario(0,mUser, mPassword, "admin"));
                //db.insertarEstudiante(new
                      //  Estudiante(0, "207610110", "Josue", "Cespedes",19,null,1));
              usuario = getUsuario(mUser, mPassword);

              //db.deleteUsuario(13);
                if (usuario != null) {
                    return true;
                }
               // db.close();
                Thread.sleep(2000);
            } catch (Exception e) {
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            usernameEditText.setError(null);
            passwordEditText.setError(null);
            if (success) {
                finish();
                Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                //putting user on shared preferences
                SharedPreferences.Editor prefsEditor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(usuario);
                prefsEditor.putString("usuario", json);
                prefsEditor.commit();
                Intent intent = new Intent(LoginActivity.this, NavDrawerActivity.class);
                //intent.putExtra("currentUser", usuario);
                LoginActivity.this.startActivity(intent);
            } else {
                passwordEditText.setError("Incorrect password");
                // passwordEditText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

}
