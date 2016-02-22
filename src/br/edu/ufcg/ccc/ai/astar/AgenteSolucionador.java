package br.edu.ufcg.ccc.ai.astar;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import br.edu.ufcg.ccc.ai.puzzle.Movimentos;
import br.edu.ufcg.ccc.ai.puzzle.Tabuleiro;

public class AgenteSolucionador {

	private List<Tabuleiro> filaPrioridade;
	private Set<Tabuleiro> listaExpandidos;
	private boolean resolveu;
	
	public AgenteSolucionador(Tabuleiro inicial) {
		
		this.filaPrioridade = new LinkedList<Tabuleiro>();
		this.listaExpandidos = new HashSet<Tabuleiro>();
		this.resolveu = false;
		
		this.filaPrioridade.add(inicial);
	}
	
	private void resolvePasso() {
		
		if (this.filaPrioridade.size() == 0) {
			
			throw new NullPointerException("A fila esvaziou e a solução não chegou.");
		}
		
		Tabuleiro atual = this.filaPrioridade.get(0);
						
		if (atual.atingiuObjetivo()) {
			this.resolveu = true;
			return;
		}
		
		this.filaPrioridade.remove(0);
		
		for (Movimentos m : atual.getMovimentosPossiveis()) {
			
			Tabuleiro aux = atual.getClone().movimentaCasaVazia(m);
			
			if (!this.listaExpandidos.contains(aux)) {
								
				this.insereOrdenado(aux);				
			}
		}
		
		this.listaExpandidos.add(atual);
	}
	
	private void insereOrdenado(Tabuleiro aux) {

		Iterator<Tabuleiro> i = this.filaPrioridade.iterator();
		
		int count = 0;
		while (i.hasNext()) {
			
			Tabuleiro t = i.next();
			
			if (t.getDistanciaManhattan() + t.getNumeroMovimentosFeitos() >= aux.getDistanciaManhattan() + aux.getNumeroMovimentosFeitos()) {
				break;
			}
			
			count++;
		}
		
		this.filaPrioridade.add(count, aux);
	}

	public Tabuleiro getEstadoAtual() {
		
		return this.filaPrioridade.get(0);
	}

	public void resolve() {
		
		while (!this.resolveu) {
			
			this.resolvePasso();			
		}		
	}
}