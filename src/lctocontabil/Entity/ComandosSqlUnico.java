package lctocontabil.Entity;

import Auxiliar.Valor;

public class ComandosSqlUnico extends ComandosSql {

    public ComandosSqlUnico() {
        t_Lancamentos.add(new Valor("VSUC_EMPRESAS_TLAN","nome"));
        t_Lancamentos.add(new Valor("BDCODEMP","col_empresa"));
        t_Lancamentos.add(new Valor("''","col_filial"));
        t_Lancamentos.add(new Valor("BDCHAVE","col_chave"));
        t_Lancamentos.add(new Valor("BDDATA","col_data"));
        t_Lancamentos.add(new Valor("BDCODPLAPADRAO","col_planoPadrao"));
        t_Lancamentos.add(new Valor("BDDEBITO","col_debito"));
        t_Lancamentos.add(new Valor("BDCREDITO","col_credito"));
        t_Lancamentos.add(new Valor("BDCODTERCEIROD","col_participanteDebito"));
        t_Lancamentos.add(new Valor("BDCODTERCEIROC","col_participanteCredito"));
        t_Lancamentos.add(new Valor("BDCODHIST","col_historicoPadrao"));
        t_Lancamentos.add(new Valor("BDCOMPL","col_complementoHistorico"));
        t_Lancamentos.add(new Valor("BDDCTO","col_documento"));
        t_Lancamentos.add(new Valor("BDVALOR","col_valor"));
        t_Lancamentos.add(new Valor("BDCONCD","col_conciliadoDebito"));
        t_Lancamentos.add(new Valor("BDCONCC","col_conciliadoCredito"));
    }

}
