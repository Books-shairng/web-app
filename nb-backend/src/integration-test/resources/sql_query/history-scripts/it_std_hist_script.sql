-- ################################################# USER TABLE
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (1, 'John Dee', 'john.dee@exmaple.com', '$2a$10$yq9VL5OqEooEmsUtxKpwN.6.v/MdpMd/A/iZ/QVZJRmjS8AIVOki6', 'USER', TRUE)

-- ################################################# QR_CODE TABLE
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (1, '12345abcde', TRUE)

-- ################################################# BOOK TABLE
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, ACTIVE, STATUS, QR_code_id, DESCRIPTION) VALUES (1, 'Effective Java', 'J. Bloch', '978-0321356680', TRUE, 'FREE', 1, 'Some description')

-- ################################################# HISTORY TABLE
INSERT INTO HISTORY (id, RETURN_DATE, COMMENTED, ACTIVE, BOOK_ID, USER_ID) VALUES (1, '2017-01-31', TRUE, TRUE, 1, 1)
