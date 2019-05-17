package sample.DBModels;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "rights")
    private Boolean rights;

    public User(String login, String password, Boolean rights) {
        this.login = login;
        this.password = password;
        this.rights = rights;
    }

    public User() {
    }

    public String toString() {
        return "models.User{" +
                "id=" + idUser +
                ", login='" + login + '\'' +
                ", rights='" + rights + '\'' +
                ", password=" + password +
                '}';
    }
}
