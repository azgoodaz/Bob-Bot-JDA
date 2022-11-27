package com.az.bobbot;

import com.az.bobbot.commands.CommandManager;
import com.az.bobbot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class bobbot {

    private final Dotenv config;
    private final ShardManager shardManager;

    public bobbot() throws LoginException
    {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("you secretly."));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGES);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListener(), new CommandManager());
    }

    public Dotenv getConfig()
    {
        return config;
    }

    public ShardManager getShardManager()
    {
        return shardManager;
    }

    public static void main (String[] args)
    {
        try
        {
        bobbot bot = new bobbot();
        }
        catch (LoginException e)
        {
            System.out.println("Error: Token is invalid.");
        }
    }
}