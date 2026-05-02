import java.util.*;
public class Library {
    private String name;
    private String address;
    private List<BookItem> bookItems;

    public Library(String name, String address) {
        this.name = name;
        this.address = address;
        this.bookItems = new ArrayList<>();
    }

    public void addBookItem(BookItem item) {
        bookItems.add(item);
    }

    public void removeBookItem(BookItem item) {
        bookItems.remove(item);
    }

    public List<BookItem> getBookItems() {
        return bookItems;
    }
}
