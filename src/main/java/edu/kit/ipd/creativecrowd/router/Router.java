package edu.kit.ipd.creativecrowd.router;

import static spark.Spark.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.kit.ipd.creativecrowd.controller.AssignmentController;
import edu.kit.ipd.creativecrowd.controller.ExperimentController;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;
import spark.Request;
import spark.servlet.SparkApplication;

/**
 * Router class used to connect http-requests to appropriate outputs
 * 
 * @author KIT
 * @version 1.0
 */
public final class Router implements SparkApplication {
	Lock mutex;

	/**
	 * Initialize all the routes
	 */
	public void init() {
		mutex = new ReentrantLock();
		// Serve static assets from src/main/resources/public
		staticFileLocation("/public");

		before((req, resp) -> {
			// Lock the GLOBAL EVIL MUTEX
			mutex.lock();

			if (!GlobalApplicationConfig.isConfigured()) {
				// Okay, looks like we do need to configure the app.
				// The context might actually be null here, if we're not running inside
				// a servlet container. But that's OK.
				GlobalApplicationConfig.configureFromServletContext(req.raw().getSession().getServletContext());
			}
		});

		after((req, resp) -> {
			mutex.unlock();
		});

		/*-?|Test Repo-Review|Philipp|c8|?*/
		get("/", (req, resp) -> {
			return "hallo welt!";
		});

		get("/experiments/:expID/delete", (request, response) -> {
			ExperimentController exp = new ExperimentController();
			return exp.deleteExperiment(request, response).body();
		});

		put("/experiments/:id", (request, response) -> {
			ExperimentController exp = new ExperimentController();
			return exp.createExperiment(request, response).body();
		});

		get("/experiments/:expID/csv", (request, response) -> {
			ExperimentController exp = new ExperimentController();
			return exp.getStatistics(request, response).body();
		});

		get("/experiments/:expID/txt", (request, response) -> {
			ExperimentController exp = new ExperimentController();
			return exp.getStatistics(request, response).body();
		});

		get("/experiments/:expID", (request, response) -> {
			ExperimentController exp = new ExperimentController();
			return exp.getStatistics(request, response).body();
		});

		post("/experiments/:expID/end", (request, response) -> {
			ExperimentController exp = new ExperimentController();
			return exp.endExperiment(request, response).body();
		});
		get("/assignment/:expID", (request, response) -> {
			AssignmentController assig = new AssignmentController();
			response = assig.show(request, response);
			return response.body();
		});

		post("/assignment/:expID", (request, response) -> {
			AssignmentController assig = new AssignmentController();
			return assig.update(request, response).body();
		});

		post("/assignment/:expID/submit", (request, response) -> {
			AssignmentController assig = new AssignmentController();
			return assig.update(request, response).body();
		});

		get("/exit", (request, response) -> {
			ExperimentController exp = new ExperimentController();
			exp.exit(request, response);
			return null;
		});
	}

	private static Router instance;

	/**
	 * constructor of the router class
	 */
	public Router() {
	}

	/**
	 * method used for guaranteeing that the router is a singleton object
	 * 
	 * @return the instance of the Router class
	 */
	public static synchronized Router getInstance() {
		if (instance == null) {
			instance = new Router();
		}
		/*-?|Test Repo-Review|jonas|c0|?*/
		return instance;
	}

	public static void main(String[] args) {
		Router.getInstance().init();
	}
}
