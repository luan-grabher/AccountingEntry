package lctocontabil.Model;

import Dates.Dates;
import SimpleView.Loading;
import fileManager.FileManager;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lctocontabil.Entity.ContabilityEntry;
import org.ini4j.Ini;
import sql.Database;
import sql.SQL;

/**
 * Antes de utilizar as funcoes, defina o ini e o db, o db já está definido como
 * o banco estático
 */
public class ContabilityEntries_Model {

    private static Database db = Database.getDatabase();
    public static Ini ini = null;

    /*SQLs*/
    private static String sql_conciliateKeys = FileManager.getText(FileManager.getFile("sql\\conciliateKeys.sql"));
    private static String sql_updateContabilityEntriesOnDatabase = FileManager.getText(FileManager.getFile("sql\\updateContabilityEntriesOnDatabase.sql"));
    private static String sql_selectAccountBalance = FileManager.getText(FileManager.getFile("sql\\selectAccountBalance.sql"));

    /**
     * AS FUNÇÕES SÃO ESTÁTICAS, NÃO INSTANCIAR
     */
    public ContabilityEntries_Model() {
    }

    /**
     * Reseta o banco de dados
     *
     * @param newdb O banco de dados que será definido
     */
    public static void setDb(Database newdb) {
        db = newdb;
    }

    /**
     * Resetar o banco e ini file
     *
     * @param newini A classe ini do arquivo Ini
     */
    public static void setIni(Ini newini) {
        ini = newini;
    }

    /**
     * Cria um mapa de classes de lançamentos contábeis a partir de um código
     * SQL da tabela de lançamentos. As colunas devem ser "*", o código busca
     * pelo próprio nome das colunas.
     *
     * @param sql Script SQL com código select
     * @param swaps mapa de trocas com variaveis e valores
     * @return Mapa de lançamentos com a chave como a key dos maps
     */
    public static Map<Integer, ContabilityEntry> getEntries(String sql, Map<String, String> swaps) {
        Map<Integer, ContabilityEntry> entries = new TreeMap<>();

        ResultSet rs = db.getResultSet(sql, swaps);

        try {
            //Percorre linhas
            while (rs.next()) {
                ContabilityEntry entry = new ContabilityEntry();

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
     * Realiza update no banco usando o arquivo sql
     * "sql\\updateContabilityEntriesOnDatabase.sql" com os lançamentos do mapa
     * informado
     *
     * @param entries Mapa de lançamentos
     * @return Retorna true se ocorrer tudo Ok
     */
    public static boolean updateContabilityEntriesOnDatabase(Map<Integer, ContabilityEntry> entries) {
        try {

            Loading loading = new Loading("Atualizando lançamentos do banco", 0, entries.size());
            int count = 0;

            for (Map.Entry<Integer, ContabilityEntry> entry : entries.entrySet()) {
                //Atualiza barra de carregamento
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

                //Se ocorrer algum erro ao fazer o update, para o código
                if (!db.query(sql_updateContabilityEntriesOnDatabase, swaps)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("[ContabilityEntries_Model] Erro ao fazer update no banco: " + e);
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Define os atributos 'conciliadoCredito' e 'conciliadoDebito' do mapa
     * informado com o valor booleano informado
     *
     * @param entries Mapa dos lançamentos
     * @param concilited Valor booleano com TRUE para 'conciliado' e FALSE para
     * 'não conciliado'
     */
    public static void defineConciliatedsTo(Map<Integer, ContabilityEntry> entries, Boolean concilited) {
        for (Map.Entry<Integer, ContabilityEntry> entry : entries.entrySet()) {
            ContabilityEntry e = entry.getValue();

            e.setConciliedCredit(concilited);
            e.setConciliedDebit(concilited);
        }
    }

    /**
     * Concilia no banco de dados a lista de chaves da empresa informada
     * conforme o valor booleano informado
     *
     * @param enterprise Código da empresa no único
     * @param keys Lista de chaves que serão conciliadas em formato de texto
     * separadas por ,
     * @param concilited TRUE para conciliar e FALSE para desconciliar
     * @return Retorna TRUE se conseguir executar a query e FALSE se não
     * conseguir
     */
    public static boolean conciliateKeysOnDatabase(Integer enterprise, String keys, Boolean concilited) {
        String whereIn = SQL.divideIn(keys, "BDCHAVE");

        Map<String, String> swaps = new HashMap<>();
        swaps.put("keysInList", whereIn);
        swaps.put("enterprise", enterprise.toString());
        swaps.put("concilited", concilited.toString());

        /*Se conseguir fazer a query retorna TRUE*/
        return db.query(sql_conciliateKeys, swaps);
    }

    /**
     * Pega Saldo de CREDITO e DEBITO de uma conta ou participante em uma data
     * diretamente do banco.
     *     
     * @param enterprise código da empresa
     * @param account Conta contábil que será retornada o saldo
     * @param participant Participante que será retornado o saldo, se for nulo
     * irá retornar o saldo da conta contabil informada com todos os
     * participantes
     * @param dateCalendar o saldo será somado até essa data
     * @return Retorna um mapa com duas chaves "credit" e "debit" com os valores
     * BigDecimal com os valores de crédito e débito respectivamente
     */
    public static Map<String, BigDecimal> selectAccountBalance(Integer enterprise, Integer account, Integer participant, Calendar dateCalendar) {

        /*Cria trocas*/
        Map<String, String> swaps = new TreeMap<>();
        swaps.put("enterprise", enterprise.toString());
        swaps.put("date", Dates.getCalendarInThisStringFormat(dateCalendar, "YYYY-MM-dd"));
        swaps.put("account", account.toString());
        swaps.put("participant", participant == null ? "NULL" : participant.toString());
        
        List<Map<String,Object>> results = db.getMap(sql_selectAccountBalance,swaps);

        try {
            //Se não retornar nada, dá erro
            if(results.isEmpty())
                throw new Error("");
            
            BigDecimal credit = new BigDecimal(results.get(0).get("SALDO").toString());
            BigDecimal debit = new BigDecimal(results.get(1).get("SALDO").toString());

            Map<String, BigDecimal> balances = new TreeMap<>();
            balances.put("credit", credit);
            balances.put("debit", debit);

            return balances;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Ocorreu um erro ao buscar o saldo da conta '" + account + "' da empresa '" + enterprise + "' até a data '" + Dates.getCalendarInThisStringFormat(dateCalendar, "YYYY-MM-dd") + "'");
        }
    }
}
