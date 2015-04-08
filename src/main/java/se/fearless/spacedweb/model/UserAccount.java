package se.fearless.spacedweb.model;

import se.fearless.spacedweb.persistance.PersistableBase;
import se.fearlessgames.common.util.uuid.UUID;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class UserAccount extends PersistableBase {
    @Column(unique = true, nullable = false, updatable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String userSalt;

    private Date timeOfRegistration;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
    private ForumId forumId = new ForumId();

    @ElementCollection
    private List<String> features;

    protected UserAccount() {
    }

    public UserAccount(UUID uuid, String username, String password, String email) {
        super(uuid);
        this.username = username;
        this.password = password;
        this.email = email;
        timeOfRegistration = new Date();
    }

    public UserAccount(UUID uuid, String username, String hash, String email, List<String> features) {
        this(uuid, username, hash, email);
        this.features = features;
    }

    public UserAccount(UUID accountPk, String username, String userHash, String userSalt, String email, List<String> features) {
        this(accountPk,username,userHash,email);
        this.features = features;
        this.userSalt = userSalt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getFeatures() {
        return features;
    }

    public boolean hasFeature(String feature) {
        return features.contains(feature);
    }

    public int getForumId() {
        return forumId.getId() + 1000;  //need to offset forumid so the builtin ids dont collide, should be done in jpa bindings instead :(
    }

    public Date getTimeOfRegistration() {
        return timeOfRegistration;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                ", hash='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }
}
