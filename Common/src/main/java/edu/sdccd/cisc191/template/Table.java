package edu.sdccd.cisc191.template;

import java.io.*;

public class Table implements Serializable {
    private final String[] header;
    private String[][] body;
    private int nextID = 0; // auto-incrementing primary key

    public Table(String[] header, String[][] body) {
        this(header);

        this.body = new String[0][this.header.length];
        for (String[] row : body) {
            insertRow(header, row);
        }
    }

    public Table(String[] header) {
        this.header = new String[header.length+1];
        this.header[0] = "_id";
        System.arraycopy(header, 0, this.header, 1, header.length);
    }

    // TODO default constructor?

    // TODO copy constructor?

    /**
     * serializes specified table to file at specified path
     * see: https://docs.oracle.com/javase/8/docs/api/java/io/ObjectOutputStream.html
     * @param path
     * @param table
     * @throws IOException
     */
    public static void saveToFile(String path, Table table) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(table);

        oos.close();
        fos.close();
    }

    /**
     * un-serializes table from file at specified path
     * see: https://docs.oracle.com/javase/8/docs/api/java/io/ObjectInputStream.html
     * @param path
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Table loadFromFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Table table = (Table) ois.readObject();

        ois.close();
        fis.close();

        return table;
    }

    /**
     * finds which column represents the specified field
     * @param field
     * @return
     */
    private int getColumnIndex(String field) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].equals(field)) {
                return i;
            }
        }
        throw new IllegalArgumentException("field " + field + " not found");
    }

    /**
     * finds one row with the specified value in the specified field
     * @param field
     * @param value
     * @return
     */
    private int getRowIndex(String field, String value) {
        int columnIndex = getColumnIndex(field);
        for (int i = 0; i < body.length; i++) {
            if (body[i][columnIndex].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * finds one row with the specified values in ALL specified fields (AND) or at least one (OR)
     * @param fields
     * @param values
     * @param or
     * @return
     */
    private int getRowIndex(String[] fields, String[] values, boolean or) {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Keys and values must be the same length");
        }

        int[] columnIndexes = new int[fields.length];
        int n = 0;
        for (String field : fields) {
            int columnIndex = getColumnIndex(field);
            columnIndexes[n] = columnIndex;
            n++;
        }

        int rowIndex = -1;
        for (int i = 0; i < body.length; i++) {
            for (int j = 0; j < columnIndexes.length; j++) {
                if (body[i][columnIndexes[j]].equals(values[j])) {
                    if (!or) {
                        rowIndex = i; // AND: this field matches, but we still need to check the others
                    } else {
                        rowIndex = i; // OR: this field matches, good enough
                        break;
                    }
                } else {
                    if (!or) {
                        rowIndex = -1;
                        break; // AND: if any field doesn't match then this isn't a match after all
                    }
                }
            }
            if (rowIndex != -1) {
                break; // once we have one match we don't need to keep searching
            }
        }

        return rowIndex;
    }

    /**
     * finds ALL rows with the specified value in the specified field
     * @param field
     * @param value
     * @return
     */
    private int[] getRowIndexes(String field, String value) {
        int columnIndex = getColumnIndex(field);

        int[] rows = new int[0];

        for (int i = 0; i < body.length; i++) {
            if (body[i][columnIndex].equals(value)) {
                // grow array
                int[] newRows = new int[rows.length+1];
                System.arraycopy(rows, 0, newRows, 0, rows.length);
                rows = newRows;

                rows[rows.length-1] = i;
            }
        }

        if (rows.length == 0) {
            return null;
        } else {
            return rows;
        }
    }

    /**
     * just so we can show the user the column labels instead of indexes
      * @return
     */
    public String[] getHeader() {
        return header;
    }

    // TODO method update header eg to add new columns?

    /**
     * Creates a new row with the specified values in the specified fields.
     * does not check for duplicates, will append instead of overwriting.
     * @param fields
     * @param values
     */
    public void insertRow(String[] fields, String[] values) {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Keys and values must be the same length");
        }

        // grow array
        String[][] newBody = new String[body.length+1][header.length];
        /* this is slower than System.arraycopy()
        for (int i = 0; i < body.length; i++) {
            newBody[i] = body[i];
        }
         */
        System.arraycopy(body, 0, newBody, 0, body.length);
        body = newBody;

        // auto-incrementing primary key
        body[body.length - 1][0] = String.valueOf(nextID);
        nextID++;

        for (int i = 0; i < fields.length; i++) {
            int columnIndex = getColumnIndex(fields[i]);
            if (columnIndex == 0) {
                throw new IllegalArgumentException("Not allowed to manually set field: " + header[0]);
            }
            body[body.length - 1][columnIndex] = values[i];
        }
    }

    /**
     * Reads one row with the specified value in the specified field
     * @param field
     * @param value
     * @return
     */
    public String[] getRowBy(String field, String value) {
        int rowIndex = getRowIndex(field, value);
        if (rowIndex == -1) {
            return null;
        }
        return body[rowIndex];

        /* TODO could also implement same way as getRowByAnd() below. maybe combine them
        int rowIndex = getRowIndex(new String[] { field }, new String[] { value }, false);
        if (rowIndex == -1) {
            return null;
        }
        return body[rowIndex];
         */
    }

    /**
     * Reads one row with the specified values in ALL specified fields
     * @param fields
     * @param values
     * @return
     */
    public String[] getRowByAnd(String[] fields, String[] values) {
        int rowIndex = getRowIndex(fields, values, false);
        if (rowIndex == -1) {
            return null;
        }
        return body[rowIndex];
    }

    /**
     * Reads one row with the specified values in at least one specified field
     * @param fields
     * @param values
     * @return
     */
    public String[] getRowByOr(String[] fields, String[] values) {
        int rowIndex = getRowIndex(fields, values, true);
        if (rowIndex == -1) {
            return null;
        }
        return body[rowIndex];
    }

    /**
     * Reads ALL rows with the specified value in the specified field
     * @param field
     * @param value
     * @return
     */
    public String[][] getRowsBy(String field, String value) {
        int[] rowIndexes = getRowIndexes(field, value);

        if (rowIndexes == null) { // || rowIndexes.length == 0
            return null;
        }

        String[][] rows = new String[rowIndexes.length][header.length];

        int i = 0;
        for (int rowIndex : rowIndexes) {
            rows[i] = body[rowIndex];
            i++;
        }

        return rows;
    }

    // TODO method that Reads ALL rows with the specified values in ALL specified fields?

    // TODO method that Reads ALL rows with the specified values in at least one specified field?

    // TODO method wraps other read methods but returns ONLY requested fields in specified order?

    /**
     * Reads ALL rows from the entire table
     * @return
     */
    public String[][] getAllRows() {
        return body;
    }

    /**
     * Updates an existing row with the specified value in the specified field.
     * fields not specified will retain their existing values.
     * @param oldField
     * @param oldValue
     * @param newFields
     * @param newValues
     * @return
     */
    public boolean updateRowBy(String oldField, String oldValue, String[] newFields, String[] newValues) {
        int rowIndex = getRowIndex(oldField, oldValue);
        if (rowIndex == -1) {
            return false;
        }

        if (newFields.length != newValues.length) {
            throw new IllegalArgumentException("Keys and values must be the same length");
        }

        for (int i = 0; i < newFields.length; i++) {
            int columnIndex = getColumnIndex(newFields[i]);
            if (columnIndex == 0) {
                throw new IllegalArgumentException("Not allowed to manually set field: " + header[0]);
            }
            body[rowIndex][columnIndex] = newValues[i];
        }

        return true;
    }

    /**
     * Deletes an existing row with the specified value in the specified field
     * @param field
     * @param value
     * @return
     */
    public boolean deleteRowBy(String field, String value) {
        int rowIndex = getRowIndex(field, value);
        if (rowIndex == -1) {
            return false;
        }

        // shrink array
        String[][] newBody = new String[body.length-1][header.length];
        for (int i = 0; i < body.length; i++) {
            if (i < rowIndex) {
                newBody[i] = body[i];
            } else if (i > rowIndex) {
                newBody[i-1] = body[i];
            } else {
                continue;
            }
        }
        /* this is less readable than manually copying the array in a for loop
        System.arraycopy(body, 0, newBody, 0, rowIndex);
        System.arraycopy(body, rowIndex+1, newBody, rowIndex, body.length - (rowIndex+1));
         */
        body = newBody;

        return true;
    }
}
