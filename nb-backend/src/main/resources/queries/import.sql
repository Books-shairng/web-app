-- ################################################# USER TABLE
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (1, 'User 0', 'email0@sb.gov', '$2a$10$hJs3yeBGLMKnimakK0tjYOWGyMBPWgtBceW6pltjEhTCTVsjWbag.', 'USER', TRUE)
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (2, 'User 1', 'email1@sb.gov', '$2a$10$X1lCYm.9nnJ.LvW43pyd4eSd3UgIrZxKKxej6ZMgt4vXaqrpfuU7W', 'USER', TRUE)
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (3, 'User 2', 'email2@sb.gov', '$2a$10$q0fSwrqVWU/M71kM/Pd.huvQpn68qmKQtSMbIWln35Ze3PVLcosBW', 'USER', TRUE)
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (4, 'User 3', 'email3@sb.gov', '$2a$10$2NoXgiZ2j6ufl2hyuxqQB.pYmSc42jaeziYSDXarptixh/.lO8/wm', 'USER', TRUE)
INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES (5, 'User 4', 'email4@sb.gov', '$2a$10$mdNtGaE7ae612t7QoX5suelZf2F0.HuIS4ipm78KyHi4s/oWDHEPa', 'USER', TRUE)

-- ################################################# QR_CODE TABLE
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (1, '77000abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (2, '77010abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (3, '77020abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (4, '77030abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (5, '77040abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (6, '77050abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (7, '77060abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (8, '77070abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (9, '77080abcde', TRUE)
INSERT INTO QR_CODE(id, DATA, ACTIVE) VALUES (10, '77090abcde', TRUE)

-- ################################################# BOOK TABLE
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (1, 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', '978-0132350884', 1, TRUE, 'BORROWED', 'Even bad code can function. But if code isn’t clean, it can bring a development organization to its knees. ')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (2, 'Effective Java', 'Joshua Bloch', '978-0321356680', 2, TRUE, 'BORROWED', 'Are you looking for a deeper understanding of the Java™ programming language so that you can write code that is clearer, more correct, more robust, and more reusable? ')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (3, 'Code Complete: A Practical Handbook of Software Construction', 'Steve McConnell', ' 978-0735619678', 3, TRUE, 'BORROWED', 'Widely considered one of the best practical guides to programming, Steve McConnell’s original CODE COMPLETE has been helping developers write better software for more than a decade. ')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (4, 'Refactoring: Improving the Design of Existing Code', 'Martin Fowler', '978-0201485677', 4, TRUE, 'BORROWED', 'As the application of object technology--particularly the Java programming language--has become commonplace, a new problem has emerged to confront the software development community. ')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (5, 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma, Richard Helm, Ralph Johson, John Vlissides', '978-0201633610', 5, TRUE, 'BORROWED', 'These texts cover the design of object-oriented software and examine how to investigate requirements, create solutions and then translate designs into code')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (6, 'ng-book: The Complete Guide to Angular 4', 'Nathan Murray, Ari Lerner, Felipe Coury, Carlos Taborda', '978-1546376231', 6, TRUE, 'FREE', 'ng-book. The in-depth, complete, and up-to-date book on Angular 4. Become an Angular 4 expert today.')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (7, 'Test Driven Development: By Example', 'Kent Beck', '978-0321146533', 7, TRUE, 'FREE', 'Follows two TDD projects from start to finish, illustrating techniques programmers can use to increase the quality of their work. ')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (8, 'Spring in Action: Covers Spring 4', 'Craig Walls', '978-1617291203', 8, TRUE, 'FREE', 'Spring in Action, Fourth Edition is a hands-on guide to the Spring Framework, updated for version 4')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (9, 'Android Programming for Beginners', 'John Horton', '978-1785883262', 9, TRUE, ' FREE', 'Kick-start your Android programming career, or just have fun publishing apps to the Google Play marketplaceA first-principles introduction ')
INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, QR_code_id, ACTIVE, STATUS, DESCRIPTION) VALUES (10, 'Android Programming for Beginners', 'John Horton', '978-1785883262', 10, TRUE, ' FREE', 'Kick-start your Android programming career, or just have fun publishing apps to the Google Play marketplaceA first-principles introduction ')

-- ################################################# ORDER DATE TABLE
INSERT INTO QUEUE (ID, ORDER_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (1, '2017-04-10 11:30:22', 2, 1, TRUE)
INSERT INTO QUEUE (ID, ORDER_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (2, '2017-04-11 11:31:22', 3, 1, TRUE)
INSERT INTO QUEUE (ID, ORDER_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (3, '2017-04-12 11:32:22', 4, 1, TRUE)
INSERT INTO QUEUE (ID, ORDER_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (4, '2017-04-13 11:33:22', 1, 2, TRUE)
INSERT INTO QUEUE (ID, ORDER_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (5, '2017-04-14 11:34:22', 1, 3, TRUE)

-- ################################################# BORROW TABLE
INSERT INTO BORROW (id, BORROW_DATE, EXPECTED_RETURN_DATE, BOOK_ID, USER_ID, CAN_EXTEND_RETURN_DATE, ACTIVE) VALUES (1, '2017-04-03', '2017-05-03', 1, 1, TRUE, TRUE)
INSERT INTO BORROW (id, BORROW_DATE, EXPECTED_RETURN_DATE, BOOK_ID, USER_ID, CAN_EXTEND_RETURN_DATE, ACTIVE) VALUES (2, '2017-04-03', '2017-05-03', 2, 2, TRUE, TRUE)
INSERT INTO BORROW (id, BORROW_DATE, EXPECTED_RETURN_DATE, BOOK_ID, USER_ID, CAN_EXTEND_RETURN_DATE, ACTIVE) VALUES (3, '2017-04-03', '2017-05-03', 3, 3, TRUE, TRUE)
INSERT INTO BORROW (id, BORROW_DATE, EXPECTED_RETURN_DATE, BOOK_ID, USER_ID, CAN_EXTEND_RETURN_DATE, ACTIVE) VALUES (4, '2017-04-04', '2017-05-04', 4, 4, FALSE, TRUE)
INSERT INTO BORROW (id, BORROW_DATE, EXPECTED_RETURN_DATE, BOOK_ID, USER_ID, CAN_EXTEND_RETURN_DATE, ACTIVE) VALUES (5, '2017-04-05', '2017-05-05', 5, 1, FALSE, TRUE)

-- ################################################# HISTORY TABLE
INSERT INTO HISTORY (id, BORROW_DATE, RETURN_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (1, '2017-04-09', '2017-05-09', 1, 6, TRUE)
INSERT INTO HISTORY (id, BORROW_DATE, RETURN_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (2, '2017-04-10', '2017-05-10', 2, 7, TRUE)
INSERT INTO HISTORY (id, BORROW_DATE, RETURN_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (3, '2017-04-11', '2017-05-11', 3, 8, TRUE)
INSERT INTO HISTORY (id, BORROW_DATE, RETURN_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (4, '2017-04-12', '2017-05-12', 4, 9, TRUE)
INSERT INTO HISTORY (id, BORROW_DATE, RETURN_DATE, BOOK_ID, USER_ID, ACTIVE) VALUES (5, '2017-04-13', '2017-05-13', 5, 10, TRUE)

-- ################################################# COMMENT TABLE
INSERT INTO COMMENT (id, RETURN_DATE, BOOK_ID, USER_ID, ACTIVE, CONTENT) VALUES (1, '2017-05-09', 1, 6, TRUE, 'GOOD BOOK')
INSERT INTO COMMENT (id, RETURN_DATE, BOOK_ID, USER_ID, ACTIVE, CONTENT) VALUES (2, '2017-05-10', 2, 7, TRUE, 'GOOD BOOK')
