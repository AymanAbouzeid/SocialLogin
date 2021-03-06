package com.spade.sociallogin;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by Ayman Abouzaid on 5/30/17.
 */

public class FacebookLoginManager {

    private static final int RC_FACEBOOK_SIGN_IN = FacebookSdk.getCallbackRequestCodeOffset();
    private final String USER_NAME = "name";
    private final String USER_EMAIL = "email";
    private final String USER_PICTURE = "picture";
    private final String PUBLIC_PROFILE = "public_profile";
    private final String FIELDS = "fields";
    private final String PARAMETERS = "id,name,email,picture.type(large)";

    private CallbackManager mCallbackManager;
    private FacebookLoginCallBack mFacebookLoginCallback;
    private LoginManager mLoginManager;
    private LogoutCallBack mLogoutCallBack;

    public static void initFacebookEvents(Application application) {
        AppEventsLogger.activateApp(application);
    }

    public FacebookLoginManager(FacebookLoginCallBack facebookLoginCallback) {
        this.mFacebookLoginCallback = facebookLoginCallback;
        initFacebookManagers();
    }

    public FacebookLoginManager(LogoutCallBack logoutCallBack) {
        this.mLogoutCallBack = logoutCallBack;
        initFacebookManagers();
    }

    private void initFacebookManagers() {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFaceBookUser(loginResult);
            }

            @Override
            public void onCancel() {
                mFacebookLoginCallback.onFacebookLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                mFacebookLoginCallback.onFacebookLoginFail(error);
            }
        });
    }

    public void loginWithFacebook(FragmentActivity fragmentActivity) {
        mLoginManager.logInWithReadPermissions(fragmentActivity, Arrays.asList(PUBLIC_PROFILE, USER_EMAIL));
    }

    public void loginWithFacebook(Fragment fragment) {
        mLoginManager.logInWithReadPermissions(fragment, Arrays.asList(PUBLIC_PROFILE, USER_EMAIL));
    }

    public void logout() {
        mLoginManager.logOut();
        mLogoutCallBack.onLogout();
    }

    private void getFaceBookUser(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = "", name = "", photo = "";
                            if (object.has(USER_EMAIL))
                                email = object.getString(USER_EMAIL);
                            if (object.has(USER_NAME))
                                name = object.getString(USER_NAME);
                            if (object.has(USER_PICTURE))
                                photo = object.getJSONObject(USER_PICTURE).getJSONObject("data").getString("url");

                            String id = loginResult.getAccessToken().getUserId();
                            String token = loginResult.getAccessToken().getToken();
                            SocialUser socialUser = new SocialUser(name, email, id, token, photo);

                            mFacebookLoginCallback.onFacebookLoginSuccess(socialUser);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, PARAMETERS);
        request.setParameters(parameters);
        request.executeAsync();
    }


    public static String getUserAccessToken() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null)
            return accessToken.getToken();
        else
            return null;
    }


    public Set<String> getDeclinedPermissions() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null)
            return accessToken.getDeclinedPermissions();
        else
            return null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_FACEBOOK_SIGN_IN)
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
