/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Consulta;
import model.GerenciadorMensagem;
import model.Paciente;
import view.NotificacaoEnviadaView;

/**
 *
 * @author gusta
 */
public class ConsultasDoProximoDiaController {
    private GerenciadorMensagem gerenciador;
    private NotificacaoEnviadaView view;
    private boolean isTelefone;
    private Date dataConsulta;
    private List<Consulta> consultas;
    
    /* Contrutor padrão */
    public ConsultasDoProximoDiaController() {
    }

    /*Contrutor parametrizado*/
    public ConsultasDoProximoDiaController(NotificacaoEnviadaView view, GerenciadorMensagem gerenciador, boolean isTelefone, Date dataConsulta) {
        this.gerenciador = gerenciador;
        this.view = view;
        this.isTelefone = isTelefone;
        this.dataConsulta = dataConsulta;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla() {
        //Esta classe é responsável pelo gerenciador de mensagens, que simula o envio de notificações ao usuário. Possui a mesma função que a classe ConsultaPorDataController
        
        //Obtém a data atual
        Calendar amanha = Calendar.getInstance();
        
        //Seta a data atual
        amanha.setTime(dataConsulta);
        
        //Adiciona 1 dia adata atual, o que representa o dia de amanhã 
        amanha.add(Calendar.DAY_OF_MONTH, 1);
        
        //verifica se as notificações de consultas do próximo dia levará em conta o número de telefone ou email. Caso isTelefone seja true, significa que será enviado
        //notificações de consultas por telefone. Caso isTelefone seja falso, será enviado notificações de consultas por email.
        if (isTelefone) {
            //Obtém as consultas dos pacientes que possuem telefone e que possuam data marcada para amanhã
            consultas = gerenciador.getConsultasDatacomTelefone(amanha);
        } else {
            //Obtém as consultas dos pacientes que possuem email e que possuam data marcada para amanhã
            consultas = gerenciador.getConsultasDatacomEmail(amanha);
        }
        
        //Seta a tabela da view de acordo com as consultas obtidas
        view.getTabela().setModel(geraTabelaConsulta());
        
        //Caso o botão OK seja clicado, a view é fechada
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
                    view.dispose();
                });
        view.setVisible(true);
    }
    
    /* Método responsável por criar uma tabela personalizada das consultas do dia seguinte. 
       Verifica se o usuário solicitou filtro por email ou sms */
    private DefaultTableModel geraTabelaConsulta() {
        String[] header = new String[3];
        //altera o nome que será exibido na tabela, caso a solicitação leve em conta telefone, a coluna que representa o número de telefone terá seu nome
        //alterado para telefone. Caso a solicitação leve em conta email, a coluna terá seu nome alterado para email
        if(this.isTelefone){
            header[0] = "Paciente";
            header[1] = "Telefone";
            header[2] = "Data consulta";
        }   
        else{
            header[0] = "Paciente";
            header[1] = "Email";
            header[2] = "Data consulta";
        }  
        Object[][] consultasObject = new Object[consultas.size()][3];
        int i = 0;
        int j;
        //É feita a adicão das consultas na tabela
        for (Consulta c : consultas) {
            j = 0;
            consultasObject[i][j] = c.getPaciente();
            j++;
            if (isTelefone) {
                consultasObject[i][j] = c.getPaciente().getTelefone();
                j++;
            } else {
                consultasObject[i][j] = c.getPaciente().getEmail();
                j++;
            }
            consultasObject[i][j] = c.getDataConsultaString();
            Paciente p = (Paciente) consultasObject[i][0];
            String nome = p.getNome();
            consultasObject[i][0] = nome;
            i++;
        }
        return new javax.swing.table.DefaultTableModel(
                consultasObject, header);
    }
}
