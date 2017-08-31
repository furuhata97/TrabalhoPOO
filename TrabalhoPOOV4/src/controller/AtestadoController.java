/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import model.Paciente;
import view.AtestadoView;

/**
 *
 * @author gusta
 */
public class AtestadoController {
    private Paciente paciente;
    private String dias;
    private String medico;
    private AtestadoView view;
    
    //Construtor padrão
    public AtestadoController(){
        
    }
    
    //Construtor parâmetrizado
    public AtestadoController(Paciente paciente, String dias, String medico, AtestadoView view){
        this.medico = medico;
        this.paciente = paciente;
        this.dias = dias;
        this.view = view;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Assim que a view é aberta, as informações do atestado são setadas e exibidas ao usuário
        view.getNome().setText(paciente.getNome());
        view.getData().setText(Date.valueOf(LocalDate.now()).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getData2().setText(Date.valueOf(LocalDate.now()).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getRepouso().setText(dias);
        view.getAssinatura().setText(medico);
        //Quando o botão OK é clicado, a view é fechada
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
        });
        //Faz a view ficar visível ao usuário
        this.view.setVisible(true);
    }
}
