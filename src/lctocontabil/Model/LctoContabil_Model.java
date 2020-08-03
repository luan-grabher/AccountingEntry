package lctocontabil.Model;

import Auxiliar.Valor;
import SimpleView.Loading;
import java.util.ArrayList;
import java.util.List;
import lctocontabil.Entity.ComandosSql;
import lctocontabil.Entity.LctoContabil;
import sql.Database;

public class LctoContabil_Model {

    private List<LctoContabil> lctos = new ArrayList<>();

    private Integer codEmpresa;
    private String dataInicio;
    private String dataFinal;
    private Integer contaCTB;

    private final ComandosSql comandosSql;
    private final String pathBancoSql;

    private final Database banco;
    private ArrayList<String[]> rs;

    public LctoContabil_Model(ComandosSql comandosSql, String pathBancoSql) {
        this.comandosSql = comandosSql;
        this.pathBancoSql = pathBancoSql;

        banco = new Database(this.pathBancoSql);
    }

    public void definirLancamentos(String filtroSql, Integer codEmpresa, String dataInicio, String dataFinal, Integer contaCTB) {
        try {
            //Reinicia Lctos
            limparLancamentos();

            //Define variaveis atuais
            this.codEmpresa = codEmpresa;
            this.dataInicio = dataInicio;
            this.dataFinal = dataFinal;
            this.contaCTB = contaCTB;

            //Buscar no banco os lançamentos
            String where = comandosSql.whereLctos(codEmpresa, dataInicio, dataFinal, contaCTB, filtroSql);
            String sqlSelectLctos = comandosSql.selectLctos(where);

            rs = banco.select(sqlSelectLctos);

            for (int i = 0; i < rs.size(); i++) {
                String[] r = rs.get(i);
                adicionarLancamento(r);
            }
        } catch (Exception e) {
            System.out.println("[LctoContabil_Model] Erro ao buscar lançamentos no banco: " + e);
            e.printStackTrace();
        }
    }

    public void definirLancamentos(List<LctoContabil> lctosCopiados) {
        limparLancamentos();
        lctos = lctosCopiados.subList(0, lctosCopiados.size());
    }

    public void fazerUpdateNoBanco() {
        try {
            Loading barra = new Loading("Atualizando lançamentos no banco", 0, lctos.size());

            //Percorre todos lctos
            for (int i = 0; i < lctos.size(); i++) {
                barra.updateBar(i);
                LctoContabil lcto = lctos.get(i);
                //--Faz comando de update utilizando dados
                String sql = comandosSql.updateLcto(lcto);
                banco.query(sql);
            }

            barra.dispose();
        } catch (Exception e) {
            System.out.println("[LctoContabil_Model] Erro ao fazer update no banco: " + e);
            e.printStackTrace();
        }
    }

    public void conciliarLctosDaLista(Integer codigoEmpresa, String chavesSeparadasPorVirgula, String conciliacaoTRUEFALSE) {
        try {
            if (!chavesSeparadasPorVirgula.equals("")) {
                //Pega lista de sqls separados pela expressão In
                List<String> sqls = comandosSql.conciliarChaves(codigoEmpresa, chavesSeparadasPorVirgula, conciliacaoTRUEFALSE);
                
                //Atualiza minimo e maximo
                //Começa barra carregamento
                Loading barra = new Loading("Conciliando lançamentos no banco", 0, sqls.size());
                
                //PErcorre todos comandos SQL para executa-los
                for (int i = 0; i < sqls.size(); i++) {
                    String sql = sqls.get(i);
                    barra.updateBar(i);
                    banco.query(sql);
                }
                
                //Termina com a barra de carregamento
                barra.dispose();
            }
        } catch (Exception e) {
            System.out.println("[LctoContabil_Model] Erro ao fazer conciliar lctos no banco: " + e);
            e.printStackTrace();
        }
    }

    public void limparLancamentos() {
        this.codEmpresa = 0;
        this.dataInicio = "";
        this.dataFinal = "";
        this.contaCTB = 0;
        lctos = new ArrayList<>();
    }

    public void adicionarLancamento(Integer codEmpresa, Long chaveLcto) {
        try {

            String where = comandosSql.whereChave(codEmpresa, chaveLcto);
            String selectSQL = comandosSql.selectLctos(where);

            rs = banco.select(selectSQL);
            for (String[] r : rs) {
                adicionarLancamento(r);
            }

        } catch (Exception e) {
        }
    }

    private void adicionarLancamento(String[] resultSetVector) {
        if (resultSetVector.length == 15) {
            //Adicionar
            lctos.add(
                    new LctoContabil(
                            new Valor(resultSetVector[0]).getInteger(),
                            new Valor(resultSetVector[1]).getInteger(),
                            new Valor(resultSetVector[2]).getInteger(),
                            new Valor(resultSetVector[3]),
                            new Valor(resultSetVector[4]).getInteger(),
                            new Valor(resultSetVector[5]).getInteger(),
                            new Valor(resultSetVector[6]).getInteger(),
                            new Valor(resultSetVector[7]).getInteger(),
                            new Valor(resultSetVector[8]).getInteger(),
                            new Valor(resultSetVector[9]).getInteger(),
                            new Valor(resultSetVector[10]),
                            new Valor(resultSetVector[11]),
                            new Valor(resultSetVector[12]),
                            new Valor(resultSetVector[13]).getBoolean(),
                            new Valor(resultSetVector[14]).getBoolean()
                    )
            );
        }
    }

    public void zerarConciliacaoLctosAtuais() {

        //Prepara string com chaves
        StringBuilder chavesLctos = new StringBuilder();
        String prefixo = "";
        
        //Percorre lctos
        Loading barra = new Loading("Zerando Conciliação", 0, lctos.size());
        for (int i = 0; i < lctos.size(); i++) {
            barra.updateBar(i);
            LctoContabil lcto = lctos.get(i);
            lcto.setConciliadoDeb(false);
            lcto.setConciliadoCred(false);
            
            //monta lista de chaves
            chavesLctos.append(prefixo);
            prefixo = ",";
            chavesLctos.append(lcto.getChave().toString());
        }
        barra.dispose();
        
        //Faz update no banco
        conciliarLctosDaLista(codEmpresa, chavesLctos.toString(), "FALSE");
    }

    public List<LctoContabil> getLctos() {
        return lctos;
    }

}
