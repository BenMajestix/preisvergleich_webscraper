import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class main {
    static Scanner scanner = new Scanner(System.in);
    static String[] searchPlatforms = {"Ebay", "Saturn"};

    @SuppressWarnings("MethodNameSameAsClassName")
    public static void main(String[] args){
        menu();

        System.out.println("Was suchst du?");
        scanner.nextLine(); //consumes the \n from menu()
        String input = scanner.nextLine();

        String userSearchString = processInput(input);

        System.out.println("Suche started nach: " + userSearchString);


        try {
            searchEbay(userSearchString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void menu(){
        System.out.println("Main Menu:");
        System.out.println("----------");
        System.out.println(" ");
        System.out.println("Bitte Wählen Sie aus, welche Seiten Sie durchsuchen wollen.");
        System.out.println("0 --> Alle Seiten");
        for(int i = 0; i < searchPlatforms.length; i++){
            System.out.println(i + " --> " + searchPlatforms[i]);
        }

        String choice;
        int intChoice = 0;

        boolean rightInput = false;
        while(!(rightInput)){
            choice = scanner.next();
            try{
                intChoice = Integer.parseInt(choice);
                if(intChoice <= searchPlatforms.length){
                    System.out.println("Option OK");
                    rightInput = true;
                }
                else{
                    System.out.println("Der Input muss eine der Optionen sein.");
                }
            }
            catch (NumberFormatException e){
                System.out.println("You have to Input a Number -- A real Menu Option");
            }
        }
    }

    public static String processInput(String input){
        String[] answers = input.split(" ");

        StringBuilder query = new StringBuilder();

        for(String s : answers){
            System.out.println(s);
            if (!(query.isEmpty())) {
                String st = "+" + s;
                query.append(st);
            }
            else{
                query.append(s);
            }
        }
        return query.toString();
    }

    public static void searchEbay(String userInput) throws IOException {

        System.out.println("Ergebnisse:");
        System.out.println("------------");
        System.out.println(" ");

        // + "&_sacat=0"
        String url = "https://www.ebay.de/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=" + userInput;
        Document document = (Document) Jsoup.connect(url).get();
        //System.out.println(document.toString());

        Elements itemNames = document.getElementsByClass("s-item__link");
        Elements itemPrice = document.getElementsByClass("s-item__price");

        ArrayList<Double> allPrices = new ArrayList<Double>();

        for(int i = 0; i < itemNames.size(); i++){
            System.out.println("Name: " + itemNames.get(i).text());
            System.out.println("Price: " + itemPrice.get(i).text());
            System.out.println(" ");

            try {
                allPrices.add(convPriEbay(itemPrice.get(i).text()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        double sum = 0d;
        for(double d : allPrices){
            sum += d;
        }

        System.out.println(" ");
        System.out.println("Avrg: " + sum / allPrices.size());
    }

    public static void searchSaturn(String userInput) throws IOException {
        String url = "https://www.saturn.de/de/search.html?query=" + userInput;
        Document document = (Document) Jsoup.connect(url).get();

        Elements names = document.select("[data-test=\"product-title\"]");
        Elements prices = document.select("[data-test=\"product-price\"]");
        for(int i = 0; i < names.size(); i++){
            System.out.println(names.get(i).text());
            System.out.println(prices.get(i).text());
        }
    }

    public static double convPriEbay(String pri) throws ParseException {
        //EUR 135,00
        pri = pri.substring(4);

        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        Number number = format.parse(pri);
        double d = number.doubleValue();

        System.out.println(d);
        return d;
    }
}
