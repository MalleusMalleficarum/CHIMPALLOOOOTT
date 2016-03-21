package edu.kit.ipd.creativecrowd.operations;

import java.util.Iterator;
import java.util.List;

import edu.kit.ipd.chimpalot.util.Email;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.mutablemodel.WorkerRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentAmazonVoucherRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentWorkerRepo;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucherRepo;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;

/**
 * Provides methods to check wether to pay workers and to pay them in pybossa.
 * @author Pascal Gabriel
 *
 */
public class WorkerPaymentChecker {

	WorkerRepo workerRepo;
	
	/**
	 * Sets the database connection for the WorkerPaymentChecker
	 * 
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public WorkerPaymentChecker() throws DatabaseException{
		this.workerRepo = new PersistentWorkerRepo();
	}
	
	/**
	 * Checks if a worker is over the set threshold with his earnings.
	 * @param workerId the worker to check
	 * @return {@code true} if he is, {@code false} if not
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean hasWorkerEarnedEnough(String workerId) throws DatabaseException{
		MutableWorker worker = workerRepo.loadWorker(workerId);
		double credit = worker.getCredit();
		if (credit >= GlobalApplicationConfig.getPyBossaPaymentThreshold()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if a worker has given his/her email, and if its valid
	 * @param workerId	the worker to check on 
	 * @return {@code true} if email is given and valid, {@code false} if not
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isPayable(String workerId) throws DatabaseException{
		MutableWorker worker = workerRepo.loadWorker(workerId);
		String email = worker.getEmail();
		if (email != null)  {
			return Email.isValidEmail(email);
		}
		return false;
	}
	
	/**
	 * Pays a worker in vouchers with a total worth close to the workers credit
	 * @param workerId the workers id
	 * @return	{@code true} if successfull, {@code false} if worker is not payable
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean payWorker(String workerId) throws DatabaseException{
		MutableWorker worker = workerRepo.loadWorker(workerId);
		int workerCredit = worker.getCredit();
		AmazonVoucherRepo voucherRepo = new PersistentAmazonVoucherRepo();
		Logger.debug(String.valueOf(workerCredit)+ " " + worker.getID());
		if (isPayable(workerId)) {
			if(workerCredit == 0) {
				return true;
			}
			List<AmazonVoucher> vouchers = voucherRepo.getVouchersWorthCloseTo(workerCredit);
			//make sure there are vouchers to send
			if (vouchers.size() == 0) {
				return false;
			}
			Iterator<AmazonVoucher> voucherIt = vouchers.iterator();
			int totalVoucherCredit = 0;
			while(voucherIt.hasNext()) {
				totalVoucherCredit += voucherIt.next().getValue();
			}
			//adjust the credit and send the vouchers
			worker.decreaseCredit(totalVoucherCredit);	
			sendVouchers(vouchers, workerId, workerCredit-totalVoucherCredit);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Pays all Workers with vouchers if they are payable.
	 * @return {@code true} if all have been paid, {@code false} if at least one has not been paid
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean payAllWorkers() throws DatabaseException{
		Iterator<MutableWorker> allWorkers = workerRepo.loadAllWorkers().iterator();
		boolean ret = true;
		while (allWorkers.hasNext()) {
			Worker current = allWorkers.next();
			boolean test = payWorker(current.getID());
			if (!test) {
				ret = test;
			}
		}
		return ret;
	}
	
	/**
	 * Pays all workers that have earned more than the set threshold.
	 * @return {@code true} if all could be paid, {@code false} if at least one worker could not be paid.
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean payAllWorkersOverThreshold() throws DatabaseException{
		Iterator<MutableWorker> allWorkers = workerRepo.loadAllWorkers().iterator();
		boolean ret = true;
		while (allWorkers.hasNext()) {
			Worker current = allWorkers.next();
			if (current.getCredit() >= GlobalApplicationConfig.getPyBossaPaymentThreshold()) {
				boolean test = payWorker(current.getID());
				if (!test) {
					ret = test;
				}
			}
		}
		return ret;
	}
	
	/*
	 * Sends the vouchers to the given worker, and tells him/her his/her credit.
	 * Make sure worker isPayable.
	 */
	private void sendVouchers(List<AmazonVoucher> vouchers, String workerId, int credit) throws DatabaseException{
		Worker worker = workerRepo.loadWorker(workerId);
		Iterator<AmazonVoucher> iterator = vouchers.iterator();
		String mailText = "Your remaining credit is: " + credit + " ct";
		mailText += "\nYour Amazon voucher codes are as follows:";
		PersistentAmazonVoucherRepo repo = new PersistentAmazonVoucherRepo();
		while (iterator.hasNext()) {
			AmazonVoucher current = iterator.next();
			mailText += "\n" + current.getCode() + " with a value of: " + current.getValue() + " ct";
			repo.useVoucher(current.getID());
			
		}
		Email email = new Email();
		email.sendMail(worker.getEmail(), "Payment for Participation in PyBossa Projects", mailText);
	}
}
