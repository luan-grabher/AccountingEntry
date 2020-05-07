package testes;

import java.util.List;
import lctocontabil.Entity.ComandosSqlUnico;
import lctocontabil.Entity.LctoContabil;
import lctocontabil.Model.LctoContabil_Model;

public class teste {
    private static final String pathBancoSql = "sci.cfg";

    public static void main(String[] args) {
        testeModelComPesquisa();
    }
    
    private static void testeModelComChave(){
        Long chave = (long) 20979;
        
        LctoContabil_Model modelo = new LctoContabil_Model(new ComandosSqlUnico(), pathBancoSql);
        modelo.adicionarLancamento(657, chave);
        
        LctoContabil l = modelo.getLctos().get(0);
        l.setConciliadoDeb(true);
        l.setConciliadoCred(true);
        
        modelo.fazerUpdateNoBanco();
        
    }

    private static void testeModelComPesquisa() {
        Integer codEmpresa = 657;
        String dataInicio = "2019-01-01";
        String dataFinal = "2020-01-01";
        Integer contaCTB = 148;
        String filtroLctos = "";

        
        LctoContabil_Model modelo = new LctoContabil_Model(new ComandosSqlUnico(), pathBancoSql);

        modelo.definirLancamentos(filtroLctos, codEmpresa, dataInicio, dataFinal, contaCTB);
        //modelo.fazerUpdateNoBanco();
        List<LctoContabil> l = modelo.getLctos();
        
        System.out.println("sss");
    }

}
