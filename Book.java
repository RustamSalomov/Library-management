public class Book {
    private String ISBN;
    private String title;
    private String subject;
    private String publisher;
    private List<Author> authors;

    public Book(String ISBN, String title, String subject, String publisher) {
        this.ISBN = ISBN;
        this.title = title;
        this.subject = subject;
        this.publisher = publisher;
        this.authors = new ArrayList<>();
    }

    public String getISBN() {
        return ISBN;
    }
    public String getTitle() {
        return title;
    }
    public String getSubject() {
        return subject;
    }
    public String getPublisher() {
        return publisher;
    }
}
