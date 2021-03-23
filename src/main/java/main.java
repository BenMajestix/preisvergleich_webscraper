import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

public class main {


    static Scanner scanner = new Scanner(System.in);
    static String[] searchPlatforms = {"Ebay", "Saturn"};
    public static boolean loadingDone;
    static consoleHelperModel loader;
    static searchModel currSearch;

    @SuppressWarnings("MethodNameSameAsClassName")
    public static void main(String[] args){
        try {
            startNewSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        currSearch = new searchModel();
        menu();

        System.out.println("Was suchst du?");
        //scanner.nextLine(); //consumes the \n from menu()
        String input = scanner.nextLine();

        String userSearchString = processInputQuery(input);

        System.out.println("Suche started nach: " + userSearchString);



        try {
            loader = new consoleHelperModel("Console Animator");
            loader.start();
            searchSaturn(userSearchString);

        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

    public static void startNewSearch() throws IOException {
        currSearch = new searchModel();
        //currSearch.sitesToSearch

        menu();
        System.out.println("Was suchst du?");
        String input = scanner.nextLine();

        String userSearchString = processInputQuery(input);

        System.out.println("Suche started nach: " + userSearchString);

        loader = new consoleHelperModel("Console Animator");
        loader.start();

        for(int i : currSearch.sitesToSearch){
            switch (i) {
                case 1 -> searchEbay(userSearchString);
                case 2 -> searchSaturn(userSearchString);
            }
        }

        loadingDone = true;
        System.out.println("\r" + " ");

        for(String s : currSearch.resultNames){
            System.out.println(s);
        }
        currSearch.calcAvrgPrice();

        System.out.println("Durchschnitt: " + currSearch.avrgPrice);
    }

    public static void menu(){
        //Menu Print
        System.out.println("Main Menu:");
        System.out.println("----------");
        System.out.println(" ");
        System.out.println("Bitte Wählen Sie aus, welche Seiten Sie durchsuchen wollen.");
        System.out.println("Jede Seite muss mit einem Leerzeichen getrennt sein. Bsp: 1 3 7");
        System.out.println("0 --> Alle Seiten");
        for(int i = 0; i < searchPlatforms.length; i++){
            System.out.println(i+1 + " --> " + searchPlatforms[i]);
        }

        boolean allInputsCorrect = false;
        while(!(allInputsCorrect)){
            currSearch.sitesToSearch.clear();
            allInputsCorrect = true;

            String input = scanner.nextLine();
            String[] splitInput = input.split(" ");

            for(String s : splitInput){
                try {
                    int c = Integer.parseInt(s);
                    if(c <= searchPlatforms.length){
                        currSearch.sitesToSearch.add(c);
                    }
                    else {
                        allInputsCorrect = false;
                        System.out.println("Die Auswahl ist ungültig.");
                        break;
                    }
                }
                catch(NumberFormatException e){
                    allInputsCorrect = false;
                    System.out.println("Die Auswahl darf nur aus Zahlen, die Optionen, bestehen. ");
                    break;
                }
            }
        }

        for(int i : currSearch.sitesToSearch){
            if(i == 0){
                currSearch.sitesToSearch.clear();
                for(int s = 1; s <= searchPlatforms.length; s++){
                    currSearch.sitesToSearch.add(s);
                    System.out.println("All Sites: " + s);
                }
                System.out.println("Sites: All");
                break;
            }
            System.out.println("Sites: " + i);
        }
    }

    public static String processInputQuery(String input){
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
        // + "&_sacat=0"
        String url = "https://www.ebay.de/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=" + userInput;
        Document document = Jsoup.connect(url).get();
        //System.out.println(document.toString());

        Elements itemNames = document.getElementsByClass("s-item__link");
        Elements itemPrice = document.getElementsByClass("s-item__price");

        for(int i = 0; i < itemNames.size(); i++){
            currSearch.resultNames.add(itemNames.get(i).text());
            try {
                currSearch.resultPrices.add(formatEbayPrice(itemPrice.get(i).text()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void searchSaturn(String userInput) throws IOException {
        String url = "https://www.saturn.de/de/search.html?query=" + userInput;
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            new Exception("Invalid Saturn Search");
            return;
        }
        //System.out.println(document.toString());
        assert document != null;
        Elements names = document.select("[data-test=\"product-title\"]");
        //Elements prices = document.select("[data-test=\"product-price\"]");
        Elements prices = document.getElementsByClass("Typostyled__StyledInfoTypo-sc-1jga2g7-0 bdjSlC StrikeThrough__StyledStrikePriceTypo-sc-1uy074f-0 lcCuFl");

        for (int i = 0; i < prices.size(); i++) {
            currSearch.resultNames.add(names.get(i).text());
            currSearch.resultPrices.add(formatSaturnPrice(prices.get(i).text()));
        }
    }

    public static double formatEbayPrice(String pri) throws ParseException {
        //EUR 135,00
        pri = pri.substring(4);

        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        Number number = format.parse(pri);
        return number.doubleValue();
    }

    public static double formatSaturnPrice(String pri) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < pri.length(); i++){
            if(Character.isDigit(pri.charAt(i))){
                builder.append(pri.charAt(i));
            }
        }
        return Double.parseDouble(builder.toString());
    }
}
