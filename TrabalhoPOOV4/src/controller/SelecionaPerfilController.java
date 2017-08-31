/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import model.GerenciadorMensagem;
import model.Medico;
import view.SelecionaPerfilView;
import model.Secretaria;
import view.GerenciadorMensagemView;
import view.MedicoView;
import view.SecretariaView;

/**
 *
 * @author gusta
 */
public class SelecionaPerfilController {
    
    private SelecionaPerfilView view;
    
        /* Contrutor padrão */
    public SelecionaPerfilController() {
    }
    
       /*Contrutor parametrizado*/
    public SelecionaPerfilController(SelecionaPerfilView view) {
        this.view = view;
        this.view.setVisible(true);
    }
    
        //Método que realiza o controle das ações realizadas na view
        public void controla() {
        //Se acionado o botão "Gerenciador de Mensagens": instancia o controller, o gerenciador e e a view correspondentes e ativa o controller.
        view.getBtnMsg().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new GerenciadorMensagemController(new GerenciadorMensagem(), new GerenciadorMensagemView()).controla();
        });
        //Se acionado o botão "Médico": instancia o controller, o gerenciador e e a view correspondentes e ativa o controller.
        view.getBtnMedico().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new MedicoController(new Medico(), new MedicoView()).controla();
        });
        //Se acionado o botão "Secretária": instancia o controller, o gerenciador e e a view correspondentes e ativa o controller.
        view.getBtnSecretaria().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
    }
}
