package org.example;

enum AccountStatus {
    ACTIVE,
    CLOSED,
    BLOCKED
}

abstract class Account {
    protected String id;
    protected String name;
    protected AccountStatus status;
    protected LibraryCard card;

    public Account(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = AccountStatus.ACTIVE;
    }

    public void setLibraryCard(LibraryCard card) {
        this.card = card;
    }

    public LibraryCard getLibraryCard() {
        return card;
    }

    public abstract int getMaxBooksAllowed();
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}

