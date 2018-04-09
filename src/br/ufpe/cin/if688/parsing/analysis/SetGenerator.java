package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {
    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {
        
    	if (g == null) throw new NullPointerException("g nao pode ser nula.");
        
    	Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
    	
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
    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {
        
        if (g == null || first == null)
            throw new NullPointerException();
                
        Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);
        
        Map<Nonterminal, Set<GeneralSymbol>> follow_aux;
        Collection <Production> productions = g.getProductions();
		GeneralSymbol start = g.getStartSymbol();
		GeneralSymbol current = null;
		GeneralSymbol current_next = null;
		Set<GeneralSymbol> set_symb;
		Set<GeneralSymbol> bi;
		follow.get(start).add(SpecialSymbol.EOF);
		boolean change = true;
		
		while(change) {
			follow_aux = new HashMap<Nonterminal, Set<GeneralSymbol>>();
			for(Nonterminal nt:follow.keySet()) {
				set_symb = new HashSet<GeneralSymbol>();
				set_symb.addAll(follow.get(nt));
				follow_aux.put(nt, set_symb);
			}
			//baseado no algoritmo do livro  ENGINEERING A COMPILER 
			for (Production p : productions)  {
				set_symb = new HashSet<GeneralSymbol>();
				set_symb.addAll(follow.get(p.getNonterminal()));
				for(int i = p.getProduction().size()-1; i>=0;i-- ) {
					if(p.getProduction().get(i) instanceof Nonterminal) {
						follow.get(p.getProduction().get(i)).addAll(set_symb);
						if(first.get(p.getProduction().get(i)).contains(SpecialSymbol.EPSILON)) {
							bi = new HashSet<GeneralSymbol>();
							bi.addAll(first.get(p.getProduction().get(i)));
							bi.remove(SpecialSymbol.EPSILON);
							set_symb.addAll(bi);
						}else {
							set_symb = new HashSet<GeneralSymbol>();
							set_symb.addAll(first.get(p.getProduction().get(i)));
						}
					}else{
						set_symb = new HashSet<GeneralSymbol>();
						set_symb.add(p.getProduction().get(i));
						set_symb.addAll(set_symb);
					}
				
				}
				
				change = !follow.equals(follow_aux);
			
			}
			
		}
	
        
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

} 
