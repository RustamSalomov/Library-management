import java.util.*;

public class BookLending {
    private Date creationDate;
    private Date dueDate;
    private Date returnDate;

    private BookItem bookItem;
    private Account borrower;

    public BookLending(BookItem bookItem, Account borrower) {
        this.bookItem = bookItem;
        this.borrower = borrower;
        this.creationDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 10);
        this.dueDate = cal.getTime();
    }

    public double returnBook() {
        this.returnDate = new Date();
        bookItem.returnBook();

        long diff = returnDate.getTime() - dueDate.getTime();
        long daysLate = diff / (1000 * 60 * 60 * 24);

        if (daysLate > 0) {
            return daysLate * 1.0; // fine per day
        }
        return 0;
    }
}