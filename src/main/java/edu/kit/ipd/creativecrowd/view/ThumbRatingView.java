package edu.kit.ipd.creativecrowd.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.util.Logger;

public class ThumbRatingView implements RatingView {
	@Override
	public String createView(Experiment ex, String assgId, int num) {
		String view = "";

		try {
			int i = 0;
			view += "<div class=\"checkboxgroup\">";
			List<RatingOption> coll = new ArrayList<RatingOption>();
			for (RatingOption op : ex.getRatingOptions()) {
				coll.add(op);
			}
			class rOptComp<T extends RatingOption> implements Comparator<T> {
				public int compare(RatingOption m1, RatingOption m2) {
					int ret = -2;
					try {
						if (m1.getValue() < m2.getValue())
							ret = 1;
						else if (m2.getValue() < m1.getValue())
							ret = -1;
						else if (m1.getValue() == m2.getValue())
							ret = 0;
					} catch (DatabaseException e) {
						e.printStackTrace();
					}
					return ret;
				}
			}
			rOptComp<RatingOption> comp = new rOptComp<RatingOption>();

			coll.sort(comp);
			System.out.println(coll);

			for (RatingOption op : coll) {
				view += "<div class=\"checkbox\">";
				if (i == 0) {
					view += "<input type='radio' name='rating" + num
							+ "option' value='" + op.getID() +"' id='" + op.getID() +  num +"' class='hidden thumbupinput'></input>"
							+ "<label for='" + op.getID() + num + "' class=\"thumb thumbup\"></label>";
				} else if (i == 1) {
					view += "<input type='radio' name='rating" + num
							+ "option' value='" + op.getID() + "' id='" + op.getID() +  num +"' class='hidden thumbdowninput'></input>"
							+ "<label for='" + op.getID() + num + "' class=\"thumb thumbdown\"></label>";
				} else {
					view += "<label for='" + op.getID()  + "'>" + op.getText() + "</label>"
							+ "<input type='radio' name='rating" + num
							+ "option' value='" + op.getID()  + "'></input>";
				}
				i++;
				view += "</div>";
			}
			view += "</div>";
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
		}

		return view;
	}
}
