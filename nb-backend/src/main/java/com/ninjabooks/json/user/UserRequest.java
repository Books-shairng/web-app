package com.ninjabooks.json.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserRequest implements Serializable
{
    private static final long serialVersionUID = -4625305707954795378L;

    @NotEmpty(message = "{default.NotEmpty.message}")
    private String firstName;

    @NotEmpty(message = "{default.NotEmpty.message}")
    private String lastName;

    @NotEmpty(message = "{default.NotEmpty.message}")
    @Email(message = "{default.Email.message}")
    private String email;

    @NotEmpty(message = "{default.NotEmpty.message}")
    @Length(min = 8, message = "{password.Length.message}")
    private String password;

    @JsonCreator
    public UserRequest(@JsonProperty(value = "firstName") String  firstName,
                       @JsonProperty(value = "lastName") String lastName,
                       @JsonProperty(value = "email") String email,
                       @JsonProperty(value = "password") String password) {
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
