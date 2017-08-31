/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import model.Paciente;
import model.Secretaria;
import view.BuscaPacienteView;
import util.GeradorDeTabelas;
import javax.swing.JOptionPane;
import view.DetalhesPacienteSecretariaView;
import view.SecretariaView;

/**
 *
 * @author gusta
 */
public class BuscaPacienteController {
    private Secretaria secretaria;
    private BuscaPacienteView view;
    private Paciente paciente;
    
    /* Contrutor padrão */
    public BuscaPacienteController() {
    }
    
    /*Contrutor parametrizado*/
    public BuscaPacienteController(BuscaPacienteView view, Secretaria secretaria) {
        this.secretaria = secretaria;
        this.view = view;
        this.paciente = new Paciente();
        this.view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Método que inclui os pacientes em uma tabela na view
        atualizaTabelaPacientes();
        
        //Caso o botão detalhes seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão detalhes foi clicado sem que nenhuma linha da
        //tabela esteja selecionada, é exibida uma mensagem ao usuário dizendo que nenhum paciente foi selecionado na tabela. Caso não haja pacientes cadastrados 
        //no sistema(tabela vazia) e o botão detalhes seja clicado, é exibida uma mensagem dizendo que não há pacientes cadastrados.
        //Caso uma linha da tabela seja selecionada e o botão detalhes seja clicado, é aberta a view para visualização de informações do paciente.
        view.getBtnDetalhesPaciente().addActionListener((ActionEvent actionEvent) -> {
            if(view.getTabelaDePacientes().getModel().getRowCount()>0){
                int linhaSelecionada = view.getTabelaDePacientes().getSelectedRow();
                if (linhaSelecionada >= 0) {
                    Paciente p = secretaria.getPacienteCPF((Long) view.getTabelaDePacientes().getModel().getValueAt(linhaSelecionada, 0));
                    new DetalhesPacienteSecretariaController(new DetalhesPacienteSecretariaView(view), p).controla();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Nenhum paciente selecionado", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
                }  
            }
            else
                JOptionPane.showMessageDialog(null, "Não há pacientes cadastrados", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
        });
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view da Secretária é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
        
        //Caso a secretária prefira, poderá digitar o CPF do paciente e clicar em buscar, assim a tela de detalhes é aberta se o paciente for encontrado.
        //Caso o botão buscar seja clicado sem que nada tenha sido digitado, é informado à secretária que nenhum CPF foi digitado
        //Caso seja digitado um CPF de algum paciente que não esteja cadastrado, a busca pelo CPF retornará null, e será exibida uma mensagem dizendo que não
        //há pacientes cadastrados com o CPF digitado. Caso o CPF seja encontrado, a tela de detalhes é aberta
        view.getBtnBuscar().addActionListener((ActionEvent actionEvent) -> {
            if (!view.getCpfDoPaciente().equals("")){
            Paciente p = secretaria.getPacienteCPF(Long.parseLong(view.getCpfDoPaciente()));
            if(p != null)
                new DetalhesPacienteSecretariaController(new DetalhesPacienteSecretariaView(view), p).controla();
            else{
                JOptionPane.showMessageDialog(null, "Não há pacientes cadastrados com o CPF digitado", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
            }
        }
            else
                JOptionPane.showMessageDialog(null, "Nenhum CPF digitado", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
            
        });
    }
    
    
    private void atualizaTabelaPacientes() {
        //É gerada uma tabela com todos os pacientes cadastrados e essa tabela é setada na tabela da view
        view.getTabelaDePacientes().setModel(GeradorDeTabelas.geraTabelaPacientes(secretaria));
    }
}
