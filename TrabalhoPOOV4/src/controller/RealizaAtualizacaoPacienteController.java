/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Paciente;
import model.Secretaria;
import util.GeraDatas;
import view.RealizaAtualizacaoPacienteView;

/**
 *
 * @author gusta
 */
public class RealizaAtualizacaoPacienteController {
    private Paciente paciente;
    private Secretaria secretaria;
    private RealizaAtualizacaoPacienteView view;
    
    /*Contrutor parametrizado*/
    public RealizaAtualizacaoPacienteController(RealizaAtualizacaoPacienteView view, Paciente paciente, Secretaria secretaria) {
        this.paciente = paciente;
        this.view = view;
        this.secretaria = secretaria;
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Seta a view com as informações de paciente.
        view.getNomePaciente().setText(paciente.getNome());
        view.getCidade().setText(paciente.getCidade());
        view.getEndereco().setText(paciente.getEndereco());
        view.getNumero().setText(String.valueOf(paciente.getNumeroCasa()));
        view.getComplemento().setText(paciente.getComplemento());
        view.getEstados().setSelectedItem(paciente.getEstado());
        view.getDataNascimento().setText(paciente.getDataNascimento().toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        view.getConvenio().setSelectedItem(paciente.getConvenio());
        view.getTelefone().setText(paciente.getTelefone());
        view.getEmail().setText(paciente.getEmail());
        
        //Ao clicar no botão Salvar, é verificado a data de nascimento e o número da casa, caso algum destes campos esteja incorreto, o cadastro não será realizado
        //Assim que esses campos são preenchidos corretamente, é chamado o campo preencheDados, que atualiza os dados em paciente e faz a persistencia no banco de dados.
        //Após isso uma mensagem de sucesso é exibida ao usuário e a view é fechada
        view.getBtnSalvar().addActionListener((ActionEvent actionEvent) -> {
            try {
               Date dataNascimento = GeraDatas.geraDataString(view.getDataNascimento().getText());
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(view, "A data não possui padrão dd/mm/aaaa!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
               Integer numero = Integer.parseInt(view.getNumero().getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Digite apenas números para número da casa", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                preencheCampos();
            } catch (ParseException ex) {
                Logger.getLogger(RealizaAtualizacaoPacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(view, "Paciente atualizado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
        });
        this.view.setVisible(true);
    }
    
        private void preencheCampos() throws ParseException {
            //Preenche o paciente com as informações digitadas na view e realiza a presistência de paciente no banco de dados
            paciente.setNome(view.getNomePaciente().getText());
            paciente.setCidade(view.getCidade().getText());
            paciente.setEndereco(view.getEndereco().getText());
            paciente.setNumeroCasa(Integer.parseInt(view.getNumero().getText()));
            paciente.setComplemento(view.getComplemento().getText());
            paciente.setEstado(view.getEstados().getItemAt(view.getEstados().getSelectedIndex()));
            paciente.setDataNascimento(GeraDatas.geraDataString(view.getDataNascimento().getText()));
            paciente.setConvenio(view.getConvenio().getItemAt(view.getConvenio().getSelectedIndex()));
            paciente.setTelefone(view.getTelefone().getText());
            paciente.setEmail(view.getEmail().getText());
            secretaria.adicionaPaciente(paciente);
    }
}
