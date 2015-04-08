package se.fearless.spacedweb.utils;

import org.springframework.stereotype.Service;
import se.fearlessgames.common.util.Digester;

@Service
public class UserAccountDigester extends Digester {
    private static final String SALT = "beefcake";

    public UserAccountDigester() {
        super(SALT);
    }
}
