/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import model.Consulta;
import model.Medico;

/**
 *
 * @author gusta
 */
public class ConsultasMesController {
    private Medico medico;
    
    /*Construtor padrão*/
    public ConsultasMesController(){
        
    }
    
    //Construtor parametrizado
    public ConsultasMesController(Medico medico){
        this.medico = medico;
    }
    
    //Método que realiza o controle da contagem de consultas realizadas no mes especificado
    public void controla(){
        //Obtém o total de consultas no mês através do método verificaConsultasMês
        int TotalConsultas = verificaConsultasMes();
        
        //Cria um formatador de data no formato mm/aaaa e transforma a data atual em String
        SimpleDateFormat f = new SimpleDateFormat("MM/yyyy");
        String dataAtual = f.format(Date.valueOf(LocalDate.now()));
        
        //Exibe uma mensagem informando o total de consultas no mês
        JOptionPane.showMessageDialog(null, "No mês "+dataAtual+" foram realizadas "+TotalConsultas+" consultas", "Total Consultas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public int verificaConsultasMes(){
        //Faz a contagem do total de consultas no mês atual. Não leva em conta as consultas do mês que ainda não ocorrem, faz a contagem de todas as consultas do mês
        
        //É utilizado um formatador que leva em conta o mês e ano da consulta
        SimpleDateFormat f = new SimpleDateFormat("MM/yyyy");
        //O mês atual é obtido de forma automática pelo sistema, que captura a data atual
        //Transforma a data atual em String, levando em consideração o mês e ano
        String dataAtual = f.format(Date.valueOf(LocalDate.now()));
        //Seta o contador de consultas para 0
        int contador =  0;
        //Obtém a lista com todas as consultas
        List<Consulta> consultas = medico.listaConsultas();
        //Para cada consulta da lista, é feita uma verificação da data da consulta com a data atual. A data da consulta é transformada em String
        //levando em consideração apenas o mês e o ano
        //Caso a String da data atual seja igual a String da data da consulta, significa que ela ocorreu/ocorrerá no mês atual, logo o contador é incrementado
        for(Consulta c: consultas){
            String dataConsulta = f.format(c.getDataConsulta());
            if(dataAtual.equals(dataConsulta))
                contador++;
        }
        return contador++;
    }
}
