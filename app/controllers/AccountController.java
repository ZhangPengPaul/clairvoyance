package controllers;

import play.mvc.Controller;

/**
 * Created by PaulZhang on 2016/11/28.
 */
public class AccountController extends Controller {
    public static void toLogin() {
        render("/account/login.html");
    }

    public static void toRegister() {
        render("/account/register.html");
    }
}
