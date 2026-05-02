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

    public boolean checkout(Account account) {
        if (status != BookStatus.AVAILABLE) return false;

        status = BookStatus.LOANED;
        new BookLending(this, account);
        return true;
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