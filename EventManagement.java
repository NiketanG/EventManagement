import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.*;

public class EventManagement {
    static int DEBUG = 0;
    // set this to 1, or pass 'DEBUG' as command line arguement to enable debug
    // messages
    // javac EventManagement.java
    // java EventManagement DEBUG

    static int event_ID;
    static int event_count = 0;
    // number of events that are specified in the file

    StringBuilder event_list = new StringBuilder();
    String[] lines;

    void create_event_file() {
        // Create Event i Registration file for saving the registration details.
        try {
            for (int i = 0; i < event_count; i++) {
                // Creating an object of a file
                File myObj = new File("Event" + (i + 1) + "_registration.dat");
                if (!(myObj.createNewFile())) {
                    if (DEBUG == 1) {
                        System.out.println("File : Event" + (i + 1) + "_registration.dat already exists.");
                    }
                }
            }
            if (DEBUG == 1) {
                System.out.println("/*All Event Registration files created/*");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating registration files for Event.");
            e.printStackTrace();
        }
    }

    void get_events() {
        // Retrieves the list of events and their information from the file
        try {
            // Creating an object of the file for reading the data
            File myObj = new File("Event_list.dat");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                event_count++;
                String event_name = myReader.nextLine();
                event_list.append(event_name);
                event_list.append("//");
            }
            myReader.close();
            event_count /= 2;
            create_event_file();
        } catch (FileNotFoundException e) {
            System.out.println("There was an error while fetching the Event List.");
            e.printStackTrace();
        }
    }

    void write_to_file(int event_ID, String name, String email, String phno, String UID) {
        try {
            FileWriter myWriter = new FileWriter("Event" + event_ID + "_registration.dat", true);
            // Writes this content into the specified file
            myWriter.write(name + System.getProperty("line.separator") + email + System.getProperty("line.separator")
                    + phno + System.getProperty("line.separator") + UID + System.getProperty("line.separator"));

            // Closing is necessary to retrieve the resources allocated
            myWriter.close();
            System.out.println("Registration Successful !");
        } catch (IOException e) {
            System.out.println("An error occurred while saving registration details.");
            e.printStackTrace();
        }
    }

    int select_Event() {
        Scanner S = new Scanner(System.in);
        int event_selected = 0;
        while (event_selected == 0) {
            System.out.println("Select the event in which you would like to participate in :");
            // Get event list from file
            lines = event_list.toString().split("//");
            for (int i = 0, line_count = 0; i < (event_count * 2); i++) {

                delayed_println("\t\t\t" + (++line_count) + "." + lines[i++]);
            }
            System.out.println("\n\n\t\t\tTo display ticket based\n\t\t\ton Ticket No. enter -1");
            event_selected = S.nextInt();
            if (event_selected == -1) {
                get_ticket();
                new Scanner(System.in).nextLine();
                clearConsole();
                event_selected = 0;
            }
        }
        clearConsole();
        return event_selected;
    }

    void get_ticket() {
        String TicketNo;
        System.out.println("Enter your ticket no : ");
        Scanner S = new Scanner(System.in);
        TicketNo = S.nextLine();

        String name = "", email = "0", phno = "0", readline = "0";
        int readline_count = 0;
        try {
            // Creating an object of the file for reading the data
            for (int i = 1; i < event_count; i++) {
                File myObj = new File("Event" + i + "_registration.dat");
                Scanner myReader = new Scanner(myObj);

                while (myReader.hasNextLine()) {
                    readline = myReader.nextLine();
                    readline_count++;
                    if (readline.equals(TicketNo)) {
                        int name_line = (readline_count - 3);
                        int email_line = (readline_count - 2);
                        int phno_line = readline_count - 1;

                        myReader.close();
                        myObj = new File("Event" + i + "_registration.dat");
                        myReader = new Scanner(myObj);
                        readline_count = 0;
                        while (myReader.hasNextLine()) {
                            readline = myReader.nextLine();
                            readline_count++;
                            if (readline_count == name_line) {
                                name = readline;
                            } else if (readline_count == phno_line) {
                                phno = readline;
                            } else if (readline_count == email_line) {
                                email = readline;
                            }
                        }
                        display_ticket(i, name, email, phno, TicketNo);
                    }
                }
                myReader.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("There was an error while fetching the Event List.");
            e.printStackTrace();
        }
    }

    void event_details(int event_selected) {
        clearConsole();
        String[] event_desc = lines[((event_selected * 2) - 1)].split("#n#");
        drawline();
        System.out.print("\n\n\n");
        typewriter("\t\t\t" + lines[((event_selected * 2) - 2)] + "\n");
        StringBuilder underline = new StringBuilder();
        for (int i = 0; i < lines[((event_selected * 2) - 2)].length(); i++) {
            underline.append("_");
        }
        typewriter("\t\t\t" + underline);
        System.out.println("\n");
        for (int i = 0; i < event_desc.length; i++)
            System.out.println(event_desc[i]);
        System.out.println("\nWould you like to register for " + lines[((event_selected * 2) - 2)] + " ? Y or N ");
        Scanner S = new Scanner(System.in);
        String choice = S.nextLine();
        if (choice.equals("Y")) {
            event_ID = event_selected;
            clearConsole();
            enterDetails();
        } else {
            clearConsole();
            intro();
        }
    }

    String create_Uid() {
        int tickcount = 0;
        tickcount += 1;
        StringBuffer str1 = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz");
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int index = (int) (str1.length() * Math.random());
            sb.append(str1.charAt(index));
        }
        sb.append(tickcount);
        return sb.toString();
    }

    void enterDetails() {
        Scanner S = new Scanner(System.in);
        drawline();
        System.out.print("\n\n\n");
        typewriter("\t\t\tEvent : " + lines[((event_ID * 2) - 2)] + "\n");
        StringBuilder eventnameheader = new StringBuilder("Event : ");
        eventnameheader.append(lines[((event_ID * 2) - 2)]);
        StringBuilder underline = new StringBuilder();
        for (int i = 0; i < eventnameheader.length(); i++) {
            underline.append("_");
        }
        typewriter("\t\t\t" + underline);
        System.out.println("\n");

        try {
            String name, email;
            String phno;

            System.out.println("Enter Name : ");
            name = S.nextLine();
            while (name.length() <= 0) {
                System.out.println("*Name cannot be blank");
                name = S.nextLine();
            }

            System.out.println("Enter Email Address : ");
            email = S.nextLine();
            while (email.length() <= 0) {
                System.out.println("*Email cannot be blank");
                email = S.nextLine();
            }

            System.out.println("Enter Phone No. : ");
            phno = S.nextLine();
            while (phno.length() != 10) {
                if (phno.length() == 0)
                    System.out.println("*Phone No. cannot be blank");
                System.out.println("*Phone No. must consist of 10 digits");
                phno = S.nextLine();
            }

            write_to_file(event_ID, name, email, phno, create_Uid());
            display_ticket(event_ID, name, email, phno, create_Uid());
            System.out.println();
            new Scanner(System.in).nextLine();
            intro();
        } catch (InputMismatchException e) {
            System.out.println("Error - Invalid data type entered. Please enter correct information in proper format.");
            enterDetails();
        }
    }

    void drawline() {
        int w = 60;
        for (int i = 0; i < w; i++) {
            System.out.print("_");
        }
    }

    void loading() {
        clearConsole();
        String anim = "|/-\\";
        System.out.println("\n\n\n");
        for (int x = 0; x < 10; x++) {
            String data = "\r\t\t\tL O A D I N G  " + anim.charAt(x % anim.length()) + " ";
            try {
                System.out.write(data.getBytes());
                Thread.sleep(100);
            } catch (InterruptedException e) {
            } catch (IOException e) {
            }
        }
    }

    void typewriter(String text) {
        String text_to_display = text;
        for (int i = 0; i <= text_to_display.length(); i++) {
            System.out.print("\r" + text_to_display.substring(0, i));
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
            }
        }
    }

    void delayed_println(String text) {
        System.out.println(text);
        try {
            Thread.sleep(650);
        } catch (InterruptedException e) {
        }
    }

    void intro() {
        clearConsole();
        drawline();
        System.out.print("\n\n\n");
        typewriter("\t\t\tW E L C O M E\n");
        typewriter("\t\t\t_____________");
        System.out.println();
        delayed_println("\n\t\tGovernment Polytechnic, Pune");
        delayed_println("\t\t\t   Events");
        event_ID = select_Event();
        event_details(event_ID);
        System.out.println("\n\n\n");
    }

    void display_ticket(int event_ID, String name, String email, String phno, String UID) {
        drawline();
        System.out.println();
        System.out.println("|     Ticket No. : " + UID);
        System.out.println("|");
        System.out.println("|");
        System.out.println("|     Event: " + lines[((event_ID * 2) - 2)]);
        System.out.println("|     Name : " + name);
        System.out.println("|     Email Address: " + email);
        System.out.println("|     Phone No: " + phno);
        System.out.println("|");
        System.out.println("|");
        drawline();
    }

    public final static void clearConsole() {
        // Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ex) {
        }
    }

    public static void main(String[] args) {
        try {
            if ((args[0].equals("DEBUG")) || (args[0].equals("debug"))) {
                DEBUG = 1;
                System.out.println("*/DEBUG MODE ON/*");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        EventManagement E1 = new EventManagement();
        E1.loading();
        E1.get_events();
        E1.intro();
        E1.enterDetails();
    }
}