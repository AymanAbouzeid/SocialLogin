package com.spade.sociallogin;

/**
 * Created by Ayman Abouzaid on 5/31/17.
 */

public interface GoogleLoginCallBack {
    void onGoogleLoginSuccess(SocialUser socialUser);

    void onGoogleLoginFail();
}
