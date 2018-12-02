import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Menu {
    
    // variaveis de instância
    private List<String> opcoes;
    private int op;
    
    /**
     * Constructor for objects of class Menu
     */
    public Menu(String[] opcoes) {
        
        this.opcoes = Arrays.asList(opcoes);
        this.op     = 0;
        
    }

    /**
     * Metodo para apresentar o menu e ler uma opcao.
     */
    public void executa() {
        
        do {
            showMenu();
            this.op = lerOpcao();
        } while (this.op == -1);
        
    }
    
    /** 
     * Apresentar o menu
     */
    private void showMenu() {
        
        System.out.println("\n* * * * * * UMeR * * * * * *\n");
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print(i+1);
            System.out.print(" - ");
            System.out.println(this.opcoes.get(i));
        }
        System.out.println("\n0 - Sair/Voltar\n");
        
    }
    
    /** 
     * Ler uma opcao valida
     */
    private int lerOpcao() {
        
        int op; 
        Scanner is = new Scanner(System.in);
        
        System.out.print("Opcao: ");
        try {
            op = is.nextInt();
        }
        catch (InputMismatchException e) { // Nao foi inscrito um int
            op = -1;
        }
        if (op<0 || op>this.opcoes.size()) {
            System.out.println("\nOpcao Invalida!!!");
            op = -1;
        }
        
        return op;
        
    }
    
    /**
     * Metodo para obter a ultima opcao lida
     */
    public int getOpcao() {
        
        return this.op;
        
    }
    
}
