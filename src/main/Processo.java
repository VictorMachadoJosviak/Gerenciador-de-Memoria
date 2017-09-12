package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import abstractions.IMemoryManager;
import model.MemCheia;
import model.MemLivre;
import model.Memoria;


public class Processo implements IMemoryManager {

	private int tamanhoMemoria;
	
	private LinkedList<Memoria> memorias;

	private int currentPid = 0;
	
	public Processo(int tamanho) {
		init(tamanho);
	}

	@Override
	public String ToList() {
		
		StringBuilder builder = new StringBuilder("*-");
		
		for (Memoria m : memorias) {
			
			if ((m instanceof MemLivre)) {
				builder.append(
						" [L|"	+ m.getInicio()
				                + "|" 
						        + m.getTamanho()
								+ "] -"
			     );
			} else {
				builder.append(
				  " [P" + ((MemCheia) m).getPid()
						+ "|" 
						+ m.getInicio() 
						+ "|"
						+ m.getTamanho() 
						+ "] -"
				);
			}
		}
		builder.append(".");
		
		return builder.toString();
	}

	@Override
	public void Create(int size) {
		// TODO Auto-generated method stub
		try {
			if (size < 1) {
				System.out.println("tamnho invalildo");
			} else {

				ListIterator<Memoria> nl = fisFit(memorias, size);

				MemLivre livre = (MemLivre) nl.next();

				MemCheia cheia = new MemCheia();
				cheia.setInicio(livre.getInicio());
				cheia.setPid(getCurrentPid());
				cheia.setTamanho(size);

				nl.set(cheia);

				if (cheia.getTamanho() < livre.getTamanho()) {
					livre.setTamanho(livre.getTamanho() - cheia.getTamanho());
					livre.setInicio(cheia.getInicio() + cheia.getTamanho());
					nl.add(livre);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("tamanho invalido");
		}

	}
	
	

	@Override
	public void KillProcess(int pid) {
		// TODO Auto-generated method stub
		try {
			
			 ListIterator<Memoria> list = memorias.listIterator();
			 
			   MemCheia memcheia = getMemCheia(list, pid);			   
			    if (memcheia != null) {
			    	
			     	MemLivre memlivre = new MemLivre();
			    	memlivre.setInicio(memcheia.getInicio());
			    	memlivre.setTamanho(memcheia.getTamanho());
			    	
			    	verificaRemove(list,memlivre);	
			    	
			    	
			    	
			    	list.next();
			    	list.set(memlivre);
			    	
			    	if (list.hasNext()) {
			    		Memoria mem = (Memoria)list.next();
			    		if ((mem instanceof MemLivre)) {
			    			memlivre.setTamanho(memlivre.getTamanho() + mem.getTamanho());
			    			list.remove();
			    		}
			    	}
			    }
			    else {			    	
			    	System.out.println("oops! pid nao encontrado");			   
			    }
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	 public boolean validarGambi(String str, int pid){
	        for (int i = 0; i <= 9; i++) {
	            String compare = "";
	            for (int j = 0; j < pid; j++) {
	                compare+=i;
	            }            
	            if(compare.equals(str)) {
	                return true;
	            }            
	        }
	        return false;
	    }

	
	public boolean gambiTeste(String pid) {
        if (pid == null || pid.length() != 14 || validarGambi(pid,14))
            return false;
        try {
            Long.parseLong(pid);
        } catch (NumberFormatException e) {
            return false;
        }
        int soma = 0;
        String calc = pid.substring(0, 12);
        char chr[] = pid.toCharArray();        
        for (int i = 0; i < 4; i++) 
            if (chr[i] - 48 >= 0 && chr[i] - 48 <= 9) 
                soma += (chr[i] - 48) * (6 - (i + 1));           
        for (int i = 0; i < 8; i++) 
            if (chr[i + 4] - 48 >= 0 && chr[i + 4] - 48 <= 9) 
                soma += (chr[i + 4] - 48) * (10 - (i + 1));        
        int dig = 11 - soma % 11;
        calc = (new StringBuilder(String.valueOf(calc)))
        		.append(dig != 10 && dig != 11 ? Integer.toString(dig) : "|").toString();
        soma = 0;
        for (int i = 0; i < 5; i++) 
            if (chr[i] - 48 >= 0 && chr[i] - 48 <= 9) 
                soma += (chr[i] - 48) * (7 - (i + 1));        
        for (int i = 0; i < 8; i++) 
            if (chr[i + 5] - 48 >= 0 && chr[i + 5] - 48 <= 9) 
                soma += (chr[i + 5] - 48) * (10 - (i + 1));        
        dig = 11 - soma % 11;
        calc = (new StringBuilder(String.valueOf(calc)))
        		.append(dig != 10 && dig != 11 ? Integer.toString(dig) : "[").toString();
        if (!pid.equals(calc))
            return false;
        return true;
    }

	
	public void setItem(List<Memoria> dataEntity, Memoria item) {
	    int itemIndex = dataEntity.indexOf(item);
	    if (itemIndex != -1) {
	        dataEntity.set(itemIndex, item);
	    }
	}
	
	private void verificaRemove(ListIterator<Memoria> list, MemLivre livre) {
		try {
			
			if (list.hasPrevious()) {
				
	    		Memoria memoria = (Memoria)list.previous();
	    		if ((memoria instanceof MemLivre)) {
	    			
	    			Memoria aux = (MemLivre)memoria;	    			
	    			livre.setInicio(aux.getInicio());
	    			
	    			livre.setTamanho(livre.getTamanho() + aux.getTamanho());
	    			list.remove();
	    		}	    		
	    		else {
	    			list.next();
	    		}
	    	}
	    	
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private MemCheia getMemCheia(ListIterator<Memoria> list, int pid) {
		try {

			MemCheia mc = null;
			while (list.hasNext()) {
				
				Memoria nodo = (Memoria) list.next();
				if ((nodo instanceof MemCheia)) {
					
					MemCheia aux = (MemCheia) nodo;
					if (aux.getPid() == pid) {
						mc = aux;
						list.previous();
						break;
					}
				}
			}
			return mc;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	private ListIterator<Memoria> fisFit(List<Memoria> memorias, int size) {
		try {
			
			ListIterator<Memoria> lista = memorias.listIterator();

			while (lista.hasNext()) {

				Memoria m = lista.next();				
				if (((m instanceof MemLivre))
						&&
						   (m.getTamanho() >= size)) {
					lista.previous();					
					return lista;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			//System.out.println(e.getMessage());
			System.out.println("espaço insuficiente");
			return null;
		}
	   return null;
	}
	
	public int getCurrentPid() {		
		return currentPid += 1;
	}

	public void setCurrentPid(int currentPid) {
		this.currentPid = currentPid;
	}
	
	public void resetProps() {
		init(tamanhoMemoria);
	}
	
	public void init(int tamanhoMemoria) {
		this.tamanhoMemoria = tamanhoMemoria;
		memorias = new LinkedList<Memoria>();
		MemLivre m = new MemLivre();
		m.setInicio(0);
		m.setTamanho(tamanhoMemoria);
		memorias.add(m);
	}

}
