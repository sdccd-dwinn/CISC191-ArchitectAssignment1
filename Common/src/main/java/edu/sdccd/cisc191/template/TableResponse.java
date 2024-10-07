package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class TableResponse {
    private ResponseType responseType;
    private String[][] foundRows;
    private boolean updated; // TODO maybe remove this?
    private String[] header;
    private Exception exception;

    public static enum ResponseType {
        CREATED_ROW,
        READ_ROW,
        UPDATED_ROW,
        DELETED_ROW,
        EXCEPTION
    }

    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toJSON(TableResponse TableResponse) throws Exception {
        return objectMapper.writeValueAsString(TableResponse);
    }
    public static TableResponse fromJSON(String input) throws Exception{
        return objectMapper.readValue(input, TableResponse.class);
    }
    protected TableResponse() {}

    public TableResponse(ResponseType responseType) {
        this.responseType = responseType;
    }

    @Override
    public String toString() {
        return String.format(
            "TableResponse[responseType=%s, foundRows='%s', updated=%s, exception='%s']",
            responseType,
            Arrays.deepToString(foundRows),
            updated,
            exception
        );
    }

    public ResponseType getResponseType() { return responseType; }
    public String[][] getFoundRows() { return foundRows; }
    public boolean getUpdated() { return updated; }
    public String[] getHeader() { return header; }
    public Exception getException() { return exception; }

    public void setResponseType(ResponseType responseType) { this.responseType = responseType; }
    public void setFoundRows(String[][] foundRows) { this.foundRows = foundRows; }
    public void setUpdated(boolean updated) { this.updated = updated; }
    public void setHeader(String[] header) { this.header = header; }
    public void setException(Exception exception) { this.exception = exception; }
}
