package edu.sdccd.cisc191.template;

import javafx.event.ActionEvent;
import javafx.beans.Observable;

import java.util.Arrays;

import static edu.sdccd.cisc191.template.TableRequest.RequestType;
import static edu.sdccd.cisc191.template.TableRequest.RequestType.*;

public class MainController {
    private MainModel model;

    public MainController(MainModel model) {
        this.model = model;
    }

    public void submitButtonEventHandler(ActionEvent event) {
        TableRequest tableRequest;

        switch (model.requestType.getValue()) {
            default:
            case "Create New Row":
                tableRequest = new TableRequest(INSERT_ROW);
                tableRequest.setUpdateFields(new String[] { "name", "phone" });
                tableRequest.setUpdateValues(new String[] { model.argument1Text.getValue(), model.argument2Text.getValue() });
                break;
            case "Read Row By Field & Value":
                tableRequest = new TableRequest(GET_ROW_BY);
                tableRequest.setFindFields(new String[] { model.argument1Text.getValue() });
                tableRequest.setFindValues(new String[] { model.argument2Text.getValue() });
                break;
            case "Read Row By Fields & Values (AND)":
                tableRequest = new TableRequest(GET_ROW_BY_AND);
                tableRequest.setFindFields(new String[] { model.argument1Text.getValue(), model.argument3Text.getValue() });
                tableRequest.setFindValues(new String[] { model.argument2Text.getValue(), model.argument4Text.getValue() });
                break;
            case "Read Row By Fields & Values (OR)":
                tableRequest = new TableRequest(GET_ROW_BY_OR);
                tableRequest.setFindFields(new String[] { model.argument1Text.getValue(), model.argument3Text.getValue() });
                tableRequest.setFindValues(new String[] { model.argument2Text.getValue(), model.argument4Text.getValue() });
                break;
            case "Read Multiple Rows By Field & Value":
                tableRequest = new TableRequest(GET_ROWS_BY); // FIXME NullPointerException?
                tableRequest.setFindFields(new String[] { model.argument1Text.getValue() });
                tableRequest.setFindValues(new String[] { model.argument2Text.getValue() });
                break;
            case "Read all Rows in Table":
                tableRequest = new TableRequest(GET_ALL_ROWS);
                break;
            case "Update an Existing Row By Field & Value":
                tableRequest = new TableRequest(UPDATE_ROW_BY);
                tableRequest.setFindFields(new String[] { model.argument1Text.getValue() });
                tableRequest.setFindValues(new String[] { model.argument2Text.getValue() });
                tableRequest.setUpdateFields(new String[] { model.argument3Text.getValue() });
                tableRequest.setUpdateValues(new String[] { model.argument4Text.getValue() });
                break;
            case "Delete an Existing Row By Field & Value":
                tableRequest = new TableRequest(DELETE_ROW_BY);
                tableRequest.setFindFields(new String[] { model.argument1Text.getValue() });
                tableRequest.setFindValues(new String[] { model.argument2Text.getValue() });
                break;
        }

        try {
            Client client = new Client();

            client.startConnection("127.0.0.1", 4444);

            //System.out.println("Request: " + tableRequest);
            TableResponse tableResponse = client.sendRequest(tableRequest);
            //System.out.println("Result: " + tableResponse.toString());

            client.stopConnection();

            // TODO display this better somehow
            switch (tableResponse.getResponseType()) {
                case CREATED_ROW:
                    model.resultText.set("Row Created");
                    break;

                case READ_ROW:
                    //model.resultText.set(String.format("Read Row(s) [%s]: %s%n", Arrays.toString(tableResponse.getHeader()), Arrays.deepToString(tableResponse.getFoundRows())));
                    if (tableResponse.getFoundRows() != null && tableResponse.getFoundRows().length > 0) {
                        String resultText = String.format("Read Row(s) [%s]: %n", Arrays.toString(tableResponse.getHeader()));
                        for (String[] foundRow : tableResponse.getFoundRows()) {
                            resultText += Arrays.toString(foundRow) + "\n";
                        }
                        model.resultText.set(resultText);
                    } else {
                        model.resultText.set(tableResponse.toString());
                    }
                    break;

                case UPDATED_ROW:
                    model.resultText.set("Row Updated");
                    break;

                case DELETED_ROW:
                    model.resultText.set("Deleted Row");
                    break;

                case EXCEPTION:
                    model.resultText.set(String.format("Exception: %s%n", tableResponse.getException().getMessage()));
                    break;

                default:
                    throw new Exception("Unknown response type: " + tableResponse.getResponseType());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // https://dev.java/learn/javafx/properties/
    public void requestTypeListener(Observable observable) {
        //System.out.println(model.requestType.getValue());
        // change field labels to match request type
        switch (model.requestType.getValue()) {
            default:
            case "Create New Row":
                // 2 args
                model.argument1LabelVisible.set(true);
                model.argument1LabelText.set("Name: ");
                model.argument1TextFieldVisible.set(true);

                model.argument2LabelVisible.set(true);
                model.argument2LabelText.set("Phone: ");
                model.argument2TextFieldVisible.set(true);

                model.argument3LabelVisible.set(false);
                model.argument3TextFieldVisible.set(false);

                model.argument4LabelVisible.set(false);
                model.argument4TextFieldVisible.set(false);
                break;

            case "Read Row By Field & Value":
            case "Read Multiple Rows By Field & Value":
            case "Delete an Existing Row By Field & Value":
                // 2 args
                model.argument1LabelVisible.set(true);
                model.argument1LabelText.set("Field: ");
                model.argument1TextFieldVisible.set(true);

                model.argument2LabelVisible.set(true);
                model.argument2LabelText.set("Value: ");
                model.argument2TextFieldVisible.set(true);

                model.argument3LabelVisible.set(false);
                model.argument3TextFieldVisible.set(false);

                model.argument4LabelVisible.set(false);
                model.argument4TextFieldVisible.set(false);
                break;

            case "Read Row By Fields & Values (AND)":
            case "Read Row By Fields & Values (OR)":
                // 4 args
                model.argument1LabelVisible.set(true);
                model.argument1LabelText.set("1st Field: ");
                model.argument1TextFieldVisible.set(true);

                model.argument2LabelVisible.set(true);
                model.argument2LabelText.set("1st Value: ");
                model.argument2TextFieldVisible.set(true);

                model.argument3LabelVisible.set(true);
                model.argument3LabelText.set("2nd Field: ");
                model.argument3TextFieldVisible.set(true);

                model.argument4LabelVisible.set(true);
                model.argument4LabelText.set("2nd Value: ");
                model.argument4TextFieldVisible.set(true);
                break;

            case "Read all Rows in Table":
                // 0 args
                model.argument1LabelVisible.set(false);
                model.argument1TextFieldVisible.set(false);

                model.argument2LabelVisible.set(false);
                model.argument2TextFieldVisible.set(false);

                model.argument3LabelVisible.set(false);
                model.argument3TextFieldVisible.set(false);

                model.argument4LabelVisible.set(false);
                model.argument4TextFieldVisible.set(false);
                break;

            case "Update an Existing Row By Field & Value":
                // 4 args
                model.argument1LabelVisible.set(true);
                model.argument1LabelText.set("Existing Field: ");
                model.argument1TextFieldVisible.set(true);

                model.argument2LabelVisible.set(true);
                model.argument2LabelText.set("Existing Value: ");
                model.argument2TextFieldVisible.set(true);

                model.argument3LabelVisible.set(true);
                model.argument3LabelText.set("New Field: ");
                model.argument3TextFieldVisible.set(true);

                model.argument4LabelVisible.set(true);
                model.argument4LabelText.set("New Value: ");
                model.argument4TextFieldVisible.set(true);
                break;
        }
    }
}
