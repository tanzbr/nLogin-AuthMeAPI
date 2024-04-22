package fr.xephi.authme.api.v3;

import com.nickuc.login.api.enums.ImplementationType;
import com.nickuc.login.api.nLoginAPI;
import com.nickuc.login.api.types.AccountData;
import com.nickuc.login.api.types.Identity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The current API of AuthMe.
 * <p>
 * Recommended method of retrieving the AuthMeApi object:
 * <code>
 * AuthMeApi authmeApi = AuthMeApi.getInstance();
 * </code>
 */
public class AuthMeApi {

    private static final AuthMeApi singleton = new AuthMeApi();

    /**
     * Get the AuthMeApi object for AuthMe.
     *
     * @return The AuthMeApi object, or null if the AuthMe plugin is not enabled or not fully initialized yet
     */
    public static AuthMeApi getInstance() {
        return singleton;
    }

    /**
     * Return the plugin instance.
     *
     * @return The AuthMe instance
     */
    public Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("AuthMe");
    }

    /**
     * Gather the version number of the plugin.
     * This can be used to determine whether certain AuthMeApi features are available or not.
     *
     * @return Plugin version identifier as a string.
     */
    public String getPluginVersion() {
        return nLoginAPI.getApi().getVersion();
    }

    /**
     * Return whether the given player is authenticated.
     *
     * @param player The player to verify
     * @return true if the player is authenticated
     */
    public boolean isAuthenticated(Player player) {
        return nLoginAPI.getApi().isAuthenticated(player.getName());
    }

    /**
     * Check whether the given player is an NPC.
     *
     * @param player The player to verify
     * @return true if the player is an npc
     */
    public boolean isNpc(Player player) {
        return false;
    }

    /**
     * Check whether the given player is unrestricted. For such players, AuthMe will not require
     * them to authenticate.
     *
     * @param player The player to verify
     * @return true if the player is unrestricted
     */
    public boolean isUnrestricted(Player player) {
        return false;
    }

    /**
     * Get the last location of an online player.
     *
     * @param player The player to process
     * @return The location of the player
     */
    public Location getLastLocation(Player player) {
        return null;
    }

    /**
     * Returns the AuthMe info of the given player's name, or empty optional if the player doesn't exist.
     *
     * @param playerName The player name to look up
     * @return AuthMe player info, or empty optional if the player doesn't exist
     */
    public Optional<AuthMePlayer> getPlayerInfo(String playerName) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(playerName))
                .map(AuthMePlayerImpl::fromPlayerAccount);
    }

    /**
     * Get the last ip address of a player.
     *
     * @param playerName The name of the player to process
     * @return The last ip address of the player
     */
    public String getLastIp(String playerName) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(playerName))
                .map(AccountData::getLastIP).orElse(null);
    }

    /**
     * Get user names by ip.
     *
     * @param address The ip address to process
     * @return The list of user names related to the ip address
     */
    public List<String> getNamesByIp(String address) {
        return nLoginAPI.getApi().getAccountsByIp(address)
                .stream()
                .map(AccountData::getLastIP)
                .collect(Collectors.toList());
    }

    /**
     * Get the last (AuthMe) login date of a player.
     *
     * @param playerName The name of the player to process
     * @return The date of the last login, or null if the player doesn't exist or has never logged in
     * @deprecated Use Java 8's Instant method {@link #getLastLoginTime(String)}
     */
    @Deprecated
    public Date getLastLogin(String playerName) {
        Long lastLogin = getLastLoginMillis(playerName);
        return lastLogin == null ? null : new Date(lastLogin);
    }

    /**
     * Get the last (AuthMe) login timestamp of a player.
     *
     * @param playerName The name of the player to process
     *
     * @return The timestamp of the last login, or null if the player doesn't exist or has never logged in
     */
    public Instant getLastLoginTime(String playerName) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(playerName))
                .map(AccountData::getLastLogin).orElse(null);
    }

    private Long getLastLoginMillis(String playerName) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(playerName))
                .map(accountData -> accountData.getLastLogin().toEpochMilli()).orElse(null);
    }

    /**
     * Return whether the player is registered.
     *
     * @param playerName The player name to check
     * @return true if player is registered, false otherwise
     */
    public boolean isRegistered(String playerName) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(playerName)).isPresent();
    }

    /**
     * Check the password for the given player.
     *
     * @param playerName      The player to check the password for
     * @param passwordToCheck The password to check
     * @return true if the password is correct, false otherwise
     */
    public boolean checkPassword(String playerName, String passwordToCheck) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(playerName))
                .map(accountData -> accountData.comparePassword(passwordToCheck)).orElse(false);
    }

    /**
     * Register an OFFLINE/ONLINE player with the given password.
     *
     * @param playerName The player to register
     * @param password   The password to register the player with
     *
     * @return true if the player was registered successfully
     */
    public boolean registerPlayer(String playerName, String password) {
        return nLoginAPI.getApi().performRegister(Identity.ofKnownName(playerName), password, null);
    }

    /**
     * Force a player to login, i.e. the player is logged in without needing his password.
     *
     * @param player The player to log in
     */
    public void forceLogin(Player player) {
        nLoginAPI.getApi().forceLogin(Identity.ofKnownName(player.getName()));
    }

    /**
     * Force a player to logout.
     *
     * @param player The player to log out
     */
    public void forceLogout(Player player) {
    }

    /**
     * Force an ONLINE player to register.
     *
     * @param player    The player to register
     * @param password  The password to use
     * @param autoLogin Should the player be authenticated automatically after the registration?
     */
    public void forceRegister(Player player, String password, boolean autoLogin) {
        nLoginAPI.getApi().performRegister(Identity.ofKnownName(player.getName()), password, null);
        if (autoLogin) {
            forceLogin(player);
        }
    }

    /**
     * Register an ONLINE player with the given password.
     *
     * @param player   The player to register
     * @param password The password to use
     */
    public void forceRegister(Player player, String password) {
        forceRegister(player, password, true);
    }

    /**
     * Unregister a player from AuthMe.
     *
     * @param player The player to unregister
     */
    public void forceUnregister(Player player) {
        nLoginAPI.getApi().performUnregister(Identity.ofKnownName(player.getName()));
    }

    /**
     * Unregister a player from AuthMe by name.
     *
     * @param name the name of the player (case-insensitive)
     */
    public void forceUnregister(String name) {
        nLoginAPI.getApi().performUnregister(Identity.ofKnownName(name));
    }

    /**
     * Change a user's password
     *
     * @param name the user name
     * @param newPassword the new password
     */
    public void changePassword(String name, String newPassword) {
        nLoginAPI.getApi().changePassword(Identity.ofKnownName(name), newPassword);
    }

    /**
     * Get all the registered names (lowercase)
     *
     * @return registered names
     */
    public List<String> getRegisteredNames() {
        List<String> registeredNames = new ArrayList<>();
        nLoginAPI.getApi().getAccounts().forEachRemaining(account -> registeredNames.add(account.getLastName().toLowerCase()));
        return registeredNames;
    }

    /**
     * Get all the registered real-names (original case)
     *
     * @return registered real-names
     */
    public List<String> getRegisteredRealNames() {
        List<String> registeredNames = new ArrayList<>();
        if (nLoginAPI.getApi().getImplementationType() == ImplementationType.NATIVE) {
            nLoginAPI.getApi().getAccounts().forEachRemaining(account -> registeredNames.add(account.getLastName()));
        }
        return registeredNames;
    }

    /**
     * Get the country code of the given IP address.
     *
     * @param ip textual IP address to lookup.
     *
     * @return two-character ISO 3166-1 alpha code for the country.
     */
    public String getCountryCode(String ip) {
        return "us";
    }

    /**
     * Get the country name of the given IP address.
     *
     * @param ip textual IP address to lookup.
     *
     * @return The name of the country.
     */
    public String getCountryName(String ip) {
        return "United States";
    }
}
