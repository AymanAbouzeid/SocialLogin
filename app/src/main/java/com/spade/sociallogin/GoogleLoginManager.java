package com.spade.sociallogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Ayman Abouzaid on 5/31/17.
 */

public class GoogleLoginManager implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int RC_GOOGLE_SIGN_IN = 1000;
    private GoogleLoginCallBack mLoginCallBack;
    private LogoutCallBack mLogoutCallBack;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;
    private FragmentActivity mFragmentActivity;

    public GoogleLoginManager(FragmentActivity fragmentActivity,
                              GoogleLoginCallBack facebookLoginCallBack) {
        this.mFragmentActivity = fragmentActivity;
        this.mLoginCallBack = facebookLoginCallBack;
        initGoogleApiClient();
    }

    public GoogleLoginManager(FragmentActivity fragmentActivity,
                              LogoutCallBack logoutCallBack) {
        this.mFragmentActivity = fragmentActivity;
        this.mLogoutCallBack = logoutCallBack;
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestProfile().requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(mFragmentActivity.getApplicationContext()).enableAutoManage(mFragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions).build();
        mGoogleApiClient.connect();
    }


    public void loginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mFragmentActivity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void loginWithGoogle(Fragment fragment) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragment.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                mLogoutCallBack.onLogout();
            }
        });
    }


    public void disconnectGoogleApi() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient.stopAutoManage(mFragmentActivity);
            mGoogleApiClient = null;
        }
    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Login Manager", result.getStatus().getStatusMessage() + " ..  " + result.getStatus().getStatusCode());
        if (result.isSuccess()) {
            GoogleSignInAccount userAccount = result.getSignInAccount();
            String userPhoto = "";
            if (userAccount.getPhotoUrl() != null) {
                userPhoto = userAccount.getPhotoUrl().toString();
            }
            SocialUser socialUser = new SocialUser(userAccount.getDisplayName(), userAccount.getEmail(),
                    userAccount.getId(), userAccount.getIdToken(), userPhoto);
            mLoginCallBack.onGoogleLoginSuccess(socialUser);
        } else {
            mLoginCallBack.onGoogleLoginFail();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
