package edu.sdccd.cisc191.template;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TableTest {
    private Table table;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        table = new Table(
            new String[] { "name" },
            new String[][] {
                { "John Smith" },
                { "Mr. Cardholder" }
            }
        );
    }

    @org.junit.jupiter.api.Test
    void getAllRows() {
        assertArrayEquals(
            new String[][] {
                { "0", "John Smith" },
                { "1", "Mr. Cardholder" }
            },
            table.getAllRows()
        );
    }

    @org.junit.jupiter.api.Test
    void getRowById() {
        assertArrayEquals(
            new String[] { "0", "John Smith" },
            table.getRowBy("_id", "0")
        );
    }

    @org.junit.jupiter.api.Test
    void getRowByName() {
        assertArrayEquals(
            new String[] { "1", "Mr. Cardholder" },
            table.getRowBy("name", "Mr. Cardholder")
        );
    }

    @org.junit.jupiter.api.Test
    void getRowByIdAndName() {
        assertArrayEquals(
            new String[] { "1", "Mr. Cardholder" },
            table.getRowByAnd(new String[] { "_id", "name" }, new String[] { "1", "Mr. Cardholder" })
        );
    }

    @org.junit.jupiter.api.Test
    void getRowByIdAndNameFail() {
        assertNull(
            table.getRowByAnd(new String[] { "_id", "name" }, new String[] { "2", "Mr. Cardholder" })
        );
    }

    @org.junit.jupiter.api.Test
    void getRowByIdOrName() {
        assertArrayEquals(
            new String[] { "1", "Mr. Cardholder" },
            table.getRowByOr(new String[] { "_id", "name" }, new String[] { "2", "Mr. Cardholder" })
        );

        assertArrayEquals(
            new String[] { "1", "Mr. Cardholder" },
            table.getRowByOr(new String[] { "_id", "name" }, new String[] { "1", "Jane Doe" })
        );
    }

    @org.junit.jupiter.api.Test
    void getRowsById() {
        assertArrayEquals(
            new String[][] { { "0", "John Smith" } },
            table.getRowsBy("_id", "0")
        );
    }

    @org.junit.jupiter.api.Test
    void getRowsByName() {
        table.insertRow(new String[] { "name" }, new String[] { "John Smith" });

        assertArrayEquals(
            new String[][] { { "0", "John Smith" }, { "2", "John Smith" } },
            table.getRowsBy("name", "John Smith")
        );
    }

    @org.junit.jupiter.api.Test
    void insertRow() {
        String[] keys = new String[] { "name" };
        String[] values = new String[] { "Jane Doe" };
        table.insertRow(keys, values);

        assertArrayEquals(
            new String[] { "2", "Jane Doe" },
            table.getRowBy("_id", "2")
        );
    }

    @org.junit.jupiter.api.Test
    void updateRowById() {
        String[] keys = new String[] { "name" };
        String[] values = new String[] { "Mr. Smith" };
        table.updateRowBy("_id", "0", keys, values);

        assertArrayEquals(
            new String[] { "0", "Mr. Smith" },
            table.getRowBy("_id", "0")
        );
    }

    @org.junit.jupiter.api.Test
    void updateRowByName() {
        String[] keys = new String[] { "name" };
        String[] values = new String[] { "Mr. Doe" };
        table.updateRowBy("name", "Mr. Cardholder", keys, values);

        assertArrayEquals(
            new String[] { "1", "Mr. Doe" },
            table.getRowBy("_id", "1")
        );
    }

    @org.junit.jupiter.api.Test
    void updateFail() {
        assertFalse(
            table.updateRowBy("name", "Mr. Nonexistent ", new String[] {}, new String[] {})
        );
    }

    @org.junit.jupiter.api.Test
    void deleteRowByIdFromBottom() {
        assertTrue(
            table.deleteRowBy("_id", "1")
        );

        assertArrayEquals(
            new String[] { "0", "John Smith" },
            table.getRowBy("_id", "0")
        );
    }

    @org.junit.jupiter.api.Test
    void deleteRowByIdFromTop() {
        assertTrue(
            table.deleteRowBy("_id", "0")
        );

        assertArrayEquals(
            new String[] { "1", "Mr. Cardholder" },
            table.getRowBy("_id", "1")
        );
    }

    @org.junit.jupiter.api.Test
    void deleteRowByIdEverything() {
        assertTrue(
            table.deleteRowBy("_id", "0")
        );

        assertTrue(
            table.deleteRowBy("_id", "1")
        );

        assertNull(
            table.getRowBy("_id", "0")
        );

        assertNull(
            table.getRowBy("_id", "1")
        );
    }

    @org.junit.jupiter.api.Test
    void saveLoadFile() throws IOException, ClassNotFoundException {
        // make sure file does not exist already
        File file = new File("TableTest.tmp");
        if (file.exists()) {
            file.delete();
        }

        // save table in memory to file
        Table.saveToFile("TableTest.tmp", table);

        // make sure file now exists
        assertTrue(file.exists() && !file.isDirectory() && file.canRead() && file.length() > 0);

        // load table back from file into memory
        Table loadedTable = Table.loadFromFile("TableTest.tmp");

        // make sure table contents are right
        assertArrayEquals(table.getAllRows(), loadedTable.getAllRows());
    }
}
