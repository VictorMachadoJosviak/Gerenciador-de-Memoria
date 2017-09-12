package main;

import java.util.Scanner;

import javax.inject.Inject;

import abstractions.IMemoryManager;
import abstractions.ManagerBase;

public class Gerenciador extends ManagerBase{

	private Scanner scan = new Scanner(System.in);

    private  IMemoryManager processo = null;
    
    private static Gerenciador gerenciador;
      
    public static Gerenciador getInstance() {
    	if(gerenciador == null)
    		gerenciador = new Gerenciador();
    	return gerenciador;
    }
  
	@Override
	public void OnCreate() {

		try {
			
			System.out.println("Tamanho da memoria");
			int opcao = scan.nextInt();
			processo = new Processo(opcao);
			OnCreateOptionsMenu();
			
			do {
				opcao = scan.nextInt();

				switch (opcao) {
				case 1:
					CriarProcesso();
					break;					
				case 2:
					MatarProcesso();
					break;
				case 3:
					ListarProcesso();
					break;					
				case 0:
					System.out.println("Programa fechado ;)");
					System.exit(0);
					break;
				default:
					System.out.println("OOOPS! Opção inválida");
					break;
				}

			} while (opcao != 0);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}

	}
	
	
	public void CriarProcesso() {
		try {
			System.out.print("Digite o tamanho do processo: ");
			int tamanho = scan.nextInt();
			processo.Create(tamanho);
			OnCreateOptionsMenu();
		} catch (Exception e) {			
			System.out.println("tamnho inválido");
			// TODO: handle exception
		}
	}
	
	public void MatarProcesso() {
		try {
			System.out.println("Digite o pid: ");
			int pid = scan.nextInt();
		 	processo.KillProcess(pid);
			OnCreateOptionsMenu();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("algo de errado não esta certo");
		}
	}
	
	public void ListarProcesso() {
		try {
			System.out.println(processo.ToList());
			OnCreateOptionsMenu();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void OnCreateOptionsMenu() {

		System.out.println(" _______________________");
		System.out.println("|___|___________________|");
		System.out.println("|   |                   |");
		System.out.println("| 1 | Criar Processo    |");
		System.out.println("| 2 | Matar Processo    |");
		System.out.println("| 3 | Listar Processo   |");
		System.out.println("| 0 | Sair              |");
		System.out.println("|___|___________________|");

	}

}
