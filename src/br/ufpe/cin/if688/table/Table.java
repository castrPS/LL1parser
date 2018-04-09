package br.ufpe.cin.if688.table;


import br.ufpe.cin.if688.parsing.analysis.*;
import br.ufpe.cin.if688.parsing.grammar.*;
import java.util.*;


public final class Table {
	private Table() {    }

	public static Map<LL1Key, List<GeneralSymbol>> createTable(Grammar g) throws NotLL1Exception {
        if (g == null) throw new NullPointerException();

        Map<Nonterminal, Set<GeneralSymbol>> first =
            SetGenerator.getFirst(g);
        Map<Nonterminal, Set<GeneralSymbol>> follow =
            SetGenerator.getFollow(g, first);

        Map<LL1Key, List<GeneralSymbol>> parsingTable = 
            new HashMap<LL1Key, List<GeneralSymbol>>();

        Collection<Production> productions = g.getProductions();
        Iterator<GeneralSymbol> t_first, t_follow;               //Iterator para First e do Follow
        GeneralSymbol s_first, s_follow;                         //Simbolos de First e Follow
        LL1Key key;
        
        for (Production production : productions) {
			t_first = first.get(production.getNonterminal()).iterator();
			
			while(t_first.hasNext()) {
				 s_first = t_first.next();
				 
	     		   if(!s_first.equals(SpecialSymbol.EPSILON) && !production.getProduction().contains(SpecialSymbol.EPSILON)) {
	     			   
	     			   key = new LL1Key(production.getNonterminal(), s_first);  //Para todo a ∈ FIRST(α), adicione A → α em M[A,a]
	         		   parsingTable.put(key, production.getProduction());
	         		  
	         		   
	     		   } else if(s_first.equals(SpecialSymbol.EPSILON) && production.getProduction().contains(SpecialSymbol.EPSILON)) {
	     			  t_follow = follow.get(production.getNonterminal()).iterator();
	     			   while(t_follow.hasNext()) {                                     //Se ε ∈ FIRST(α), então, para todo b ∈FOLLOW(A), adicione A → α em M[A,b]
	     				   s_follow = t_follow.next();
	     				   key = new LL1Key(production.getNonterminal(), s_follow);
	     				   System.out.println(key);
	     				   parsingTable.put(key, production.getProduction());
	     			   }
	     		   } 
	     	   }
			}
        
        return parsingTable;
    }
}
