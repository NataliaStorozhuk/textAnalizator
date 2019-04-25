package sample;

import lombok.Data;

@Data
public class User {

    private int id;
    private String login;
    private String password;
    private Boolean rights;


    public User(int id, String login, String password, Boolean rights) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.rights = rights;
    }
}
