package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome;

/**
 * @see edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome
 * @author Philipp + Alexis
 */
class PersistentPaymentOutcome implements PaymentOutcome {

	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent payment outcome. */
	private String id;

	/**
	 * Instantiates a new persistent payment outcome.
	 *
	 * @param id, a unique id of a persistent payment outcome
	 * @param connection, a connection to the database
	 */
	PersistentPaymentOutcome(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj.toString());

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome#basicPaymentWasPaid()
	 */
	@Override
	public boolean isApproved() throws DatabaseException {
		int resultInt = 0;
		try {
			String sql = connection.formatString("SELECT receives_base_payment FROM paymentoutcome WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> paid = connection.query(sql);
			if (paid.iterator().hasNext()) {
				resultInt = paid.iterator().next().iterator().next().asInt();
			}
			else {
				throw new DatabaseException("wanted basic payment does not exist");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		boolean result = false;
		if (resultInt == 1) {
			result = true;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome#getBonusPaidCents()
	 */
	@Override
	public int getBonusPaidCents() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT bonus_amount FROM paymentoutcome WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> bonuses = connection.query(sql);
			if (bonuses.iterator().hasNext()) {
				result = bonuses.iterator().next().iterator().next().asInt();
			} else {
				throw new DatabaseException("wanted bonus payment does not exist");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
}
