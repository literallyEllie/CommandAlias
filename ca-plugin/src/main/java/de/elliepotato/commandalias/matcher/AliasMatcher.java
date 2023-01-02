package de.elliepotato.commandalias.matcher;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.registry.AliasRegistry;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class AliasMatcher {

    public static AliasMatcher newMatcher(AliasRegistry registry) {
        return new AliasMatcher(registry);
    }

    private final AliasRegistry registry;

    private AliasMatcher(AliasRegistry registry) {
        this.registry = registry;
    }

    public List<CommandAlias> match(String query) {
        String messageLower = query.toLowerCase(Locale.ROOT);
        String[] args = messageLower.split(" ");
        String firstArg = args[0];

        return registry.getRegisteredAliases().stream()
                .filter(CommandAlias::isEnabled)
                .filter(alias -> {
                    String label = alias.getLabel();
                    Set<String> aliases = alias.getAliases();

                    return label.equals(messageLower)
                            || aliases.contains(firstArg)
                            || aliases.contains(messageLower);
                }).sorted().toList();
    }

}
