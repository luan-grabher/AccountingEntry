package lctocontabil.Model;

import Dates.Dates;
import SimpleView.Loading;
import fileManager.FileManager;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import lctocontabil.Entity.ContabilityEntry;
import org.ini4j.Ini;
import sql.Database;

/**
 * Antes de utilizar as funcoes, defina o ini e o db, o db já está definido como
 * o banco estático
 */
public class ContabilityEntries_Model {

    private static Database db = Database.getDatabase();
    public static Ini ini = null;

    /**
     * O banco estatico e o INI já devem estar definidos de forma estática
     */
    public ContabilityEntries_Model() {
    }

    /**
     * Reseta o banco de dados
     *
     * @param db O banco de dados que será definido
     */
    public static void setDb(Database db) {
        ContabilityEntries_Model.db = db;
    }

    /**
     * Resetar o banco e ini file
     *
     * @param ini A classe ini do arquivo Ini
     */
    public static void setIni(Ini ini) {
        ContabilityEntries_Model.ini = ini;
    }

    /**
     * Pega os lançamentos desse SQL e dessas trocas
     *
     * @param sql Script SQL com código select
     * @param swaps mapa de trocas com variaveis e valores
     * @return Mapa de lançamentos com a chave como a key dos maps
     */
    public Map<Integer, ContabilityEntry> getEntries(String sql, Map<String, String> swaps) {
        Map<Integer, ContabilityEntry> entries = new TreeMap<>();

        ResultSet rs = db.getResultSet(sql, swaps);

        try {
            //Percorre linhas
            while (rs.next()) {
                ContabilityEntry entry = new ContabilityEntry();

                /*
                t_Lancamentos.add(new Valor("BDCONCD", "col_conciliadoDebito"));
                t_Lancamentos.add(new Valor("BDCONCC", "col_conciliadoCredito"));
                 */
                entry.setKey(rs.getInt("BDCHAVE"));
                entry.setEnterprise(rs.getInt("BDCODEMP"));

                //data
                Calendar date = Calendar.getInstance();
                date.setTime(rs.getDate("BDDATA"));
                entry.setDate(date);

                entry.setDefaultPlan(rs.getInt("BDCODPLAPADRAO"));
                entry.setAccountDebit(rs.getInt("BDDEBITO"));
                entry.setAccountCredit(rs.getInt("BDCREDITO"));
                entry.setParticipantDebit(rs.getInt("BDCODTERCEIROD"));
                entry.setParticipantCredit(rs.getInt("BDCODTERCEIROC"));
                entry.setHistoryCode(rs.getInt("BDCODHIST"));
                entry.setComplement(rs.getString("BDCOMPL"));
                entry.setDocument(rs.getString("BDDCTO"));
                entry.setValue(rs.getBigDecimal("BDVALOR"));
                entry.setConciliedCredit(rs.getBoolean("BDCONCC"));
                entry.setConciliedDebit(rs.getBoolean("BDCONCD"));

                entries.put(entry.getKey(), entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entries;
    }

    /**
     * Realiza update no banco dos lançamentos da lista
     *
     * @param entries Mapa de lançamentos
     * @return Retorna true se ocorrer tudo Ok
     */
    public boolean updateContabilityEntriesOnDatabase(Map<Integer, ContabilityEntry> entries) {
        try {
            String sqlUpdateContabilityEntriesOnDatabase = FileManager.getText(FileManager.getFile("sql\\updateContabilityEntriesOnDatabase.sql"));

            Loading loading = new Loading("Atualizando lançamentos do banco", 0, entries.size());
            int count = 0;

            for (Map.Entry<Integer, ContabilityEntry> entry : entries.entrySet()) {
                count++;

                loading.updateBar(count + " de " + entries.size(), count);

                Integer key = entry.getKey();
                ContabilityEntry e = entry.getValue();

                Map<String, String> swaps = new TreeMap<>();
                swaps.put("key", e.getKey().toString());
                swaps.put("enterprise", e.getEnterprise().toString());
                swaps.put("date", Dates.getCalendarInThisStringFormat(e.getDate(), "yyyy-mm-dd"));
                swaps.put("defaultPlan", e.getDefaultPlan().toString());
                swaps.put("accountDebit", e.getAccountDebit().toString());
                swaps.put("accountCredit", e.getAccountCredit().toString());
                swaps.put("participantDebit", e.getParticipantDebit().toString());
                swaps.put("participantCredit", e.getParticipantCredit().toString());
                swaps.put("historyCode", e.getHistoryCode().toString());
                swaps.put("complement", e.getComplement());
                swaps.put("document", e.getDocument());
                swaps.put("value", e.getValue().toPlainString());
                swaps.put("conciliedCredit", e.getConciliedCredit().toString());
                swaps.put("conciliedDebit", e.getConciliedDebit().toString());

                if (!db.query(sqlUpdateContabilityEntriesOnDatabase, swaps)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("[LctoContabil_Model] Erro ao fazer update no banco: " + e);
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Conciliar lançamentos da lista
     *
     * @param entries Mapa dos lançamentos
     * @param conciliated Se o débito e o crédito serão conciliados ou não.
     */
    public void defineConciliatedsTo(Map<Integer, ContabilityEntry> entries, Boolean conciliated) {
        for (Map.Entry<Integer, ContabilityEntry> entry : entries.entrySet()) {
            ContabilityEntry e = entry.getValue();

            e.setConciliedCredit(conciliated);
            e.setConciliedDebit(conciliated);
        }
    }
}
