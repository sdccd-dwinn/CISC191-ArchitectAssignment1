package edu.sdccd.cisc191.template;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

// https://stackoverflow.com/a/36873768
public class MainView {
    private MainController controller;
    private MainModel model;
    GridPane view;

    final Label requestTypeLabel = new Label("Request Type: ");
    final ComboBox requestTypeComboBox = new ComboBox();

    final Label argument1Label = new Label();
    final TextField argument1TextField = new TextField();

    final Label argument2Label = new Label();
    final TextField argument2TextField = new TextField();

    final Label argument3Label = new Label();
    final TextField argument3TextField = new TextField();

    final Label argument4Label = new Label();
    final TextField argument4TextField = new TextField();

    final Label resultLabel = new Label("Result: ");
    final TextArea resultTextArea = new TextArea();

    final Button submitButton = new Button ("Submit");

    public MainView(MainController controller, MainModel model) {
        this.controller = controller;
        this.model = model;

        // https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm
        requestTypeComboBox.getItems().addAll(
            "Create New Row",
            "Read Row By Field & Value",
            "Read Row By Fields & Values (AND)",
            "Read Row By Fields & Values (OR)",
            "Read Multiple Rows By Field & Value",
            "Read all Rows in Table",
            "Update an Existing Row By Field & Value",
            "Delete an Existing Row By Field & Value"
        );
        requestTypeComboBox.setValue("Create New Row");

        // https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm
        model.requestType.bind(requestTypeComboBox.valueProperty());
        requestTypeComboBox.valueProperty().addListener(controller::requestTypeListener);
        resultTextArea.textProperty().bind(model.resultText);

        argument1Label.visibleProperty().bind(model.argument1LabelVisible);
        argument1Label.textProperty().bind(model.argument1LabelText);
        argument1TextField.visibleProperty().bind(model.argument1TextFieldVisible);
        model.argument1Text.bind(argument1TextField.textProperty());
        argument1TextField.setText("Jenny");

        argument2Label.visibleProperty().bind(model.argument2LabelVisible);
        argument2Label.textProperty().bind(model.argument2LabelText);
        argument2TextField.visibleProperty().bind(model.argument2TextFieldVisible);
        model.argument2Text.bind(argument2TextField.textProperty());
        argument2TextField.setText("867-5309");

        argument3Label.visibleProperty().bind(model.argument3LabelVisible);
        argument3Label.textProperty().bind(model.argument3LabelText);
        argument3TextField.visibleProperty().bind(model.argument3TextFieldVisible);
        model.argument3Text.bind(argument3TextField.textProperty());

        argument4Label.visibleProperty().bind(model.argument4LabelVisible);
        argument4Label.textProperty().bind(model.argument4LabelText);
        argument4TextField.visibleProperty().bind(model.argument4TextFieldVisible);
        model.argument4Text.bind(argument4TextField.textProperty());

        // https://reintech.io/blog/javafx-building-modern-responsive-java-applications-tutorial
        submitButton.setOnAction(controller::submitButtonEventHandler);

        view = new GridPane();
        view.setVgap(4);
        view.setHgap(10);
        view.setPadding(new Insets(5, 5, 5, 5));
        view.add(requestTypeLabel, 0, 0);
        view.add(requestTypeComboBox, 1, 0);
        view.add(argument1Label, 0, 1);
        view.add(argument1TextField, 1, 1, 3, 1);
        view.add(argument2Label, 0, 2);
        view.add(argument2TextField, 1, 2, 3, 1);
        view.add(argument3Label, 0, 3);
        view.add(argument3TextField, 1, 3, 3, 1);
        view.add(argument4Label, 0, 4);
        view.add(argument4TextField, 1, 4, 3, 1);
        view.add(resultLabel, 0, 5);
        view.add(resultTextArea, 1, 5, 4, 1);
        view.add(submitButton, 0, 6);
    }

    public Parent asParent() {
        return view;
    }
}
