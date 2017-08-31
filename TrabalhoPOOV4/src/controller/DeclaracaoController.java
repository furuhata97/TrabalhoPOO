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
import view.DeclaracaoView;

/**
 *
 * @author gusta
 */
public class DeclaracaoController {
    private Paciente paciente;
    private String acompanhante;
    private String medico;
    private DeclaracaoView view;
    
    //Construtor padrão
    public DeclaracaoController(){
        
    }
    
    //Construtor parametrizado
    public DeclaracaoController(Paciente paciente, String acompanhante, String medico, DeclaracaoView view){
        this.medico = medico;
        this.paciente = paciente;
        this.acompanhante = acompanhante;
        this.view = view;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Preenche a view com as informações passadas no construtor
        view.getPaciente().setText(paciente.getNome());
        view.getData().setText(Date.valueOf(LocalDate.now()).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getData2().setText(Date.valueOf(LocalDate.now()).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getAcompanhante().setText(acompanhante);
        view.getAssinatura().setText(medico);
        
        //Caso o botão Ok seja clicado, a view é fechada
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
        });
        this.view.setVisible(true);
    }
}
