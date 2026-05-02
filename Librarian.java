public class Librarian extends Account {
    public Librarian(String id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxBooksAllowed() {
        return Integer.MAX_VALUE;
    }
}
