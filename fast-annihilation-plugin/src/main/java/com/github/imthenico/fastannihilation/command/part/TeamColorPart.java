package com.github.imthenico.fastannihilation.command.part;

import com.github.imthenico.annihilation.api.team.TeamColor;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TranslatableComponent;
import org.jetbrains.annotations.Nullable;

public class TeamColorPart implements CommandPart {

    private final String name;

    public TeamColorPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException {
        try {
            TeamColor color = TeamColor.valueOf(stack.next());

            context.setValue(this, color);
        } catch (Exception e) {
            throw new ArgumentParseException("Invalid team color name");
        }
    }
}