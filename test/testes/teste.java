package testes;

import java.util.HashMap;
import java.util.Map;

/*
import fileManager.FileManager;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import lctocontabil.Model.ContabilityEntries_Model;
import sql.Database;*/

public class teste {

    public static void main(String[] args) {
        
        Map<Integer, Integer> map = new HashMap<>();
        
        map.put(1,1);
        map.put(null, null);
        
        System.out.println("ss");
        
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
