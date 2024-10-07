package edu.sdccd.cisc191.template;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class MainModel {
    // https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm
    public SimpleStringProperty requestType = new SimpleStringProperty();

    public SimpleBooleanProperty argument1LabelVisible = new SimpleBooleanProperty(true);
    public SimpleStringProperty argument1LabelText = new SimpleStringProperty("Name: ");
    public SimpleBooleanProperty argument1TextFieldVisible = new SimpleBooleanProperty(true);
    public SimpleStringProperty argument1Text = new SimpleStringProperty();

    public SimpleBooleanProperty argument2LabelVisible = new SimpleBooleanProperty(true);
    public SimpleStringProperty argument2LabelText = new SimpleStringProperty("Phone: ");
    public SimpleBooleanProperty argument2TextFieldVisible = new SimpleBooleanProperty(true);
    public SimpleStringProperty argument2Text = new SimpleStringProperty();

    public SimpleBooleanProperty argument3LabelVisible = new SimpleBooleanProperty(false);
    public SimpleStringProperty argument3LabelText = new SimpleStringProperty();
    public SimpleBooleanProperty argument3TextFieldVisible = new SimpleBooleanProperty(false);
    public SimpleStringProperty argument3Text = new SimpleStringProperty();

    public SimpleBooleanProperty argument4LabelVisible = new SimpleBooleanProperty(false);
    public SimpleStringProperty argument4LabelText = new SimpleStringProperty();
    public SimpleBooleanProperty argument4TextFieldVisible = new SimpleBooleanProperty(false);
    public SimpleStringProperty argument4Text = new SimpleStringProperty();

    public SimpleStringProperty resultText = new SimpleStringProperty();

    public MainModel() {

    }
}
