package com.eladrador.common.player;

import java.util.HashMap;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.scheduling.RepeatingTask;

import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;

public class ActionBar {

	private static final double PACKET_RESEND_PERIOD_SECONDS = 1.0;
	private static final HashMap<Player, ActionBar> actionBarMap = new HashMap<>();

	private PacketPlayOutChat packet;

	public ActionBar(String text) {
		packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"), ChatMessageType.GAME_INFO);
	}

	public void send(Player player) {
		actionBarMap.put(player, this);
		new RepeatingTask(PACKET_RESEND_PERIOD_SECONDS) {
			@Override
			protected void run() {
				if (actionBarMap.get(player) == ActionBar.this) {
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
				} else {
					stop();
				}
			}
		}.start();
	}

	public void send(Player player, double durationSeconds) {
		send(player);
		new DelayedTask() {
			@Override
			protected void run() {
				actionBarMap.remove(player);
			}
		}.start();
	}

}
