package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextArea codeArea;

    public TableView getTableView() {
        return tableView;
    }

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tableColumn;

    public String code;
    ObservableList<Row> getList()
    {
        ObservableList<Row> rows= FXCollections.observableArrayList();

        return rows;
    }

    @FXML
    void assemble(ActionEvent event)
    {
        System.out.println("be");
        tableView.getItems().clear();
        Assembler.refresh();
        code=codeArea.getText();
        Assembler.setCode(code);
        Assembler.assemble();
        fillTable();
    }

    void fillTable()
    {
        Assembler.output.add("\t\tSYMBOL TABLE COMPONENTS");

        for (int i=0;i<Assembler.symT.size();i++)
            Assembler.output.add(Assembler.symT.get(i));
        for (int i=0;i<Assembler.output.size();i++)
        {
            tableView.getItems().add(new Row(Assembler.output.get(i)));
            tableColumn.setCellFactory(column -> {
                return new TableCell<Row, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(empty ? "" : getItem().toString());
                        setGraphic(null);

                        TableRow<Row> currentRow = getTableRow();

                        if (!isEmpty()) {

                            if(item.contains("***")) {
                                currentRow.setStyle("-fx-background-color: #dc4b21");
                            }else if(item.contains(".")) {
                                currentRow.setStyle("-fx-background-color: gold " );
                            }else if(item.contains("=") || item.contains("SYMBOL"))
                                currentRow.setStyle("-fx-background-color: lightgreen");



                        }
                    }
                };
            });
        }}


    @FXML
    void save(ActionEvent event)
    {
        String code = codeArea.getText();
        File file= new File("code.txt");
        if (!file.exists()) {
            System.err.println(file.getName() + " not found. Full path: " + file.getAbsolutePath());
            return ;
        }
        BufferedWriter writer = null;

        try {

            writer=new BufferedWriter(new FileWriter(file));
            writer.write(code,0,code.length());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void load(ActionEvent event)
    {

        int words=0;
        File file = new File("code.txt");
        if (!file.exists()) {
            System.err.println(file.getName() + " not found. Full path: " + file.getAbsolutePath());
            return ;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line = null;
            String code="";
            while ((line=reader.readLine())!= null)
            {

                code=code+line+"\n";

            }
            codeArea.setText(code);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableColumn.setCellValueFactory(
                new PropertyValueFactory<Row, SimpleStringProperty>("line")
        );
    }
}
