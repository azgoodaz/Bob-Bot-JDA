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

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
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
        else if( command.equals("ping"))
        {
            event.reply("Pong.").queue();
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
            if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE))
            {
                event.getChannel().sendMessage("You don't have permission to do this.");
                return;
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        // Say
        OptionData option1 = new OptionData(OptionType.STRING, "message", "Say something", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "Say something", true).setChannelTypes(ChannelType.TEXT);
        commandData.add(Commands.slash("say", "Say something").addOptions(option1, option2));

        // Misc
        commandData.add(Commands.slash("ping", "Am I alive? Let's check."));
        commandData.add(Commands.slash("members", "How popular are we?"));
        commandData.add(Commands.slash("vanity", "Want to see my Vanity?"));

        // Purge


        // Command Data Update
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
