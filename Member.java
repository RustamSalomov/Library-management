public class Member extends Account {
    private static final int MAX_BOOKS = 5;

    public Member(String id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxBooksAllowed() {
        return MAX_BOOKS;
    }
}

