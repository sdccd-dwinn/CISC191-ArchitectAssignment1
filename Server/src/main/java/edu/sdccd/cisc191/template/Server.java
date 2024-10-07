package edu.sdccd.cisc191.template;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.sdccd.cisc191.template.TableResponse.ResponseType.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static final String fileName = "PersistentTable.dat";
    private Table table;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.loadTable(fileName); // load persisted data
            // keep the server processing new connections indefinitely
            while (true) {
                server.start(4444);
                server.stop();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        log("Waiting for client connection...");

        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        log("Connection opened");

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            TableRequest request = TableRequest.fromJSON(inputLine);
            log("Request: " + request.toString());

            TableResponse response = new TableResponse();

            try {
                switch(request.getRequestType()) {
                    case INSERT_ROW:
                        table.insertRow(request.getUpdateFields(), request.getUpdateValues());
                        response.setResponseType(CREATED_ROW);
                        Table.saveToFile(fileName, table); // save updated table to disk
                        break;

                    case GET_ROW_BY:
                        response.setFoundRows(
                                new String[][] {
                                        table.getRowBy(request.getFindFields()[0], request.getFindValues()[0])
                                }
                        );
                        response.setHeader(table.getHeader());
                        response.setResponseType(READ_ROW);
                        break;

                    case GET_ROW_BY_AND:
                        response.setFoundRows(
                                new String[][] {
                                        table.getRowByAnd(request.getFindFields(), request.getFindValues())
                                }
                        );
                        response.setHeader(table.getHeader());
                        response.setResponseType(READ_ROW);
                        break;

                    case GET_ROW_BY_OR:
                        response.setFoundRows(
                                new String[][] {
                                        table.getRowByOr(request.getFindFields(), request.getFindValues())
                                }
                        );
                        response.setHeader(table.getHeader());
                        response.setResponseType(READ_ROW);
                        break;

                    case GET_ROWS_BY:
                        response.setFoundRows(
                            table.getRowsBy(request.getFindFields()[0], request.getFindValues()[0])
                        );
                        response.setHeader(table.getHeader());
                        response.setResponseType(READ_ROW);
                        break;

                    case GET_ALL_ROWS:
                        response.setFoundRows(table.getAllRows());
                        response.setHeader(table.getHeader());
                        response.setResponseType(READ_ROW);
                        break;

                    case UPDATE_ROW_BY:
                        response.setUpdated(table.updateRowBy(request.getFindFields()[0], request.getFindValues()[0], request.getUpdateFields(), request.getUpdateValues()));
                        response.setResponseType(UPDATED_ROW);
                        Table.saveToFile(fileName, table); // save updated table to disk
                        break;

                    case DELETE_ROW_BY:
                        response.setUpdated(table.deleteRowBy(request.getFindFields()[0], request.getFindValues()[0]));
                        response.setResponseType(DELETED_ROW);
                        Table.saveToFile(fileName, table); // save updated table to disk
                        break;

                    default:
                        throw new Exception("Unknown request type: " + request.getRequestType());
                }
            } catch (Exception e) {
                response.setResponseType(EXCEPTION);
                response.setException(e);
            }

            log("Response: " + response.toString());
            out.println(TableResponse.toJSON(response));
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        log("Connection closed");
    }

    /**
     * loads saved table from specified path, or creates a new one if not present
     * @throws Exception
     */
    public void loadTable(String path) throws Exception {
        // check if file with persisted data exists
        // https://stackoverflow.com/a/1816676
        File file = new File(path);
        if (file.exists() && !file.isDirectory() && file.canRead() && file.length() > 0) {
            // try to load it
            table = Table.loadFromFile(path); // TODO catch exceptions?
            log("Table loaded from file: " + path);
        } else {
            // otherwise make one with some sample data
            table = new Table(
                    new String[] { "name", "phone" },
                    new String[][] {
                            { "John Doe", "555-1234" },
                            { "Mr. Cardholder", "888-8888" },
                            { "Jenny", "867-5309"}
                    }
            );
            Table.saveToFile(path, table);
            log("Could not find file " + path + ", creating one with sample data");
        }
    }

    /**
     * wraps println() and prepends the current date and time
     * @param message
     */
    private void log(String message) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S Z").format(new Date());

        System.out.println(date + "\t" + message);
    }
}
