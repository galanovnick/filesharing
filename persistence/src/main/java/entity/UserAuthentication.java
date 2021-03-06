package entity;

import entity.tiny.UserAuthenticationId;
import entity.tiny.UserId;

/**
 * UserAuthentication entity.
 */
public class UserAuthentication {

    private UserAuthenticationId id;

    private UserId userId;

    private String token;

    public UserAuthentication(UserId userId, String token) {
        this.id = new UserAuthenticationId(0);
        this.userId = userId;
        this.token = token;
    }

    public UserAuthenticationId getId() {
        return id;
    }

    public void setId(UserAuthenticationId id) {
        this.id = id;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAuthentication that = (UserAuthentication) o;

        if (!id.equals(that.id)) return false;
        if (!userId.equals(that.userId)) return false;
        return token.equals(that.token);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }
}
