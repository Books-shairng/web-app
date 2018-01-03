package com.ninjabooks.json.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserRequest implements Serializable
{
    private static final long serialVersionUID = -4625305707954795378L;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Min(value = 8)
    private String password;

    public UserRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        firstName = replaceAllNonAlphabeticCharacters(firstName);
        lastName = replaceAllNonAlphabeticCharacters(lastName);
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    private String replaceAllNonAlphabeticCharacters(String string) {
        return string.replaceAll("[^\\p{Alpha}]", "");
    }
}
