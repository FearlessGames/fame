package se.fearless.spacedweb.model;

public class Salts {
    private final String userSalt;
    private final String oneTimeSalt;

    public Salts(String userSalt, String oneTimeSalt) {
        this.userSalt = userSalt;
        this.oneTimeSalt = oneTimeSalt;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public String getOneTimeSalt() {
        return oneTimeSalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Salts salts = (Salts) o;

        if (oneTimeSalt != null ? !oneTimeSalt.equals(salts.oneTimeSalt) : salts.oneTimeSalt != null) return false;
        if (userSalt != null ? !userSalt.equals(salts.userSalt) : salts.userSalt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userSalt != null ? userSalt.hashCode() : 0;
        result = 31 * result + (oneTimeSalt != null ? oneTimeSalt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return userSalt +":"+oneTimeSalt;
    }
}
