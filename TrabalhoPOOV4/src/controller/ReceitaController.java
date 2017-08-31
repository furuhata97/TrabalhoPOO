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
import view.ReceitaView;

/**
 *
 * @author gusta
 */
public class ReceitaController {
    
    private Paciente paciente;
    private String receita;
    private String medico;
    private ReceitaView view;
    
    //Construtor padrão
    public ReceitaController(){
        
    }
    
    //Construtor parametrizado
    public ReceitaController(Paciente paciente, String receita, String medico, ReceitaView view){
        this.medico = medico;
        this.paciente = paciente;
        this.receita = receita;
        this.view = view;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Assim que a view é aberta, as informações da receita são setadas e exibidas ao usuário
        view.getNome().setText(paciente.getNome());
        view.getData().setText(Date.valueOf(LocalDate.now()).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getReceita().setText(receita);
        view.getAssinatura().setText(medico);
        view.getReceita().setEditable(false);
        
        //Quando o botão OK é clicado, a view é fechada
        view.getBtnOk().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
        });
        this.view.setVisible(true);
    }
}