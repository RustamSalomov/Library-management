public class LibraryActionHandler {

    public void checkOutBook(Member member, BookItem item)
            throws LibraryActionException {

        if (item.getStatus() != BookStatus.AVAILABLE) {
            throw new LibraryActionException("Book not available.");
        }

        if (member.getBorrowedBooksCount() >= member.getMaxBooksAllowed()) {
            throw new LibraryActionException("Limit reached.");
        }

        item.checkout(member);
        member.borrowBook(item);
    }

    public double returnBook(Member member, BookItem item)
            throws LibraryActionException {

        if (item.getStatus() != BookStatus.LOANED) {
            throw new LibraryActionException("Book is not loaned.");
        }

        member.returnBook(item);
        item.returnBook();

        return 0;
    }
