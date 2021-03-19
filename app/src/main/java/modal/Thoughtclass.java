package modal;

import com.google.firebase.Timestamp;

public class Thoughtclass {
    String username;
    Timestamp Timeadded;
    String tittle;
    String thought;
    String userid;
    String imageurl;

    public Thoughtclass(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimeadded() {
        return Timeadded;
    }

    public void setTimeadded(Timestamp timeadded) {
        Timeadded = timeadded;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Thoughtclass(String username, Timestamp timeadded, String tittle, String thought, String userid, String imageurl) {
        this.username = username;
        this.Timeadded = timeadded;
        this.tittle = tittle;
        this.thought = thought;
        this.userid = userid;
        this.imageurl = imageurl;
    }
}
