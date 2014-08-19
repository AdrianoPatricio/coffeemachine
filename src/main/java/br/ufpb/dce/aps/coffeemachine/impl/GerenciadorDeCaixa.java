package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeCaixa {

	private Coin[] reverso = Coin.reverse();
	private int total, inteiro, centavos;

	private ArrayList<Coin> spartacus = new ArrayList<Coin>();
	private ArrayList<Coin> trocos = new ArrayList<Coin>();

	public void inserirMoedas(ComponentsFactory factory, Coin coin)
			throws CoffeeMachineException {
		try {
			total += coin.getValue();
			spartacus.add(coin);
			inteiro = total/100;
			centavos = total%100;
			factory.getDisplay().info("Total: US$ " + inteiro + "." + centavos);
		} catch (NullPointerException e) {
			throw new CoffeeMachineException("moeda invalida");
		}
	}

	public void cancelar(ComponentsFactory factory)
			throws CoffeeMachineException {
		if (total == 0) {
			throw new CoffeeMachineException("sem moedas inseridas");
		}
		liberaMoedas(factory, true);

	}

	public void liberaMoedas(ComponentsFactory factory, Boolean troco) {
		if (troco) {
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
		limparMoedas();
		factory.getDisplay().info(Messages.INSERT_COINS);
	}

	public boolean calculaTroco(ComponentsFactory factory, double valorBebida) {
		double troco = total - valorBebida;
		for (Coin re : reverso) {
			if (re.getValue() <= troco) {
				int count = factory.getCashBox().count(re);
				while (re.getValue() <= troco && count > 0) {
					troco = troco - re.getValue();
					trocos.add(re);
				}
			}
		}
		return (troco == 0);
	}

	public void liberarTroco(ComponentsFactory factory, double valorBebida) {
		for (Coin re : reverso) {
			for (Coin troco : trocos) {
				if (troco == re) {
					factory.getCashBox().release(re);
				}
			}
		}
	}

	public boolean conferirDinheiro(ComponentsFactory factory,double valorBebida) {
		if (total < valorBebida || total == 0) {
			factory.getDisplay().warn(Messages.NO_ENOUGHT_MONEY);
			liberaMoedas(factory, false);
			return false;
		}
		return true;
	}

	public boolean verificarTroco(ComponentsFactory factory, double valorDaBebida) {
		if (!calculaTroco(factory, valorDaBebida)) {
			factory.getDisplay().warn(Messages.NO_ENOUGHT_CHANGE);
			liberaMoedas(factory, false);
			return false;
		}
		return true;
	}

	public void limparMoedas() {
		spartacus.clear();
		total = 0;
	}

	public int getTotal() {
		return total;
	}
}