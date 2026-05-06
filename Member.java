public class Member extends Account {
    private static final int MAX_BOOKS = 5;

    private List<BookItem> borrowedBooks = new ArrayList<>();

    public Member(String id, String name) {
        super(id, name);
    }

    public void borrowBook(BookItem item) {
        borrowedBooks.add(item);
    }

    public void returnBook(BookItem item) {
        borrowedBooks.remove(item);
    }

    public int getBorrowedBooksCount() {
        return borrowedBooks.size();
    }

    @Override
    public int getMaxBooksAllowed() {
        return MAX_BOOKS;
    }
}

