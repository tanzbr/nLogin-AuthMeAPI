package fr.xephi.authme.api.v3;

import com.nickuc.login.api.types.AccountData;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link AuthMePlayer}. This implementation is not part of the API and
 * may have breaking changes in subsequent releases.
 */
class AuthMePlayerImpl implements AuthMePlayer {

    private static final String DEFAULT_EMAIL = "example@domain.com";
    private static final String DEFAULT_IP = "127.0.0.1";

    private String name;
    private UUID uuid;
    private String email;

    private Instant registrationDate;
    private String registrationIpAddress;

    private Instant lastLoginDate;
    private String lastLoginIpAddress;

    AuthMePlayerImpl() {
    }

    /**
     * Maps the given player auth to an AuthMePlayer instance. Returns an empty optional if
     * the player auth is null.
     *
     * @param playerAuth the player auth or null
     * @return the mapped player auth, or empty optional if the argument was null
     */
    static AuthMePlayer fromPlayerAccount(AccountData account) {
        AuthMePlayerImpl authMeUser = new AuthMePlayerImpl();
        authMeUser.name = account.getLastName();
        authMeUser.uuid = account.getUniqueId().orElse(null);
        authMeUser.email = nullIfDefault(account.getEmail().orElse(null), DEFAULT_EMAIL);
        authMeUser.registrationDate = account.getCreationDate();
        authMeUser.registrationIpAddress = nullIfDefault(account.getLastAddress(), DEFAULT_IP);
        authMeUser.lastLoginDate = account.getLastLogin();
        authMeUser.lastLoginIpAddress = nullIfDefault(account.getLastAddress(), DEFAULT_IP);
        return authMeUser;
    }

    @Override
    public String getName() {
        return name;
    }

    public Optional<UUID> getUuid() {
        return Optional.ofNullable(uuid);
    }

    @Override
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    @Override
    public Instant getRegistrationDate() {
        return registrationDate;
    }

    @Override
    public Optional<String> getRegistrationIpAddress() {
        return Optional.ofNullable(registrationIpAddress);
    }

    @Override
    public Optional<Instant> getLastLoginDate() {
        return Optional.ofNullable( lastLoginDate);
    }

    @Override
    public Optional<String> getLastLoginIpAddress() {
        return Optional.ofNullable(lastLoginIpAddress);
    }

    private static Instant toInstant(Long epochMillis) {
        return epochMillis == null ? null : Instant.ofEpochMilli(epochMillis);
    }

    private static <T> T nullIfDefault(T value, T defaultValue) {
        return defaultValue.equals(value) ? null : value;
    }
}
