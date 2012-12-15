package de.ulei.nebeneinkuenfte.solitaire;

import java.io.Serializable;

public class Configuration implements Serializable {

	private static final long serialVersionUID = -8674018396618747727L;
	private int cardToDraw = 3;

    public int getCardsToDraw() {
        return cardToDraw;
    }

    public void setCardToDraw(int cardToDraw) {
        this.cardToDraw = cardToDraw;
    }

}