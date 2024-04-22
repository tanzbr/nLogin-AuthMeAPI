package fr.xephi.authme.api;

import com.nickuc.login.api.nLoginAPI;
import com.nickuc.login.api.types.AccountData;
import com.nickuc.login.api.types.Identity;
import org.bukkit.entity.Player;

import java.util.Date;

public class NewAPI {

    private static final NewAPI api = new NewAPI();

    public static NewAPI getInstance() {
        return api;
    }

    public void changePassword(String name, String newPassword) {
        nLoginAPI.getApi().changePassword(Identity.ofKnownName(name), newPassword);
    }

    public boolean checkPassword(String playerName, String passwordToCheck) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(playerName))
                .map(accountData -> accountData.comparePassword(passwordToCheck)).orElse(false);
    }

    public void forceLogin(Player player) {
        nLoginAPI.getApi().forceLogin(Identity.ofKnownName(player.getName()));
    }

    public void forceLogout(Player player) {
    }

    public void forceRegister(Player player, String password) {
        nLoginAPI.getApi().performRegister(Identity.ofKnownName(player.getName()), password);
    }

    public void forceRegister(String player, String password) {
        nLoginAPI.getApi().performRegister(Identity.ofKnownName(player), password);
    }

    public void forceRegister(Player player, String password, boolean autoLogin) {
        Identity identity = Identity.ofKnownName(player.getName());
        nLoginAPI.getApi().performRegister(identity, password);
        if (autoLogin) {
            nLoginAPI.getApi().forceLogin(identity);
        }
    }

    public void forceUnregister(Player player) {
        nLoginAPI.getApi().performUnregister(Identity.ofKnownName(player.getName()));
    }

    public void forceUnregister(String player) {
        nLoginAPI.getApi().performUnregister(Identity.ofKnownName(player));
    }

    public String getLastIp(String player) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(player))
                .map(AccountData::getLastIP).orElse(null);
    }

    public Date getLastLogin(String player) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(player))
                .map(accountData -> new Date(accountData.getLastLogin().toEpochMilli())).orElse(null);
    }

    public String getPluginVersion() {
        return nLoginAPI.getApi().getVersion();
    }

    public boolean isAuthenticated(Player player) {
        return nLoginAPI.getApi().isAuthenticated(player.getName());
    }

    public boolean isAuthenticated(String player) {
        return nLoginAPI.getApi().isAuthenticated(player);
    }

    public boolean isRegistered(Player player) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(player.getName())).isPresent();
    }

    public boolean isRegistered(String player) {
        return nLoginAPI.getApi().getAccount(Identity.ofKnownName(player)).isPresent();
    }

    public void registerPlayer(Player player, String password) {
        nLoginAPI.getApi().performRegister(Identity.ofKnownName(player.getName()), password);
    }

    public void registerPlayer(String player, String password) {
        nLoginAPI.getApi().performRegister(Identity.ofKnownName(player), password);
    }
}
