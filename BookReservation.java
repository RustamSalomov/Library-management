import java.util.*;
enum ReservationStatus {
    WAITING,
    COMPLETED,
    CANCELED
}

public class BookReservation {
    private Date creationDate;
    private ReservationStatus status;
    private BookItem bookItem;
    private Account member;

public BookReservation(BookItem bookItem, Account member) {
    this.bookItem = bookItem;
    this.member = member;
    this.creationDate = new Date();
    this.status = ReservationStatus.WAITING;

    bookItem.setStatus(BookStatus.RESERVED);
}

    public void completeReservation() {
        status = ReservationStatus.COMPLETED;
    }

    public void cancelReservation() {
        status = ReservationStatus.CANCELED;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
