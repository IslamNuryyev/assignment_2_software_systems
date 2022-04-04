// for my windows command prompt:
// set PATH_TO_FX=PATH TO javafx-sdk-17.0.2\lib
// javac --module-path %PATH_TO_FX% --add-modules javafx.controls BarChartAir.java
// java --module-path %PATH_TO_FX% --add-modules javafx.controls BarChartAir.java

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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class BarChartAir extends Application {
     //Part1
     @Override
     public void start(Stage stage) {
       String file = "airline_safety.csv";
       String line;
       ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

       //Reading from CSV
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
//            StreamResult consoleResult = new StreamResult(System.out);
//            transformer.transform(source, consoleResult);
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


        System.out.println("summary = " + summary);



        // Chris's Code: (End of Part 2)
        try {
            DocumentBuilderFactory dbFactorySum =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilderSum = dbFactorySum.newDocumentBuilder();
            Document docSum = dBuilderSum.newDocument();

            // root element
            Element rootElementSum = docSum.createElement("Summary");
            docSum.appendChild(rootElementSum);

            for (int ii = 1; ii<8; ii++) {
                Element stat = docSum.createElement("Stat");
                rootElementSum.appendChild(stat);
//                Attr attr = docSum.createAttribute();
//                attr.setValue(summary.get(57).get(ii));
//                stat.setAttributeNode(attr);

                Element name = docSum.createElement("Name");
                name.appendChild(docSum.createTextNode(lines.get(0).get(ii)));
                stat.appendChild(name);

                Element minVal = docSum.createElement("Min");
                minVal.appendChild(docSum.createTextNode(lines.get(57).get(ii)));
                stat.appendChild(minVal);

                Element maxVal = docSum.createElement("Max");
                maxVal.appendChild(docSum.createTextNode(lines.get(58).get(ii)));
                stat.appendChild(maxVal);

                Element avgVal = docSum.createElement("Avg");
                avgVal.appendChild(docSum.createTextNode(lines.get(59).get(ii)));
                stat.appendChild(avgVal);

            }


            
            //  Writing to XML file.
            TransformerFactory transformerFactorySum = TransformerFactory.newInstance();
            Transformer transformerSum = transformerFactorySum.newTransformer();
            DOMSource sourceSum = new DOMSource(docSum);
            StreamResult resultSum = new StreamResult(new File("airline_summary_statistic.xml"));
            transformerSum.transform(sourceSum, resultSum);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(summary.get(58).get(0));

        // Part 3

         stage.setTitle("Bar Chart");
         final CategoryAxis xAxis = new CategoryAxis();
         final NumberAxis yAxis = new NumberAxis();
         final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);

         bc.setTitle("Fatal Incidents");
         xAxis.setLabel("Airline");
         yAxis.setLabel("Number of Incidents");

         XYChart.Series series1 = new XYChart.Series();
         series1.setName("1985-1999");

         System.out.println("TEST");

         for (int ii = 1; ii<lines.size()-3; ii++) {
             series1.getData().add(new XYChart.Data(lines.get(ii).get(0),Integer.valueOf(lines.get(ii).get(3))));
         }

         XYChart.Series series2 = new XYChart.Series();
         series2.setName("2000-2014");

         for (int jj = 1; jj<lines.size()-3; jj++) {
             series2.getData().add(new XYChart.Data(lines.get(jj).get(0),Integer.valueOf(lines.get(jj).get(6))));
         }




         Scene scene  = new Scene(bc,1200,600);
         bc.getData().addAll(series1, series2);
         stage.setScene(scene);
         stage.show();

    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

//public class BarChartAir extends Application {
//    // final static Strings
//
//    @Override
//    public void start(Stage stage) {
//        stage.setTitle("Bar Chart");
//        final CategoryAxis xAxis = new CategoryAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
//
//        bc.setTitle("Country Summary");
//        xAxis.setLabel("Country");
//        yAxis.setLabel("Value");
//
//
//
//
//        Scene scene  = new Scene(bc,800,600);
////        bc.getData().addAll(series1, series2, series3);
//        stage.setScene(scene);
//        stage.show();
//    }
//}

