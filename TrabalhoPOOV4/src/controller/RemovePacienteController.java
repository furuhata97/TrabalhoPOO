package controller;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import model.Paciente;
import model.Secretaria;
import view.RemovePacienteView;
import util.GeradorDeTabelas;
import view.DetalhesPacienteSecretariaView;
import view.SecretariaView;

/**
 *
 * @author gusta
 */
public class RemovePacienteController {
    
    private Secretaria secretaria;
    private RemovePacienteView view;
    
    //construtor padrão
    public RemovePacienteController(){
        
    }
    
    //construtor parametrizado
    public RemovePacienteController(RemovePacienteView view, Secretaria secretaria){
        this.view = view;
        this.secretaria = secretaria;
        this.view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Método que inclui os pacientes em uma tabela na view
        atualizaTabelaPacientes();
        
        //Caso o botão remover seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão remover foi clicado sem que nenhuma linha da
        //tabela esteja selecionada, é exibida uma mensagem ao usuário dizendo que nenhum paciente foi selecionado na tabela. Caso não haja pacientes cadastrados 
        //no sistema(tabela vazia) e o botão remover seja clicado, é exibida uma mensagem dizendo que não há pacientes cadastrados.
        //Caso uma linha da tabela seja selecionada e o botão remover seja clicado, é aberta a view para confirmação da remoção.
        view.getBtnRemover().addActionListener((ActionEvent actionEvent) -> {
            if(view.getTabela().getModel().getRowCount()>0){
               int linhaSelecionada = view.getTabela().getSelectedRow();
                if (linhaSelecionada >= 0) {
                    Paciente paciente = secretaria.getPacienteCPF((Long) view.getTabela().getModel().getValueAt(linhaSelecionada, 0));
                    new DetalhesPacienteSecretariaController(new DetalhesPacienteSecretariaView(view), paciente, true, secretaria).controla();
                    atualizaTabelaPacientes();
                }
                else{
                    JOptionPane.showMessageDialog(view, "Nenhum paciente selecionado", "Erro", JOptionPane.WARNING_MESSAGE);
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
        
        //Caso a secretária prefira, poderá digitar o CPF do paciente e clicar em buscar, assim a tela de remoção é aberta se o paciente for encontrado.
        //Caso o botão buscar seja clicado sem que nada tenha sido digitado, é informado à secretária que nenhum CPF foi digitado
        //Caso seja digitado um CPF de algum paciente que não esteja cadastrado, a busca pelo CPF retornará null, e será exibida uma mensagem dizendo que não
        //há pacientes cadastrados com o CPF digitado. Caso o CPF seja encontrado, a tela de remoção é aberta
        view.getBtnBuscar().addActionListener((ActionEvent actionEvent) -> {
            if (!view.getCPF().equals("")){
                Paciente p = secretaria.getPacienteCPF(Long.parseLong(view.getCPF()));
                if(p != null){
                    new DetalhesPacienteSecretariaController(new DetalhesPacienteSecretariaView(view), p, true, secretaria).controla();
                    atualizaTabelaPacientes();
                }
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
        view.getTabela().setModel(GeradorDeTabelas.geraTabelaPacientes(secretaria));
    }
    
}
