package com.az.bobbot.listeners;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class mediaonly extends ListenerAdapter {

    private final Dotenv config;

    public mediaonly(Dotenv config) {
        this.config = config;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if (!config.get("MEDIA").contains((CharSequence) event.getGuild().getTextChannelById("MEDIA")))
        {
            return;
        }

        Message message = event.getMessage();
        List<Message.Attachment> attachments = event.getMessage().getAttachments().stream().filter(attachment ->
        {
            String fileExtension = attachment.getFileExtension();
            return fileExtension != null && fileExtension.matches("png|jpeg|jpg");
        }).toList();

        if (!message.getContentRaw().matches("^https?://.+\\.(png|jpeg|jpg)$") && attachments.isEmpty())
        {
            message.delete().queue();
        }
    }
}