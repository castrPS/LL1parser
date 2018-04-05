package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {
    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {
        
    	if (g == null) throw new NullPointerException("g nao pode ser nula.");
        
    	Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
    	/*
    	 * Implemente aqui o mÃ©todo para retornar o conjunto first
    	 */
    	
    	Collection <Production> productions = g.getProductions();
 		Nonterminal nonTerminal = null;
 		Set<GeneralSymbol> st = null;

 		for(Production p :  g.getProductions()) {
 			nonTerminal = p.getNonterminal();
 			st = getFirstSymbol (null, productions, nonTerminal);     // Lista do first do Não Terminal
 			
 			first.get(nonTerminal).addAll(st);


 		}
        return first;
    	
    }

    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {
        
        if (g == null || first == null)
            throw new NullPointerException();
                
        Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);
        
        /*
         * implemente aqui o mÃ©todo para retornar o conjunto follow
         */
        
       
        
        return follow;
    }
    
    //mÃ©todo para inicializar mapeamento nÃ£oterminais -> conjunto de sÃ­mbolos
    private static Map<Nonterminal, Set<GeneralSymbol>>
    initializeNonterminalMapping(Grammar g) {
    Map<Nonterminal, Set<GeneralSymbol>> result = 
        new HashMap<Nonterminal, Set<GeneralSymbol>>();

    for (Nonterminal nt: g.getNonterminals())
        result.put(nt, new HashSet<GeneralSymbol>());

    return result;
}

    public static Set <GeneralSymbol> getFirstSymbol (Set <GeneralSymbol> aux, Collection <Production> productions, GeneralSymbol symbol){
		List <GeneralSymbol> current = null;
		List<Production> p_symbol = new ArrayList<Production>();
		for(Production p :  productions) {	
			if(p.getNonterminal() == symbol) {        //Pegando a lista de produções para o Terminal
				p_symbol.add(p);
			}
		}

		for (Production p : p_symbol) {
			current = p.getProduction();
			if (aux == null) { 
				aux = new HashSet<GeneralSymbol>();
			}
			for(int i = 0; i < current.size(); i++) {

				if (current.get(i) instanceof Terminal) {          //X é um terminal / First(x) = {x}
					aux.add(current.get(i));
					break;
				}
				if(current.contains(SpecialSymbol.EPSILON)) {    // X -> Vazio
					aux.add(SpecialSymbol.EPSILON);
					break;
				}
				else if (current.get(i) instanceof Nonterminal) {           //Se X é um não-terminal
					Set<GeneralSymbol> aux1 = getFirstSymbol(aux, productions, current.get(i));
					if(i == current.size() -1 && aux1.contains(SpecialSymbol.EPSILON) ) {   
						aux1.add(SpecialSymbol.EPSILON);
					}
					if(!aux1.remove(SpecialSymbol.EPSILON)) {    
						break;
					}
					aux.addAll(aux1);
				}

			}
		}


		return aux; 
		
	}
}