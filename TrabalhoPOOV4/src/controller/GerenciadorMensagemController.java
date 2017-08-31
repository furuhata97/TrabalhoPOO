/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;
import model.GerenciadorMensagem;
import view.GerenciadorMensagemView;
import view.NotificacaoEnviadaView;
import view.SelecionaPerfilView;

/**
 *
 * @author gusta
 */
public class GerenciadorMensagemController {
    private GerenciadorMensagem gerenciador;
    private GerenciadorMensagemView view;
    
    /* Contrutor padrão */
    public GerenciadorMensagemController() {
    }
    
    /*Contrutor parametrizado*/
    public GerenciadorMensagemController(GerenciadorMensagem gerenciador, GerenciadorMensagemView view) {
        this.gerenciador = gerenciador;
        this.view = view;
        this.view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Se o botão Voltar for pressionado, a view é fechada e retorna para a view de seleção de perfil
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SelecionaPerfilController(new SelecionaPerfilView()).controla();
        });
        
        
        //Se o botão de Email for apertado, é obtida a data de hoje e esta data é passada para a chamada do método que exibirá as consultas do dia seguinte para pacientes
        //que possuem email
        view.getBtnEmail().addActionListener((ActionEvent actionEvent) -> {
            Date dataConsulta = Date.valueOf(LocalDate.now());
            new ConsultasDoProximoDiaController(new NotificacaoEnviadaView(view), gerenciador, false, dataConsulta).controla();
        });
        
        //Se o botão de Telefone for apertado, é obtida a data de hoje e esta data é passada para a chamada do método que exibirá as consultas do dia seguinte para pacientes
        //que possuem telefone
        view.getBtnSMS().addActionListener((ActionEvent actionEvent) -> {
            Date dataConsulta = Date.valueOf(LocalDate.now());
            new ConsultasDoProximoDiaController(new NotificacaoEnviadaView(view), gerenciador, true, dataConsulta).controla();
        });
    }
}
