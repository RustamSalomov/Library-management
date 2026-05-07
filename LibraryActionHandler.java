import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryActionHandler {

    public void checkOutBook(Member member, BookItem book) throws LibraryActionException {
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new LibraryActionException("Book is not available for checkout.");
        }
        if (book.isReferenceOnly()) {
            throw new LibraryActionException("Reference-only books cannot be issued.");
        }
        if (member.getTotalBooksCheckedOut() >= 5) {
            throw new LibraryActionException("Member checkout limit reached (max 5).");
        }

        BookLending lending = new BookLending(book.getBarcode(), member.getId());
        book.setStatus(BookStatus.LOANED);
        member.addLending(lending);
    }

    public double returnBook(Member member, BookItem book) throws LibraryActionException {
        BookLending lending = member.getLendingForBook(book.getBarcode());
        if (lending == null) throw new LibraryActionException("Lending record not found.");

        double fineAmount = 0;
        if (LocalDate.now().isAfter(lending.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(lending.getDueDate(), LocalDate.now());
            fineAmount = daysLate * 1.0;
            member.addFine(new Fine(fineAmount, member.getId()));
        }

        book.setStatus(BookStatus.AVAILABLE);
        member.removeLending(lending);

        if (book.hasReservations()) {
            book.setStatus(BookStatus.RESERVED);
        }

        return fineAmount;
    }

    public void renewBook(Member member, BookItem book) throws LibraryActionException {
        if (book.isReservedByOther(member)) {
            throw new LibraryActionException("Renewal failed: book is reserved by another member.");
        }

        BookLending lending = member.getLendingForBook(book.getBarcode());
        if (lending == null) throw new LibraryActionException("No active lending record found.");

        lending.setDueDate(lending.getDueDate().plusDays(10));
    }
}
