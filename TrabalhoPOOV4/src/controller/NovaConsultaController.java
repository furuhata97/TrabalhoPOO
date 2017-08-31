/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import model.Consulta;
import model.Paciente;
import model.Secretaria;
import util.GeraDatas;
import view.NovaConsultaView;
import view.SecretariaView;

/**
 *
 * @author gusta
 */
public class NovaConsultaController {
    private Secretaria secretaria;
    private NovaConsultaView view;
    private Consulta consulta;
    
    //Construtor padrão
    public NovaConsultaController() {
    }

    //Construtor parametrizado.
    public NovaConsultaController(NovaConsultaView view, Secretaria secretaria) {
        this.secretaria = secretaria;
        this.view = view;
        this.consulta =  new Consulta();
        view.setVisible(true);
        view.getCPFInvalido().setVisible(false);
        view.getFormatoInvalido().setVisible(false);
        view.getSemHorario().setVisible(false);
        view.getNomeMedico().setVisible(false);
    }
    
    //Método que realiza o controle das ações realizadas na view
    public void controla(){
        //É feita uma verificação quando o foco deste campo é perdido. Caso o campo fique em branco, uma mensagem
        //ficará visível ao usuário, alertando-o de que o campo de médico não deve ficar em branco. Assim que o campo é preenchido a mensagem é ocultada
        view.getMedico().addFocusListener(new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent e){
                String medico = view.getMedico().getText();
                if(medico.equals("")){
                    view.getNomeMedico().setVisible(true);
                }
                else
                    view.getNomeMedico().setVisible(false);
            }
        });
        
        //É feita uma verificação quando o foco deste campo é perdido. Caso o campo fique em branco, ou seja digitado um CPF que não exista no banco de dados, uma mensagem
        //ficará visível ao usuário, alertando-o de que o campo CPF está incorreto. Assim que o campo é preenchido corretamente a mensagem é ocultada
        view.getCpfPaciente().addFocusListener(new FocusAdapter() {  
            @Override  
            public void focusLost(FocusEvent e) {
                String cpf = view.getCpfPaciente().getText();
                if(!cpf.equals("")){
                    Paciente p = secretaria.getPacienteCPF(Long.parseLong(cpf));
                    if(p==null){
                        //view.getCpfPaciente().grabFocus();
                        view.getCPFInvalido().setVisible(true);
                    }
                    else
                        view.getCPFInvalido().setVisible(false);
                }
                else
                    view.getCPFInvalido().setVisible(true);
            }  
        });
        
        //É feita uma verificação quando o foco deste campo é perdido. Caso o campo fique em branco, possua uma 
        //data anterior à data atual ou possua formato inválido, uma mensagem ficará visível ao usuário, alertando-o de que o campo 
        //de data foi preenchido de forma errada. Assim que uma data válida é preenchida a mensagem é ocultada
        view.getDataConsulta().addFocusListener(new FocusAdapter() {  
            @Override  
            public void focusLost(FocusEvent e) {
                try {
                    Date data = GeraDatas.geraDataString(view.getDataConsulta().getText());
                    Date d = Date.valueOf(LocalDate.now());
                    if(data.before(d)){
                        view.getFormatoInvalido().setVisible(true);
                        return; 
                    }
                } catch (ParseException ex) {
                    view.getFormatoInvalido().setVisible(true);
                    return;
                }
                view.getFormatoInvalido().setVisible(false);
            }  
        });
        
        //É feita uma verificação quando este campo ganha foco. Caso os campos de médico e data estejam preenchidos de
        //forma errada, uma mensagem ficará visível ao usuário, alertando-o de que não há horários disponíveis e a comboBox ficará vazia.
        //Caso os campos médico e data estejam corretos, a mensagem será ocultada e a comboBox será preenchida com todos os horários disponíveis na data solicitada com
        //o médico solicitado
        view.getHorario().addFocusListener(new FocusAdapter() {  
        @Override  
        public void focusGained(FocusEvent e) {
            view.getSemHorario().setVisible(false);
            try {
                Date data = GeraDatas.geraDataString(view.getDataConsulta().getText());
                ArrayList<LocalTime> horas = horariosDisponiveis(data, view.getTipoConsulta().getSelectedItem().toString(), view.getMedico().getText());
                view.getHorario().removeAllItems();
                if(horas.isEmpty())
                    view.getSemHorario().setVisible(true);
                for(int i = 0; i<horas.size(); i++){
                    view.getHorario().addItem(horas.get(i).toString());
                }
                view.getHorario().setMaximumRowCount(5);
            } catch (ParseException | NullPointerException ex) {
                //Logger.getLogger(NovaConsultaController.class.getName()).log(Level.SEVERE, null, ex);
                view.getSemHorario().setVisible(true);
            }
            System.out.println("Recebeu focus: " + e);  
        }  
        });
        
        //Quando o botão OK for clicado, será verificada as informações que foram preenchidas. Caso a data esteja incorreta e/ou médico esteja em branco e/ou
        //CPF esteja incorreto, não será permitido agendamento da consulta enquanto estes campos não forem preenchidos corretamente. 
        //Caso os campos sejam preenchidos corretamente, será feita uma chamada ao método criaConsulta, que criará a consulta com os dados digitados na view e
        //adicionará essa consulta ao banco de dados, uma mensagem de sucesso será exibida e a view será fechada
        view.getBtnAgendar().addActionListener((ActionEvent actionEvent) -> {
            Long cpf;
            Date dataConsulta;
            Paciente p;
            Long codConsulta;
            try {
                codConsulta = Long.parseLong("");
            } catch (NumberFormatException nfe) {
                codConsulta = null;
            }
            try {
                cpf = new Long(view.getCpfPaciente().getText());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(view, "CPF deve conter apenas números", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                dataConsulta = GeraDatas.geraDataString(view.getDataConsulta().getText());
                Date d = Date.valueOf(LocalDate.now());
                if(dataConsulta.before(d)){
                    JOptionPane.showMessageDialog(view, "Não é possível agendar uma consulta em uma data anterior a hoje!", "Erro", JOptionPane.WARNING_MESSAGE);
                    return; 
                }
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(view, "Sua data não tem o padrão correto dd/mm/aaaa!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(view.getMedico().getText().equals("")){
                JOptionPane.showMessageDialog(view, "Nenhum Médico digitado", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            p = secretaria.getPacienteCPF(cpf);
            if (p == null) {
                JOptionPane.showMessageDialog(view, "Paciente não encontrado!", "Erro", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Consulta agendada com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
                criaConsulta(codConsulta, dataConsulta, view.getHorario().getSelectedItem().toString(),view.getMedico().getText(), p,view.getTipoConsulta().getSelectedItem().toString());
                new SecretariaController(new Secretaria(), new SecretariaView()).controla();
            }
        });
        
        //Caso o botão cancelar seja clicado, a view é fechada e retornará para a view de Secretária
        view.getBtnCancelar().addActionListener((ActionEvent actionEvent) -> {
            view.dispose();
            new SecretariaController(new Secretaria(), new SecretariaView()).controla();
        });
    }
    
    //Realiza a criação de uma nova consulta com dados passados por parâmetro e persiste essa consulta no banco de dados
     private void criaConsulta(Long codConsulta, Date dataConsulta, String hora, String medico, Paciente paciente, String tipoConsulta) {
        consulta = new Consulta(codConsulta, dataConsulta, hora, medico, paciente, tipoConsulta);
        secretaria.adicionaConsulta(consulta);
    }
     
     
    public ArrayList<LocalTime> horariosDisponiveis(Date data, String tipo, String medico){
        //Este método devolve uma lista com todos os horários disponíveis, na data solicitada e com o tipo e médico desejado
        
        //Este array list recebe uma lista com todos os horários possíveis em que qualquer consulta pode ser marcada. As horas vão das 8:00 da manhã até às 17:30
        ArrayList<LocalTime> horas = listaTodosHorarios();
        
        //Recebe todas as consultas cadastradas
        List<Consulta> listaConsultas = secretaria.listaConsultas();
        
        //Caso o médico passado como parâmetro esteja em branco, significa que não pode haver horários para consultas disponíveis sem um médico, logo é retornado o valor null
        if(medico.equals(""))
            return null;
        
        //Caso o médico não esteja em branco, é feita uma comparação para cada consulta que está em listaConsultas.
        //Caso a data da consulta seja igual a data passada por parâmetro e o médico da consulta seja igual ao médico passado por parâmetro, significa que não
        //pode haver uma nova consulta nesse horário, logo esse horário é removido das horas disponíveis. É verificado então o tipo da Consulta
        //Caso o tipo seja Normal, o próximo horário disponível também é removido, pois a consulta normal leva uma hora, e cada horário possui duração de 
        //30 minutos
        for(int i = 0; i<listaConsultas.size(); i++){
            if(listaConsultas.get(i).getDataConsulta().compareTo(data)==0 && medico.equals(listaConsultas.get(i).getMedico())){
                System.out.println(data);
                if(listaConsultas.get(i).getTipoConsulta().equals("Normal")){
                    LocalTime local = LocalTime.parse(listaConsultas.get(i).getHora());
                    horas.remove(local);
                    try{
                        LocalTime local2 = LocalTime.parse(listaConsultas.get(i+1).getHora());
                        horas.remove(local2);
                    }catch(IndexOutOfBoundsException iou){
                        System.out.println("Fim das consultas");
                    }
                }
                else{
                    LocalTime local = LocalTime.parse(listaConsultas.get(i).getHora());
                    System.out.println(local);
                    horas.remove(local);
                }
            }   
        }
        //caso o tipo de consulta seja normal, significa que a consulta tem duração de 1 hora, logo a consulta utilizará 2 horários
        if (tipo.equals("Normal")){
            LocalTime cal;
            ArrayList<LocalTime> maisHorasPraExcluir = new ArrayList<>();
            maisHorasPraExcluir.add(horas.get(horas.size()-1));
            //é feita a verificação do próximo horário disponível. Caso o próximo horário seja 30 minutos maior, significa que os 2 horários podem ser selecionados 
            //para consulta do tipo Normal. Caso o próximo horário seja 1 hora maior, significa que o próximo horário ao atual já está ocupado e a consulta 
            //não pode ser marcada neste horário. Exemplo: há 1 horário disponível às 15:00 e o próximo disponível é às 16 horas. Nesse caso a consulta de tipo 
            //Normal não pode ser marcada às 15:00 pois o horário das 15:30 já está ocupado
            for (int i = 0; i<horas.size()-1; i++){
                if(horas.get(i).getMinute() == 30)
                    cal = LocalTime.of(horas.get(i).getHour()+1, 0, 0);
                else
                    cal = LocalTime.of(horas.get(i).getHour(), 30, 0);
                if (!horas.get(i+1).equals(cal)){
                    maisHorasPraExcluir.add(horas.get(i));
                }
            }
            for(int i = 0; i<maisHorasPraExcluir.size(); i++){
                horas.remove(maisHorasPraExcluir.get(i));
            }
        }
        return horas;
    }
    
    public ArrayList<LocalTime> listaTodosHorarios(){
        ArrayList<LocalTime> listaHoras = new ArrayList<>();
        LocalTime l;
        //começa em 8 pois foi definido que o primeiro horário de consulta na clínica começa às 8 e vai até 17 pos foi definido que o último horário de consulta é às 17:30
        for (int i = 8; i<=17; i++){
            l = LocalTime.of(i, 0, 0);
            listaHoras.add(l);
            l = LocalTime.of(i, 30, 0);
            listaHoras.add(l);
        }
        return listaHoras;
    }
}
