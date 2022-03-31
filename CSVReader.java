import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;


public class CSVReader {
     //Part1
    public static void main(String[] args) {
       String file = "airline_safety.csv";
       String line;
       ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while((line = br.readLine()) != null){
                String[] values = line.split(",");
                lines.add(new ArrayList<>(Arrays.asList(values)));
            }
        } catch (Exception e){
            System.out.println(e);
        }

        lines.get(0).add(8, "total_incidents_85_14");

        for (int i = 1; i < lines.size(); i++) {
            //2nd + 5th column
            int incident_85 = Integer.parseInt(lines.get(i).get(2));
            int incident_14 = Integer.parseInt(lines.get(i).get(5));
            int total = incident_85 + incident_14;
            
            lines.get(i).add(8, String.valueOf(total));
        }
        System.out.println("lines = " + lines);  
        
     
        try {
            DocumentBuilderFactory dbFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            // root element
            Element rootElement = doc.createElement("airlines");
            doc.appendChild(rootElement);

            for (int i = 1; i < lines.size(); i++) {
                Element airline = doc.createElement("airline");
                // airline.appendChild(doc.createTextNode(lines.get(i).get(0)));
                rootElement.appendChild(airline);  
                Attr attr = doc.createAttribute("company");
                attr.setValue(lines.get(i).get(0));
                airline.setAttributeNode(attr);

                Element seat = doc.createElement("avail_seat_km_per_week");
                seat.appendChild(doc.createTextNode(lines.get(i).get(1)));
                airline.appendChild(seat);  
                
                Element incidents_85_99 = doc.createElement("incidents_85_99");
                incidents_85_99.appendChild(doc.createTextNode(lines.get(i).get(2)));
                airline.appendChild(incidents_85_99);  

                Element fatal_accidents_85_99 = doc.createElement("fatal_accidents_85_99");
                fatal_accidents_85_99.appendChild(doc.createTextNode(lines.get(i).get(3)));
                airline.appendChild(fatal_accidents_85_99);  

                Element fatalities_85_99 = doc.createElement("fatalities_85_99");
                fatalities_85_99.appendChild(doc.createTextNode(lines.get(i).get(4)));
                airline.appendChild(fatalities_85_99); 

                Element incidents_00_14 = doc.createElement("incidents_00_14");
                incidents_00_14.appendChild(doc.createTextNode(lines.get(i).get(5)));
                airline.appendChild(incidents_00_14); 

                Element fatal_accidents_00_14 = doc.createElement("fatal_accidents_00_14");
                fatal_accidents_00_14.appendChild(doc.createTextNode(lines.get(i).get(6)));
                airline.appendChild(fatal_accidents_00_14); 

                Element fatalities_00_14 = doc.createElement("fatalities_00_14");
                fatalities_00_14.appendChild(doc.createTextNode(lines.get(i).get(7)));
                airline.appendChild(fatalities_00_14); 

                Element total_incidents_85_14 = doc.createElement("total_incidents_85_14");
                total_incidents_85_14.appendChild(doc.createTextNode(lines.get(i).get(8)));
                airline.appendChild(total_incidents_85_14); 
            }
   
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("converted_airline_safety.xml"));
            transformer.transform(source, result);
            
            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }




        //Part2
       ArrayList<ArrayList<String>> summary = new ArrayList<ArrayList<String>>();
       summary = lines;
        summary.add(new ArrayList<>(Arrays.asList("minimum")));
        summary.add(new ArrayList<>(Arrays.asList("maximum")));
        summary.add(new ArrayList<>(Arrays.asList("average")));

        for (int i = 1; i < 8; i++) {
            long max = 0;
            long min = Long.parseLong((summary.get(1).get(i)));
            long sum = 0;
            for (int j = 1; j < summary.size() - 3; j++) {
                if (Long.parseLong(summary.get(j).get(i)) > max) {
                    max = Long.parseLong(summary.get(j).get(i));
                }

                if (Long.parseLong(summary.get(j).get(i)) < min) {
                    min = Long.parseLong(summary.get(j).get(i));
                }

                sum += Long.parseLong(summary.get(j).get(i));
            }
            long average = sum / (summary.size() - 3);

            summary.get(57).add(String.valueOf(min));
            summary.get(58).add(String.valueOf(max));
            summary.get(59).add(String.valueOf(average));
        }

       System.out.println("\n\n");


       System.out.println("\n\n");
       System.out.println("summary = " + summary);

        

    }
}

