/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.ParseException;
import javax.swing.JOptionPane;
import model.Secretaria;
import model.Paciente;
import view.CadastraPacienteView;
import util.GeraDatas;
import view.SecretariaView;

/**
 *
 * @author gusta
 */
public class CadastraPacienteController {
    
    private Secretaria secretaria;
    private CadastraPacienteView view;
    private Paciente paciente;
    
    /* Contrutor padrão */
    public CadastraPacienteController() {
    }
    
    /*Contrutor parametrizado*/
    public CadastraPacienteController(CadastraPacienteView view, Secretaria secretaria) {
        this.secretaria = secretaria;
        this.view = view;
        this.paciente = new Paciente();
        this.view.setVisible(true);
    }
    
    /*Cria um novo paciente com as informações digitadas na view e chama o método para inclusão do paciente no banco de dados*/
    private void criaPaciente(Long cpf, String nome, Date dataNascimento, String endereco, String telefone, String email, String tipoConvenio, Integer numero, String complemento, String estado, String cidade) {
        paciente = new Paciente(cpf, nome, dataNascimento, endereco, telefone, email, tipoConvenio, cidade, estado, numero, complemento);
        secretaria.adicionaPaciente(paciente);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        
        //Ao clicar no botão Salvar, será feita uma verificação do CPF e da data de nascimento digitada. Caso algum dos dois campos não possua formato correto, é exibida
        //uma mensagem informando o erro. O cadastro só será realizado quando o CPF e a data de nascimento possuírem formato adequado
        //O CPF e o número precisam conter apenas números e a data de nascimento deve ser digitada no formato dd/mm/aaaa
        //Se o banco de dados já possuir algum paciente cadastrado com o mesmo CPF digitado, uma mensagem de aviso será exibida ao usuário
        //Caso todas as informações digitadas estejam corretas, é chamado o método para criação de um novo paciente com as informações digitadas na view
        view.getBtnSalvar().addActionListener((ActionEvent actionEvent) ->{
            Long cpf;
            Date dataNascimento;
            try{
                cpf = new Long(view.getCpf());
            }catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(view, "CPF deve conter apenas números", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                dataNascimento = GeraDatas.geraDataString(view.getDataNascimento());
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(view, "A data não possui padrão dd/mm/aaaa!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try{
                Integer numero = new Integer(view.getNumero());
            }catch(NumberFormatException e){
               JOptionPane.showMessageDialog(view, "Número da casa deve conter apenas números", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(secretaria.getPacienteCPF(cpf) != null){
                JOptionPane.showMessageDialog(view, "Já existe um paciente com o mesmo CPF cadastrado", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            //Caso todas as informações estejam corretas, será criado um novo paciente com as informações digitadas
            criaPaciente(cpf, view.getNomeCompleto(), dataNascimento, view.getEndereco(), view.getTelefone(), view.getEmail(), view.getConvenio(), view.getNumero(), view.getComplemento(), view.getEstados(), view.getCidade());
            JOptionPane.showMessageDialog(view, "Paciente cadastrado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
        
        //Ao clicar no botão Cancelar não serão alteradas nenhuma das informações do banco de dados e será retornado para a view de secretária.
        view.getBtnCancelar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
    }
}
