import java.util.Scanner;
import java.util.GregorianCalendar;
import java.io.*;
import java.util.*;
import java.lang.String;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.joining;

/**
 * Escreva a descricao da classe UMeR aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class UMeRApp {
   
    // Construtor privado
    private UMeRApp(){};
    
    // Umer
    private static UMeR umer;

    // Menus da aplicacao
    private static Menu menuInicial, menuCliente, menuMotorista, menuEstatisticas;
   
    // Metodo prinipal
    public static void main(String[] args) {
        
        String loggedUser;
        
        umer = UMeR.initApp();
        carregarMenus(); 
        
        do {
            loggedUser = null;
            menuInicial.executa();
          
            switch(menuInicial.getOpcao()){
                case 1:
                    // Iniciar Sessao Cliente
                    loggedUser = login(1); 
                    if(loggedUser!=null)
                        menuCliente(loggedUser);
                    // realizar 1 viagem em espera em todos os taxis com lista;
                    break;
                case 2:
                    // Iniciar Sessao Motorista
                    loggedUser = login(2);
                    if(loggedUser!=null)
                        menuMotorista(loggedUser);
                    // realizar 1 viagem em espera em todos os taxis com lista;
                    break;
                case 3:
                    // Registar
                    registo();
                    break;
                case 4:
                    // Estatisticas
                    menuEstatisticas();
                    break;
            }
        } while(menuInicial.getOpcao()!=0);
        umer.fechaSessao();
        
    }
    
    private static void carregarMenus(){
        
        String[] ops1 = {"Iniciar Sessao Cliente",
                         "Iniciar Sessao Motorista",
                         "Registar",
                         "Estatisticas"};
        
        String[] ops2 = {"Ver Taxis Disponiveis",
                         "Solicitar Viagem",
                         "Fazer Reserva",
                         "Avaliar Ultimo Motorista",
                         "Ver Viagens Efetuadas"};
                         
        String[] ops3 = {"Sinalizar Disponibilidade",
                         "Inserir Viatura",
                         "Associar Viatura",
                         "Registar Viagem",
                         "Ver Viagens Efetuadas"};
                         
        String[] ops4 = {"Total Faturado Por Viatura Especifica",
                         "Total Faturado Pela UMeR",
                         "Clientes Que Mais Gastam",
                         "Motoristas Com Menor Precisao De Precos"};
        
        menuInicial      = new Menu(ops1);
        menuCliente      = new Menu(ops2);
        menuMotorista    = new Menu(ops3);
        menuEstatisticas = new Menu(ops4);
        
    }
    
    private static String login(int a) {
        
        String email,nome,pass,morada,dataNasc;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        System.out.println("\n* * * * *  LOGIN  * * * * *");
        System.out.println("\nE-mail:");
        email = input.nextLine();
        System.out.println("\nPassword:");
        pass = input.nextLine();
        try{
            umer.iniciaSessao(email,pass,a);
            return email;
        }catch(SemAutorizacaoException e) {
            System.out.println("\n"+e.getMessage());
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
        return null;
        
    }
    
    private static void registo() {
        
        String email, nome, pass, morada, sopcao;
        int dia, mes, ano, opcao;
        GregorianCalendar nascimento;
        Scanner input = new Scanner(System.in);
        Scanner saida = new Scanner(System.in);
        
        System.out.println("\n* * * * * REGISTO * * * * *");
        System.out.println("\n (Aperte 0 para cancelar)");
        do { 
            System.out.println("\nEmail: ");
            email = input.nextLine();
            if (email.equals("0")) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                        /* go back */
            }
            if (email.trim().isEmpty() || email==null)
                System.out.println("Email invalido! Por favor tente outra vez.");
        } while(email.trim().isEmpty() || email==null);
        do {
            System.out.println("\nNome: ");
            nome = input.nextLine();
            if (nome.equals("0")) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                        /* go back */
            }
            if (nome.trim().isEmpty() || nome==null)
                System.out.println("Nome invalido! Por favor tente outra vez.");
        } while(nome.trim().isEmpty() || nome==null);
        do { 
            System.out.println("\nPassword: ");
            pass = input.nextLine();
            if (pass.equals("0")) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                        /* go back */
            }
            if (pass.trim().isEmpty() || pass==null)
                System.out.println("Password invalida! Por favor tente outra vez.");
        } while(pass.trim().isEmpty() || pass==null);
        do { 
            System.out.println("\nMorada: ");
            morada = input.nextLine();
            if (morada.equals("0")) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                return;                        /* go back */
            }
            if (morada.trim().isEmpty() || morada==null)
                System.out.println("Morada invalida! Por favor tente outra vez.");
        } while(morada.trim().isEmpty() || morada==null);
        do { 
            System.out.println("\nDia de nascimento:    (1-31)");
            dia = input.nextInt();
            if (dia==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                input.close();
                return;                        /* go back */
            }
            if (dia<0 || dia>31) {
                System.out.println("\nDia invalido! Por favor tente outra vez.");
            }
        } while(dia<0 || dia>31);
        do { 
            System.out.println("\nMes de nascimento:    (1-12)");
            mes = input.nextInt();
            if (mes==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                input.close();
                return;                        /* go back */
            }
            if (mes<0 || mes>12)
                System.out.println("\nMes invalido! Por favor tente outra vez.");
        } while(mes<0 || mes>12);
        do { 
            System.out.println("\nAno de nascimento:");
            ano = input.nextInt();
            if (ano==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                input.close();
                return;                        /* go back */
            }
            if (ano<0 || Integer.toString(ano).length()!=4)
                System.out.println("\nAno invalido! Por favor tente outra vez.");
        } while(ano<0 || Integer.toString(ano).length()!=4);
        do {
            System.out.println("\n1 - Cliente ou 2 - Motorista?");
            opcao = input.nextInt();
            if (opcao==0) {
                System.out.println("\nRegisto cancelado!");
                System.out.println("\n (Pressione para continuar)");
                saida.nextLine();
                saida.close();
                input.close();
                return;                        /* go back */
            }
            if (opcao<0 || opcao>2)
                System.out.println("\nOpcao invalida! Por favor tente outra vez.");
        } while(opcao<0 || opcao>2);

        nascimento = new GregorianCalendar(ano,mes,dia);
        
        if(opcao==1) {
            Cliente c = new Cliente(email,nome,pass,morada,nascimento);
            try{
                umer.registarAtor(c);
            }catch(UtilizadorExistenteException e){
                System.out.println(e.getMessage());
            }
        }else{
            Motorista m = new Motorista(email, nome, pass, morada, nascimento);
            try{
                umer.registarAtor(m);
            }catch(UtilizadorExistenteException e){
                System.out.println(e.getMessage());
            }
        }
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        input.close();
        
    }
    
    private static void menuCliente(String email) {
        
        Scanner saida = new Scanner(System.in);
        
        do {
            menuCliente.executa();
            
            switch(menuCliente.getOpcao()){
                case 1:
                    // Ver Taxis Disponiveis
                    umer.verTaxisDisponiveis();
                    break;
                case 2:
                    // Solicitar Viagem
                    umer.solicitarViagem(email);
                    break;
                case 3:
                    // Fazer Reserva
                    umer.fazerReserva(email);
                    break;
                case 4:
                    // Avaliar ultima viagem;
                    umer.avaliarViagem(email);
                    break;
                case 5:
                    // Ver Viagens Efetuadas
                    umer.viagensEfetuadasCliente(email);
                    break;
            }
        } while(menuCliente.getOpcao() !=0);
        System.out.println("\nLog out efetuado com sucesso!");
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        
    }
    
    private static void menuMotorista(String email) {
        
        Scanner saida = new Scanner(System.in);
        
        do {
            menuMotorista.executa();
            
            switch(menuMotorista.getOpcao()){
                case 1:
                    // Sinalizar Disponibilidade
                    umer.setDisponivel(email);
                    break;
                case 2:                                      
                    // Criar/Inserir Viatura
                    umer.inserirViatura(email);
                    break;
                case 3:
                    // Associar Viatura
                    umer.associarViatura(email);
                    break;    
                case 4:                                        
                    // Registar Viagem
                    umer.registarViagem(email);
                    break;
                case 5:                 
                    // Ver Viagens Efetuadas
                    umer.viagensEfetuadasMotorista(email);
                    break;    
                               }
        } while(menuMotorista.getOpcao() !=0);
        System.out.println("\nLog out efetuado com sucesso!");
        System.out.println("\n (Pressione para continuar)");
        saida.nextLine();
        saida.close();
        
    }
    
    private static void menuEstatisticas() {
        
        do {
            menuEstatisticas.executa();
            
            switch(menuEstatisticas.getOpcao()){
                case 1:                                      
                    // Total Faturado Por Viatura Especifica
                    umer.totalFaturadoPorViatura();
                    break;
                case 2:
                    // Total Faturado Pela UMeR
                    umer.totalFaturadoUMeR();
                    break;
                case 3:
                    // Clientes Que Mais Gastam (10)
                    umer.clientesQueMaisGastam();
                    break;
                case 4:
                    // Motoristas Com Menor Precisao De Precos (5)
                    umer.motoristasComMenorPrecisao();
                    break;
                               }
        } while(menuMotorista.getOpcao() !=0);
        
    }    
         
}
