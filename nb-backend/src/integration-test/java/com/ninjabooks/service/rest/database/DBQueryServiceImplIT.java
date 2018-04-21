package com.ninjabooks.service.rest.database;

import com.ninjabooks.config.AbstractBaseIT;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ENCRYPTED_PASSWORD;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.SQLGrammarException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.entry;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class DBQueryServiceImplIT extends AbstractBaseIT
{
    private static final String EXPECTED_INSTERT_RESULT = "1";
    private static final String NOT_SQL_COMMAND = "simple text";
    private static final String SELECT_QUERY = "SELECT * FROM USER";
    private static final String INSERT_QUERY =
        "INSERT INTO USER (NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) " +
        "VALUES ('John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)";

    @Autowired
    private DBQueryService sut;

    @Test
    public void testExecuteQueryWithSQLSelectionShouldReturnExpectedEntity() throws Exception {
        List<Map<String, String>> actual = sut.execute(SELECT_QUERY);

        assertThat(actual)
            .flatExtracting(Map::entrySet)
            .contains(
                entry("EMAIL", EMAIL),
                entry("PASSWORD", ENCRYPTED_PASSWORD),
                entry("NAME", NAME)
            );
    }

    @Test
    public void testExecuteQueryWithSQLInsertShouldInsertNewEntity() throws Exception {
        List<Map<String, String>> actual = sut.execute(INSERT_QUERY);

        assertThat(actual)
            .flatExtracting(Map::entrySet)
            .contains(entry("EXECUTE RESULT", EXPECTED_INSTERT_RESULT));
    }

    @Test
    public void testExecuteQueryWithWrongSQLCommandShouldThrowsException() throws Exception {
        assertThatExceptionOfType(SQLGrammarException.class)
            .isThrownBy(() -> sut.execute(NOT_SQL_COMMAND))
            .withCauseInstanceOf(SQLSyntaxErrorException.class);
    }
}
