package com.nickuc.login.authmeapi;

import com.nickuc.login.api.enums.TwoFactorType;
import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import com.nickuc.login.api.event.bukkit.auth.RegisterEvent;
import com.nickuc.login.api.event.bukkit.auth.SessionLoginEvent;
import com.nickuc.login.api.event.bukkit.auth.request.LoginRequestEvent;
import com.nickuc.login.api.event.bukkit.command.PreCommandExecuteEvent;
import com.nickuc.login.api.event.bukkit.command.UnregisterEvent;
import com.nickuc.login.api.event.bukkit.twofactor.TwoFactorAddEvent;
import com.nickuc.login.api.event.bukkit.twofactor.TwoFactorRemoveEvent;
import fr.xephi.authme.events.AuthMeAsyncPreLoginEvent;
import fr.xephi.authme.events.AuthMeAsyncPreRegisterEvent;
import fr.xephi.authme.events.EmailChangedEvent;
import fr.xephi.authme.events.RestoreSessionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Bootstrap extends JavaPlugin {

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new nLoginListener(), this);
    }

    public class nLoginListener implements Listener {

        @EventHandler
        public void onLoginRequest(LoginRequestEvent e) {
            getServer().getPluginManager().callEvent(new fr.xephi.authme.events.ProtectInventoryEvent(e.getPlayer(), e.isAsynchronous()));
        }

        @EventHandler
        public void onAuthenticate(AuthenticateEvent e) {
            getServer().getPluginManager().callEvent(new fr.xephi.authme.events.RestoreInventoryEvent(e.getPlayer(), e.isAsynchronous()));
            getServer().getPluginManager().callEvent(new fr.xephi.authme.events.LoginEvent(e.getPlayer(), e.isAsynchronous()));
        }

        @EventHandler
        public void onRegister(RegisterEvent e) {
            getServer().getPluginManager().callEvent(new fr.xephi.authme.events.RegisterEvent(e.getPlayer(), e.isAsynchronous()));
        }

        @EventHandler
        public void onUnregister(UnregisterEvent e) {
            Player player = Bukkit.getPlayer(e.getPlayerName());
            switch (e.getSource()) {
                case BY_ADMIN:
                    getServer().getPluginManager().callEvent(new fr.xephi.authme.events.UnregisterByAdminEvent(e.getPlayerName(), e.isAsynchronous()));
                    break;
                case BY_PLAYER:
                    getServer().getPluginManager().callEvent(new fr.xephi.authme.events.UnregisterByPlayerEvent(player, e.isAsynchronous()));
                    break;
            }
        }

        @EventHandler
        public void onAsyncSession(SessionLoginEvent e) {
            getServer().getPluginManager().callEvent(new RestoreSessionEvent(e.getPlayer(), e.isAsynchronous()));
        }

        @EventHandler
        public void onAsyncTwoFactorAdd(TwoFactorAddEvent e) {
            if (e.getType() == TwoFactorType.EMAIL) {
                Player player = Bukkit.getServer().getPlayer(e.getPlayerName());
                if (player != null) {
                    getServer().getPluginManager().callEvent(new EmailChangedEvent(player, null, e.getAccount(), true));
                }
            }
        }

        @EventHandler
        public void onPreCommandExecute(PreCommandExecuteEvent e) {
            switch (e.getType()) {
                case LOGIN: {
                    AuthMeAsyncPreLoginEvent event = new AuthMeAsyncPreLoginEvent(e.getPlayer(), e.isAsynchronous());
                    event.setCanLogin(!e.isCancelled());
                    getServer().getPluginManager().callEvent(event);
                    e.setCancelled(!event.canLogin());
                    break;
                }
                case REGISTER: {
                    AuthMeAsyncPreRegisterEvent event = new AuthMeAsyncPreRegisterEvent(e.getPlayer(), e.isAsynchronous());
                    event.setCanRegister(!e.isCancelled());
                    getServer().getPluginManager().callEvent(event);
                    e.setCancelled(!event.canRegister());
                    break;
                }
            }
        }

        @EventHandler
        public void onAsyncTwoFactorRemove(TwoFactorRemoveEvent e) {
            if (e.getType() == TwoFactorType.EMAIL) {
                getServer().getPluginManager().callEvent(new EmailChangedEvent(e.getPlayer(), e.getAccount(), null, true));
            }
        }
    }
}
