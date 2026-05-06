enum BookStatus {
    AVAILABLE,
    RESERVED,
    LOANED,
    LOST
}

public class BookItem {
    private String barcode;
    private Book book;
    private BookStatus status;
    private Rack rack;

    public BookItem(String barcode, Book book) {
        this.barcode = barcode;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }
    public void setStatus(BookStatus status) {
    this.status = status;
}

public BookLending checkout(Account account) throws LibraryActionException {

    if (status != BookStatus.AVAILABLE) {
        throw new LibraryActionException("Book is not available.");
    }

    if (account instanceof Member member) {
        if (member.getBorrowedBooksCount() >= member.getMaxBooksAllowed()) {
            throw new LibraryActionException("Max book limit reached.");
        }
    }

    status = BookStatus.LOANED;

    BookLending lending = new BookLending(this, account);
    return lending;
}

    public void returnBook() {
        status = BookStatus.AVAILABLE;
    }

    public String getBarcode() {
        return barcode;
    }
    public BookStatus getStatus() {
        return status;
    }
    public Book getBook() {
        return book;
    }
}
