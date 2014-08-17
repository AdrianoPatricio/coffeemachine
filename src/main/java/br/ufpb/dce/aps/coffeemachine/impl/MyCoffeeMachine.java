package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class MyCoffeeMachine implements CoffeeMachine {

	private int total;
	private int inteiro, centavos;
	private ComponentsFactory factory;
	private ArrayList<Coin> spartacus = new ArrayList<Coin>();
	private GerenciadorDeBebidas gerenteBebidas;
	private Coin[] reverso = Coin.reverse();

	public MyCoffeeMachine(ComponentsFactory factory) {
		this.factory = factory;
		gerenteBebidas = new GerenciadorDeBebidas(this.factory);
		factory.getDisplay().info(Messages.INSERT_COINS);
	}

	public void insertCoin(Coin dime) throws CoffeeMachineException {
		try {
			total += dime.getValue();
			spartacus.add(dime);
			inteiro = total/100;
			centavos = total%100;
			factory.getDisplay().info("Total: US$ " + inteiro + "." + centavos);
		} catch (NullPointerException e) {
			throw new CoffeeMachineException("moeda invalida");
		}
	}

	public void cancel() throws CoffeeMachineException {
		if (total == 0) {
			throw new CoffeeMachineException("sem moedas inseridas");
		}
		cancel(true);
		
	}
		
	public void cancel(Boolean confirm) throws CoffeeMachineException{
		if (spartacus.size() > 0) {
			if(confirm){
				factory.getDisplay().warn(Messages.CANCEL);
			}
			for (Coin re : reverso) {
				for (Coin aux : spartacus) {
					if (aux == re) {
						factory.getCashBox().release(aux);
					}
				}
			}
			total = 0;
			spartacus.clear();
		}
		factory.getDisplay().info(Messages.INSERT_COINS);
	}
	
	public boolean calculaTroco (double troco){
		for(Coin re : reverso){
			if(re.getValue() <= troco && factory.getCashBox().count (re) > 0){
					troco -= re.getValue();
			}
		}		
		return (troco == 0);
	}
	
	public void liberarTroco (double troco){
		for(Coin re : reverso){
			while(re.getValue() <= troco ){
				factory.getCashBox().release (re);
				troco -= re.getValue(); 
			}
		}
	}
	
	public void select(Drink drink) {
		
		if(total < gerenteBebidas.getValor() || total == 0){
			factory.getDisplay().warn(Messages.NO_ENOUGHT_MONEY);
			cancel(false);
			return;
		}

		gerenteBebidas.iniciarBebida(drink);
		
		if (!gerenteBebidas.conferirIngredientes()) {
			cancel(false);
			return;
		}
		if(!gerenteBebidas.verificaAcucar()){ 
			cancel(false);
			return;
		} 
		if(total % gerenteBebidas.getValor() != 0 && total > gerenteBebidas.getValor()){
			if(!calculaTroco(total - gerenteBebidas.getValor())){
				factory.getDisplay().warn(Messages.NO_ENOUGHT_CHANGE);
				cancel(false);
				return;
			}
		}
				
		gerenteBebidas.misturarIngredientes();
		gerenteBebidas.release();
		
		if(total % gerenteBebidas.getValor() != 0 && total > gerenteBebidas.getValor()){
			liberarTroco(total - gerenteBebidas.getValor());
		}
		factory.getDisplay().info(Messages.INSERT_COINS);
		spartacus.clear();
	}
}