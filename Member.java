import java.util.*;

public class Member extends Account {

    private static final int MAX_BOOKS = 5;

    private List<BookItem> borrowedBooks = new ArrayList<>();
    private List<BookLending> lendings = new ArrayList<>();
    private List<Fine> fines = new ArrayList<>();

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

    public int getTotalBooksCheckedOut() {
        return borrowedBooks.size();
    }

    public void addLending(BookLending lending) {
        lendings.add(lending);
    }

    public void removeLending(BookLending lending) {
        lendings.remove(lending);
    }

    public BookLending getLendingForBook(String barcode) {

        for (BookLending lending : lendings) {

            if (lending.getBookBarcode().equals(barcode)) {
                return lending;
            }
        }

        return null;
    }

    public void addFine(Fine fine) {
        fines.add(fine);
    }

    public String getId() {
        return id;
    }

    @Override
    public int getMaxBooksAllowed() {
        return MAX_BOOKS;
    }
}

