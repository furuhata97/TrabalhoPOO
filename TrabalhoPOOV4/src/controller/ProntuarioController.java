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
import view.CadastraAlteraProntuarioView;
import view.MedicoView;
import view.ProntuarioView;

/**
 *
 * @author gusta
 */
public class ProntuarioController {
    private Medico medico;
    private Paciente paciente;
    private ProntuarioView view;
    
    //Construtor padrão
    public ProntuarioController(){
        
    }
    
    //Construtor parametrizado
    public ProntuarioController(ProntuarioView view, Paciente paciente, Medico medico){
        this.medico = medico;
        this.paciente = paciente;
        this.view = view;
        this.view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Preenche a view com as informações do prontuário do paciente
        preencheDados();
        
        //Caso o botão de Alteração/Cadastro de prontuário seja clicado, é aberta a view para a edição/cadastro dos mesmos e a view é atualizada com os novos dados
        view.getBtnCadastraAlteraProntuario().addActionListener((ActionEvent actionEvent) -> {
            new CadastraAlteraProntuarioController(new CadastraAlteraProntuarioView(view), medico, paciente).controla();
            preencheDados();
        });
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view de Médico é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new MedicoController(new Medico(), new MedicoView()).controla();
        });
        
        //Caso o botão de exclusão seja clicado, é verificado se o Paciente possui algum prontuário setado como null, caso possua, uma mensagem é exibida informando
        //que o paciente não possui prontuário para remoção. Caso contrário, uma janela de confirmação de exclusão é aberta, Caso o usuário clique em sim
        //O método removeDados é chamado para realizar a exclusão e o método preencheDados atualiza as informações da view
        view.getBtnRemoveProntuario().addActionListener((ActionEvent actionEvent) -> {
            if(paciente.getSintomas() == null){
                JOptionPane.showMessageDialog(null, "O paciente não possui prontuário cadastrado", "Erro", JOptionPane.WARNING_MESSAGE);
            }
            else{
               int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Deseja remover o prontuário do paciente?", "Confirmar remoção", JOptionPane.YES_NO_CANCEL_OPTION);
            if(showConfirmDialog == 0){
                removeDados();
                preencheDados();
            } 
            }
            
        });
    }
    
    public void preencheDados(){
        //Caso o paciente não possua prontuário, os campos de texto ficam em branco.
        //Quando o botão de cadastro de prontuário for clicado, o usuário poderá preencher os campos de texto com as informações desejadas, ou deixar todos em branco.
        //Caso todos os campos de texto estejam em branco, estes serão setados em paciente como null. Assim, para saber se o paciente não possui 
        //prontuário cadastrado, basta verificar se este possui pelo menos algum campo correspondente ao prontuário setado com null
        //Neste caso, a verificação é feita através do campo Sintomas do paciente. Caso ele esteja setado com null,
        //significa que o prontuário ainda não foi cadastrado
        view.getNomePaciente().setText(paciente.getNome());
        if(paciente.getSintomas() == null){
            view.getSintomas().setText("");
            view.getTratamento().setText("");
            view.getDiagnostico().setText("");
        }
        //Caso o campo Sintomas do paciente não esteja setado como null, significa que este possui prontuário cadastrado, logo os campos da view são preenchidos
        //com as informações corretas. O botão de Cadastro de Prontuário também tem seu nome alterado para Alterar Dados Adicionais
        else{
            view.getBtnCadastraAlteraProntuario().setText("Alterar Prontuário");
            view.getSintomas().setText(paciente.getSintomas());
            view.getTratamento().setText(paciente.getTratamento());
            view.getDiagnostico().setText(paciente.getDiagnostico());
        }
        //Faz com que os campos de texto não fiquem disponíveis para edição
        view.getSintomas().setEditable(false);
        view.getTratamento().setEditable(false);
        view.getDiagnostico().setEditable(false);
    }
    
    public void removeDados(){
            //Assim que o prontuário é removido, os respectivos campos no paciente são setados com null, e é feita a chamada do método para atualização do paciente
            //no banco de dados. O botão que antes estava marcado com Alterar prontuário volta a ser marcado com Cadastrar prontuário
            view.getBtnCadastraAlteraProntuario().setText("Cadastrar Prontuário");
            paciente.setSintomas(null);
            paciente.setTratamento(null);
            paciente.setDiagnostico(null);
            medico.adicionaProntuarioPaciente(paciente);
    }
}
