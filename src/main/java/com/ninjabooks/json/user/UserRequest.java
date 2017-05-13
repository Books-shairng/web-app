package com.ninjabooks.json.user;

/**
 *
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserRequest
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserRequest() {
        super();
    }

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
