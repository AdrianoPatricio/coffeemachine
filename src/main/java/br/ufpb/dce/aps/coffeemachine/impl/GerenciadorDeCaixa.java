package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeCaixa {

	private Coin[] reverso = Coin.reverse();
	private int total;
	private int inteiro, centavos;
	private ArrayList<Coin> spartacus = new ArrayList<Coin>();

	public void inserirMoeda(ComponentsFactory factory, Coin dime) throws CoffeeMachineException {
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

	public void cancelar(ComponentsFactory factory) throws CoffeeMachineException {
		if (this.total == 0) {
			throw new CoffeeMachineException("sem moedas inseridas");
		}
		this.liberarMoedas(factory, true);

	}

	public void liberarMoedas(ComponentsFactory factory, Boolean confirmacao) {
		if (confirmacao) {
			factory.getDisplay().warn(Messages.CANCEL);
		}
		for (Coin re : this.reverso) {
			for (Coin aux : this.spartacus) {
				if (aux == re) {
					factory.getCashBox().release(aux);
				}
			}
		}
		this.total = 0;
		this.zerarMoedas();
		factory.getDisplay().info(Messages.INSERT_COINS);
	}

	public boolean calculaTroco(ComponentsFactory factory, double troco) {
		this.reverso = Coin.reverse();
		for (Coin c : this.reverso) {
			if (c.getValue() <= troco && factory.getCashBox().count(c) > 0) {
				troco -= c.getValue();
			}
		}
		return (troco == 0);
	}

	public void liberaTroco(ComponentsFactory factory, double valorDaBebida) {
		double troco = this.total - valorDaBebida;
		this.reverso = Coin.reverse();
		for (Coin c : this.reverso) {
			while (c.getValue() <= troco) {
				factory.getCashBox().release(c);
				troco -= c.getValue();
			}
		}
	}

	public boolean conferirDinheiro(ComponentsFactory factory, double valorDaBebida) {
		if (this.total < valorDaBebida || this.total == 0) {
			factory.getDisplay().warn(Messages.NO_ENOUGHT_MONEY);
			this.liberarMoedas(factory,false);
			return false;
		}
		return true;
	}

	public boolean verificarTroco(ComponentsFactory factory, double valorDaBebida) {
		if (this.total % valorDaBebida != 0 && this.total > valorDaBebida) {
			if (!this.calculaTroco(factory ,this.total - valorDaBebida)) {
				factory.getDisplay().warn(Messages.NO_ENOUGHT_CHANGE);
				this.liberarMoedas(factory, false);
				return false;
			}
		}
		return true;
	}

	public void zerarMoedas() {
		this.spartacus.clear();
	}

	public int getTotal() {
		return total;
	}
}