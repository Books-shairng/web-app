-- ################################################# USER TABLE
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (1, 'John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)

-- ################################################# QR_CODE TABLE
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (1, '12345abcde', TRUE)

-- ################################################# BOOK TABLE
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, ACTIVE, STATUS, QR_code_id, DESCRIPTION) VALUES (1, 'Effective Java', 'J. Bloch', '978-0321356680', TRUE, 'FREE', 1, 'Some description')

-- ################################################# ORDER DATE TABLE
INSERT INTO QUEUE (ID, ORDER_DATE, ACTIVE, BOOK_ID, USER_ID) VALUES (1, '2017-03-21 08:17:00', TRUE, 1, 1)

-- ################################################# BORROW TABLE
INSERT INTO BORROW (id, BORROW_DATE, EXPECTED_RETURN_DATE, CAN_EXTEND_RETURN_DATE, ACTIVE, BOOK_ID, USER_ID) VALUES (1, '2017-01-01', '2017-01-31', TRUE, TRUE, 1, 1)

-- ################################################# HISTORY TABLE
INSERT INTO HISTORY (id, RETURN_DATE, COMMENTED, ACTIVE, BOOK_ID, USER_ID) VALUES (1, '2017-01-31', TRUE, TRUE, 1, 1)

-- ################################################# HISTORY TABLE
INSERT INTO COMMENT (id, CONTENT, DATE, ACTIVE, BOOK_ID, USER_ID) VALUES (1, 'any comment content', '2017-05-05 23:22:10', TRUE, 1, 1)
