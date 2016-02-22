package br.edu.ufcg.ccc.ai.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tabuleiro {
	
	// indice invertido
	private static final int[][] linhasColunasCasas = {{0,0}, {0,1}, {0,2}, {1,0}, {1,1}, {1,2}, {2,0}, {2,1}, {2,2}};

	// isso é o tabuleiro
	private int[][] casas;
	private int linhaVazio;
	private int colunaVazio;
	
	private String movimentosFeitos;
	
	/**
	 * Construtor
	 */
	public Tabuleiro () {
		
		// 8-puzzle: 9 casas
		this.casas = new int[3][3];
		
		this.preencheCasas();
		
		this.linhaVazio = 2;
		this.colunaVazio = 2;
		
		this.movimentosFeitos = "";
	}
	
	/**
	 * Construtor privado para clonagem
	 */
	private Tabuleiro(boolean vazio) { // o parâmetro de nada serve
		
		// 8-puzzle: 9 casas
		this.casas = new int[3][3];
	}
	
	/**
	 * Preenche o tabuleiro aleatoriamente
	 */
	private void preencheCasas() {
		
		// coloca as peças em ordem numa lista
		List<Integer> pecasLista = new ArrayList<Integer>();
		pecasLista.add(1);
		pecasLista.add(2);
		pecasLista.add(3);
		pecasLista.add(4);
		pecasLista.add(5);
		pecasLista.add(6);
		pecasLista.add(7);
		pecasLista.add(8);
					
		// embaralha as peças
		Collections.shuffle(pecasLista);
		
		// nem toda disposição aleatória é solúvel
		this.garanteSolucao(pecasLista);
		
		// a casa vazia é sempre a do canto inferior direito
		pecasLista.add(9); // a casa vazia tem valor 9!
				
		// agora que a lista de peças está completa e garante solução,
		// passamos ela para as casas do tabuleiro
		this.preenheTabuleiro(pecasLista);
	}
	
	/**
	 * Copia uma lista com peças para as casas do tabuleiro
	 * @param pecasLista
	 */
	private void preenheTabuleiro(List<Integer> pecasLista) {
		
		//passa a lista embaralhada para a matriz
		for (int l = 0; l < this.casas.length; l++) {
			
			for (int c = 0; c < this.casas[l].length; c++) {
				
				this.casas[l][c] = pecasLista.get((l * 3) + c);
				
			}
		}
		
	}

	/**
	 * Garante que a disposição das peças permite uma solução
	 * @param pecasLista
	 */
	private void garanteSolucao(List<Integer> pecasLista) {
		int inversoes = 0;
		
		// obtém o número de inversões na disposição atual
		for (int i = 1; i < pecasLista.size(); i++) {
			
			for (int j = i - 1; j >= 0; j--) {
				
				// inversão é uma peça menor na frente de uma maior
				if (pecasLista.get(i) < pecasLista.get(j)) {
					inversoes++;
				}
			}
		}
		
		// se o número de inversões for ímpar, corrige o problema
		// invertendo as duas últimas peças
		if ((inversoes % 2) != 0) {
			int aux = pecasLista.get(pecasLista.size() - 1);
			
			pecasLista.set(pecasLista.size() - 1, pecasLista.get(pecasLista.size() - 2));
			pecasLista.set(pecasLista.size() - 2, aux);
		}
	}

	/**
	 * Verifica se o estado objetivo foi atingido
	 * @return
	 */
	public boolean atingiuObjetivo() {
		
		int[] estado = this.getEstadoSerial();
		
		for (int i = 0; i < estado.length; i++) {
			
			if (estado[i] != i + 1) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Obtém o estado atual do tabuleiro de forma serial
	 * ao invés da forma convencional que é matricial.
	 * @return
	 */
	private int[] getEstadoSerial() {
		
		int[] estado = new int[this.casas.length * this.casas[0].length];
		
		// passa a matriz para um array
		for (int l = 0; l < this.casas.length; l++) {
			
			for (int c = 0; c < this.casas[l].length; c++) {
				
				estado[(l * 3) + c] = this.casas[l][c];				
			}
		}
		
		return estado;
	}
	
	/**
	 * Obtém a distância de manhattah total do tabuleiro
	 * @return
	 */
	public int getDistanciaManhattan() {
		
		int count = 0;
		
		for (int l = 0; l < this.casas.length; l++) {
			
			for (int c = 0; c < this.casas[l].length; c++) {
				
				count += this.getDistanciaManhattanPeca(this.casas[l][c], l, c);
				
			}
		}
		
		return count;		
	}

	/**
	 * Obtém a distância de manhattan para uma peça do tabuleiro
	 * @param peca
	 * @param linha
	 * @param coluna
	 * @return
	 */
	private int getDistanciaManhattanPeca(int peca, int linha, int coluna) {
		
		int[] linhaColunaAlvo = Tabuleiro.linhasColunasCasas[peca - 1];
		
		return Math.abs(linha - linhaColunaAlvo[0]) + Math.abs(coluna - linhaColunaAlvo[1]);				
	}
	
	/**
	 * Obtém uma lista dos possíveis movimentos
	 * que a CASA VAZIA pode realizar.
	 * @return
	 */
	public Movimentos[] getMovimentosPossiveis() {
		
		List<Movimentos> lista = new ArrayList<Movimentos>(4);
		
		if (this.linhaVazio > 0) {
			lista.add(Movimentos.Cima);
		}
		
		if (this.linhaVazio < 2) {
			lista.add(Movimentos.Baixo);
		}
		
		if (this.colunaVazio > 0) {
			lista.add(Movimentos.Esquerda);
		}
		
		if (this.colunaVazio < 2) {
			lista.add(Movimentos.Direita);
		}
		
		return lista.toArray(new Movimentos[lista.size()]);
	}
		
	/**
	 * Movimenta a peça vazia pelo tabuleiro
	 * @param movimento
	 */
	public Tabuleiro movimentaCasaVazia(Movimentos movimento) {
		
		switch (movimento) {
		
			case Cima: {
				
				if (this.linhaVazio > 0) {
					int peca = this.casas[this.linhaVazio - 1][this.colunaVazio];
					this.casas[this.linhaVazio - 1][this.colunaVazio] = 9;
					this.casas[this.linhaVazio][this.colunaVazio] = peca;
					this.linhaVazio--;
					
					this.movimentosFeitos += 'C';
				}
				
				break;
			}
			case Baixo: {
				
				if (this.linhaVazio < 2) {
					int peca = this.casas[this.linhaVazio + 1][this.colunaVazio];
					this.casas[this.linhaVazio + 1][this.colunaVazio] = 9;
					this.casas[this.linhaVazio][this.colunaVazio] = peca;
					this.linhaVazio++;
					
					this.movimentosFeitos += 'B';
				}
				
				break;
			}
			case Esquerda: {
				
				if (this.colunaVazio > 0) {
					int peca = this.casas[this.linhaVazio][this.colunaVazio - 1];
					this.casas[this.linhaVazio][this.colunaVazio - 1] = 9;
					this.casas[this.linhaVazio][this.colunaVazio] = peca;
					this.colunaVazio--;
					
					this.movimentosFeitos += 'E';
				}
	
				break;
			}
			case Direita: {
	
				if (this.colunaVazio < 2) {
					int peca = this.casas[this.linhaVazio][this.colunaVazio + 1];
					this.casas[this.linhaVazio][this.colunaVazio + 1] = 9;
					this.casas[this.linhaVazio][this.colunaVazio] = peca;
					this.colunaVazio++;
					
					this.movimentosFeitos += 'D';
				}
				
				break;
			}
		}
		
		return this;
	}
	
	/**
	 * Verifica se este tabuleiro está com a mesma disposição de um outro
	 * @param outro
	 * @return
	 */
	public boolean equals(Object o) {
		
		Tabuleiro outro = (Tabuleiro)o;
		
		for (int l = 0; l < this.casas.length; l++) {
			
			for (int c = 0; c < this.casas[l].length; c++) {
				
				if (this.casas[l][c] != outro.casas[l][c]) {
					return false;
				}
				
			}
		}
		
		if (this.linhaVazio != outro.linhaVazio || this.colunaVazio != outro.colunaVazio) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Obtém um clone deste tabuleiro. O clone pode ser manipulado sem causar
	 * alterações no original.
	 * @return
	 */
	public Tabuleiro getClone() {
		
		Tabuleiro clone = new Tabuleiro(true);
		
		for (int l = 0; l < this.casas.length; l++) {
			
			for (int c = 0; c < this.casas[l].length; c++) {
				
				clone.casas[l][c] = this.casas[l][c];
				
			}
		}
		
		clone.linhaVazio = this.linhaVazio;
		clone.colunaVazio = this.colunaVazio;
		
		clone.movimentosFeitos = this.movimentosFeitos;
		
		return clone;
	}
	
	public String toString() {
		
		String string = "";
		
		for (int l = 0; l < this.casas.length; l++) {
			
			for (int c = 0; c < this.casas[l].length; c++) {
				
				string += (casas[l][c] == 9 ? "*" : casas[l][c]) + " ";
				
			}
			
			string += "\n";
		}
		
		return string;
	}
	
	private String getSerieString() {
		
		StringBuilder sb = new StringBuilder(9);
		
		for (int l = 0; l < this.casas.length; l++) {
			for (int c = 0; c < this.casas[l].length; c++) {
				sb.append(this.casas[l][c]);
			}
		}
		
		return sb.toString();
	}

	public int hashCode() {
		
		return this.getSerieString().hashCode();
	}

	public String getMovimentosFeitos() {
		
		return this.movimentosFeitos;
	}
	
	public int getNumeroMovimentosFeitos() {
		
		return this.movimentosFeitos.length();
	}
}