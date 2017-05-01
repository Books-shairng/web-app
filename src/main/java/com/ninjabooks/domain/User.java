package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
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
    private LocalDate lastPasswordReset;

    @Column(name = "AUTHORITY")
    private String authoritiy;

    @OneToMany(mappedBy = "user")
    private List<Queue> queues;

    @OneToMany(mappedBy = "user")
    private List<Borrow> borrows;

    @OneToMany(mappedBy = "user")
    private List<History> histories;

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
        this.lastPasswordReset = LocalDate.now();
        this.authoritiy = "USER";
    }

    public User(String name, String email, String password, LocalDate lastPasswordReset, String  authoritiy) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastPasswordReset = lastPasswordReset;
        this.authoritiy = "USER";
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

    public LocalDate getLastPasswordReset() {
        return lastPasswordReset;
    }

    public void setLastPasswordReset(LocalDate lastPasswordReset) {
        this.lastPasswordReset = lastPasswordReset;
    }

    public String getAuthoritiy() {
        return authoritiy;
    }

    public void setAuthoritiy(String authoritiy) {
        this.authoritiy = authoritiy;
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
