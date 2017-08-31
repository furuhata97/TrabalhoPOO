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
import util.GeradorDeTabelas;
import view.DadosAdicionaisView;
import view.MedicoView;
import view.ProntuarioView;
import view.RelatoriosMedicosView;
import view.SelecionaPerfilView;

/**
 *
 * @author gusta
 */
public class MedicoController {
    
    private Medico medico;
    private MedicoView view;
    
    /* Contrutor padrão */
    public MedicoController() {
    }
    
    /*Contrutor parametrizado*/
    public MedicoController(Medico medico, MedicoView view) {
        this.medico = medico;
        this.view = view;
        this.view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Método que inclui os pacientes em uma tabela na view
        atualizaTabelaPacientes();
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view de Seleção de Perfil é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            SelecionaPerfilController selecaoPerfilController = new SelecionaPerfilController(new SelecionaPerfilView());
            selecaoPerfilController.controla();
        });
        
        //Caso o botão de dados adicionais seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão de dados adicionais
        //foi clicado sem que nenhuma linha da tabela esteja selecionada, é exibida uma mensagem ao usuário dizendo que nenhum paciente foi selecionado na tabela.
        //Caso não haja pacientes com consulta marcada na data atual(tabela vazia) e o botão de dados adicionais seja clicado, é exibida uma mensagem dizendo que
        //não há pacientes com consulta na data atual. Caso uma linha da tabela seja selecionada e o botão de dados adicionais seja clicado, é aberta a view
        //para manipulação dos dados adicionais do paciente.
        view.getBtnInfAdicionais().addActionListener((ActionEvent actionEvent) -> {
            if(view.getTabelaPacientes().getModel().getRowCount()>0){
               int linhaSelecionada = view.getTabelaPacientes().getSelectedRow();
                if (linhaSelecionada >= 0) {
                    Paciente p = medico.getPacienteCPF((Long) view.getTabelaPacientes().getModel().getValueAt(linhaSelecionada, 0));
                    view.dispose();
                    new DadosAdicionaisController(new DadosAdicionaisView(), p, medico).controla();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Nenhum paciente selecionado", "Dados Adicionais", JOptionPane.INFORMATION_MESSAGE);
                } 
            }
            else
                JOptionPane.showMessageDialog(null, "Não há pacientes com consulta marcada hoje", "Dados acidionais", JOptionPane.INFORMATION_MESSAGE);
        });
        
        //Caso o botão de prontuário seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão de prontuário
        //foi clicado sem que nenhuma linha da tabela esteja selecionada, é exibida uma mensagem ao usuário dizendo que nenhum paciente foi selecionado na tabela.
        //Caso não haja pacientes com consulta marcada na data atual(tabela vazia) e o botão de prontuário seja clicado, é exibida uma mensagem dizendo que
        //não há pacientes com consulta na data atual. Caso uma linha da tabela seja selecionada e o botão de prontuário seja clicado, é aberta a view
        //para manipulação do prontuário do paciente.
        view.getBtnProntuario().addActionListener((ActionEvent actionEvent) -> {
            if(view.getTabelaPacientes().getModel().getRowCount()>0){
               int linhaSelecionada = view.getTabelaPacientes().getSelectedRow();
                if (linhaSelecionada >= 0) {
                    Paciente p = medico.getPacienteCPF((Long) view.getTabelaPacientes().getModel().getValueAt(linhaSelecionada, 0));
                    view.dispose();
                    new ProntuarioController(new ProntuarioView(), p, medico).controla();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Nenhum paciente selecionado", "Prontuário", JOptionPane.INFORMATION_MESSAGE);
                } 
            }
            else
                JOptionPane.showMessageDialog(null, "Não há pacientes com consulta marcada hoje", "Prontuário", JOptionPane.INFORMATION_MESSAGE);
        });
        
        //Caso o botão de relatórios seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão de relatórios
        //foi clicado sem que nenhuma linha da tabela esteja selecionada, a view de relatórios é aberta passando null no lugar do paciente.
        //Caso não haja pacientes com consulta marcada na data atual(tabela vazia) e o botão de relatórios seja clicado, a view de relatórios é aberta
        //passando null no lugar do paciente. Caso uma linha da tabela seja selecionada e o botão de relatórios seja clicado, é aberta a view de relatórios
        //para manipulação dos relatórios médicos, passando o paciente selecionado como parâmetro. Caso a view de relatório seja aberta com um paciente sendo igual a null
        //não será possível gerar receita, atestado e declaração de acompanhamento, mas será possível ver o relatório de consultas do mês
        view.getBtnRelatorios().addActionListener((ActionEvent actionEvent) -> {
            if(view.getTabelaPacientes().getModel().getRowCount()>0){
               int linhaSelecionada = view.getTabelaPacientes().getSelectedRow();
                if (linhaSelecionada >= 0) {
                    Paciente p = medico.getPacienteCPF((Long) view.getTabelaPacientes().getModel().getValueAt(linhaSelecionada, 0));
                    view.dispose();
                    new RelatoriosMedicosController(new RelatoriosMedicosView(), medico, p).controla();
                }
                else{
                    view.dispose();
                    new RelatoriosMedicosController(new RelatoriosMedicosView(), medico, null).controla();
                } 
            }
            else{
                view.dispose();
                new RelatoriosMedicosController(new RelatoriosMedicosView(), medico, null).controla();
            }
        });
    }
    
    private void atualizaTabelaPacientes() {
        //É gerada uma tabela com todos os pacientes que possuem consulta marcada na data atual, e essa tabela é setada na tabela da view
        view.getTabelaPacientes().setModel(GeradorDeTabelas.geraTabelaPacientesMedico(medico));
    }
}
