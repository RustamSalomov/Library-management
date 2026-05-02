CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;


CREATE TABLE address (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street_address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zipcode VARCHAR(20),
    country VARCHAR(100)
);


CREATE TABLE person (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(20),
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES address(id)
);


CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT,
    password VARCHAR(255),
    status ENUM('Active', 'Closed', 'Cancelled', 'Blacklisted') DEFAULT 'Active',
    date_of_membership DATE,
    total_books_checkout INT DEFAULT 0,
    FOREIGN KEY (person_id) REFERENCES person(id)
);


CREATE TABLE librarians (
    id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT,
    password VARCHAR(255),
    status ENUM('Active', 'Closed', 'Cancelled', 'Blacklisted') DEFAULT 'Active',
    FOREIGN KEY (person_id) REFERENCES person(id)
);


CREATE TABLE library_cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(100),
    barcode VARCHAR(100),
    issued_at DATE,
    active BOOLEAN DEFAULT TRUE,
    member_id INT,
    FOREIGN KEY (member_id) REFERENCES members(id)
);


CREATE TABLE racks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    number INT,
    location_identifier VARCHAR(100)
);


CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ISBN VARCHAR(50),
    title VARCHAR(255),
    subject VARCHAR(150),
    publisher VARCHAR(150),
    language VARCHAR(50),
    number_of_pages INT
);


CREATE TABLE authors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150),
    description TEXT
);


CREATE TABLE book_authors (
    book_id INT,
    author_id INT,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (author_id) REFERENCES authors(id)
);


CREATE TABLE book_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    barcode VARCHAR(100),
    is_reference_only BOOLEAN DEFAULT FALSE,
    borrowed_date DATE,
    due_date DATE,
    price DECIMAL(10, 2),
    format ENUM('Hardcover', 'Paperback', 'Audiobook', 'Ebook', 'Newspaper', 'Magazine', 'Journal'),
    status ENUM('Available', 'Reserved', 'Loaned', 'Lost') DEFAULT 'Available',
    date_of_purchase DATE,
    publication_date DATE,
    book_id INT,
    rack_id INT,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (rack_id) REFERENCES racks(id)
);


CREATE TABLE book_reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_date DATE,
    status ENUM('Waiting', 'Pending', 'Completed', 'Cancelled', 'None') DEFAULT 'Waiting',
    book_item_id INT,
    member_id INT,
    FOREIGN KEY (book_item_id) REFERENCES book_items(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);


CREATE TABLE book_lending (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_date DATE,
    due_date DATE,
    return_date DATE,
    book_item_id INT,
    member_id INT,
    FOREIGN KEY (book_item_id) REFERENCES book_items(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);


CREATE TABLE fines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_date DATE,
    amount DECIMAL(10, 2),
    lending_id INT,
    FOREIGN KEY (lending_id) REFERENCES book_lending(id)
);


CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_on DATE,
    content TEXT,
    member_id INT,
    FOREIGN KEY (member_id) REFERENCES members(id)
);


CREATE TABLE catalog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creation_date DATE,
    total_books INT DEFAULT 0
);


DELIMITER $$

CREATE TRIGGER before_lending_insert_check_limit
BEFORE INSERT ON book_lending
FOR EACH ROW
BEGIN
    DECLARE current_checkouts INT;
    DECLARE book_status VARCHAR(50);
    DECLARE is_ref BOOLEAN;

    SELECT total_books_checkout INTO current_checkouts
    FROM members
    WHERE id = NEW.member_id;

    IF current_checkouts >= 5 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'This member has already reached the maximum limit of 5 books.';
    END IF;

    SELECT status, is_reference_only INTO book_status, is_ref
    FROM book_items
    WHERE id = NEW.book_item_id;

    IF is_ref = TRUE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'This book is for reference only and cannot be checked out.';
    END IF;

    IF book_status != 'Available' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'This book item is not available for checkout.';
    END IF;

    SET NEW.creation_date = CURDATE();
    SET NEW.due_date = DATE_ADD(CURDATE(), INTERVAL 14 DAY);
END$$


CREATE TRIGGER after_lending_insert
AFTER INSERT ON book_lending
FOR EACH ROW
BEGIN
    UPDATE book_items
    SET status = 'Loaned', borrowed_date = CURDATE()
    WHERE id = NEW.book_item_id;

    UPDATE members
    SET total_books_checkout = total_books_checkout + 1
    WHERE id = NEW.member_id;
END$$


CREATE TRIGGER after_lending_update
AFTER UPDATE ON book_lending
FOR EACH ROW
BEGIN
    DECLARE days_late INT;
    DECLARE reserved_count INT;

    IF NEW.return_date IS NOT NULL AND OLD.return_date IS NULL THEN

        SET days_late = DATEDIFF(NEW.return_date, NEW.due_date);

        IF days_late > 0 THEN
            INSERT INTO fines (creation_date, amount, lending_id)
            VALUES (CURDATE(), days_late * 1.00, NEW.id);
        END IF;

        UPDATE members
        SET total_books_checkout = total_books_checkout - 1
        WHERE id = NEW.member_id;

        SELECT COUNT(*) INTO reserved_count
        FROM book_reservations
        WHERE book_item_id = NEW.book_item_id
        AND status = 'Waiting';

        IF reserved_count > 0 THEN
            UPDATE book_items
            SET status = 'Reserved'
            WHERE id = NEW.book_item_id;

            INSERT INTO notifications (created_on, content, member_id)
            SELECT
                CURDATE(),
                'The book you reserved is now available. Please collect it within 3 days.',
                member_id
            FROM book_reservations
            WHERE book_item_id = NEW.book_item_id
            AND status = 'Waiting'
            ORDER BY creation_date ASC
            LIMIT 1;

            UPDATE book_reservations
            SET status = 'Pending'
            WHERE book_item_id = NEW.book_item_id
            AND status = 'Waiting'
            ORDER BY creation_date ASC
            LIMIT 1;

        ELSE
            UPDATE book_items
            SET status = 'Available'
            WHERE id = NEW.book_item_id;
        END IF;

    END IF;
END$$


CREATE TRIGGER after_reservation_insert
AFTER INSERT ON book_reservations
FOR EACH ROW
BEGIN
    UPDATE book_items
    SET status = 'Reserved'
    WHERE id = NEW.book_item_id
    AND status = 'Available';
END$$

DELIMITER ;
