package edu.osumc.bmi.oauth2.core.domain;

import java.io.Serializable;
import java.util.Objects;

public class UserClientId implements Serializable {

    private static final long serialVersionUID = 1L;
    private long userId;
    private long clientId;

    public UserClientId() {
    }

    public UserClientId(long userId, long clientId) {
        this.userId = userId;
        this.clientId = clientId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserClientId)) return false;
        UserClientId that = (UserClientId) o;
        return getUserId() == that.getUserId() &&
                getClientId() == that.getClientId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getClientId());
    }
}
