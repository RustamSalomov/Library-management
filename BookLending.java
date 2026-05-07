import java.time.LocalDate;

public class BookLending {

    private String bookBarcode;
    private String memberId;

    private LocalDate creationDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public BookLending(String bookBarcode, String memberId) {

        this.bookBarcode = bookBarcode;
        this.memberId = memberId;

        this.creationDate = LocalDate.now();
        this.dueDate = creationDate.plusDays(10);
    }

    public String getBookBarcode() {
        return bookBarcode;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
