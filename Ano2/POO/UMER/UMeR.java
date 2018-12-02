import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.Serializable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.lang.String;
import java.util.Comparator;
import java.util.stream.Collectors;


/**
 * Escreva a descricao da classe UMeR aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class UMeR implements Serializable{
    
    private Ator ator;
    private HashMap<String,Cliente>   clientes;
    private HashMap<String,Motorista> motoristas;
    private HashMap<Integer,Taxi>     taxis;
   
    public UMeR() {
       
        this.ator       = null;
        this.clientes   = new HashMap<>();
        this.motoristas = new HashMap<>();
        this.taxis      = new HashMap<>(); 
       
    }
   
    public UMeR(UMeR umer) {
        this.ator       = null;
        this.clientes   = umer.getClientes();
        this.motoristas = umer.getMotoristas();
        this.taxis      = umer.getTaxis();
    }
    
    private HashMap<String,Cliente> getClientes() {
        HashMap<String,Cliente> aux = new HashMap<String,Cliente>();
        for(Cliente c: this.clientes.values())
            aux.put(c.getEmail(), c.clone());
        return aux;
    }
    
    private HashMap<String,Motorista> getMotoristas() {
        HashMap<String,Motorista> aux = new HashMap<String,Motorista>();
        for(Motorista m: this.motoristas.values())
            aux.put(m.getEmail(), m.clone());
        return aux;
    }
    
    private HashMap<Integer,Taxi> getTaxis() {
        HashMap<Integer,Taxi> aux = new HashMap<Integer,Taxi>();
        for(Taxi t: this.taxis.values())
            aux.put(t.getIDTaxi(), t.clone());
        return aux;
    }
    
    public Cliente getCliente(String email) {
        
        return this.clientes.get(email).clone();
        
    }
    
    public Motorista getMotorista(String email) {
        
        return this.motoristas.get(email).clone();
        
    }
    
    /**
     * variavel 'a' -> 1 = cliente
     *              -> 2 = motorista 
     */
    public void iniciaSessao(String email,String pass,int a) throws SemAutorizacaoException {
       
        Scanner saida = new Scanner(System.in);
        
        if(a==1) ator = clientes.get(email);
        if(a==2) ator = motoristas.get(email);
       
        if(ator!=null){
            if(!ator.getPass().equals(pass)){
                ator = null;
                throw new SemAutorizacaoException("Password incorreta!");
            }
        } else {
            if (a==1)
                throw new SemAutorizacaoException("O e-mail " + email + " nao esta registado como cliente!");
            else 
                throw new SemAutorizacaoException("O e-mail " + email + " nao esta registado como motorista!");
        }
        System.out.println("\nLog in efetuado com sucesso!");
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
    
    }
    
    /**
     * nao podem ter o mesmo email mesmo que sejam atores diferentes
     */
    public void registarAtor(Ator a) throws UtilizadorExistenteException {
        
        int     tipo;
        boolean existe = clientes.containsKey(a.getEmail());
        
        if(!existe) {
            existe = motoristas.containsKey(a.getEmail());
        }
        if(existe) {
            throw new UtilizadorExistenteException();
        }
        if(a instanceof Cliente) {
            tipo = 1;
            Cliente c = (Cliente) a;
            clientes.put(c.getEmail(),c);
        } else{
            tipo = 2;
            Motorista m = (Motorista) a;
            motoristas.put(m.getEmail(),m);
        }
        if (tipo==1)
            System.out.println("\nCliente registado com sucesso!");
        else
            System.out.println("\nMotorista registado com sucesso!");
        
    }
    
    // Metodos solicitados pelo cliente;
    public void verTaxisDisponiveis() {
        
        boolean disp  = false;
        int     id;
        String  tipo  = "";
        Scanner saida = new Scanner(System.in); 
        
        for(Motorista m: this.motoristas.values()) {
            if (m.getDisponibilidade()) {
                id = m.getIDTaxi();
                if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                    disp = true;
                    break;
                }
            }
        }
        if (disp) {
            System.out.println("\nTaxis disponiveis:");
            for(Motorista m: this.motoristas.values()) {
                if (m.getDisponibilidade()) {
                    id   = m.getIDTaxi();
                    if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                        if (taxis.get(id) instanceof Ligeiro) {
                            tipo = "Ligeiro";
                            System.out.println("ID: " + id + "  Tipo: " + tipo);
                        }
                        if (taxis.get(id) instanceof Carrinha) {
                            tipo = "Carrinha";
                            System.out.println("ID: " + id + "  Tipo: " + tipo);
                        }
                        if (taxis.get(id) instanceof Moto) {
                            tipo = "Moto";
                            System.out.println("ID: " + id + "  Tipo: " + tipo);
                        }
                    }
                }
            }
        }
        else
            System.out.println("\nDe momento, nao existem taxis disponiveis!"); 
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        
    }
    
    public void solicitarViagem(String email) {
        
        int    xa, ya, xd, yd, opcao, minutos, id, tipo;
        double distancia = Double.MAX_VALUE, d, velocidade, precoKm, precoHora, 
               distOaD, tempoAteCliente, tempoOaD, tempoTotalEstimado, custoEstimado, 
               distanciaTotal, minutosP;
        boolean disponivel = false, temFila;
        Ponto2D coords     = null;
        Ponto2D origem     = null;
        Ponto2D destino    = null;
        Taxi    t          = null;
        String motorista   = "", custoEstimadoS = "", tempoTotalEstimadoS = "";
        Scanner input      = new Scanner(System.in);
        Scanner saida      = new Scanner(System.in);
        
        // Verificar se ha taxis disponiveis;
        for(Motorista m: this.motoristas.values()) {
            if (m.getDisponibilidade()) {
                id = m.getIDTaxi();
                temFila = taxis.get(id).getTemFila();
                if (!taxis.get(id).getTemFila()) {
                    disponivel = true;
                    break;
                }
                else {
                    List<Viagem> fila = taxis.get(id).getFilaDeEspera();
                    if (fila.isEmpty())
                        disponivel = true;
                }
            }
        }
        if (!disponivel)
            System.out.println("\nLamentamos, mas de momento nao" +
                               "\nha motoristas disponiveis!");
        if (disponivel) {
            System.out.println("\nCoordenadas atuais:");
            System.out.println("\nX:");
            xa = input.nextInt();
            System.out.println("\nY:");
            ya = input.nextInt();
            System.out.println("\nCoordenadas do destino:");
            System.out.println("\nX:");
            xd = input.nextInt();
            System.out.println("\nY:");
            yd = input.nextInt();
            do {
                System.out.println("\n1 - Taxi mais proximo ou 2 - taxi especifico?");
                opcao = input.nextInt();
                if (opcao!=1 && opcao!=2)
                    System.out.println("\nOpcao invalida! Por favor tente outra vez.");
                if (opcao==1) {
                    // Calcular qual o taxi mais proximo e a sua distancia;
                    for(Motorista m: this.motoristas.values()) {
                        if (m.getDisponibilidade()) {
                            id = m.getIDTaxi();
                            if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                                coords = taxis.get(id).getCoordenadas();
                                d      = Ponto2D.calculaDistancia(xa,ya,coords);
                                if (d<distancia) {
                                    distancia = d;
                                    t         = taxis.get(id);
                                    motorista = m.getEmail();
                                }
                            }
                        }
                    }
                    velocidade      = t.getVelocidade();
                    precoKm         = t.getPrecoKm();
                    precoHora       = t.getPrecoHora();
                    // Calcular tempo ate chegar ao cliente;
                    tempoAteCliente = distancia / velocidade;
                    // Calcular tempo ate chegar ao destino;
                    origem          = new Ponto2D(xa,ya);
                    destino         = new Ponto2D(xd,yd);
                    distOaD         = Ponto2D.calculaDistancia(xa,ya,destino);
                    tempoOaD        = distOaD / velocidade;
                    // Calcular e indicar o custo estimado e tempo total de viagem;
                    distanciaTotal     = distancia + distOaD;
                    tempoTotalEstimado = tempoAteCliente + tempoOaD;
                    custoEstimado      = (distanciaTotal * precoKm) + 
                                         (tempoTotalEstimado * precoHora);
                    // Verificar qual a data da viagem;
                    Calendar now = Calendar.getInstance();
                    int ano    = now.get(Calendar.YEAR);
                    int mes    = now.get(Calendar.MONTH);
                    int dia    = now.get(Calendar.DAY_OF_MONTH);
                    int hora   = now.get(Calendar.HOUR_OF_DAY);
                    int minuto = now.get(Calendar.MINUTE);
                    GregorianCalendar data = new GregorianCalendar(ano,mes,dia,hora,minuto);
                    // Atribuir viagem ao motorista;
                    Viagem v   = new Viagem(origem, destino, motorista, email,
                                            tempoTotalEstimado, custoEstimado,
                                            distanciaTotal, data);
                    motoristas.get(motorista).setViagem(v);
                    motoristas.get(motorista).setDisponibilidade(false);
                    DecimalFormat df    = new DecimalFormat("###,##0.00");
                    custoEstimadoS      = df.format(custoEstimado);
                    tempoTotalEstimadoS = df.format(tempoTotalEstimado);
                    String[] x;
                    x = tempoTotalEstimadoS.split(",");
                    minutosP = Double.parseDouble(x[1])/100;
                    minutos          = (int) (minutosP * 60);
                    // Informacao final;
                    System.out.println("\nCusto Estimado: " + custoEstimadoS      + " €");
                    System.out.println("Tempo Total:    " + x[0] + " horas " +
                                        minutos + " minutos");
                } 
                if (opcao==2) {
                    do {
                        System.out.println("\nQual o tipo de taxi que deseja:");
                        System.out.println("1 - Ligeiro ou 2 - Carrinha ou 3 - Moto?");
                        tipo = input.nextInt();
                        disponivel = false;
                        if (tipo==1) {
                            // Verificar se ha um ligeiro disponivel;
                            for(Motorista m: this.motoristas.values()) {
                                id = m.getIDTaxi();
                                if (m.getDisponibilidade() && taxis.get(id) instanceof Ligeiro) {
                                    if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                                        disponivel = true;
                                        break;
                                    }
                                }
                            }
                            if (!disponivel) {
                                System.out.println("\nLamentamos, mas de momento nao" +
                                                   "\nexistem motoristas disponiveis!");
                                System.out.println("\n (Pressione para continuar)");
                                saida.nextLine();
                                saida.close();                   
                                return;
                            }
                            // Calcular qual o ligeiro mais proximo e a sua distancia;
                            for(Motorista m: this.motoristas.values()) {
                                id = m.getIDTaxi();
                                if (m.getDisponibilidade() && taxis.get(id) instanceof Ligeiro) {
                                    if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                                        coords = taxis.get(id).getCoordenadas();
                                        d      = Ponto2D.calculaDistancia(xa,ya,coords);
                                        if (d<distancia) {
                                            distancia = d;
                                            t         = taxis.get(id);
                                            motorista = m.getEmail();
                                        }
                                    }
                                }
                            }
                        }
                        if (tipo==2) {
                            // Verificar se ha uma carrinha disponivel;
                            for(Motorista m: this.motoristas.values()) {
                                id = m.getIDTaxi();
                                if (m.getDisponibilidade() && taxis.get(id) instanceof Carrinha) {
                                    if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                                        disponivel = true;
                                        break;
                                    }
                                }
                            }
                            if (!disponivel) {
                                System.out.println("\nLamentamos, mas de momento nao" +
                                                   "\nexistem motoristas disponiveis!");
                                System.out.println("\n (Pressione para continuar)");
                                saida.nextLine();
                                saida.close();                   
                                return;
                            }
                            // Calcular qual a carrinha mais proxima e a sua distancia;
                            for(Motorista m: this.motoristas.values()) {
                                id = m.getIDTaxi();
                                if (m.getDisponibilidade() && taxis.get(id) instanceof Carrinha) {
                                    if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                                        coords = taxis.get(id).getCoordenadas();
                                        d      = Ponto2D.calculaDistancia(xa,ya,coords);
                                        if (d<distancia) {
                                            distancia = d;
                                            t         = taxis.get(id);
                                            motorista = m.getEmail();
                                        }
                                    }
                                }
                            }
                        }
                        if (tipo==3) {
                            // Verificar se ha uma moto disponivel;
                            for(Motorista m: this.motoristas.values()) {
                                id = m.getIDTaxi();
                                if (m.getDisponibilidade() && taxis.get(id) instanceof Moto) {
                                    if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                                        disponivel = true;
                                        break;
                                    }
                                }
                            }
                            if (!disponivel) {
                                System.out.println("\nLamentamos, mas de momento nao" +
                                                   "\nexistem motoristas disponiveis!");
                                System.out.println("\n (Pressione para continuar)");
                                saida.nextLine();
                                saida.close();
                                return;
                            }
                            // Calcular qual a moto mais proxima e a sua distancia;
                            for(Motorista m: this.motoristas.values()) {
                                id = m.getIDTaxi();
                                if (m.getDisponibilidade() && taxis.get(id) instanceof Moto) {
                                    if (!taxis.get(id).getTemFila() || (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())) {
                                        coords = taxis.get(id).getCoordenadas();
                                        d      = Ponto2D.calculaDistancia(xa,ya,coords);
                                        if (d<distancia) {
                                            distancia = d;
                                            t         = taxis.get(id);
                                            motorista = m.getEmail();
                                        }
                                    }
                                }
                            }
                        }
                        if (tipo!=1 && tipo!=2 && tipo!=3)
                            System.out.println("\nOpcao invalida! Por favor tente outra vez.");
                    } while(opcao!=1 && opcao!=2 && opcao!=3);
                    velocidade      = t.getVelocidade();
                    precoKm         = t.getPrecoKm();
                    precoHora       = t.getPrecoHora();
                    // Calcular tempo ate chegar ao cliente;
                    tempoAteCliente = distancia / velocidade;
                    // Calcular tempo ate chegar ao destino;
                    origem          = new Ponto2D(xa,ya);
                    destino         = new Ponto2D(xd,yd);
                    distOaD         = Ponto2D.calculaDistancia(xa,ya,destino);
                    tempoOaD        = distOaD / velocidade;
                    // Calcular e indicar o custo estimado e tempo total de viagem;
                    distanciaTotal     = distancia + distOaD;
                    tempoTotalEstimado = tempoAteCliente + tempoOaD;
                    custoEstimado      = (distanciaTotal * precoKm) + 
                                         (tempoTotalEstimado * precoHora);
                    // Verificar qual a data da viagem;
                    Calendar now = Calendar.getInstance();
                    int ano    = now.get(Calendar.YEAR);
                    int mes    = now.get(Calendar.MONTH);
                    int dia    = now.get(Calendar.DAY_OF_MONTH);
                    int hora   = now.get(Calendar.HOUR_OF_DAY);
                    int minuto = now.get(Calendar.MINUTE);
                    GregorianCalendar data = new GregorianCalendar(ano,mes,dia,hora,minuto);
                    // Atribuir viagem ao motorista;
                    Viagem v   = new Viagem(origem, destino, motorista, email,
                                            tempoTotalEstimado, custoEstimado,
                                            distanciaTotal, data);
                    motoristas.get(motorista).setViagem(v);
                    motoristas.get(motorista).setDisponibilidade(false);
                    DecimalFormat df    = new DecimalFormat("###,##0.00");
                    custoEstimadoS      = df.format(custoEstimado);
                    tempoTotalEstimadoS = df.format(tempoTotalEstimado);
                    String[] x;
                    x = tempoTotalEstimadoS.split(",");
                    minutosP = Double.parseDouble(x[1])/100;
                    minutos = (int) (minutosP * 60);
                    // Informacao final;
                    System.out.println("\nCusto Estimado: " + custoEstimadoS      + " €");
                    System.out.println("Tempo Total:    " + x[0] + " horas " +
                                        minutos + " minutos");
                }
            } while(opcao!=1 && opcao!=2);
            System.out.println("\nPor favor aguarde pelo servico solicitado. Obrigado!");
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        input.close();
        saida.close();
        
    }
    
    public void fazerReserva(String email) {
        
        int     id, xo, yo, xd, yd, cond = 0;
        boolean existe, cond2 = false;
        Ponto2D origem, destino;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        System.out.println("\nQual o taxi que deseja reservar?");
        System.out.println("\n  (Aperte 0 para cancelar)");
        do {
            existe = false;
            System.out.println("\nID:");
            id = input.nextInt();
            if (id>0) {
                for(Taxi t: this.taxis.values()) {
                    if (t.getIDTaxi()==id) {
                        existe = true;
                        break;
                    }
                }
                if (!existe)
                    System.out.println("\nViatura inexistente! Por favor tente outra vez.");
            }
            if (id<0)
                System.out.println("\nID invalido! Por favor tente outra vez.");
            if (id==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;     
            }
        } while(id<0 || !existe);
        for(Taxi t: this.taxis.values()) {
            if (t.getIDTaxi()==id) {
                if (t.getTemFila()) {
                    for (Motorista m: this.motoristas.values()) {
                        if (m.getIDTaxi()==id) {
                            if (m.getDisponibilidade()) {
                                System.out.println("\nO motorista deste taxi esta disponivel!");
                                System.out.println("Por favor solicite viagem.");
                                cond = -1;
                                break;
                            }
                            else {
                                if (m.getViagem().getMotorista().equals("")) {
                                    cond = 1;
                                    break;
                                }
                                else {
                                    System.out.println("\nEsta viatura encontra-se em viagem!");
                                    System.out.println("Por favor aguarde e solicite viagem assim que possivel.");
                                    cond = -1;
                                    break;
                                }
                            }
                        }
                    }
                    if (cond==0) {
                        cond = 1;
                        break;
                    }
                    if (cond==-1) 
                        break;
                }
                else {
                    System.out.println("\nNao pode reservar viagem nesta viatura\n"+
                                       "pois esta nao possui fila de espera!");
                    break;
                }
            }
        }
        if (cond==1) {
            System.out.println("\nCoordenadas de origem:");
            System.out.println("\nX:");
            xo = input.nextInt();
            System.out.println("\nY:");
            yo = input.nextInt();
            System.out.println("\nCoordenadas do destino:");
            System.out.println("\nX:");
            xd = input.nextInt();
            System.out.println("\nY:");
            yd = input.nextInt();
            Taxi t = taxis.get(id);
            origem          = new Ponto2D(xo,yo);
            destino         = new Ponto2D(xd,yd);
            GregorianCalendar data = new GregorianCalendar(0,0,0,0,0);
            // Adicionar viagem a lista de espera da viatura com dados por definir;
            Viagem v   = new Viagem(origem, destino, "", email, 0, 0, 0, data);
            List<Viagem> novaFila = taxis.get(id).getFilaDeEspera();
            novaFila.add(v);
            taxis.get(id).setFilaDeEspera(novaFila);
            System.out.println("\nPor favor aguarde pelo servico solicitado. Obrigado!");
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        input.close();
        saida.close();
                            
    } 
    
    public void avaliarViagem(String email) {
        
        Viagem v = clientes.get(email).getViagem();
        int opcao, c, classificacao = 0, totalViagens;
        String motorista;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        if (v.getMotorista().equals(""))
            System.out.println("\nAinda nao realizou qualquer viagem!");
        if (!v.getMotorista().equals("")) {
            do {
                System.out.println("\nClassificacao:    (0-100)");
                opcao = input.nextInt();
                if (opcao<0 || opcao>100)
                    System.out.println("\nValor invalido! Por favor tente outra vez.");
            } while(opcao<0 || opcao>100);
            motorista = v.getMotorista();
            totalViagens = motoristas.get(motorista).getTotalViagens();
            if (totalViagens>0) {
                c = motoristas.get(motorista).getClassificacao();
                classificacao = (int) ((opcao+c)/2);
            }
            if (totalViagens==0)
                classificacao = opcao;
            motoristas.get(motorista).setClassificacao(classificacao);
            System.out.println("\nObrigado por avaliar os nossos servicos!");
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
        
    }
    
    public void viagensEfetuadasCliente(String email) {
        
        int dia, mes, ano, i, xo, yo, xd, yd, hora, minuto;
        String motorista;
        boolean cond1, cond2, sg = false;
        GregorianCalendar data1, data2, dataV;
        Viagem v;
        List<Viagem> viagens;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        // Introduçao da primeira data;
        System.out.println("\nIntrouza a primeira data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        do {
            System.out.println("\nDia:    (1-31)");
            dia = input.nextInt();
            if (dia==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data1;
        data1 = new GregorianCalendar(ano,mes,dia);
        // Introduçao da segunda data;
        System.out.println("\nIntrouza a segunda data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        System.out.println("\nDia:    (1-31)");
        dia = input.nextInt();
        do {
            if (dia==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data2;
        data2 = new GregorianCalendar(ano,mes,dia);
        if (data1.before(data2)) {
            viagens = clientes.get(email).getHistorico();
            v       = new Viagem();
            System.out.println("\nViagens realizadas:\n");
            for(i=0; i<viagens.size(); i++) {
                v     = viagens.get(i);
                dataV = v.getData();
                cond1 = dataV.after(data1);
                cond2 = dataV.before(data2);
                if (cond1) {
                    if (cond2) {
                        sg = true;
                        xo = v.getOrigem().getX();
                        yo = v.getOrigem().getY();
                        xd = v.getDestino().getX();
                        yd = v.getDestino().getY();
                        motorista  = v.getMotorista();
                        String iS  = String.format("%2d", i);
                        String xoS = String.format("%2d", xo);
                        String yoS = String.format("%2d", yo);
                        String xdS = String.format("%2d", xd);
                        String ydS = String.format("%2d", yd);
                        dia    = dataV.get(Calendar.DAY_OF_MONTH);
                        mes    = dataV.get(Calendar.MONTH);
                        ano    = dataV.get(Calendar.YEAR);
                        hora   = dataV.get(Calendar.HOUR_OF_DAY);
                        minuto = dataV.get(Calendar.MINUTE);
                        System.out.println(iS+".    Motorista: "+motorista);
                        System.out.println("       Origem:  X - "+xoS+" Y - "+yoS);
                        System.out.println("       Destino: X - "+xdS+" Y - "+ydS);
                        System.out.println("       Data: "+dia+"/"+mes+"/"+ano+"  -  "+
                                            hora+":"+minuto+"\n");
                    }
                    else
                        break;
                }
            }
            if (!sg)
                System.out.println("Nao realizou qualquer viagem neste periodo!\n");
        }
        else
            System.out.println("\nSegunda data e anterior a primeira!");
        System.out.println(" (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
        return;                        
        
    }
    
    // Metodos solicitados pelo motorista;
    public void setDisponivel(String email) {
        
        int id, n = 0;
        Scanner saida = new Scanner(System.in);
        
        if (motoristas.get(email).getDisponibilidade()) 
            System.out.println("\nJa se encontra sinalizado como disponivel!");
        else {
            id = motoristas.get(email).getIDTaxi();
            if (id!=0) {
                if (motoristas.get(email).getViagem().getMotorista().equals("")) {
                    if (!taxis.get(id).getTemFila()) {
                        motoristas.get(email).setDisponibilidade(true);
                        System.out.println("\nDisponibilidade sinalizada!");
                    }
                    else {
                        if (taxis.get(id).getFilaDeEspera().isEmpty()) {
                            motoristas.get(email).setDisponibilidade(true);
                            System.out.println("\nDisponibilidade sinalizada!");
                        }
                        else {
                            n = taxis.get(id).getFilaDeEspera().size();
                            motoristas.get(email).setDisponibilidade(true);
                            System.out.println("\nDisponibilidade sinalizada!");
                            if (n==1)
                                System.out.println("\nTem 1 viagem em espera!");
                            else
                                System.out.println("\nTem "+n+" viagens em espera!");
                            System.out.println("\nRealize e registe estas viagens para\n"+
                                               "poder receber novas solicitacoes!");
                        }
                    }
                }
                else {
                    System.out.println("\nRegiste a viagem atual para " +
                                       "\npoder ficar disponivel!");
                }
            }
            else {
                System.out.println("\nAssocie-se a uma viatura para poder " +
                                   "\nficar disponivel!");
            }
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
            
    }
    
    public void inserirViatura(String email) {
        
        int     opcao, tipo = 0, id, x, y, temFila;
        double  v, pKm, pHora, f;
        boolean existe;
        Ponto2D pto;
        Taxi    l, c, m;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        do {
            System.out.println("\nQual o tipo de taxi:");
            System.out.println("1 - Ligeiro ou 2 - Carrinha ou 3 - Moto?");
            opcao = input.nextInt();
            if (opcao==1)
                tipo = 1;
            if (opcao==2)
                tipo = 2;
            if (opcao==3)
                tipo = 3;
            if (opcao!=1 && opcao!=2 && opcao!=3)
                System.out.println("\nTipo invalido! Por favor tente outra vez.");
        } while(opcao!=1 && opcao!=2 && opcao!=3);
        do {
            existe = false;
            System.out.println("\nID da viatura:");
            id = input.nextInt();
            if (id>0) {
                for(Taxi t: this.taxis.values()) {
                    if (t.getIDTaxi()==id) {
                        System.out.println("\nID ja existe! Por favor tente outra vez.");
                        existe = true;
                        break;
                    }
                }
            }
            else
                System.out.println("\nID invalido! Por favor tente outra vez.");
        } while(id<=0 || existe);
        do {
            System.out.println("\nVelocidade media por km: (Minimo 40km)");
            v = input.nextDouble();
            if (v<40)
                System.out.println("\nVelocidade invalida! Por favor tente outra vez.");
        } while(v<40);
        do {
            System.out.println("\nPreco base por km: (Minimo 0,20€)");
            pKm = input.nextDouble();
            if (pKm<0.2)
                System.out.println("\nPreco invalido! Por favor tente outra vez.");
        } while(pKm<0.2);
        do {
            System.out.println("\nPreco base por hora: (Minimo 10€)");
            pHora = input.nextDouble();
            if (pHora<10)
                System.out.println("\nPreco invalido! Por favor tente outra vez.");
        } while(pHora<10);
        do {
            System.out.println("\nFila de espera?");
            System.out.println("1 - SIM ou 0 - NAO");
            temFila = input.nextInt();
            if (temFila!=1 && temFila!=0)
                System.out.println("\nValor invalido! Por favor tente outra vez.");
        } while(temFila!=1 && temFila!=0);
        System.out.println("\nCoordenadas da viatura:");
        System.out.println("\nX:");
        x = input.nextInt();
        System.out.println("\nY:");
        y = input.nextInt();
        pto = new Ponto2D(x,y);
        List<Viagem> historico    = new ArrayList<Viagem>();
        List<Viagem> filaDeEspera = new ArrayList<Viagem>();
        if (tipo==1) {
            if (temFila==1)
                l = new Ligeiro(id,v,pKm,pHora,pto,historico,true,filaDeEspera);
            else
                l = new Ligeiro(id,v,pKm,pHora,pto,historico,false,filaDeEspera);
            taxis.put(l.getIDTaxi(),l);
            System.out.println("\nLigeiro inserido com sucesso!");
        }
        if (tipo==2) {
            if (temFila==1)
                c = new Carrinha(id,v,pKm,pHora,pto,historico,true,filaDeEspera);
            else
                c = new Carrinha(id,v,pKm,pHora,pto,historico,false,filaDeEspera);
            taxis.put(c.getIDTaxi(),c);
            System.out.println("\nCarrinha inserida com sucesso!");
        }
        if (tipo==3) {
            if (temFila==1)
                m = new Moto(id,v,pKm,pHora,pto,historico,true,filaDeEspera);
            else
                m = new Moto(id,v,pKm,pHora,pto,historico,false,filaDeEspera);
            taxis.put(m.getIDTaxi(),m);
            System.out.println("\nMoto inserida com sucesso!");
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
        
    }
    
    public void associarViatura(String email) {
        
        int     id = motoristas.get(email).getIDTaxi();
        boolean exist = false, assoc = false, cond = false;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        if (!motoristas.get(email).getViagem().getMotorista().equals(""))
            System.out.println("\nEsta a meio de uma viagem! Tente mais tarde.");
        else {
            System.out.println("\n     (Aperte 0 para cancelar)");
            System.out.println("\nQual o ID do taxi: (Inteiro positivo)");
            id = input.nextInt();
            if (id>0) {
                for(Taxi t: this.taxis.values()) {
                    if (t.getIDTaxi()==id) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    if (motoristas.get(email).getIDTaxi()==id)
                        System.out.println("\nJa se encontra associado a esta viatura!");
                    if (motoristas.get(email).getIDTaxi()!=id) {
                        for(Motorista m: this.motoristas.values()) {
                            if (m.getIDTaxi()==id) {
                                assoc = true;
                                break;
                            }
                        }
                        if (assoc)
                            System.out.println("\nViatura associada a outro motorista!");
                        if (!assoc) {
                            motoristas.get(email).setIDTaxi(id);
                            motoristas.get(email).setDisponibilidade(false);
                            System.out.println("\nViatura associada com sucesso!");
                        }
                    }
                }
                if (!exist)
                    System.out.println("\nViatura inexistente!");
            }
            if (id==0)
                System.out.println("\nAssociacao cancelada!");
            if (id<0)
                System.out.println("\nID invalido!");
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
            
    }
    
    public void registarViagem(String email) {
        
        int     id, idTaxi, fator, f, totalViagens;
        double  duracaoEstimada, duracaoReal, custoEstimado, custoReal, distancia, 
                fiabilidade, diferenca, kms, precoKm, precoHora, velocidade, 
                tempoAteCliente, distOaD, tempoOaD, distanciaTotal;
        String  cliente = "", motorista;
        Viagem  viagem  = new Viagem(), v = motoristas.get(email).getViagem();
        Ponto2D origem, destino, coords;
        Taxi    t;
        GregorianCalendar data;
        Scanner saida   = new Scanner(System.in);
        
        motorista = v.getMotorista();
        if (motorista.equals("")) {
            if (motoristas.get(email).getDisponibilidade()) {
                id = motoristas.get(email).getIDTaxi();
                if (!taxis.get(id).getTemFila())
                    System.out.println("\nNao tem nenhuma viagem a registar!");
                if (taxis.get(id).getTemFila() && taxis.get(id).getFilaDeEspera().isEmpty())
                    System.out.println("\nNao tem nenhuma viagem a registar!");
                if (taxis.get(id).getTemFila() && !taxis.get(id).getFilaDeEspera().isEmpty()) {
                    t = taxis.get(id);
                    motoristas.get(email).setDisponibilidade(false);
                    v = t.getFilaDeEspera().get(0);
                    cliente      = v.getCliente();
                    origem       = v.getOrigem();
                    destino      = v.getDestino();
                    // Verificar qual a data da viagem;
                    Calendar now = Calendar.getInstance();
                    int ano    = now.get(Calendar.YEAR);
                    int mes    = now.get(Calendar.MONTH);
                    int dia    = now.get(Calendar.DAY_OF_MONTH);
                    int hora   = now.get(Calendar.HOUR_OF_DAY);
                    int minuto = now.get(Calendar.MINUTE);
                    data = new GregorianCalendar(ano,mes,dia,hora,minuto);
                    // Obter dados relevantes da viatura;
                    coords          = t.getCoordenadas();
                    velocidade      = t.getVelocidade();
                    precoKm         = t.getPrecoKm();
                    precoHora       = t.getPrecoHora();
                    // Calcular distancia cliente-viatura;
                    int xc = origem.getX();
                    int yc = origem.getY();
                    distancia = Ponto2D.calculaDistancia(xc,yc,coords);
                    // Calcular tempo ate chegar ao cliente;
                    tempoAteCliente = distancia / velocidade;
                    // Calcular tempo ate chegar ao destino;
                    distOaD         = Ponto2D.calculaDistancia(xc,yc,destino);
                    tempoOaD        = distOaD / velocidade;
                    // Calcular e indicar o custo e tempo total de viagem estimados;
                    distanciaTotal     = distancia + distOaD;
                    duracaoEstimada = tempoAteCliente + tempoOaD;
                    custoEstimado      = (distanciaTotal * precoKm) + 
                                         (duracaoEstimada * precoHora);
                    fator        = motoristas.get(email).getFator();
                    kms          = motoristas.get(email).getKms();
                    totalViagens = motoristas.get(email).getTotalViagens();
                    idTaxi       = motoristas.get(email).getIDTaxi();
                    t            = taxis.get(idTaxi);
                    precoKm      = t.getPrecoKm();
                    precoHora    = t.getPrecoHora();
                    // Calcular a fiabilidade;
                    fiabilidade = Math.random() + 0.5;
                    // Calcular duracao real da viagem tendo em conta a fiabilidade da viatura;
                    duracaoReal = duracaoEstimada * fiabilidade;
                    // Caso seja necessario, calcular custo real;
                    diferenca = Math.abs(duracaoReal - duracaoEstimada);
                    if (diferenca<=(0.25*duracaoEstimada)) {
                        custoReal = (distancia   * precoKm) + 
                                    (duracaoReal * precoHora);
                        viagem = new Viagem(origem,destino,email,cliente,duracaoReal,custoReal,
                                            distanciaTotal,data);
                    }
                    if (diferenca>(0.25*duracaoEstimada))
                        viagem = new Viagem(origem,destino,email,cliente,duracaoReal,
                                            custoEstimado,distanciaTotal,data);
                    if (duracaoReal<=duracaoEstimada) {
                        f = (int) (((((fator/100)*totalViagens)+1) / (totalViagens+1)) * 100);
                        motoristas.get(email).setFator(f);
                    }
                    if (duracaoReal>duracaoEstimada) {
                        f = (int) (((((fator/100)*totalViagens)) / (totalViagens+1)) * 100);
                        motoristas.get(email).setFator(f);
                    }
                    motoristas.get(email).setTotalViagens(totalViagens+1);
                    motoristas.get(email).setKms(kms+distancia);
                    // Set coordenadas;
                    t.setCoordenadas(destino);
                    // Adicionar viagem ao historico do cliente;
                    List<Viagem> auxC = clientes.get(cliente).getHistorico();
                    auxC.add(viagem);
                    clientes.get(cliente).setHistorico(auxC);
                    // Adicionar viagem ao historico do motorista;
                    List<Viagem> auxM = motoristas.get(email).getHistorico();
                    auxM.add(viagem);
                    motoristas.get(email).setHistorico(auxM);
                    // Adicionar viagem ao historico da viatura;
                    List<Viagem> auxT = taxis.get(idTaxi).getHistorico();
                    auxT.add(viagem);
                    taxis.get(idTaxi).setHistorico(auxT);
                    // Remover viagem da fila de espera da viatura;
                    List<Viagem> auxF = taxis.get(idTaxi).getFilaDeEspera();
                    auxF.remove(0);
                    taxis.get(idTaxi).setFilaDeEspera(auxF);
                    System.out.println("\nViagem registada com sucesso!");
                    int n = t.getFilaDeEspera().size();
                    if (n==0)
                        System.out.println("\nJa pode receber solicitacoes!");
                    if (n==1)
                        System.out.println("\nAinda tem uma viagem em fila de espera!");
                    if (n>1)
                        System.out.println("\nAinda tem "+n+" viagens em fila de espera!");
                }
            } else
                System.out.println("\nNao esta disponivel para fazer viagens!");
        }
        else {
            cliente      = v.getCliente();
            origem       = v.getOrigem();
            destino      = v.getDestino();
            data         = v.getData();
            fator        = motoristas.get(email).getFator();
            kms          = motoristas.get(email).getKms();
            totalViagens = motoristas.get(email).getTotalViagens();
            idTaxi       = motoristas.get(email).getIDTaxi();
            t            = taxis.get(idTaxi);
            precoKm      = t.getPrecoKm();
            precoHora    = t.getPrecoHora();
            // Set coordenadas;
            t.setCoordenadas(destino);
            // Calcular valores pre-viagem;
            custoEstimado   = v.getCusto();
            duracaoEstimada = v.getDuracao();
            distancia       = v.getDistancia();
            // Calcular a fiabilidade;
            fiabilidade = Math.random() + 0.5;
            // Calcular duracao real da viagem tendo em conta a fiabilidade da viatura;
            duracaoReal = duracaoEstimada * fiabilidade;
            // Caso seja necessario, calcular custo real;
            diferenca = Math.abs(duracaoReal - duracaoEstimada);
            if (diferenca<=(0.25*duracaoEstimada)) {
                custoReal = (distancia   * precoKm) + 
                            (duracaoReal * precoHora);
                viagem = new Viagem(origem,destino,email,cliente,duracaoReal,custoReal,distancia,data);
            }
            if (diferenca>(0.25*duracaoEstimada))
                viagem = new Viagem(origem,destino,email,cliente,duracaoReal,custoEstimado,distancia,data);
            if (duracaoReal<=duracaoEstimada) {
                f = (int) (((((fator/100)*totalViagens)+1) / (totalViagens+1)) * 100);
                motoristas.get(email).setFator(f);
            }
            if (duracaoReal>duracaoEstimada) {
                f = (int) (((((fator/100)*totalViagens)) / (totalViagens+1)) * 100);
                motoristas.get(email).setFator(f);
            }
            motoristas.get(email).setTotalViagens(totalViagens+1);
            motoristas.get(email).setKms(kms+distancia);
            // Adicionar viagem ao historico do cliente;
            List<Viagem> auxC = clientes.get(cliente).getHistorico();
            auxC.add(viagem);
            clientes.get(cliente).setHistorico(auxC);
            // Adicionar viagem ao historico do motorista;
            List<Viagem> auxM = motoristas.get(email).getHistorico();
            auxM.add(viagem);
            motoristas.get(email).setHistorico(auxM);
            // Adicionar viagem ao historico da viatura;
            List<Viagem> auxT = taxis.get(idTaxi).getHistorico();
            auxT.add(viagem);
            taxis.get(idTaxi).setHistorico(auxT);
            // Reset da viagem do motorista;
            Viagem v2 = new Viagem();
            motoristas.get(email).setViagem(v2);
            System.out.println("\nViagem registada com sucesso!");
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
            
    }
    
    public void viagensEfetuadasMotorista(String email) {
        
        int dia, mes, ano, i, xo, yo, xd, yd, hora, minuto;
        String cliente;
        boolean cond1, cond2, sg = false;
        GregorianCalendar data1, data2, dataV;
        Viagem v;
        List<Viagem> viagens;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        // Introduçao da primeira data;
        System.out.println("\nIntrouza a primeira data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        do {
            System.out.println("\nDia:    (1-31)");
            dia = input.nextInt();
            if (dia==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data1;
        data1 = new GregorianCalendar(ano,mes,dia);
        // Introduçao da segunda data;
        System.out.println("\nIntrouza a segunda data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        System.out.println("\nDia:    (1-31)");
        dia = input.nextInt();
        do {
            if (dia==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nOperacao cancelada!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data2;
        data2 = new GregorianCalendar(ano,mes,dia);
        if (data1.before(data2)) {
            viagens = motoristas.get(email).getHistorico();
            v       = new Viagem();
            System.out.println("\nViagens realizadas:\n");
            for(i=0; i<viagens.size(); i++) {
                v     = viagens.get(i);
                dataV = v.getData();
                cond1 = dataV.after(data1);
                cond2 = dataV.before(data2);
                if (cond1) {
                    if (cond2) {
                        sg = true;
                        xo = v.getOrigem().getX();
                        yo = v.getOrigem().getY();
                        xd = v.getDestino().getX();
                        yd = v.getDestino().getY();
                        cliente  = v.getCliente();
                        String iS  = String.format("%2d", i);
                        String xoS = String.format("%2d", xo);
                        String yoS = String.format("%2d", yo);
                        String xdS = String.format("%2d", xd);
                        String ydS = String.format("%2d", yd);
                        dia    = dataV.get(Calendar.DAY_OF_MONTH);
                        mes    = dataV.get(Calendar.MONTH);
                        ano    = dataV.get(Calendar.YEAR);
                        hora   = dataV.get(Calendar.HOUR_OF_DAY);
                        minuto = dataV.get(Calendar.MINUTE);
                        System.out.println(iS+".    Cliente: "+cliente);
                        System.out.println("       Origem:  X - "+xoS+" Y - "+yoS);
                        System.out.println("       Destino: X - "+xdS+" Y - "+ydS);
                        System.out.println("       Data: "+dia+"/"+mes+"/"+ano+"  -  "+
                                            hora+":"+minuto+"\n");
                    }
                    else
                        break;
                }
            }
            if (!sg)
                System.out.println("Nao realizou qualquer viagem neste periodo!\n");
        }
        else
            System.out.println("\nSegunda data e anterior a primeira!");
        System.out.println(" (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
        return;                        
        
    }

    // Metodos do Menu estatisticas;
    public void totalFaturadoPorViatura() {
        
        int dia, mes, ano,i,id;
        double total = 0.0;
        boolean cond1, cond2, sg = false;
        GregorianCalendar data1, data2, dataV;
        Viagem v;
        String res;
        List<Viagem> viagens;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in); 
        
        // Introduçao da primeira data;
        System.out.println("\nIntroduza o ID da viatura pretendida:");
        id = input.nextInt();
        if(!taxis.containsKey(id)){
            System.out.println("\nTaxi nao existe");
            return;
        }
        System.out.println("\nIntrouza a primeira data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        do {
            System.out.println("\nDia:    (1-31)");
            dia = input.nextInt();
            if (dia==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data1;
        data1 = new GregorianCalendar(ano,mes,dia);
        // Introduçao da segunda data;
        System.out.println("\nIntrouza a segunda data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        System.out.println("\nDia:    (1-31)");
        dia = input.nextInt();
        do {
            if (dia==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data2;
        data2 = new GregorianCalendar(ano,mes,dia);
        if (data1.before(data2)) {
            viagens = taxis.get(id).getHistorico();
            for(i=0; i<viagens.size(); i++) {
                v     = viagens.get(i);
                dataV = v.getData();
                cond1 = dataV.after(data1);
                cond2 = dataV.before(data2);
                if (cond1) {
                    if (cond2) {
                        sg = true;
                        total  += v.getCusto();
                    }
                    else
                        break;
                }
            }
            if (!sg){
                System.out.println("\nNao realizou qualquer viagem neste periodo!");
            }
            else {
                DecimalFormat df = new DecimalFormat("###,##0.00");
                res = df.format(total);
                System.out.println("\nTotal Faturado: " + res + " €\n");
            }
        }
        else
            System.out.println("\nSegunda data e anterior a primeira!");
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();     
        
    }
    
    public void totalFaturadoUMeR() {
        
        int dia, mes, ano,i,id;
        double total = 0.0;
        boolean cond1, cond2, sg = false;
        GregorianCalendar data1, data2, dataV;
        Viagem v;
        String res = "";
        List<Viagem> viagens;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in); 
        
        // Introduçao da primeira data;
        System.out.println("\nIntrouza a primeira data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        do {
            System.out.println("\nDia:    (1-31)");
            dia = input.nextInt();
            if (dia==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data1;
        data1 = new GregorianCalendar(ano,mes,dia);
        // Introduçao da segunda data;
        System.out.println("\nIntrouza a segunda data:");
        System.out.println("\n(Aperte 0 para cancelar)");
        System.out.println("\nDia:    (1-31)");
        dia = input.nextInt();
        do {
            if (dia==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                      
            }
            if (dia<0 || dia>31)
                System.out.println("\nDia invalido! Por favor tente outra vez.");
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                       
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        // Definir data2;
        data2 = new GregorianCalendar(ano,mes,dia);
        if (data1.before(data2)) {
            for(Taxi t: taxis.values()) {
                viagens = t.getHistorico();
                for(i=0; i<viagens.size(); i++) {
                    v     = viagens.get(i);
                    dataV = v.getData();
                    cond1 = dataV.after(data1);
                    cond2 = dataV.before(data2);
                    if (cond1) {
                        if (cond2) {
                            sg = true;
                            total  += v.getCusto();
                        }
                        else
                            break;
                    }
                }
                if (!sg){
                   System.out.println("\nNao foram realizadas quaisquer viagens neste periodo!");
                }
                else {
                    DecimalFormat df = new DecimalFormat("###,##0.00");
                    res = df.format(total);
                    System.out.println("\nTotal Faturado pela UMeR: " + res + " €\n");   
                }
            }
        }
        else
            System.out.println("\nSegunda data e anterior a primeira!");
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
        
    }
    
    public void clientesQueMaisGastam() {
        
        List<Cliente> tmp = new ArrayList<Cliente>();
        
        tmp = clientes.values().stream()
                         .sorted(new ComparatorClientePago()) 
                         .limit(10) 
                         .collect(Collectors.toList());
        System.out.println("\nClientes que mais gastam:\n");
        for(Cliente c: tmp)
            System.out.println("\t" + c.getTotalPago() + "  -  " + c.getEmail());
        
    }
    
    public void motoristasComMenorPrecisao() {
        
        List<Motorista> tmp = new ArrayList<Motorista>();  
        
        tmp = motoristas.values().stream()
                         .sorted(new ComparatorMotoristaFator()) 
                         .limit(5) 
                         .collect(Collectors.toList());
        System.out.println("\nMotoristas com menor precisao:\n");
        for(Motorista m: tmp)
            System.out.println("\t" + m.getFator() + "  -  " + m.getEmail());
        
    }
    
    // Metodos para gravar/carregar em/de ficheiro;
    public String toString() {
  
        StringBuilder sb = new StringBuilder("\n********************************************************");  
        sb.append("\n********************                ********************");
        sb.append("\n********************    Clientes    ********************");
        sb.append("\n********************                ********************");
        sb.append("\n********************************************************\n\n");
        sb.append(this.clientes.values()
                               .stream()
                               .map(Cliente::toString)
                               .collect(joining("\n")));
        sb.append("\n********************************************************");
        sb.append("\n*******************                  *******************");
        sb.append("\n*******************    Motoristas    *******************");
        sb.append("\n*******************                  *******************");
        sb.append("\n********************************************************\n\n");
        sb.append(this.motoristas.values()
                                 .stream()
                                 .map(Motorista::toString)
                                 .collect(joining("\n")));
        return sb.toString();
        
    }

    public void log(String f, boolean ap) throws IOException {
        
        FileWriter fw = new FileWriter(f, ap);
        fw.write("\n---------- LOG - LOG - LOG -- LOG - LOG - LOG ----------\n");
        fw.write(this.toString());
        fw.write("\n---------- LOG - LOG - LOG -- LOG - LOG - LOG ----------\n");
        fw.flush();
        fw.close();
        
    }
    
    public void guardaApp() {
        
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("estado.txt")); 
            oos.writeObject(this);
            oos.flush(); 
            oos.close();
        } catch(IOException i) {
            System.out.println(i.getMessage());
        }
        
    }
    
    public static UMeR initApp() {
        
        UMeR umer = new UMeR();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("estado.txt"));
            umer = (UMeR) ois.readObject();
            ois.close();
            return umer;
        } catch(IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return umer;
        
    }
    
    public void fechaSessao() {
        
        try {
            this.guardaApp();
            this.log("log.txt", true);
        }
        catch (IOException e) {
            System.out.println("\nNao foi possivel guardar os dados!");
        }
        System.out.println("\nObrigado! Ate breve...");
        
    }
   
}
