package de.eliepotato.commandalias.api.hook;

import de.eliepotato.commandalias.api.util.AliasPriority;
import org.jetbrains.annotations.NotNull;

public interface ExecutionHook extends Comparable<ExecutionHook> {

    AliasPriority getPriority();

    @Override
    default int compareTo(@NotNull ExecutionHook o) {
        return o.getPriority().compareTo(getPriority());
    }

}
