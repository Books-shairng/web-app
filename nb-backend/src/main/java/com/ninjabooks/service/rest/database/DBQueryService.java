package com.ninjabooks.service.rest.database;

import java.util.List;
import java.util.Map;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface DBQueryService
{
    List<Map<String, String>> execute(String query);
}
