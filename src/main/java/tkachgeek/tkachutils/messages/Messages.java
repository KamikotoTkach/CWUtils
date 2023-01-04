package tkachgeek.tkachutils.messages;

import net.kyori.adventure.text.Component;

import java.util.HashMap;

public class Messages {
    private final HashMap<String, Message> messages = new HashMap<>();

    public Messages(String locale, Message message) {
        locale = MessagesUtils.formatLocale(locale);

        messages.put(locale, message);
    }

    public Messages(Message message) {
        this(MessagesUtils.getDefaultLocale(), message);
    }

    public Messages(String locale, String message) {
        this(locale, new Message(message));
    }

    public Messages(String message) {
        this(MessagesUtils.getDefaultLocale(), message);
    }

    public Messages(String locale, Component message) {
        this(locale, new Message(message));
    }

    public Messages(Component message) {
        this(MessagesUtils.getDefaultLocale(), message);
    }

    public Messages add(String locale, Message message) {
        locale = MessagesUtils.formatLocale(locale);
        messages.put(locale, message);

        return this;
    }

    public Messages add(Message message) {
        return this.add(MessagesUtils.getDefaultLocale(), message);
    }

    public Messages add(String locale, String message) {
        return this.add(locale, new Message(message));
    }

    public Messages add(String message) {
        return this.add(MessagesUtils.getDefaultLocale(), message);
    }

    public Messages add(String locale, Component message) {
        return this.add(locale, new Message(message));
    }

    public Messages add(Component message) {
        return this.add(MessagesUtils.getDefaultLocale(), message);
    }

    public Message get(String locale) {
        return messages.getOrDefault(locale.toLowerCase(), new Message("empty"));
    }

    public Message get() {
        return this.get(MessagesUtils.getDefaultLocale());
    }
}
