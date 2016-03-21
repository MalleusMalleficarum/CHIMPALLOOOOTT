package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment
 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment
 * @author Philipp & Alexis
 */
class PersistentAssignment implements MutableAssignment {

	/** The unique id of a persistent assignment. */
	private String id;

	/** The connection to the database. */
	private DatabaseConnection connection;

	/**
	 * Instantiates a new persistent assignment.
	 *
	 * @param id, a unique id of a persistent assignment
	 * @param connection, a connection to the database
	 */
	PersistentAssignment(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#isSubmitted()
	 */
	@Override
	public boolean isSubmitted() throws DatabaseException {
		boolean ret = false;
		try {
			List<Iterable<Value>> value = new ArrayList<Iterable<Value>>();
			String sql = connection.formatString("SELECT is_submitted FROM assignment WHERE id = {?};", Value.fromString(this.id));
			value = (List<Iterable<Value>>) connection.query(sql);
			ret = (value.iterator().next().iterator().next().asInt() == 1);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#getMTurkAssignmentID()
	 */
	@Override
	public AssignmentId getMTurkAssignmentID() throws DatabaseException {
		AssignmentId ret;
		try {
			String sql = connection.formatString("SELECT mturkid FROM assignment WHERE id = {?};", Value.fromString(this.id));
			ret = new AssignmentId(connection.query(sql).iterator().next().iterator().next().asString());
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#getWorkerID()
	 */
	@Override
	public WorkerId getWorkerID() throws DatabaseException {
		WorkerId ret;
		try {
			String sql = connection.formatString("SELECT worker_mturkid FROM assignment WHERE id = {?};", Value.fromString(this.id));
			ret = new WorkerId(connection.query(sql).iterator().next().iterator().next().asString());
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#getPaymentOutcome()
	 */
	@Override
	public PaymentOutcome getPaymentOutcome() throws DatabaseException {
		PaymentOutcome ret;
		try {
			String sql = connection.formatString("SELECT id FROM paymentoutcome WHERE assignmentid = {?};", Value.fromString(this.id));
			ret = new PersistentPaymentOutcome(connection.query(sql).iterator().next().iterator().next().asString(), this.connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment#setSubmitted()
	 */
	@Override
	public void setSubmitted() throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE assignment SET is_submitted = 1 WHERE id = {?};", Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment#setPaymentOutcome(boolean, int)
	 */
	@Override
	public void setPaymentOutcome(boolean receives_base_payment,
			int bonusPaymentAmountInCents) throws DatabaseException {
		try {
			List<Value> values = new ArrayList<Value>();
			int receives_base_payment_int = 0;
			if (receives_base_payment) {
				receives_base_payment_int = 1;
			}
			values.add(Value.fromInt(receives_base_payment_int));
			values.add(Value.fromInt(bonusPaymentAmountInCents));
			values.add(Value.fromString(id));
			String sql = connection.formatString("UPDATE paymentoutcome SET receives_base_payment = {?},bonus_amount = {?} WHERE assignmentid = {?};", values);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment#setWorker(edu.kit.ipd.creativecrowd.mturk.WorkerId)
	 */
	@Override
	public void setWorker(WorkerId worker) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE assignment SET worker_mturkid = {?} WHERE id = {?};", Value.fromString(worker.getId()), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment#markAsPaid()
	 */
	@Override
	public void markAsPaid() throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE assignment SET is_paid = 1 WHERE id = {?};", Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public boolean isPaid() throws DatabaseException {
		int resultInt;
		try {
			String sql = connection.formatString("SELECT is_paid FROM assignment WHERE id = {?};", Value.fromString(this.id));
			resultInt = connection.query(sql).iterator().next().iterator().next().asInt();
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
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment#getTaskConstellation()
	 */
	@Override
	public MutableTaskConstellation getTaskConstellation() throws DatabaseException {
		MutableTaskConstellation ret;
		try {
			String sql = connection.formatString("SELECT id FROM taskconstellation WHERE assignmentid = {?};", Value.fromString(this.id));
			ret = new PersistentTaskConstellation(connection.query(sql).iterator().next().iterator().next().asString(), this.connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment#setAssignmentID(edu.kit.ipd.creativecrowd.mturk.AssignmentId)
	 */
	@Override
	public void setAssignmentID(AssignmentId id) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE assignment SET MTURKID = {?} WHERE id = {?};", Value.fromString(id.getId()), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public MutableExperiment getExperiment() throws DatabaseException {
		MutableExperiment ret = null;
		try {
			String sql = connection.formatString("SELECT experimentid FROM assignment WHERE id = {?};", Value.fromString(this.id));
			ret = new PersistentExperiment(connection.query(sql).iterator().next().iterator().next().asString(), this.connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#getID()
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
}
