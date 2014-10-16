package com.geeekr.game;

import java.awt.Color;

public class Cell {

	private boolean isNew;
	private boolean isSum;

	private int value;

	public Cell() {
	}

	public Color getBackground() {
		switch (this.value) {
		case 2:
			return new Color(0xeee4da);
		case 4:
			return new Color(0xede0c8);
		case 8:
			return new Color(0xf2b179);
		case 16:
			return new Color(0xf59563);
		case 32:
			return new Color(0xf67c5f);
		case 64:
			return new Color(0xf65e3b);
		case 128:
			return new Color(0xedcf72);
		case 256:
			return new Color(0xedcc61);
		case 512:
			return new Color(0xedc850);
		case 1024:
			return new Color(0xedc53f);
		case 2048:
			return new Color(0xedc22e);
		}
		return new Color(0xcdc1b4);
	}

	public Color getForeground() {
		return value < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);
	}

	public Cell(int value) {
		this.value = value;
	}

	public boolean isEmpty() {
		return value == 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isSum() {
		return isSum;
	}

	public void setSum(boolean isSum) {
		this.isSum = isSum;
	}

	@Override
	public String toString() {
		return "Model [value=" + value + "]";
	}

}
