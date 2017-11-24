package com.ninjabooks.service.rest.comment;

import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.json.comment.CommentResponse;

import java.util.Set;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface CommentRestService
{
    /**
     * Fetching all book comments by book ISBN number, beacuse in dn cam be store
     * mulitple editions.
     *
     * @param isbn - number which is necessary to find book
     * @return set of all fits books
     */

    Set<CommentResponse> getComments(String isbn);

    /**
     * Add comment to book when user return borrowed book. There is two weeks window
     * period to end book.
     *
     * @param comment - contet which contains proper comment
     * @param userID - user id
     * @param bookID - book id
     */

    void addComment(String comment, Long userID, Long bookID) throws CommentException;
}
