package com.spade.sociallogin;

/**
 * Created by Ayman Abouzaid on 5/31/17.
 */

public interface LoginCallBack {
    void onLoginSuccess(SocialUser socialUser);

    void onLoginCancel();

    void onLoginFail(Exception e);

    void onLoginFail();

}
