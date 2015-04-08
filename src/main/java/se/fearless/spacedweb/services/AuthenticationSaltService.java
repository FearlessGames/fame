package se.fearless.spacedweb.services;

import se.fearless.spacedweb.model.Salts;

public interface AuthenticationSaltService {
    public Salts requestSalts(String username);
    public String getOneTimeSaltForUsername(String username);
}
