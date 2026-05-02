import java.time.LocalDate;

public class Fine {
    private double amount;
    private LocalDate creationDate;
    private String memberId;

    public Fine(double amount, String memberId) {
        this.amount = amount;
        this.creationDate = LocalDate.now();
        this.memberId = memberId;
    }

    public double getAmount() { return amount; }
}
