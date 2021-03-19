package util;

import android.app.Application;

public class JournalAPI extends Application {

    private String username;
    private String userid;
    private static JournalAPI instance;

    public JournalAPI(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public static JournalAPI getInstance() {
        if(instance==null)
            instance=new JournalAPI();
        return instance;
    }


}
