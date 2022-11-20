package com.az.bobbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        long enable = System.currentTimeMillis();

        String command = event.getName();

        if ( command.equals("say"))
        {
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();

            MessageChannel channel;
            OptionMapping channelOption = event.getOption("channel");

            if (channelOption != null)
            {
                channel = channelOption.getAsMessageChannel();
            } else
            {
                channel = event.getChannel();
            }
            channel.sendMessage(message).queue();
            event.reply("Message sent.").setEphemeral(true).queue();
        }
        else if( command.equals("servers"))
        {
            if (event.getMember().getId() == ("OWNER"))
            {
                event.reply("It's still a work in progress.").setEphemeral(true).queue();
            }
        }
        else if( command.equals("ping"))
        {
            event.reply("Pong. My ping is: " + TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - enable)) + "s!").queue();
        }
        else if( command.equals("members"))
        {
            event.reply("We currently have " + String.valueOf(event.getGuild().getMemberCount()) + " members.").setEphemeral(true).queue();
        }
        else if( command.equals("vanity"))
        {
            event.reply("Our server invite code: " + String.valueOf(event.getGuild().getVanityUrl())).setEphemeral(true).queue();
        }
        else if( command.equals("purge"))
        {
            if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE))
            {
                event.reply("It's still a work in progress.").setEphemeral(true).queue();
            }
            else if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE))
            {
                event.getChannel().sendMessage("You don't have permission to do this.");
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        // Say
        OptionData s1 = new OptionData(OptionType.STRING, "message", "Say something", true);
        OptionData s2 = new OptionData(OptionType.CHANNEL, "channel", "Say something").setChannelTypes(ChannelType.TEXT);
        commandData.add(Commands.slash("say", "Say something").addOptions(s1, s2));

        // Misc
        commandData.add(Commands.slash("ping", "Am I alive? Let's check."));
        commandData.add(Commands.slash("members", "How popular are we?"));
        commandData.add(Commands.slash("vanity", "Want to see my Vanity?"));
        commandData.add(Commands.slash("servers", "What servers are we in?"));

        // Purge
        OptionData p1 = new OptionData(OptionType.CHANNEL, "channel", "Impulse the brain.", true).setChannelTypes(ChannelType.TEXT);
        OptionData p2 = new OptionData(OptionType.INTEGER, "amount","How much?", true);
        commandData.add(Commands.slash("purge", "The Neuralyzer").addOptions(p1, p2));

        // Command Data Update
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
