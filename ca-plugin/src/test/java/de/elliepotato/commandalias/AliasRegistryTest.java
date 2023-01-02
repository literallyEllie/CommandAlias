package de.elliepotato.commandalias;

import de.eliepotato.commandalias.api.alias.CommandAlias;
import de.eliepotato.commandalias.api.alias.builder.AliasBuilder;
import de.eliepotato.commandalias.api.registry.AliasRegistry;
import de.elliepotato.commandalias.registry.AliasRegistryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class AliasRegistryTest {

    private static AliasRegistry registry;

    @BeforeAll
    public static void beforeAll() {
        registry = new AliasRegistryImpl();
    }

    @Test
    public void testRegisterAndUnregisterAliasByReference() {
        Collection<CommandAlias> registeredAliases = registry.getRegisteredAliases();
        Assertions.assertNotNull(registeredAliases);

        CommandAlias alias = AliasBuilder.newBuilder().label("test").aliases("alias").build();

        registry.registerAlias(alias);
        Assertions.assertEquals(1, registeredAliases.size());

        registry.unregisterAlias(alias);
        Assertions.assertEquals(0, registeredAliases.size());
    }

    @Test
    public void testUnregisterAliasByLabel() {
        Collection<CommandAlias> registeredAliases = registry.getRegisteredAliases();
        Assertions.assertNotNull(registeredAliases);

        CommandAlias alias = AliasBuilder.newBuilder().label("test").aliases("alias").build();

        registry.registerAlias(alias);
        Assertions.assertEquals(1, registeredAliases.size());

        registry.unregisterAlias("noMatch");
        Assertions.assertEquals(1, registeredAliases.size());

        registry.unregisterAlias("test");
        Assertions.assertEquals(0, registeredAliases.size());
    }

}
