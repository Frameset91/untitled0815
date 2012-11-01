package utilities.ki;

import java.util.ArrayList;

public class RatingResult {
	private int rating;
	private ArrayList<Position> winningchips;
	
	public RatingResult(int rating, ArrayList<Position> winningchips) {
		this.rating = rating;
		this.winningchips = winningchips;
	}
	
	public RatingResult(int rating) {
		this.rating = rating;
		this.winningchips = null;
	}
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public ArrayList<Position> getWinningchips() {
		return winningchips;
	}
	public void setWinningchips(ArrayList<Position> winningchips) {
		this.winningchips = winningchips;
	}
}
