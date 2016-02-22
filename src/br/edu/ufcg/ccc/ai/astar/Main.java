package br.edu.ufcg.ccc.ai.astar;

import br.edu.ufcg.ccc.ai.puzzle.Movimentos;
import br.edu.ufcg.ccc.ai.puzzle.Tabuleiro;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		
		Tabuleiro t = new Tabuleiro();
		Tabuleiro clone = t.getClone();
		
		AgenteSolucionador agente = new AgenteSolucionador(t);
		
		long ini = System.currentTimeMillis();
		agente.resolve();
		long fim = System.currentTimeMillis();
		
		System.out.println("Estado inicial\n" + clone.toString());
		
		int count = 0;
		for (Character c : agente.getEstadoAtual().getMovimentosFeitos().toCharArray()) {
			
			if (c == 'C') {
				clone.movimentaCasaVazia(Movimentos.Cima);
			}
			else if (c == 'B') {
				clone.movimentaCasaVazia(Movimentos.Baixo);
			}
			else if (c == 'E') {
				clone.movimentaCasaVazia(Movimentos.Esquerda);
			}
			else if (c == 'D') {
				clone.movimentaCasaVazia(Movimentos.Direita);
			}
				
			
			System.out.println("Passo " + ++count + "\n" + clone.toString() );
		}
		
		System.out.println("Solução em " + (fim - ini) + " milisegundos");
	}
}