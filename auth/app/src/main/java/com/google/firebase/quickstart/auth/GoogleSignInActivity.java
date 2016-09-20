/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends BaseActivity implements GoogleSignInView, View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;

    private TextView mDetailTextView;

    private GoogleSignInManager mApiClientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        mApiClientManager = new GoogleSignInManager(getApplicationContext(), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mApiClientManager.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
        mApiClientManager.onStop();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mApiClientManager.onSignInResult(data);
        }
    }

    @Override
    public void showSignedOutUser() {
        mStatusTextView.setText(R.string.signed_out);
        mDetailTextView.setText(null);

        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
    }

    @Override
    public void showSignedInUser(@NonNull String email, @NonNull String uId) {
        hideProgressDialog();
        mStatusTextView.setText(getString(R.string.google_status_fmt, email));
        mDetailTextView.setText(getString(R.string.firebase_status_fmt, uId));

        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
    }

    @Override
    public void showAuthFailedError() {
        Toast.makeText(this, "Auth failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConnectionFailedError(@NonNull String msg) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + msg);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            mApiClientManager.signOut();
        } else if (i == R.id.disconnect_button) {
            mApiClientManager.revokeAccess();
        }
    }

    private void signIn() {
        Intent signInIntent = mApiClientManager.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
