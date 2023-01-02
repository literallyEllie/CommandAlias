package de.eliepotato.commandalias.api.hook.condition.permission;

import de.eliepotato.commandalias.api.hook.condition.PlayerCondition;
import de.eliepotato.commandalias.api.util.AliasPriority;
import org.bukkit.entity.Player;

/**
 * A run condition to run for players which requires they must have a permission for
 * the alias to run.
 */
public class PermissionCondition implements PlayerCondition {

    public static PlayerCondition of(String requiredPermission, String noPermissionMessage) {
        return new PermissionCondition(requiredPermission, noPermissionMessage);
    }

    private String requiredPermission;
    private String noPermissionMessage;

    public PermissionCondition(String requiredPermission, String noPermissionMessage) {
        this.requiredPermission = requiredPermission;
        this.noPermissionMessage = noPermissionMessage;
    }

    @Override
    public AliasPriority getPriority() {
        return AliasPriority.FIRST;
    }

    @Override
    public boolean test(Player player) {
        if (requiredPermission == null) {
            return true;
        }

        return player.hasPermission(requiredPermission);
    }

    /**
     * @return Required permission for the player to have to run.
     */
    public String getRequiredPermission() {
        return requiredPermission;
    }

    /**
     * Set the required permission for the player to have to run.
     *
     * @param requiredPermission Required permission.
     */
    public void setRequiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public void setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
    }

}
