package edu.kit.ipd.creativecrowd.util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This no-op servlet fixes the Servlet API filter behaviour (which Spark requires)
 * for servlet containers that get confused when there are no servlets defined.
 * It is never called at runtime.
 */

public final class DummyServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {
	}
}