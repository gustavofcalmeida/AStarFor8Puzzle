package br.edu.ufcg.ccc.ai.puzzle;

import java.util.Comparator;

public class TabuleirosComparator implements Comparator<Tabuleiro> {

	public int compare(Tabuleiro o1, Tabuleiro o2) {

		Integer fn1 = new Integer(o1.getNumeroMovimentosFeitos() + o1.getDistanciaManhattan());
		Integer fn2 = new Integer(o2.getNumeroMovimentosFeitos() + o2.getDistanciaManhattan());
		
		return fn1.compareTo(fn2);
	}
}
