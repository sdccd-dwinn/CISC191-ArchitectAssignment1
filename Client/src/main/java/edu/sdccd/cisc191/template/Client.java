package edu.sdccd.cisc191.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import static edu.sdccd.cisc191.template.TableRequest.RequestType.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
        Client client = new Client();
        try {
            // Create a Scanner object to read from the keyboard.
            Scanner keyboard = new Scanner(System.in);

            while (true) {
                System.out.println("Main Menu:");
                System.out.println("\t1) Create New Row");
                System.out.println("\t2) Read Row By Field & Value");
                System.out.println("\t3) Read Row By Fields & Values (AND)");
                System.out.println("\t4) Read Row By Fields & Values (OR)");
                System.out.println("\t5) Read Multiple Rows By Field & Value");
                System.out.println("\t6) Read all Rows in Table");
                System.out.println("\t7) Update an Existing Row By Field & Value");
                System.out.println("\t8) Delete an Existing Row By Field & Value");
                System.out.println("\t0) Exit");
                System.out.print("Enter Your Choice: ");

                TableRequest tableRequest;
                String tmp0;
                String tmp1;
                String tmp2;
                String tmp3;

                // FIXME doesn't handle spaces in input properly

                switch (keyboard.next()) {
                    case "1":
                        System.out.print("Name: ");
                        tmp0 = keyboard.next();
                        System.out.print("Phone: ");
                        tmp1 = keyboard.next();

                        tableRequest = new TableRequest(INSERT_ROW);
                        tableRequest.setUpdateFields(new String[] { "name", "phone" });
                        tableRequest.setUpdateValues(new String[] { tmp0, tmp1 });
                        processRequest(client, tableRequest);
                        break;

                    case "2":
                        System.out.print("Field: ");
                        tmp0 = keyboard.next();
                        System.out.print("Value: ");
                        tmp1 = keyboard.next();

                        tableRequest = new TableRequest(GET_ROW_BY);
                        tableRequest.setFindFields(new String[] { tmp0 });
                        tableRequest.setFindValues(new String[] { tmp1 });
                        processRequest(client, tableRequest);
                        break;

                    case "3":
                        System.out.print("1st Field: ");
                        tmp0 = keyboard.next();
                        System.out.print("1st Value: ");
                        tmp1 = keyboard.next();
                        System.out.print("2nd Field: ");
                        tmp2 = keyboard.next();
                        System.out.print("2nd Value: ");
                        tmp3 = keyboard.next();
                        // TODO accept an arbitrary number of fields in a loop?

                        tableRequest = new TableRequest(GET_ROW_BY_AND);
                        tableRequest.setFindFields(new String[] { tmp0, tmp2 });
                        tableRequest.setFindValues(new String[] { tmp1, tmp3 });
                        processRequest(client, tableRequest);
                        break;

                    case "4":
                        System.out.print("1st Field: ");
                        tmp0 = keyboard.next();
                        System.out.print("1st Value: ");
                        tmp1 = keyboard.next();
                        System.out.print("2nd Field: ");
                        tmp2 = keyboard.next();
                        System.out.print("2nd Value: ");
                        tmp3 = keyboard.next();
                        // TODO accept an arbitrary number of fields in a loop?

                        tableRequest = new TableRequest(GET_ROW_BY_OR);
                        tableRequest.setFindFields(new String[] { tmp0, tmp2 });
                        tableRequest.setFindValues(new String[] { tmp1, tmp3 });
                        processRequest(client, tableRequest);
                        break;

                    case "5":
                        System.out.print("Field: ");
                        tmp0 = keyboard.next();
                        System.out.print("Value: ");
                        tmp1 = keyboard.next();

                        tableRequest = new TableRequest(GET_ROWS_BY);
                        tableRequest.setFindFields(new String[] { tmp0 });
                        tableRequest.setFindValues(new String[] { tmp1 });
                        processRequest(client, tableRequest);
                        break;

                    case "6": // read all
                        tableRequest = new TableRequest(GET_ALL_ROWS);
                        processRequest(client, tableRequest);
                        break;

                    case "7":
                        System.out.print("Existing Field: ");
                        tmp0 = keyboard.next();
                        System.out.print("Existing Value: ");
                        tmp1 = keyboard.next();
                        System.out.print("New Field: ");
                        tmp2 = keyboard.next();
                        System.out.print("New Value: ");
                        tmp3 = keyboard.next();

                        tableRequest = new TableRequest(UPDATE_ROW_BY);
                        tableRequest.setFindFields(new String[] { tmp0 });
                        tableRequest.setFindValues(new String[] { tmp1 });
                        tableRequest.setUpdateFields(new String[] { tmp2 });
                        tableRequest.setUpdateValues(new String[] { tmp3 });
                        processRequest(client, tableRequest);
                        break;

                    case "8":
                        System.out.print("Field: ");
                        tmp0 = keyboard.next();
                        System.out.print("Value: ");
                        tmp1 = keyboard.next();

                        tableRequest = new TableRequest(DELETE_ROW_BY);
                        tableRequest.setFindFields(new String[] { tmp0 });
                        tableRequest.setFindValues(new String[] { tmp1 });
                        processRequest(client, tableRequest);
                        break;

                    case "0":
                        return;

                    default:
                        System.out.println("???");
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void processRequest(Client client, TableRequest tableRequest) throws Exception {
        //System.out.println("Request: " + tableRequest);
        client.startConnection("127.0.0.1", 4444);

        TableResponse tableResponse = client.sendRequest(tableRequest);
        //System.out.println("Result: " + tableResponse.toString());
        client.stopConnection();

        // TODO display this better somehow
        switch (tableResponse.getResponseType()) {
            case CREATED_ROW:
                System.out.println("Row Created");
                break;

            case READ_ROW:
                System.out.printf("Read Row(s) [%s]: %s%n", Arrays.toString(tableResponse.getHeader()), Arrays.deepToString(tableResponse.getFoundRows()));
                break;

            case UPDATED_ROW:
                System.out.println("Row Updated");
                break;

            case DELETED_ROW:
                System.out.println("Deleted Row");
                break;

            case EXCEPTION:
                System.out.printf("Exception: %s%n", tableResponse.getException().getMessage());
                break;

            default:
                throw new Exception("Unknown response type: " + tableResponse.getResponseType());
        }
    }

    public TableResponse sendRequest(TableRequest tableRequest) throws Exception {
        out.println(TableRequest.toJSON(tableRequest));
        return TableResponse.fromJSON(in.readLine());
    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
