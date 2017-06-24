package com.ninjabooks.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent user in database
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "USER")
public class User extends BaseEntity
{
    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "LAST_PASSWORD_RESET")
    private LocalDateTime lastPasswordReset;

    @Column(name = "AUTHORITY")
    private final String authoritiy = "USER";

    @OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY, mappedBy = "user")
    private List<Queue> queues = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY, mappedBy = "user")
    private List<Borrow> borrows = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY, mappedBy = "user")
    private List<History> histories = new ArrayList<>(0);

    public User() {
    }

    /**
     * Create new instance of user object
     *
     * @param name
     * @param email
     * @param password
     */

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastPasswordReset = LocalDateTime.now();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastPasswordReset() {
        return lastPasswordReset;
    }

    public void setLastPasswordReset(LocalDateTime lastPasswordReset) {
        this.lastPasswordReset = lastPasswordReset;
    }

    public String getAuthoritiy() {
        return authoritiy;
    }


    public List<Queue> getQueues() {
        return queues;
    }

    public void setQueues(List<Queue> queues) {
        this.queues = queues;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
