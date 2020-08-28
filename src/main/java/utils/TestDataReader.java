package utils;

import java.util.ResourceBundle;

public class TestDataReader {
    public static final String PASSWORD = "testdata.password";
    public static final String TOKEN = "testdata.token";
    public static final String LOGIN = "testdata.login";
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("login");

    public static String getPasswordFromProperty() {
        return resourceBundle.getString(PASSWORD);
    }

    public static String getTokenFromProperty() {
        return resourceBundle.getString(TOKEN);
    }

    public static String getLoginFromProperty() {
        return resourceBundle.getString(LOGIN);
    }

}
