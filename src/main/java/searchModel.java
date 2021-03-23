import java.util.ArrayList;

public class searchModel {
    String searchQuery;

    ArrayList<String> resultNames = new ArrayList<String>();
    ArrayList<Double> resultPrices = new ArrayList<Double>();
    ArrayList<Integer> sitesToSearch = new ArrayList<Integer>();

    double avrgPrice;
    ArrayList<Double> avrgPricePlatform = new ArrayList<Double>();

    public void calcAvrgPrice(){
        double all = 0;
        for(double d : resultPrices){
            all += d;
        }
        avrgPrice = all / resultPrices.size();
    }



    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public ArrayList<String> getResultNames() {
        return resultNames;
    }

    public void setResultNames(ArrayList<String> resultNames) {
        this.resultNames = resultNames;
    }

    public ArrayList<Double> getResultPrices() {
        return resultPrices;
    }

    public void setResultPrices(ArrayList<Double> resultPrices) {
        this.resultPrices = resultPrices;
    }

    public ArrayList<Integer> getSitesToSearch() {
        return sitesToSearch;
    }

    public void setSitesToSearch(ArrayList<Integer> sitesToSearch) {
        this.sitesToSearch = sitesToSearch;
    }

    public double getAvrgPrice() {
        return avrgPrice;
    }

    public void setAvrgPrice(double avrgPrice) {
        this.avrgPrice = avrgPrice;
    }

    public ArrayList<Double> getAvrgPricePlatform() {
        return avrgPricePlatform;
    }

    public void setAvrgPricePlatform(ArrayList<Double> avrgPricePlatform) {
        this.avrgPricePlatform = avrgPricePlatform;
    }
}
