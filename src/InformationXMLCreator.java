import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InformationXMLCreator {
    private FileOutputStream FOS;
    private HashMap<String, String> information[] = new HashMap[4];

    public void fillInformationXML() {
        writeToFile("<information>\n");

        // Clients
        writeGroupToXML(information[0], "clients", "client", "id_client");

        // Agencies
        writeGroupToXML(information[1], "agencies", "agency", "id_agency");

        // Rooms
        writeGroupToXML(information[2], "rooms", "room", "id_type");

        // Hotels
        writeGroupToXML(information[3], "hotels", "hotel", "id_hotel");

        writeToFile("</information>\n");        }

    private void writeGroupToXML(HashMap<String, String> group, String groupName, String elementName, String idAttribute) {
        if (group == null || group.isEmpty()) {
            return;
        }

        writeToFile("<" + groupName + ">\n"); // Opening Group Tag

        // Entry Information Loop
        for (Map.Entry<String, String> entry : group.entrySet()) {
            writeToFile("<" + elementName + " " + idAttribute + "=\"" + entry.getKey() + "\">" + entry.getValue() + "</" + elementName + ">\n");
        }
        writeToFile("</" + groupName + ">\n"); // Closing Group Tag
    }

    public void createInformationArray (ArrayList<Booking> bookingsCollection) {
        HashMap clients = new HashMap<String, String>();
        HashMap agencies = new HashMap<String, String>();
        HashMap rooms = new HashMap<String, String>();
        HashMap hotels = new HashMap<String, String>();

        for (Booking b : bookingsCollection) {

            // Clients
            if (!clients.containsKey(b.getClientID())) {
                clients.put(b.getClientID(), b.getClientName());
            }

            // Agencys
            if (!agencies.containsKey(b.getAgencyID())) {
                agencies.put(b.getAgencyID(), b.getAgencyName());
            }

            // Rooms
            if (!rooms.containsKey(b.getRoomID())) {
                rooms.put(b.getRoomID(), b.getRoomType());
            }

            // Hotels
            if (!hotels.containsKey(b.getHotelID())) {
                hotels.put(b.getHotelID(), b.getHotelName());
            }

        }

        // Adding filled HashMaps to Information array
        this.information[0] = clients;
        this.information[1] = agencies;
        this.information[2] = rooms;
        this.information[3] = hotels;
    }

    public void createXMLFile() {
        Scanner input = new Scanner(System.in);
        File InformationXML;
        boolean fileExists = true;
        InformationXML = new File("src/information.xml");

        try {
            FOS = new FileOutputStream(InformationXML);
            fillInformationXML();
            FOS.close();

            System.out.println("The information XML has been created successfully.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeToFile(String text) {
        try {
            FOS.write(text.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
