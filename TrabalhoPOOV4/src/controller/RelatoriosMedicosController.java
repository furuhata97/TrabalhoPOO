/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import model.Medico;
import model.Paciente;
import view.AtestadoView;
import view.DeclaracaoView;
import view.MedicoView;
import view.ReceitaView;
import view.RelatoriosMedicosView;

/**
 *
 * @author gusta
 */
public class RelatoriosMedicosController {
    private Medico medico;
    private Paciente paciente;
    private RelatoriosMedicosView view;
    
    //Construtor padrão
    public RelatoriosMedicosController(){
        
    }
    
    //Construtor parametrizado
    public RelatoriosMedicosController(RelatoriosMedicosView view, Medico medico, Paciente paciente){
        this.medico = medico;
        this.view = view;
        this.paciente = paciente;
        this.view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view de Médico é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new MedicoController(new Medico(), new MedicoView()).controla();
        });
        
        //Caso o botão de Receita seja clicado, é verificado o paciente que foi passado como parâmetro. Caso o paciente seja = null, uma mensagem é exibida informando
        //que nenhum paciente foi selecionado. Caso o paciente seja diferente de null, é aberta uma janela onde o usuário deverá digitar o conteúdo da receita e clicar 
        //em OK. Caso clique em Ok, uma janela será aberta onde deverá ser digitado a assinatura do médico. Caso o usuário clique em Ok, a view da receita é aberta
        //e exibida ao usuário, simulando a geração automática da receita
        view.getBtnReceita().addActionListener((ActionEvent actionEvent) -> {
            if(paciente == null){
                JOptionPane.showMessageDialog(null, "Nenhum Paciente foi selecionado. Impossível gerar Receita", "Erro", JOptionPane.WARNING_MESSAGE);
            }
            else{
                String receita = JOptionPane.showInputDialog(null, "Digite a receita", "Receita", JOptionPane.PLAIN_MESSAGE);
                if(receita != null){
                   String medicoAssinatura = JOptionPane.showInputDialog(null, "Assinatura do médico:", "Receita", JOptionPane.PLAIN_MESSAGE);
                   if(medicoAssinatura != null)
                      new ReceitaController(paciente, receita, medicoAssinatura, new ReceitaView(view)).controla(); 
                } 
            }
        });
        
        //Caso o botão de Atestado seja clicado, é verificado o paciente que foi passado como parâmetro. Caso o paciente seja = null, uma mensagem é exibida informando
        //que nenhum paciente foi selecionado. Caso o paciente seja diferente de null, é aberta uma janela onde o usuário deverá digitar o número de dias de repouso e clicar 
        //em OK. Caso clique em Ok, uma janela será aberta onde deverá ser digitado a assinatura do médico. Caso o usuário clique em Ok, a view do atestado é aberta
        //e exibida ao usuário, simulando a geração automática de atestado
        view.getBtnAtestado().addActionListener((ActionEvent actionEvent) -> {
            if(paciente == null){
                JOptionPane.showMessageDialog(null, "Nenhum Paciente foi selecionado. Impossível gerar Atestado", "Erro", JOptionPane.WARNING_MESSAGE);
            }
            else{
                String dias = JOptionPane.showInputDialog(null, "Digite a quantidade de dias em repouso", "Atestado", JOptionPane.PLAIN_MESSAGE);
                if(dias != null){
                   String medicoAssinatura = JOptionPane.showInputDialog(null, "Assinatura do médico:", "Atestado", JOptionPane.PLAIN_MESSAGE);
                   if(medicoAssinatura != null)
                      new AtestadoController(paciente, dias, medicoAssinatura, new AtestadoView(view)).controla(); 
                } 
            }
        });
        
        //Caso o botão de Acompanhamento seja clicado, é verificado o paciente que foi passado como parâmetro. Caso o paciente seja = null, uma mensagem é exibida informando
        //que nenhum paciente foi selecionado. Caso o paciente seja diferente de null, é aberta uma janela onde o usuário deverá digitar o nome do acompanhante e clicar 
        //em OK. Caso clique em Ok, uma janela será aberta onde deverá ser digitado a assinatura do médico. Caso o usuário clique em Ok, a view de acompanhamento é aberta
        //e exibida ao usuário, simulando a geração automática de uma declaração de companhamento
        view.getBtnAcompanhamento().addActionListener((ActionEvent actionEvent) -> {
            if(paciente == null){
                JOptionPane.showMessageDialog(null, "Nenhum Paciente foi selecionado. Impossível gerar Atestado", "Erro", JOptionPane.WARNING_MESSAGE);
            }
            else{
                String acompanhante = JOptionPane.showInputDialog(null, "Digite o nome do acompanhante", "Atestado", JOptionPane.PLAIN_MESSAGE);
                if(acompanhante != null){
                   String medicoAssinatura = JOptionPane.showInputDialog(null, "Assinatura do médico:", "Atestado", JOptionPane.PLAIN_MESSAGE);
                   if(medicoAssinatura != null)
                      new DeclaracaoController(paciente, acompanhante, medicoAssinatura, new DeclaracaoView(view)).controla(); 
                } 
            }
        });
        
        //Caso o botão de Relatórios de Consultas do mês seja clicado, a view de relatórios será aberta
        view.getBtnPacientes().addActionListener((ActionEvent actionEvent) -> {
            new ConsultasMesController(medico).controla();
        });
    }
    
}
