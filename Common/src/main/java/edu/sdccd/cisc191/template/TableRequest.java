package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class TableRequest {
    private RequestType requestType;
    private String[] findFields;
    private String[] findValues;
    private String[] updateFields;
    private String[] updateValues;

    public static enum RequestType {
        INSERT_ROW,
        GET_ROW_BY,
        GET_ROW_BY_AND,
        GET_ROW_BY_OR,
        GET_ROWS_BY,
        GET_ALL_ROWS,
        UPDATE_ROW_BY,
        DELETE_ROW_BY
    }

    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toJSON(TableRequest tableRequest) throws Exception {
        return objectMapper.writeValueAsString(tableRequest);
    }
    public static TableRequest fromJSON(String input) throws Exception{
        return objectMapper.readValue(input, TableRequest.class);
    }
    protected TableRequest() {}

    public TableRequest(RequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return String.format(
            "TableRequest[requestType=%s, findFields='%s', findValues='%s', updateFields='%s', updateValues='%s']",
            requestType,
            Arrays.toString(findFields),
            Arrays.toString(findValues),
            Arrays.toString(updateFields),
            Arrays.toString(updateValues)
        );
    }

    public RequestType getRequestType() { return requestType; }
    public String[] getFindFields() { return findFields; }
    public String[] getFindValues() { return findValues; }
    public String[] getUpdateFields() { return updateFields; }
    public String[] getUpdateValues() { return updateValues; }

    public void setRequestType(RequestType requestType) { this.requestType = requestType; }
    public void setFindFields(String[] findFields) { this.findFields = findFields; }
    public void setFindValues(String[] findValues) { this.findValues = findValues; }
    public void setUpdateFields(String[] updateFields) { this.updateFields = updateFields; }
    public void setUpdateValues(String[] updateValues) { this.updateValues = updateValues; }
}
