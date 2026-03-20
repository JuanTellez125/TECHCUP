package edu.dosw.TECHCUP.core.model;

public class UserBasicBuilder implements UserBuilder{
    private User user;
    public UserBasicBuilder(User user) {
        this.user = user;
    }


    @Override
    public void buildName(String name) {user.setName(name);}

    @Override
    public void buildEmail(String email) {user.setEmail(email);}

    @Override
    public void buildPassword(String password) {user.setPassword(password);}
}
