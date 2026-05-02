import java.util.*;
public class LibraryCard {
    private String barcode;
    private Date issueDate;
    private Account owner;

    public LibraryCard(String barcode, Account owner) {
        this.barcode = barcode;
        this.owner = owner;
        this.issueDate = new Date();
    }

    public String getBarcode() {
        return barcode;
    }
    public Account getOwner() {
        return owner;
    }
}
