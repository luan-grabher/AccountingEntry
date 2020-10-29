package testes;

/*
import fileManager.FileManager;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import lctocontabil.Model.ContabilityEntries_Model;
import sql.Database;*/

public class teste {

    public static void main(String[] args) {
        
        String str = "assd 556 asfasf 12746279 JHKJ 24114 55";
        
        // [][2345][2323]
        // [12123][232]
        // [21321][21312][234]

        String[] numbers = str.split("[^0-9]+");
        String number = "";
        for (String number1 : numbers) {
            if(!number1.equals("")){
                number = number1;
                break;
            }
        }
        
        System.out.println(number);
        
        /*
        Database.setStaticObject(new Database(FileManager.getFile("sci.cfg")));
        
        ContabilityEntries_Model model = new ContabilityEntries_Model();
        
        Calendar cal = Calendar.getInstance();
        cal.set(2020, 0, 1);       
        
        Map<String, BigDecimal> balances = model.selectAccountBalance(1, 278, null,cal);
        
        System.out.println("Credito: " + balances.get("credit"));
        System.out.println("Debito: " + balances.get("debit"));*/
    }
}
