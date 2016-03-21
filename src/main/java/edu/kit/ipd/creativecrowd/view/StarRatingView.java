package edu.kit.ipd.creativecrowd.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

public class StarRatingView implements RatingView {
	public String createView(Experiment ex, String assgId, int num) {
		try {
			String view = "";

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

			int i = 5;
			view += "<fieldset class=\"rating\">";
			for (RatingOption op : coll) {
				view += "<input type=\"radio\" id=\"" + op.getID() + "\" name=\"rating\" value=\"5\" /><label for=\"" + op.getID() + "\">" + i-- + " stars</label>";
			}
			view += "</fieldset>";

			return view;
			} catch (DatabaseException e) {
				Logger.logException(e.getMessage());
				e.printStackTrace();
			}
		return new String();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.view.RatingView#createPreview(edu.kit.ipd.creativecrowd.readablemodel.ConfigModel, int)
	 */
	@Override
	public String createPreview(ConfigModelJson config, int num) {
		String view = "";

		List<RatingOption> coll = new ArrayList<RatingOption>();
			for (RatingOption op : config.getRatingOptionsJson()) {
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

		int i = 5;
		view += "<fieldset class=\"rating\">";
		for (RatingOption op : coll) {
			view += "<input type=\"radio\" id=\"" + op.getID() + "\" name=\"rating\" value=\"5\" /><label for=\"" + op.getID() + "\">" + i-- + " stars</label>";
		}
		view += "</fieldset>";

		return view;
	}

}
