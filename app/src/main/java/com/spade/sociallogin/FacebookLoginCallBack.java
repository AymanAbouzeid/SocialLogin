package com.spade.sociallogin;

/**
 * Created by Ayman Abouzaid on 5/31/17.
 */

public interface FacebookLoginCallBack {
    void onFacebookLoginSuccess(SocialUser socialUser);

    void onFacebookLoginCancel();

    void onFacebookLoginFail(Exception e);
}
