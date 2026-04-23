package fr.cpe.service;

import com.google.inject.Singleton;

import fr.cpe.commands.Command;

/**
 * Executor for Command objects.
 * Receives and executes commands sent via the bus.
 */
@Singleton
public class CommandExecutor {

    public void execute(Command command) {
        if (command != null) {
            command.execute();
        }
    }
}