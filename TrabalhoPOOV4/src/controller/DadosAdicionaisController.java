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
import view.CadastraAlteraDadosAdicionaisView;
import view.DadosAdicionaisView;
import view.MedicoView;

/**
 *
 * @author gusta
 */
public class DadosAdicionaisController {
    
    private Medico medico;
    private Paciente paciente;
    private DadosAdicionaisView view;
    
    //Construtor padrão
    public DadosAdicionaisController(){
        
    }
    
    //Construtor parametrizado
    public DadosAdicionaisController(DadosAdicionaisView view, Paciente paciente, Medico medico){
        this.medico = medico;
        this.paciente = paciente;
        this.view = view;
        this.view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Preenche a view com as informações de dados adicionais do paciente
        preencheDados();
        
        //Caso o botão de Alteração/Cadastro de Dados adicionais seja clicado, é aberta a view para a edição/cadastro dos mesmos e a view é atualizada com os novos dados
        view.getBtnCadastraAltera().addActionListener((ActionEvent actionEvent) -> {
            new CadastraAlteraDadosAdicionaisController(new CadastraAlteraDadosAdicionaisView(view), medico, paciente).controla();
            preencheDados();
        });
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view de Médico é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new MedicoController(new Medico(), new MedicoView()).controla();
        });
        
        //Caso o botão de exclusão seja clicado, é verificado se o Paciente possui algum dado adicional setado como null, caso possua, uma mensagem é exibida informando
        //que o paciente não possui dados adicionais para remoção. Caso contrário, uma janela de confirmação de exclusão é aberta, Caso o usuário clique em sim
        //O método removeDados é chamado para realizar a exclusão e o método preencheDados atualiza as informações da view
        view.getBtnExclui().addActionListener((ActionEvent actionEvent) -> {
            if(paciente.getFuma() == null){
                JOptionPane.showMessageDialog(null, "O paciente não possui dados adicionais cadastrados", "Erro", JOptionPane.WARNING_MESSAGE);
            }
            else{
               int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Deseja remover os dados adicionais do paciente?", "Confirmar remoção", JOptionPane.YES_NO_CANCEL_OPTION);
            if(showConfirmDialog == 0){
                removeDados();
                preencheDados();
            } 
            }
            
        });
    }
    
    
    public void preencheDados(){
        //Caso o paciente não possua dados adicionais, as informações são preenchidas com Não Informado e os campos de texto ficam em branco.
        //Sempre que o botão de cadastro de dados adicionais for clicado, as informações da comboBox serão automaticamente preenchidas, cabendo ao usuário selecionar
        //a opção correta. Assim, para saber se o paciente não possui dados adicionais cadastrados, basta verificar se este possui pelo menos algum campo
        //de dados adicionais setado com null. Neste caso, a verificação é feita através do campo Fuma do paciente. Caso ele esteja setado com null,
        //significa que os dados adicionais ainda não foram cadastrados
        view.getNome().setText(paciente.getNome());
        if(paciente.getFuma() == null){
            view.getFuma().setText("Não Informado");
            view.getBebe().setText("Não Informado");
            view.getColesterol().setText("Não Informado");
            view.getDiabetes().setText("Não Informado");
            view.getdCardiaca().setText("Não Informado");
            view.getCirurgias().setText("");
            view.getAlergias().setText("");
        }
        //Caso o campo Fuma do paciente não esteja setado como null, significa que este possui dados adicionais cadastrados, logo os campos da view são preenchidos
        //com as informações corretas. O botão de Cadastro de Dados adicionais também tem seu nome alterado para Alterar Dados Adicionais
        else{
            view.getBtnCadastraAltera().setText("Alterar Dados Adicionais");
            view.getFuma().setText(paciente.getFuma());
            view.getBebe().setText(paciente.getBebe());
            view.getColesterol().setText(paciente.getColesterol());
            view.getDiabetes().setText(paciente.getDiabete());
            view.getdCardiaca().setText(paciente.getdCardiaca());
            view.getCirurgias().setText(paciente.getCirurgias());
            view.getAlergias().setText(paciente.getAlergias());
        }
        //Faz com que os campos de texto não fiquem disponíveis para edição
        view.getCirurgias().setEditable(false);
        view.getAlergias().setEditable(false);
    }
    
    public void removeDados(){
            //Assim que os dados adicionais são removidos, os respectivos campos no paciente são setados com null, e é feita a chamada do método para atualização do paciente
            //no banco de dados. O botão que antes estava marcado com Alterar dados adicionais volta a ser marcado com Cadastrar Dados Adicionais
            view.getBtnCadastraAltera().setText("Cadastrar Dados Adicionais");
            paciente.setFuma(null);
            paciente.setBebe(null);
            paciente.setColesterol(null);
            paciente.setDiabete(null);
            paciente.setdCardiaca(null);
            paciente.setCirurgias(null);
            paciente.setAlergias(null);
            medico.adicionaDadosPaciente(paciente);
    }
}
