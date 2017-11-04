package com.ninjabooks.service.rest.comment;

import com.ninjabooks.json.comment.CommentResponse;

import java.util.Set;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface CommentService
{
    /**
     * Fetching all book comments by book ISBN number, beacuse in dn cam be store
     * mulitple editions.
     *
     * @param isbn - number which is necessary to find book
     * @return set of all fits books
     */

    Set<CommentResponse> getComments(String isbn);
}
