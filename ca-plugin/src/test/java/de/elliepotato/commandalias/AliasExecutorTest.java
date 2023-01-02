package de.elliepotato.commandalias;

import de.eliepotato.commandalias.api.executor.AliasExecutor;
import de.elliepotato.commandalias.executor.AliasExecutorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AliasExecutorTest {

    private static AliasExecutor executor;

    @BeforeAll
    public static void beforeAll() {
        executor = new AliasExecutorImpl();
    }

    @Test
    public void test() {

    }

}
