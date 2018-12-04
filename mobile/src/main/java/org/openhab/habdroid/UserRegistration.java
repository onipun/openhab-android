package org.openhab.habdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class UserRegistration extends AppCompatActivity {
    private EditText rName;
    private EditText rPassword;
    private EditText rEmail;
    private Button rSubmit;
    private LoginDbInitiate loginDbInitiate;
    private View registerForm;
    private View registration_progress;
    private static final String TAG = UserRegistration.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate:  entered");
        super.onCreate(savedInstanceState);
        loginDbInitiate = LoginDbInitiate.getsLoginDbInitiate(this.getApplicationContext());
        setContentView(R.layout.activity_user_registration);
        rName = findViewById(R.id.nameEditText);
        rPassword = findViewById(R.id.passwordEditText);
        rEmail = findViewById(R.id.emailEditText);
        rSubmit = findViewById(R.id.registerBtn);
        registerForm = findViewById(R.id.register_form);
        registration_progress = findViewById(R.id.registration_progress);

        rSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                attempRegistration();
            }
        });

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
            registerForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            registration_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            registration_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registration_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            registration_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            registration_progress.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public boolean attempRegistration() {

        String name = rName.getText().toString();
        String password = rPassword.getText().toString();
        String email = rEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (!TextUtils.isEmpty(password) && !isPasswordVaild()) {
            rPassword.setError("Password too short");
            focusView = rPassword;
            cancel = true;
        }

        if (!TextUtils.isEmpty(email) && !isEmailValid()) {
            rEmail.setError("Email is not valid");
            focusView = rEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            rName.setError("this space is required");
            focusView = rName;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            rPassword.setError("this space is required");
            focusView = rPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            rEmail.setError("this space is required");
            focusView = rEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            showProgress(false);
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Registration registration = new Registration(rName.getText().toString(), rPassword.getText().toString(),rEmail.getText().toString());
            registration.execute((Void) null);
            return true;
        }
        return false;
    }

    private boolean isEmailValid() {
        //TODO: Replace this with your own logic
        return rEmail.getText().toString().contains("@");
    }

    public boolean isPasswordVaild() {
        return rPassword.length() > 4;
    }

    public class Registration extends AsyncTask<Void, Void, Boolean> {

        private String rName;
        private String rPassword;
        private String rEmail;

        Registration(String rName, String rPassword, String rEmail) {
            this.rName = rName;
            this.rPassword = rPassword;
            this.rEmail = rEmail;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Log.d(TAG, "doInBackground: success test thread");
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            if (loginDbInitiate.insertNewUser(rName, rPassword, rEmail)) {

                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            showProgress(false);
            finish();

            if (success){
            Intent intent = new Intent(UserRegistration.this, LoginActivity.class);
            startActivity(intent);
            }else{
                Snackbar.make(null,"error",Snackbar.LENGTH_SHORT);
            }

        }
    }
}
