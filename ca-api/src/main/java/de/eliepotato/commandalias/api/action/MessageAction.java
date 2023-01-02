package de.eliepotato.commandalias.api.action;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.executor.context.ExecutionContext;

import java.util.List;

public class MessageAction implements AliasAction {

    public static MessageAction of(String... messages) {
        return new MessageAction(List.of(messages));
    }

    private List<String> messages;

    public MessageAction(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public boolean run(CommandAlias alias, ExecutionContext context) {
        // TODO placeholders

        for (String message : messages) {
            context.getExecutor().sendMessage(message);
        }

        return true;
    }
}
