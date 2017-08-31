package controller;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import model.Paciente;
import model.Secretaria;
import util.GeradorDeTabelas;
import view.AtualizaPacienteView;
import view.SecretariaView;
import view.RealizaAtualizacaoPacienteView;

/**
 *
 * @author gusta
 */
public class AtualizaPacienteController {
    private Secretaria secretaria;
    private AtualizaPacienteView view;
    private Paciente paciente;
    
    /* Contrutor padrão */
    public AtualizaPacienteController() {
    }

    /*Contrutor parametrizado.*/
    public AtualizaPacienteController(AtualizaPacienteView view, Secretaria secretaria) {
        this.secretaria = secretaria;
        this.view = view;
        view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Método que inclui os pacientes em uma tabela na view
        atualizaTabelaPacientes();
        
        //Caso o botão atualizar seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão atualizar foi clicado sem que nenhuma linha da
        //tabela esteja selecionada, é exibida uma mensagem ao usuário dizendo que nenhum paciente foi selecionado na tabela. Caso não haja pacientes cadastrados 
        //no sistema(tabela vazia) e o botão atualizar seja clicado, é exibida uma mensagem dizendo que não há pacientes cadastrados.
        //Caso uma linha da tabela seja selecionada e o botão atualizar seja clicado, é aberta a view para atualização de pacientes.
        view.getBtnAtualizar().addActionListener((ActionEvent actionEvent) -> {
            if(view.getjTable1().getModel().getRowCount()>0){
               int linhaSelecionada = view.getjTable1().getSelectedRow();
               if (linhaSelecionada >= 0) {
                   Paciente p = secretaria.getPacienteCPF((Long) view.getjTable1().getModel().getValueAt(linhaSelecionada, 0));
                   new RealizaAtualizacaoPacienteController(new RealizaAtualizacaoPacienteView(view), p, secretaria).controla();
                   atualizaTabelaPacientes();
               }
               else{
                   JOptionPane.showMessageDialog(null, "Nenhum paciente selecionado", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
               }               
            }
            else
                JOptionPane.showMessageDialog(null, "Não há pacientes cadastrados", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view da Secretária é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
        
        //Caso a secretária prefira, poderá digitar o CPF do paciente e clicar em buscar, assim a tela de atualização é aberta se o paciente for encontrado.
        //Caso o botão buscar seja clicado sem que nada tenha sido digitado, é informado à secretária que nenhum CPF foi digitado
        //Caso seja digitado um CPF de algum paciente que não esteja cadastrado, a busca pelo CPF retornará null, e será exibida uma mensagem dizendo que não
        //há pacientes cadastrados com o CPF digitado. Caso o CPF seja encontrado, a tela de atualização é aberta
        view.getBtnBuscar().addActionListener((ActionEvent actionEvent) -> {
            if(!view.getCPF().equals("")){
                Paciente p = secretaria.getPacienteCPF(Long.parseLong(view.getCPF()));
                if(p != null){
                    new RealizaAtualizacaoPacienteController(new RealizaAtualizacaoPacienteView(view), p, secretaria).controla();
                    atualizaTabelaPacientes();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Não há pacientes cadastrados com o CPF digitado", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Nenhum CPF digitado", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    private void atualizaTabelaPacientes() {
        //É gerada uma tabela com todos os pacientes cadastrados e essa tabela é setada na tabela da view
        view.getjTable1().setModel(GeradorDeTabelas.geraTabelaPacientes(secretaria));
    }
}
