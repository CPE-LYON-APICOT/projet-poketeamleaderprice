package fr.cpe.service;

import com.google.inject.Inject;

import fr.cpe.App;

/**
 * Implementation of GameMessageService that stores received messages.
 */
public class GameMessageServiceImpl implements GameMessageService {

    private final MessageStore messageStore;

    @Inject
    public GameMessageServiceImpl() {
        this.messageStore = App.injector.getInstance(MessageStore.class);
    }

    @Override
    public void sayHosting() {
        messageStore.setLastMessage("Hosting");
        System.out.println("[GameMessageService] Hosting");
    }

    @Override
    public void saySearching() {
        messageStore.setLastMessage("Searching");
        System.out.println("[GameMessageService] Searching");
    }

    @Override
    public void sayConnect() {
        messageStore.setLastMessage("Connect");
        System.out.println("[GameMessageService] Connect");
    }

    @Override
    public void sayAttack() {
        messageStore.setLastMessage("Attack");
        System.out.println("[GameMessageService] Attack");
    }

    @Override
    public void sayChangePokemon() {
        messageStore.setLastMessage("ChangePokemon");
        System.out.println("[GameMessageService] Change Pokemon");
    }

    @Override
    public void sayUseItem() {
        messageStore.setLastMessage("UseItem");
        System.out.println("[GameMessageService] Use Item");
    }

    @Override
    public void sayQuit() {
        messageStore.setLastMessage("Quit");
        System.out.println("[GameMessageService] Quit");
    }
}
