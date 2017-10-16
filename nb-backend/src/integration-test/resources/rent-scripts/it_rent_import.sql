-- ################################################# USER TABLE
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (1, 'John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)

-- ################################################# QR_CODE TABLE
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (1, '12345abcde', TRUE)

-- ################################################# BOOK TABLE
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, ACTIVE, STATUS, QR_code_id, DESCRIPTION) VALUES (1, 'Effective Java', 'J. Bloch', '978-0321356680', TRUE, 'FREE', 1, 'Some description')
