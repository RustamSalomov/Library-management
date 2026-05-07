import java.util.*;
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
    private boolean referenceOnly;
    private List<BookReservation> reservations = new ArrayList<>();

    public BookItem(String barcode, Book book) {
        this.barcode = barcode;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }
    public void setStatus(BookStatus status) {
        this.status = status;
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
    public boolean isReferenceOnly() {
        return referenceOnly;
    }

    public boolean hasReservations() {
        return !reservations.isEmpty();
    }

    public boolean isReservedByOther(Member member) {

        for (BookReservation reservation : reservations) {

            if (reservation.getMember() != member &&
                    reservation.getStatus() == ReservationStatus.WAITING) {

                return true;
            }
        }

        return false;
    }

    public void addReservation(BookReservation reservation) {
        reservations.add(reservation);
    }
}
