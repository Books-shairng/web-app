-- ################################################# USER TABLE
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (1, 'John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)

-- ################################################# QR_CODE TABLE
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (1, '12345abcde', TRUE)

-- ################################################# BOOK TABLE
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, ACTIVE, STATUS, QR_code_id, DESCRIPTION) VALUES (1, 'Effective Java', 'J. Bloch', '978-0321356680', TRUE, 'BORROWED', 1, 'Some description')

-- ################################################# BORROW TABLE
INSERT INTO BORROW (id, BORROW_DATE, EXPECTED_RETURN_DATE, CAN_EXTEND_RETURN_DATE, ACTIVE, BOOK_ID, USER_ID) VALUES (1, '2017-01-01', '2017-01-31', TRUE, TRUE, 1, 1)

