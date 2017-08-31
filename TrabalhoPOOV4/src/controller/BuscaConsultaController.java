package controller;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Consulta;
import model.Paciente;
import model.Secretaria;
import org.apache.commons.beanutils.PropertyUtils;
import view.BuscaConsultaView;
import view.DetalhesConsultaView;
import view.SecretariaView;

/**
 *
 * @author gusta
 */
public class BuscaConsultaController {

    private Secretaria secretaria;
    private BuscaConsultaView view;
    private List<Consulta> consultas;
    
    /* Contrutor padrão */
    public BuscaConsultaController() {
    }

    /*Contrutor parametrizado.*/
    public BuscaConsultaController(BuscaConsultaView view, Secretaria secretaria, List<Consulta> consultas) {
        this.secretaria = secretaria;
        this.view = view;
        this.consultas = consultas;
        view.setVisible(true);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //Método que inclui as consultas em uma tabela na view
        atualizaTabelaConsultas();
        
        //Caso o botão Voltar seja clicado, a view é fechada e a view da Secretária é aberta novamente
        view.getBtnVoltar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
        
        //Caso o botão detalhes seja clicado, é feita uma verificação da linha da tabela que foi selecionda. Se a botão detalhes foi clicado sem que nenhuma linha da
        //tabela esteja selecionada, é exibida uma mensagem ao usuário dizendo que nenhuma consulta foi selecionado na tabela. Caso o paciente não possua nenhuma 
        //consulta cadastrada(tabela vazia) e o botão alterar seja clicado, é exibida uma mensagem dizendo que o paciente não possui consultas.
        //Caso uma linha da tabela seja selecionada e o botão detalhes seja clicado, é aberta a view para visualização de informações da consulta selecionada.
        view.getBtnDetalhes().addActionListener((ActionEvent actionEvent) -> {
            if(view.getTabela().getModel().getRowCount()>0){
               int linhaSelecionada = view.getTabela().getSelectedRow();
                if (linhaSelecionada >= 0) {
                    Consulta co = secretaria.getConsultaCodigo((Long) view.getTabela().getModel().getValueAt(linhaSelecionada, 0));
                    new DetalhesConsultaController(new DetalhesConsultaView(view), co, 2, secretaria).controla();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Nenhuma consulta selecionada", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
                } 
            }
            else
                JOptionPane.showMessageDialog(null, "O paciente não possui consultas", "Detalhes", JOptionPane.INFORMATION_MESSAGE);
        });
        
    }
    
    private void atualizaTabelaConsultas() {
        //Gera a tabela de consultas e coloca essa tabela na tabela da view
        view.getTabela().setModel(geraTabelaConsultas());
    }
    
    private DefaultTableModel geraTabelaConsultas() {
        //Serão exibidas todas as consultas do paciente. Para isso, quando a classe BuscaConsultaController é criada, é passado uma lista
        //com todas as consultas do paciente como parâmetro. Essa lista é obtida através do CPF do paciente que se deseja buscar consultas.
        //Através dessa lista de consultas, é gerada uma tabela com as informações que serão exibidas ao usuário
        Object[][] consultas = new Object[this.consultas.size()][Consulta.class.getDeclaredFields().length];
        Date d = Date.valueOf(LocalDate.now());
        int i = 0;
        int j = 0;
        //Cada consulta contida na lista de consultas é adicionada à tabela
        for (Consulta c : this.consultas) {
               for (Field field : c.getClass().getDeclaredFields()) {
                try {
                    consultas[i][j] = PropertyUtils.getSimpleProperty(c, field.getName());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    consultas[i][j] = "";
                }
                j++;
            }
            Paciente p = (Paciente) consultas[i][4];
            String nome = p.getNome();
            consultas[i][4] = nome;
            j = 0;
            i++; 
            
        }
        return new javax.swing.table.DefaultTableModel(
                consultas, new String[]{
                    "Código", "Data Consulta", "Hora", "Médico", "Paciente", "Tipo"
                });

    }
    
}
