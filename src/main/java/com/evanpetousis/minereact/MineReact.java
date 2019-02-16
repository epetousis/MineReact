package com.evanpetousis.minereact;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public final class MineReact extends JavaPlugin implements Listener {
    private HashMap<UUID, LinkedList<ReactableMessage>> messageStore;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("react") && args.length >= 2) {
            // We have a specific set of reactions that any player can use.
            // You can only react to the last 10 messages a player has sent. This is for memory reasons.
            // Generally no one will really want to react to anything older than that anyway.
            String reactionString = args[0];
            Reaction reaction;
            try {
                //if (!(reaction_string.startsWith(":") && reaction_string.endsWith(":"))) {
                    reaction = Reaction.valueOf(reactionString.toUpperCase());
                //}
            } catch (IllegalArgumentException e) {
                sender.sendMessage("There's no reaction with name " + reactionString);
                return false;
            }
            String playerString = args[1];
            @SuppressWarnings("deprecation")
            Player player = Bukkit.getServer().getPlayer(playerString);
            UUID playerId;
            if (player != null) {
                playerId = player.getUniqueId();
            } else {
                sender.sendMessage("There's no player with name " + playerString);
                return false;
            }
            int messageIndex = 0;
            if (args.length > 2) {
                messageIndex = Integer.parseInt(args[2]);
            }
            if (!messageStore.containsKey(playerId)) {
                sender.sendMessage("There's no messages for player with name " + playerString);
                return false;
            }
            ReactableMessage message = messageStore.get(playerId).get(messageIndex);
            message.addReact(reaction);
            int reactCount = message.getReacts(reaction);
            String statusMsg = String.format("%1$s reacted %2$s to %3$s [%4$s %5$s]: '%6$s'", sender.getName(), message.getReactIcon(reaction), playerString, reactCount, message.getReactIcon(reaction), StringUtils.abbreviate(message.message, 10));
            Bukkit.getServer().broadcastMessage(statusMsg);
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        this.messageStore = new HashMap<>();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.isCancelled()) {
            UUID uniqueId = event.getPlayer().getUniqueId();
            LinkedList<ReactableMessage> messages = messageStore.getOrDefault(uniqueId, new LinkedList<>());
            if (messages.size() == 10) {
                messages.removeLast();
            }
            messages.addFirst(new ReactableMessage(event.getMessage()));
            messageStore.put(uniqueId, messages);
        }
    }
}
